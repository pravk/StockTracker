package mobile.pk.com.stocktracker.adapters.PagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.Collection;
import java.util.List;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.ui.fragment.WatchlistStockFragment;
import mobile.pk.com.stocktracker.watchlist.WatchlistManager;

/**
 * Created by hello on 8/21/2015.
 */
public class WatchlistFragmentPagerAdapter extends BaseFragmentPagerAdapter<Watchlist> {

    List<Watchlist> watchlistList;

    public WatchlistFragmentPagerAdapter(FragmentManager fragmentManager, Context context)
    {
        super(fragmentManager, context);
    }

    @Override
    protected Fragment getFragment(Watchlist watchlist) {
        return WatchlistStockFragment.newInstance(watchlist.getId());
    }

    @Override
    protected CharSequence getPageTitle(Watchlist watchlist) {
        return watchlist.getWatchlistName();
    }

    @Override
    protected List<Watchlist> getData() {
        return WatchlistManager.getInstance().getUserWatchlists(getContext());
    }
}
