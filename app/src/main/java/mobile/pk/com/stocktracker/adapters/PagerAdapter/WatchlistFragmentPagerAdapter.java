package mobile.pk.com.stocktracker.adapters.PagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.Collection;
import java.util.List;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.ui.fragment.WatchlistStockFragment;

/**
 * Created by hello on 8/21/2015.
 */
public class WatchlistFragmentPagerAdapter extends BaseFragmentPagerAdapter<Watchlist> {

    List<Watchlist> watchlistList;

    public WatchlistFragmentPagerAdapter(FragmentManager fragmentManager, Context context)
    {
        super(fragmentManager, context);
    }

    private void createDefaultWatchList() {
        Watchlist watchlist = new Watchlist();
        watchlist.setWatchlistName(getContext().getString(R.string.default_watchlist_name));
        watchlist.save();
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
        List<Watchlist> watchlistList = Watchlist.find(Watchlist.class, null);
        if(watchlistList == null || watchlistList.size()==0)
        {
            createDefaultWatchList();
            watchlistList = Watchlist.find(Watchlist.class, null);
        }
        return watchlistList;
    }
}
