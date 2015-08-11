package mobile.pk.com.stocktracker.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.WatchListAdapter;
import mobile.pk.com.stocktracker.common.Application;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.Watchlist;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WatchlistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WatchlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchlistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String WATCH_LIST_ID = "WATCH_LIST_ID";

    // TODO: Rename and change types of parameters
   private long watchListId;
    private WatchListAdapter watchlistAdapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        ButterKnife.inject(this, view);

        final RecyclerView recyclerView =   (RecyclerView) view.findViewById(R.id.watchlist);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        Watchlist watchlist = Watchlist.findById(Watchlist.class, watchListId);
        watchlistAdapter = new WatchListAdapter(getActivity(), watchlist, ((Application)getActivity().getApplication()).getRestClient().getPricingService() );
        recyclerView.setAdapter(watchlistAdapter);

        return view;
    }

    @OnClick(R.id.fab_add_new_stock)
    public void onAddNewStock(){
        SelectStockDialog.newInstance("Add new Stock", new SelectStockDialog.SelectStockDialogListener() {
            @Override
            public void onStockSelect(Stock stock) {
                watchlistAdapter.addStock(stock);
                Toast.makeText(getActivity(), stock.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(),"Selection cancelled", Toast.LENGTH_SHORT).show();
            }
        }).show(getChildFragmentManager(), null);
    }

    @Override
    public void onCreateOptionsMenu(
        Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.watchlist_toolbar_menu, menu);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.refresh_view:
                watchlistAdapter.refreshPrices(watchlistAdapter.getWatchListStocks());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
