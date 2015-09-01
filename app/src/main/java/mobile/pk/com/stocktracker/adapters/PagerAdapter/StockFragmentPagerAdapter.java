package mobile.pk.com.stocktracker.adapters.PagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.ui.fragment.RSSNewsFragment;

/**
 * Created by hello on 8/31/2015.
 */
public class StockFragmentPagerAdapter extends BaseFragmentPagerAdapter<String> {

    private final Stock stock;
    private List<String> tabs;

    public StockFragmentPagerAdapter(FragmentManager fragmentManager, Context context, Stock stock) {
        super(fragmentManager, context);
        this.stock = stock;
    }

    @Override
    protected List<String> getData() {
        if(tabs == null)
            tabs = Collections.singletonList("News");
        return tabs;
    }

    @Override
    protected Fragment getFragment(String s) {
        return RSSNewsFragment.newInstance(getContext(), stock);
    }

    @Override
    protected CharSequence getPageTitle(String s) {
        return s;
    }
}
