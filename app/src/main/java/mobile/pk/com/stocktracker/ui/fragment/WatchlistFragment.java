package mobile.pk.com.stocktracker.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.adapters.PagerAdapter.WatchlistFragmentPagerAdapter;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.event.WatchlistDeleteEvent;
import mobile.pk.com.stocktracker.event.WatchlistNameChangedEvent;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.EditWatchlistActivity;

/**
 * Created by hello on 8/20/2015.
 */
public class WatchlistFragment extends ContainerFragment {

    public static WatchlistFragment newInstance(Context context) {
        WatchlistFragment fragment = new WatchlistFragment();
        fragment.setContext(context);
        EventBus.getDefault().register(fragment);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseActivity.EDIT_WATCHLIST_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Long watchlistId = data.getLongExtra(EditWatchlistActivity.WATCH_LIST_ID, 0);
                Watchlist watchlist = Watchlist.findById(Watchlist.class, watchlistId);
                if(watchlist != null)
                    EventBus.getDefault().post(new WatchlistNameChangedEvent(watchlist));
                else
                    EventBus.getDefault().post(new WatchlistDeleteEvent(watchlistId));
            }
        }
    }

    public void onEvent(WatchlistStockFragment.OnCreateEvent event){
        Intent intent = new Intent(getActivity(), EditWatchlistActivity.class);
        startActivityForResult(intent, BaseActivity.EDIT_WATCHLIST_REQUEST);
    }
    public void onEvent(WatchlistStockFragment.OnEditEvent event){
        Intent intent = new Intent(getActivity(), EditWatchlistActivity.class);
        intent.putExtra(EditWatchlistActivity.WATCH_LIST_ID, event.getData().getId());
        startActivityForResult(intent, BaseActivity.EDIT_WATCHLIST_REQUEST);
    }


    @Override
    protected FragmentStatePagerAdapter getAdapter(FragmentManager fragmentManager, Context context) {
        return new WatchlistFragmentPagerAdapter(fragmentManager,context);
    }
}
