package mobile.pk.com.stocktracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.event.DrawerSelectionChangeEvent;
import mobile.pk.com.stocktracker.event.EditTransactionEvent;
import mobile.pk.com.stocktracker.event.PortfolioChangeEvent;
import mobile.pk.com.stocktracker.event.ShowPositionDetailEvent;
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.event.WatchlistChangeEvent;
import mobile.pk.com.stocktracker.event.WatchlistDeleteEvent;
import mobile.pk.com.stocktracker.ui.activity.EditTransactionActivity;
import mobile.pk.com.stocktracker.ui.activity.TransactionActivity;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private NavigationDrawerHelper navigationDrawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

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
        showHome();
        //nvDrawer.getMenu().performIdentifierAction(R.id.default_watchlist, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
       /* SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_tracker).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationDrawerHelper.setupContent(navigationView);
    }

    public void onEvent(DrawerSelectionChangeEvent event) {

        MenuItem menuItem = event.getMenuItem();

        Fragment fragment = null;
        Fragment fragmentClass = null;
        if(menuItem.getTitle().equals(getString(R.string.create_new_watchlist)))
        {
            Intent intent = new Intent(this, EditWatchlistActivity.class);
            startActivityForResult(intent, EDIT_WATCHLIST_REQUEST );
        }
        else if(menuItem.getTitle().equals(getString(R.string.create_new_portfolio)))
        {
            Intent intent = new Intent(this, EditPortfolioActivity.class);
            startActivityForResult(intent, EDIT_PORTFOLIO_REQUEST );
        }
        else
        {
            Intent intent = menuItem.getIntent();
            if(intent != null)
            {
                long watchlistId = intent.getLongExtra("watchlistId", 0);
                if(watchlistId != 0)
                    fragmentClass = WatchlistFragment.newInstance(watchlistId);
                if(fragmentClass == null)
                {
                    long portfolioId = intent.getLongExtra("portfolioId", 0);
                    fragmentClass = PortfolioFragment.newInstance(portfolioId);
                }

                try {
                    if(fragmentClass != null)
                        fragment = fragmentClass;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                // Highlight the selected item, update the title, and close the drawer
                menuItem.setChecked(true);
                setTitle(menuItem.getTitle());
            }

        }

        mDrawer.closeDrawers();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseActivity.EDIT_WATCHLIST_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Long watchlistId = data.getLongExtra(EditWatchlistActivity.WATCH_LIST_ID, 0);
                if(watchlistId != 0) {
                    Watchlist watchlist = Watchlist.findById(Watchlist.class, watchlistId);
                    EventBus.getDefault().post(new WatchlistChangeEvent(watchlist));
                }
            }
        }
        else if (requestCode == BaseActivity.EDIT_PORTFOLIO_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Long portfolioId = data.getLongExtra(EditPortfolioActivity.PORTFOLIO_ID, 0);
                if(portfolioId != 0) {
                    Portfolio portfolio = Portfolio.findById(Portfolio.class, portfolioId);
                    EventBus.getDefault().post(new PortfolioChangeEvent(portfolio));
                }
            }
        }

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

    public void onEvent(WatchlistChangeEvent event){
        setupDrawerContent(nvDrawer);
        nvDrawer.getMenu().performIdentifierAction(event.getWatchlist().getId().intValue(), 0);
    }
    public void onEvent(PortfolioChangeEvent event){
        setupDrawerContent(nvDrawer);
        nvDrawer.getMenu().performIdentifierAction(event.getPortfolio().getId().intValue(), 0);
    }
    public void onEvent(WatchlistDeleteEvent event){
        setupDrawerContent(nvDrawer);
        showHome();
    }
    public void onEvent(ShowPositionDetailEvent event){
        Intent intent = new Intent(this, TransactionActivity.class);
        intent.putExtra(TransactionActivity.POSITION_ID, event.getPosition().getId());
        startActivity(intent);
    }


    public void showHome()
    {
        HomeFragment fragment = HomeFragment.newInstance(null,null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        setTitle(R.string.app_name);
    }
}