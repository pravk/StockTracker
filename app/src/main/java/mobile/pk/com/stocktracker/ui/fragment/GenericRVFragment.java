package mobile.pk.com.stocktracker.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.melnykov.fab.FloatingActionButton;
import com.orm.SugarRecord;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.GenericRVAdapter;
import mobile.pk.com.stocktracker.common.Application;
import mobile.pk.com.stocktracker.ui.BaseActivity;


public abstract class GenericRVFragment<T> extends Fragment {

    private static final String TAG = GenericRVFragment.class.getSimpleName();
    private Context context;
    Handler handler;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected Tracker mTracker;
    /*public static GenericRVFragment newInstance(Long portfolioId) {
        GenericRVFragment fragment = new GenericRVFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        handler = new Handler();
        handler.postDelayed(timeUpdater,60000);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(Application.getInstance().getApplicationContext());
        String refreshInterval = settings.getString("priceRefreshInterval", "5");
        try
        {
            final int refreshInMins = Integer.parseInt(refreshInterval);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reset();
                    if(!isDetached())
                        handler.postDelayed(this, refreshInMins*60*1000);
                }
            }, refreshInMins*60*1000);
        }
        catch (Exception e)
        {
            //Do not auto refresh
        }

        // Obtain the shared Tracker instance.
        Application application = Application.getInstance();
        mTracker = application.getDefaultTracker();

        /*try {
            EventBus.getDefault().register(this);
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }

    Runnable timeUpdater = new Runnable() {
        @Override
        public void run() {
            resetTime();

            if(!isDetached())
                handler.postDelayed(timeUpdater,60000);
        }
    };

    @Override
    public void onResume() {
       super.onResume();
        Log.i(TAG, "Setting screen name: " + this.getClass().getSimpleName());
        mTracker.setScreenName(this.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    protected void resetTime() {
        getAdapter().notifyDataSetChanged();
    }

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
        RecyclerView.LayoutManager layoutManager = getLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager)layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreData(current_page);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add_new);
        fab.setVisibility(showAddNewItem() ? View.VISIBLE : View.GONE);

        fab.attachToRecyclerView(recyclerView);

        final GenericRVAdapter adapter = getAdapter();
        recyclerView.setAdapter(adapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, 200);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.setEnabled(showRefreshAction());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(showRefreshAction())
                {
                    swipeRefreshLayout.setRefreshing(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            reset();
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(),R.string.refresh_complete, Toast.LENGTH_SHORT).show();
                        }
                    }, 200);
                }

            }
        });
        return view;
    }

    protected boolean showAddNewItem(){
        return true;
    }

    protected void loadMoreData(int currentPage){
        //TO be implented by derived classes
    }

    public void reset()
    {
        new AsyncTask<Void,Void, Void>(){
            protected void onPreExecute(Void result) {
                if(getActivity() != null)
                    ((BaseActivity)getActivity()).showProgressDialog(R.string.refreshing);
            }
            @Override
            protected Void doInBackground(Void... params) {
                getAdapter().refreshData();
                getAdapter().populatePrices(true);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if(getActivity() != null)
                    ((BaseActivity)getActivity()).hideProgressDialog();
                getAdapter().notifyDataSetChanged();
            }

        }.execute();

    }

    protected RecyclerView.LayoutManager getLayoutManager(Context context){
        return new LinearLayoutManager(context);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.generic_toolbar_menu, menu);
       // menu.findItem(R.id.refresh_view).setVisible(showRefreshAction());
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
        reset();
    }

    protected abstract boolean onEditView();

    protected boolean onRefreshView(){
        reset();
        return true;
    }

    protected abstract boolean showEditAction();

    protected abstract boolean showRefreshAction();

    protected abstract GenericRVAdapter<T> getAdapter();

    @OnClick(R.id.fab_add_new)
    public void onAddNewTransaction(){
        onAddNewItem();
    }

    protected abstract void onAddNewItem();

    public Context getContext() {
        if(context == null)
            return Application.getInstance().getApplicationContext();
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
