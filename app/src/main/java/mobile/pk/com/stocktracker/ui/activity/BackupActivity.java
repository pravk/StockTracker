package mobile.pk.com.stocktracker.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.tasks.DropboxBackupListTask;
import mobile.pk.com.stocktracker.dao.tasks.DropboxBackupTask;
import mobile.pk.com.stocktracker.dao.tasks.DropboxRestoreTask;
import mobile.pk.com.stocktracker.ui.BaseActivity;


public class BackupActivity extends BaseActivity {

    ///////////////////////////////////////////////////////////////////////////
    //                      Your app-specific settings.                      //
    ///////////////////////////////////////////////////////////////////////////

    // Replace this with your app key and secret assigned by Dropbox.
    // Note that this is a really insecure way to do this, and you shouldn't
    // ship code which contains your key & secret in such an obvious way.
    // Obfuscation is good.
    private static final String APP_KEY = "u2m4de3yv42bvto";
    private static final String APP_SECRET = "m942wjcrmffu0vq";

    ///////////////////////////////////////////////////////////////////////////
    //                      End app-specific settings.                       //
    ///////////////////////////////////////////////////////////////////////////

    // You don't need to change these, leave them alone.
    private static final String ACCOUNT_PREFS_NAME = "prefs";
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    private static final boolean USE_OAUTH1 = false;
    private static final String TAG = BackupActivity.class.getSimpleName();

    DropboxAPI<AndroidAuthSession> mApi;

    private boolean mLoggedIn;

    @InjectView(R.id.btn_connect_dropbox)
    public BootstrapButton connectDropbox;

    public BackupActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);

        setupToolbar();

        ButterKnife.inject(this);
        checkAppKeySetup();
        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);
        // Display the proper UI state if logged in or not
        setLoggedIn(mApi.getSession().isLinked());
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mApi.getSession();

        // The next part must be inserted in the onResume() method of the
        // activity from which session.startAuthentication() was called, so
        // that Dropbox authentication completes properly.
        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                storeAuth(session);
                setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i(TAG, "Error authenticating", e);
            }
        }
    }

    @OnClick(R.id.btn_connect_dropbox)
    public void onClick() {
        // This logs you out if you're logged in, or vice versa
        if (mLoggedIn) {
            logOut();
        } else {
            // Start the remote authentication
            if (USE_OAUTH1) {
                mApi.getSession().startAuthentication(this);
            } else {
                mApi.getSession().startOAuth2Authentication(this);
            }
        }
    }

    @OnClick(R.id.btn_backup)
    public void onBackup()
    {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait", "Creating backup file", true);
        DropboxBackupTask task = new DropboxBackupTask(this, mApi){
            protected void onPostExecute(Boolean result) {
                progressDialog.hide();
                if(result)
                    showToast("Backup Sucessfull");
                else
                    showToast("Backup Failed");
            }
        };
        task.execute();
    }

    @OnClick(R.id.btn_restore)
    public void onRestore()
    {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait", "Fetching backups from dropbox", true);
        DropboxBackupListTask backupListTask = new DropboxBackupListTask(this.getApplicationContext(), mApi){
            protected void onPostExecute(List<String> result) {
                progressDialog.hide();
                final String [] resultArray = result.toArray(new String [result.size()]);
                final AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                        .setSingleChoiceItems(resultArray, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        ListView lw = ((AlertDialog)dialog).getListView();
                                        String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                        dialog.dismiss();

                                        restoreBackup(checkedItem);
                                    }
                                }
                        )
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Select a backup to restore");
                builder.show();
            }
        };
        backupListTask.execute();
    }

    private void restoreBackup(String backupName) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait", "Restoring from backup", true);
        DropboxRestoreTask restoreTask = new DropboxRestoreTask(this.getApplicationContext(), mApi){
            protected void onPostExecute(Boolean result) {
                progressDialog.hide();
                if(result)
                    showToast("Restore Sucessfull");
                else
                    showToast("Restore Failed");
            }
        };
        restoreTask.execute(backupName);
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);

        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        loadAuth(session);
        return session;
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void loadAuth(AndroidAuthSession session) {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;

        if (key.equals("oauth2:")) {
            // If the key is set to "oauth2:", then we can assume the token is for OAuth 2.
            session.setOAuth2AccessToken(secret);
        } else {
            // Still support using old OAuth 1 tokens.
            session.setAccessTokenPair(new AccessTokenPair(key, secret));
        }
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void storeAuth(AndroidAuthSession session) {
        // Store the OAuth 2 access token, if there is one.
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, "oauth2:");
            edit.putString(ACCESS_SECRET_NAME, oauth2AccessToken);
            edit.commit();
            return;
        }
        // Store the OAuth 1 access token, if there is one.  This is only necessary if
        // you're still using OAuth 1.
        AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
        if (oauth1AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, oauth1AccessToken.key);
            edit.putString(ACCESS_SECRET_NAME, oauth1AccessToken.secret);
            edit.commit();
            return;
        }
    }

    /**
     * Convenience function to change UI state based on being logged in
     */
    private void setLoggedIn(boolean loggedIn) {
        mLoggedIn = loggedIn;
        if (loggedIn) {
            connectDropbox.setText("Unlink from Dropbox");
            findViewById(R.id.btn_backup).setEnabled(true);
            findViewById(R.id.btn_restore).setEnabled(true);

        } else {
            connectDropbox.setText("Connect to Dropbox");
            findViewById(R.id.btn_backup).setEnabled(false);
            findViewById(R.id.btn_restore).setEnabled(false);
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast("URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
            finish();
        }
    }

    private void logOut() {
        // Remove credentials from the session
        mApi.getSession().unlink();

        // Clear our stored keys
        clearKeys();
        // Change UI state to display logged out version
        setLoggedIn(false);
    }
    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }
}
