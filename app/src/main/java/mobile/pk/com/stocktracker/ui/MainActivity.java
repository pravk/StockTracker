package mobile.pk.com.stocktracker.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.event.DrawerSelectionChangeEvent;
import mobile.pk.com.stocktracker.event.PortfolioChangeEvent;
import mobile.pk.com.stocktracker.event.PortfolioDeleteEvent;
import mobile.pk.com.stocktracker.event.PortfolioNameChangedEvent;
import mobile.pk.com.stocktracker.event.ShowPositionDetailEvent;
import mobile.pk.com.stocktracker.event.WatchlistNameChangedEvent;
import mobile.pk.com.stocktracker.event.WatchlistDeleteEvent;
import mobile.pk.com.stocktracker.ui.activity.BackupActivity;
import mobile.pk.com.stocktracker.ui.activity.SearchActivity;
import mobile.pk.com.stocktracker.ui.fragment.PortfolioFragment;
import mobile.pk.com.stocktracker.ui.fragment.PortfolioPositionFragment;
import mobile.pk.com.stocktracker.ui.fragment.UserSettingsFragment;
import mobile.pk.com.stocktracker.ui.activity.TransactionActivity;
import mobile.pk.com.stocktracker.ui.fragment.WatchlistFragment;
import mobile.pk.com.stocktracker.ui.fragment.WatchlistStockFragment;
import mobile.pk.com.stocktracker.watchlist.WatchlistManager;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private NavigationDrawerHelper navigationDrawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        EventBus.getDefault().register(this);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Set the menu icon instead of the launcher icon.
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        navigationDrawerHelper = new NavigationDrawerHelper(this);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        loadWorldIndices();
        //nvDrawer.getMenu().performIdentifierAction(R.id.default_watchlist, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {


            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.search).getActionView();
            ComponentName cn = new ComponentName(this, SearchActivity.class);
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(cn));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                onSearchRequested();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationDrawerHelper.setupContent(navigationView);
    }

    public void onEvent(DrawerSelectionChangeEvent event) {

        resetUI();
        MenuItem menuItem = event.getMenuItem();

        Fragment fragment = null;
        Fragment fragmentClass = null;
        if(menuItem.getItemId() == R.id.drawer_watchlist)
        {
            fragmentClass = WatchlistFragment.newInstance(this);
            /*Intent intent = new Intent(this, EditWatchlistActivity.class);
            startActivityForResult(intent, EDIT_WATCHLIST_REQUEST );*/
        }
        else if(menuItem.getItemId() == R.id.drawer_portfolio)
        {
            fragmentClass = PortfolioFragment.newInstance(this);
        }
        else if(menuItem.getItemId() ==R.id.drawer_backup_restore)
        {
            Intent intent = new Intent(this, BackupActivity.class);
            startActivity(intent);
        }
        else if(menuItem.getItemId() ==R.id.world_indices){
            loadWorldIndices();

        }
        else if(menuItem.getTitle().equals(getString(R.string.settings)))
        {
            fragmentClass = UserSettingsFragment.newInstance(this,null);
        }
        else
        {
            Intent intent = menuItem.getIntent();
            if(intent != null)
            {
                long watchlistId = intent.getLongExtra("watchlistId", 0);
                if(watchlistId != 0)
                    fragmentClass = WatchlistStockFragment.newInstance(watchlistId);
                if(fragmentClass == null)
                {
                    long portfolioId = intent.getLongExtra("portfolioId", 0);
                    fragmentClass = PortfolioPositionFragment.newInstance(portfolioId);
                }


            }

        }

        try {
            if(fragmentClass != null)
                fragment = fragmentClass;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());

        mDrawer.closeDrawers();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == BaseActivity.EDIT_WATCHLIST_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Long watchlistId = data.getLongExtra(EditWatchlistActivity.WATCH_LIST_ID, 0);
                if(watchlistId != 0) {
                    Watchlist watchlist = Watchlist.findById(Watchlist.class, watchlistId);
                    EventBus.getDefault().post(new WatchlistNameChangedEvent(watchlist));
                }
            }
        }
        else */if (requestCode == BaseActivity.EDIT_PORTFOLIO_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Long portfolioId = data.getLongExtra(EditPortfolioActivity.PORTFOLIO_ID, 0);
                if(portfolioId != 0) {
                    Portfolio portfolio = Portfolio.findById(Portfolio.class, portfolioId);
                    EventBus.getDefault().post(new PortfolioChangeEvent(portfolio));
                }
            }
        }

    }

    public void resetUI()
    {
        findViewById(R.id.sliding_tabs).setVisibility(View.GONE);
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(WatchlistNameChangedEvent event){
        Fragment fragment = WatchlistFragment.newInstance(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
    public void onEvent(WatchlistDeleteEvent event){
        Fragment fragment = WatchlistFragment.newInstance(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
    public void onEvent(PortfolioNameChangedEvent event){
        Fragment fragment = PortfolioFragment.newInstance(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
    public void onEvent(PortfolioDeleteEvent event){
        Fragment fragment = PortfolioFragment.newInstance(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
    public void onEvent(PortfolioChangeEvent event){
        setupDrawerContent(nvDrawer);
        nvDrawer.getMenu().performIdentifierAction(event.getPortfolio().getId().intValue(), 0);
    }

    public void onEvent(ShowPositionDetailEvent event){
        Intent intent = new Intent(this, TransactionActivity.class);
        intent.putExtra(TransactionActivity.POSITION_ID, event.getPosition().getId());
        startActivity(intent);
    }


    /*public void showHome()
    {
        Fragment fragment = PortfolioSummaryFragment.newInstance(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        setTitle(R.string.porfolio_summary);
        mDrawer.closeDrawers();
    }*/


    private void loadWorldIndices() {
        showProgressDialog(R.string.loading);
        WatchlistManager.getInstance().getWorldMarketWatchlist(getApplicationContext(), new WatchlistManager.WorldMarketLoadComplete() {
            @Override
            public void loadComplete(final Watchlist watchlist) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        Fragment fragment = WatchlistStockFragment.newInstance(watchlist.getId());
                        replaceFragment(fragment, null);
                    }
                });
            }
        });
    }

    @Override
    protected void replaceFragment(Fragment fragment, String title){
        super.replaceFragment(fragment,title);
        mDrawer.closeDrawers();
    }
}