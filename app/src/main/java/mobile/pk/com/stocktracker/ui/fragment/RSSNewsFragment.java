package mobile.pk.com.stocktracker.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import mobile.pk.com.stocktracker.adapters.GenericRVAdapter;
import mobile.pk.com.stocktracker.adapters.RSSNewsAdapter;
import mobile.pk.com.stocktracker.dao.Stock;
import nl.matshofman.saxrssreader.RssItem;

/**
 * Created by hello on 8/31/2015.
 */
public class RSSNewsFragment extends GenericRVFragment<RssItem> {

    RSSNewsAdapter adapter;
    private Stock stock;

    public static RSSNewsFragment newInstance(Context context, Stock stock) {
        RSSNewsFragment fragment = new RSSNewsFragment();
        fragment.stock = stock;
        fragment.setContext(context);
        return fragment;
    }

    @Override
    protected boolean onEditView() {
        return false;
    }

    @Override
    protected boolean showEditAction() {
        return false;
    }

    @Override
    protected boolean showRefreshAction() {
        return true;
    }

    @Override
    protected GenericRVAdapter<RssItem> getAdapter() {
        if(adapter == null)
            adapter = new RSSNewsAdapter(getContext(), stock);
        return adapter;
    }

    @Override
    protected void onAddNewItem() {

    }
}
