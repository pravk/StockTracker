package mobile.pk.com.stocktracker.dao.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.exception.DropboxException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hello on 8/26/2015.
 */
public class DropboxRestoreTask  extends AsyncTask<String, Long, Boolean> {

    private DropboxAPI<?> mApi;

    private Context mContext;

    private String mErrorMsg;

    public DropboxRestoreTask(Context context, DropboxAPI<?> api) {
        mContext = context.getApplicationContext();
        mApi = api;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String fileName = params[0];
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DropboxAPI.DropboxInputStream inputStream = mApi.getFileStream(fileName, null);
            String content =  getStringFromInputStream(inputStream);

            if(!TextUtils.isEmpty(content) ) {
                DeSerializerTask task = new DeSerializerTask();
                task.executeSync(content);
            }
        } catch (DropboxException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is, "UTF8"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}