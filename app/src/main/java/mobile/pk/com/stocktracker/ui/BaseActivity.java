package mobile.pk.com.stocktracker.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.common.Application;
import mobile.pk.com.stocktracker.event.ShowPositionDetailEvent;
import mobile.pk.com.stocktracker.event.ShowStockDetailEvent;
import mobile.pk.com.stocktracker.ui.activity.StockActivity;
import mobile.pk.com.stocktracker.ui.activity.TransactionActivity;

/**
 * Created by hello on 8/11/2015.
 */
public class BaseActivity extends AppCompatActivity {
    public static final int EDIT_WATCHLIST_REQUEST = 1000;
    public static final int EDIT_PORTFOLIO_REQUEST = 2000;

    public static final int ADD_PORTFOLIO_TRANSACTION = 3000;
    public static final int EDIT_USER_TRANSACTION = 4000;
    public static final int RESULT_SETTINGS = 5000;
    private static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog pd;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(EventBus.getDefault().hasSubscriberForEvent(this.getClass()))
            EventBus.getDefault().register(this);

        // Obtain the shared Tracker instance.
        Application application = Application.getInstance();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + this.getClass().getSimpleName());
        mTracker.setScreenName(this.getTitle().toString() + ":" + this.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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

    public void onEvent(ShowStockDetailEvent event){
        Intent intent = new Intent(this, StockActivity.class);
        intent.putExtra(StockActivity.STOCK_ID, event.getStock().getId());
        startActivity(intent);
    }

}
