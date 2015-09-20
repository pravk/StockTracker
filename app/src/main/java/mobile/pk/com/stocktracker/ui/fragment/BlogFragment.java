package mobile.pk.com.stocktracker.ui.fragment;

import android.content.Context;

import mobile.pk.com.stocktracker.adapters.BlogAdapter;
import mobile.pk.com.stocktracker.adapters.GenericRVAdapter;
import mobile.pk.com.stocktracker.adapters.RSSNewsAdapter;
import mobile.pk.com.stocktracker.dao.BlogPost;
import mobile.pk.com.stocktracker.dao.Stock;
import nl.matshofman.saxrssreader.RssItem;

/**
 * Created by hello on 8/31/2015.
 */
public class BlogFragment extends GenericRVFragment<BlogPost> {

    BlogAdapter adapter;

    public static BlogFragment newInstance(Context context) {
        BlogFragment fragment = new BlogFragment();
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
    protected GenericRVAdapter<BlogPost> getAdapter() {
        if(adapter == null)
            adapter = new BlogAdapter(getContext());
        return adapter;
    }

    @Override
    protected void onAddNewItem() {

    }

    @Override
    protected boolean showAddNewItem(){
        return false;
    }


    protected void loadMoreData(int currentPage){
        adapter.loadPage(currentPage);
    }
}

