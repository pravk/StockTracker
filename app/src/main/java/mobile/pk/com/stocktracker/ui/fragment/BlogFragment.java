package mobile.pk.com.stocktracker.ui.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.BlogAdapter;
import mobile.pk.com.stocktracker.adapters.GenericRVAdapter;
import mobile.pk.com.stocktracker.dao.BlogPost;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.activity.BlogSearchActivity;

/**
 * Created by hello on 8/31/2015.
 */
public class BlogFragment extends GenericRVFragment<BlogPost> {

    public void setAdapter(BlogAdapter adapter) {
        this.adapter = adapter;
    }

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

    @Override
    public void onCreateOptionsMenu (Menu menu, final MenuInflater inflater){
        MenuItem item = menu.findItem(R.id.search);
        if(item == null)
            return;
        SearchView sv = new SearchView(((BaseActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");

                Intent intent = new Intent(getContext(), BlogSearchActivity.class);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
                return false;
            }
        });
    }


    protected void loadMoreData(int currentPage){
        adapter.loadPage(currentPage);
    }
}

