package mobile.pk.com.stocktracker.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.WatchListAdapter;
import mobile.pk.com.stocktracker.adapters.viewholder.WatchlistStockViewHolder;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.dao.WatchlistStock;
import mobile.pk.com.stocktracker.event.WatchlistChangeEvent;
import mobile.pk.com.stocktracker.event.WatchlistDeleteEvent;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.EditWatchlistActivity;
import mobile.pk.com.stocktracker.ui.SelectStockDialog;

public class WatchlistFragment extends GenericRVFragment<WatchlistStockViewHolder> {

   private static final String WATCH_LIST_ID = "WATCH_LIST_ID";

    // TODO: Rename and change types of parameters
   private long watchListId;
   private WatchListAdapter watchlistAdapter;
    private Watchlist watchlist;

    public static WatchlistFragment newInstance(Long watchListId) {
        WatchlistFragment fragment = new WatchlistFragment();
        Bundle args = new Bundle();
        args.putLong(WATCH_LIST_ID, watchListId);
        fragment.setArguments(args);
        return fragment;
    }

    @InjectView(R.id.fab_add_new_stock)
    FloatingActionButton fabAddNewStock;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            watchListId = getArguments().getLong(WATCH_LIST_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    protected boolean hasMenuOptions() {
        return true;
    }

    @Override
    protected boolean onEditView() {
        Intent intent = new Intent(getActivity(), EditWatchlistActivity.class);
        intent.putExtra(EditWatchlistActivity.WATCH_LIST_ID, watchListId);
        startActivityForResult(intent, BaseActivity.EDIT_WATCHLIST_REQUEST);
        return true;
    }

    @Override
    protected boolean onRefreshView() {
        watchlistAdapter.refreshPrices();
        return true;
    }

    @Override
    protected boolean showEditAction() {
        return true;
    }

    @Override
    protected boolean showRefreshAction() {
        return true;
    }

    @Override
    protected RecyclerView.Adapter<WatchlistStockViewHolder> getAdapter() {
        if(watchlistAdapter == null)
        {
            watchlist = Watchlist.findById(Watchlist.class, watchListId);
            watchlistAdapter = new WatchListAdapter(getActivity(), watchlist );
        }
        return null;
    }

    @Override
    protected void onAddNewClick() {
        SelectStockDialog.newInstance("Add new Stock", new SelectStockDialog.SelectStockDialogListener() {
            @Override
            public void onStockSelect(Stock stock) {
                WatchlistStock watchlistStock = WatchlistStock.from(stock, watchlist);

                watchlistAdapter.addItem(watchlistStock);
                Toast.makeText(getActivity(), stock.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Selection cancelled", Toast.LENGTH_SHORT).show();
            }
        }).show(getChildFragmentManager(), null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == BaseActivity.EDIT_WATCHLIST_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Long watchlistId = data.getLongExtra(EditWatchlistActivity.WATCH_LIST_ID, 0);
                Watchlist watchlist = Watchlist.findById(Watchlist.class, watchlistId);
                if(watchlist != null)
                    EventBus.getDefault().post(new WatchlistChangeEvent(watchlist));
                else
                    EventBus.getDefault().post(new WatchlistDeleteEvent(watchlistId));
            }
        }
    }
}
