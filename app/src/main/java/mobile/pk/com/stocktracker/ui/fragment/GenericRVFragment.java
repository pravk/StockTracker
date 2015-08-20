package mobile.pk.com.stocktracker.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.PortfolioAdapter;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioViewHolder;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.event.PortfolioChangeEvent;
import mobile.pk.com.stocktracker.event.PortfolioDeleteEvent;
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.event.TransactionDeleteEvent;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.EditPortfolioActivity;
import mobile.pk.com.stocktracker.ui.activity.EditTransactionActivity;


public abstract class GenericRVFragment<T extends RecyclerView.ViewHolder> extends Fragment {

   private RecyclerView.Adapter<T> adapter;

    /*public static GenericRVFragment newInstance(Long portfolioId) {
        GenericRVFragment fragment = new GenericRVFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(hasMenuOptions());
        EventBus.getDefault().register(this);
    }

    protected abstract boolean hasMenuOptions();

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rv_generic, container, false);
        ButterKnife.inject(this, view);

        final RecyclerView recyclerView =   (RecyclerView) view.findViewById(R.id.recyler_view);
        recyclerView.setLayoutManager(getLayoutManager(getActivity()));

        recyclerView.setAdapter(getAdapter());

        return view;
    }

    protected RecyclerView.LayoutManager getLayoutManager(Context context){
        return new LinearLayoutManager(context);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.generic_toolbar_menu, menu);
        menu.findItem(R.id.refresh_view).setVisible(showRefreshAction());
        menu.findItem(R.id.edit_view).setVisible(showEditAction());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.refresh_view:
                return onRefreshView();

            case R.id.edit_view:
                return onEditView();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract boolean onEditView();

    protected abstract boolean onRefreshView();

    protected abstract boolean showEditAction();

    protected abstract boolean showRefreshAction();

    protected abstract RecyclerView.Adapter<T> getAdapter();

    @OnClick(R.id.fab_add_new)
    public void onAddNewTransaction(){
        onAddNewClick();
    }

    protected abstract void onAddNewClick();

}
