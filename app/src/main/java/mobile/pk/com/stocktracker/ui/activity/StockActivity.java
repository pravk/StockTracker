package mobile.pk.com.stocktracker.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.PagerAdapter.StockFragmentPagerAdapter;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.fragment.ContainerFragment;
import mobile.pk.com.stocktracker.ui.fragment.StockSearchResultFragment;

/**
 * Created by hello on 8/29/2015.
 */
public class StockActivity extends BaseActivity {

    public static final String STOCK_ID = "STOCK_ID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        long stockId = intent.getLongExtra(STOCK_ID, -1);
        final Stock stock = Stock.findById(Stock.class, stockId);

        Fragment fragment = new ContainerFragment() {
            @Override
            protected FragmentStatePagerAdapter getAdapter(FragmentManager fragmentManager, Context context) {
                return new StockFragmentPagerAdapter(fragmentManager,context, stock);
            }
        };

        replaceFragment(fragment,stock.getName());
    }
}
