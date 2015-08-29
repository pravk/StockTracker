package mobile.pk.com.stocktracker.ui.fragment;

import android.os.Bundle;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.adapters.GenericRVAdapter;
import mobile.pk.com.stocktracker.adapters.WatchListStockAdapter;
import mobile.pk.com.stocktracker.adapters.viewholder.WatchlistStockViewHolder;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.dao.WatchlistStock;
import mobile.pk.com.stocktracker.event.WatchlistRefreshEvent;
import mobile.pk.com.stocktracker.ui.SelectStockDialog;

public class WatchlistStockFragment extends GenericRVFragment<WatchlistStockViewHolder> {

   private static final String WATCH_LIST_ID = "WATCH_LIST_ID";

    // TODO: Rename and change types of parameters
   private long watchListId;
   private WatchListStockAdapter watchlistAdapter;
    private Watchlist watchlist;

    public static WatchlistStockFragment newInstance(Long watchListId) {
        WatchlistStockFragment fragment = new WatchlistStockFragment();
        Bundle args = new Bundle();
        args.putLong(WATCH_LIST_ID, watchListId);
        fragment.setArguments(args);
        return fragment;
    }

   /* @InjectView(R.id.fab_add_new_stock)
    FloatingActionButton fabAddNewStock;*/

    public WatchlistStockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            watchListId = getArguments().getLong(WATCH_LIST_ID);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected boolean onEditView() {
        EventBus.getDefault().post(new OnEditEvent(watchlist));
        return true;
        /*
        return true;*/
    }

    @Override
    protected  boolean onCreateNew(){

        EventBus.getDefault().post(new OnCreateEvent());
        return true;

    }


    @Override
    protected boolean onRefreshView() {
        getAdapter().populatePrices(true);
        return true;
    }

    @Override
    protected boolean showEditAction() {
        return !watchlist.isSystem();
    }

    @Override
    protected boolean showCreateAction() {
        return !watchlist.isSystem();
    }

    @Override
    protected boolean showRefreshAction() {
        return true;
    }

    @Override
    protected void onAddNewItem() {
        SelectStockDialog.newInstance("Add new Stock", new SelectStockDialog.SelectStockDialogListener() {
            @Override
            public void onStockSelect(Stock stock) {
                WatchlistStock watchlistStock = WatchlistStock.from(stock, watchlist);
                reset();
                Toast.makeText(getActivity(), stock.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Selection cancelled", Toast.LENGTH_SHORT).show();
            }
        }).show(getChildFragmentManager(), null);
    }

    @Override
    protected GenericRVAdapter getAdapter() {
        if(watchlistAdapter == null)
        {
            watchlist = Watchlist.findById(Watchlist.class, watchListId);
            watchlistAdapter = new WatchListStockAdapter(getActivity(), watchlist );
        }
        return watchlistAdapter;
    }

    public void onEvent(WatchlistRefreshEvent event){
        reset();
    }

    public class OnCreateEvent {

    }

    public class OnEditEvent{
        private final Watchlist data;

        public OnEditEvent(Watchlist data)
        {
            this.data = data;
        }

        public Watchlist getData() {
            return data;
        }
    }
    public class OnDeleteEvent {
        private final Watchlist data;

        public OnDeleteEvent(Watchlist data)
        {
            this.data = data;
        }
    }
}
