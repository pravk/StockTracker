package mobile.pk.com.stocktracker.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orm.Database;
import com.orm.SugarApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hello on 8/1/2015.
 */
public class Application extends SugarApp {

    private RestClient restClient;
    private static Application instance;

    public static Application getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // SharedObjects.context = this;

        restClient = new RestClient(this);
        instance = this;
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

}
