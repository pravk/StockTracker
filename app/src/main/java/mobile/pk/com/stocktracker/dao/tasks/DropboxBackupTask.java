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
public class DropboxBackupTask extends AsyncTask<Void, Long, Boolean> {

    private DropboxAPI<?> mApi;

    private DropboxAPI.UploadRequest mRequest;
    private Context mContext;
    private ProgressDialog mDialog;

    private String mErrorMsg;

    public DropboxBackupTask(Context context, DropboxAPI<?> api){
        mContext = context.getApplicationContext();
        mApi = api;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String fileName = "backup-" + DateUtils.formatDateTime(mContext, Calendar.getInstance().getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR);
        try {
            SerializerTask task = new SerializerTask();
            byte [] bytes = task.executeSync(null).getBytes("utf-8");
            InputStream stream = new ByteArrayInputStream(bytes);
            mApi.putFileOverwrite(fileName, stream, bytes.length, new ProgressListener() {
                @Override
                public long progressInterval() {
                    // Update the progress bar every half-second or so
                    return 500;
                }
                @Override
                public void onProgress(long bytes, long total) {
                    publishProgress(bytes);
                }
            });
        } catch (DropboxException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
