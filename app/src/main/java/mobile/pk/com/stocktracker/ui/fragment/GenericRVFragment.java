package mobile.pk.com.stocktracker.ui.fragment;

import android.content.Context;
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

import com.orm.SugarRecord;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.GenericRVAdapter;


public abstract class GenericRVFragment<T extends RecyclerView.ViewHolder> extends Fragment {

    private Context context;

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
        /*try {
            EventBus.getDefault().register(this);
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/
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

        GenericRVAdapter adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        adapter.reset();
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
        menu.findItem(R.id.create_new).setVisible(showCreateAction());

    }

    protected boolean showCreateAction() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.refresh_view:
                return onRefreshView();

            case R.id.edit_view:
                return onEditView();
            case R.id.create_new:
                return onCreateNew();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected  boolean onCreateNew(){
        return true;
    }

    protected void refreshData(){
        this.getAdapter().reset();
    }

    protected abstract boolean onEditView();

    protected abstract boolean onRefreshView();

    protected abstract boolean showEditAction();

    protected abstract boolean showRefreshAction();

    protected abstract <D extends SugarRecord> GenericRVAdapter<T,D> getAdapter();

    @OnClick(R.id.fab_add_new)
    public void onAddNewTransaction(){
        onAddNewClick();
    }

    protected abstract void onAddNewClick();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class OnCreateEvent<T> {
        private final T data;

        public OnCreateEvent(T data)
        {
            this.data = data;
        }
    }
    public class OnEditEvent<T> {
        private final T data;

        public OnEditEvent(T data)
        {
            this.data = data;
        }
    }
    public class OnDeleteEvent<T> {
        private final T data;

        public OnDeleteEvent(T data)
        {
            this.data = data;
        }
    }
}
