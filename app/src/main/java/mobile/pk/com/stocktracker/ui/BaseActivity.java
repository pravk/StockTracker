package mobile.pk.com.stocktracker.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;

/**
 * Created by hello on 8/11/2015.
 */
public class BaseActivity extends AppCompatActivity {
    public static final int EDIT_WATCHLIST_REQUEST = 1000;
    public static final int EDIT_PORTFOLIO_REQUEST = 2000;

    public static final int ADD_PORTFOLIO_TRANSACTION = 3000;
    public static final int EDIT_USER_TRANSACTION = 4000;
    public static final int RESULT_SETTINGS = 5000;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(EventBus.getDefault().hasSubscriberForEvent(this.getClass()))
            EventBus.getDefault().register(this);
    }

    protected void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showProgressDialog(int resourceId)
    {
        if(pd == null)
            pd = new ProgressDialog(this);
        pd.setMessage(getString(resourceId));
        pd.show();
    }

    public void hideProgressDialog()
    {
        if(pd != null)
            pd.dismiss();
    }

    protected void replaceFragment(Fragment fragment, String title){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        if(!TextUtils.isEmpty(title))
            setTitle(title);
    }
}
