package mobile.pk.com.stocktracker.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.orm.Database;
import com.orm.SugarApp;

import java.util.ArrayList;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.transaction.processor.TransactionProcessor;

/**
 * Created by hello on 8/1/2015.
 */
public class Application extends SugarApp {

    private RestClient restClient;
    private static Application instance;
    private TransactionProcessor transactionProcessor;
    private Tracker mTracker;

    public static Application getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // SharedObjects.context = this;

        restClient = new RestClient(this);
        instance = this;
        transactionProcessor = new TransactionProcessor();
    }

    public RestClient getRestClient() {
        return restClient;
    }

    public Cursor executeQuery(String query, String...arguments){
        Database db = getDatabase();
        SQLiteDatabase sqLiteDatabase = db.getDB();
        ArrayList toRet = new ArrayList();
        return sqLiteDatabase.rawQuery(query, arguments);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

}
