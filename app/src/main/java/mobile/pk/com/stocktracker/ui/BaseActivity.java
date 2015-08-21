package mobile.pk.com.stocktracker.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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
}
