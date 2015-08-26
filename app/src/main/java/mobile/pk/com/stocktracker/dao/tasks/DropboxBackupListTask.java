package mobile.pk.com.stocktracker.dao.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateUtils;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.exception.DropboxException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hello on 8/26/2015.
 */
public class DropboxBackupListTask extends AsyncTask<Void, Long, List<String>> {

    private DropboxAPI<?> mApi;

    private Context mContext;

    public DropboxBackupListTask(Context context, DropboxAPI<?> api){
        mContext = context.getApplicationContext();
        mApi = api;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        DropboxAPI.Entry entries = null;
        List<String> backupList = new ArrayList<>();
        try {
            entries = mApi.metadata("", 100, null, true, null);
            for (DropboxAPI.Entry e : entries.contents) {
                if (!e.isDeleted && !e.isDir) {
                    backupList.add(e.fileName());
                }
            }

        } catch (DropboxException e) {
            e.printStackTrace();

        }
        return backupList;

    }

}
