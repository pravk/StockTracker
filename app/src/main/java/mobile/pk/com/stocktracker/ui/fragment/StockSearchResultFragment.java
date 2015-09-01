package mobile.pk.com.stocktracker.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import mobile.pk.com.stocktracker.adapters.GenericRVAdapter;
import mobile.pk.com.stocktracker.adapters.StockSearchResultAdapter;
import mobile.pk.com.stocktracker.adapters.viewholder.StockSearchResultViewHolder;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.StockPrice;
import mobile.pk.com.stocktracker.dao.tasks.PriceLoadTask;
import mobile.pk.com.stocktracker.service.TickerSearchService;

/**
 * Created by hello on 8/29/2015.
 */
public class StockSearchResultFragment extends GenericRVFragment<TickerSearchService.Match> {

    private static final String QUERY = "QUERY";

    public static StockSearchResultFragment newInstance(Context context, String query) {
        StockSearchResultFragment fragment = new StockSearchResultFragment();
        fragment.setContext(context.getApplicationContext());
        Bundle args = new Bundle();
        args.putString(QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }
    StockSearchResultAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String query = getArguments().getString(QUERY);
            adapter = new StockSearchResultAdapter(getActivity(), query);
        }
    }
    protected boolean showAddNewItem(){
        return false;
    }
    @Override
    protected boolean onEditView() {
        return false;
    }

    @Override
    protected boolean onRefreshView() {
        return false;
    }

    @Override
    protected boolean showEditAction() {
        return false;
    }

    @Override
    protected boolean showRefreshAction() {
        return false;
    }

    @Override
    protected StockSearchResultAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onAddNewItem() {

    }
}
