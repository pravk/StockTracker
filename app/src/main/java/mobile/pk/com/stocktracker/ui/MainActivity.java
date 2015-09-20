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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.webkit.WebView;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.event.DrawerSelectionChangeEvent;
import mobile.pk.com.stocktracker.event.PortfolioChangeEvent;
import mobile.pk.com.stocktracker.event.PortfolioDeleteEvent;
import mobile.pk.com.stocktracker.event.PortfolioNameChangedEvent;
import mobile.pk.com.stocktracker.event.ShowPositionDetailEvent;
import mobile.pk.com.stocktracker.event.ViewBlogPostEvent;
import mobile.pk.com.stocktracker.event.WatchlistNameChangedEvent;
import mobile.pk.com.stocktracker.event.WatchlistDeleteEvent;
import mobile.pk.com.stocktracker.ui.activity.BackupActivity;
import mobile.pk.com.stocktracker.ui.activity.SearchActivity;
import mobile.pk.com.stocktracker.ui.activity.WebviewActivity;
import mobile.pk.com.stocktracker.ui.fragment.BlogFragment;
import mobile.pk.com.stocktracker.ui.fragment.PortfolioFragment;
import mobile.pk.com.stocktracker.ui.fragment.PortfolioPositionFragment;
import mobile.pk.com.stocktracker.ui.fragment.UserSettingsFragment;
import mobile.pk.com.stocktracker.ui.activity.TransactionActivity;
import mobile.pk.com.stocktracker.ui.fragment.WatchlistFragment;
import mobile.pk.com.stocktracker.ui.fragment.WatchlistStockFragment;
import mobile.pk.com.stocktracker.watchlist.WatchlistManager;

public class MainActivity extends BaseActivity {

    private static final java.lang.String SELECTED_TAB = "SELECTED_TAB";
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
        if(savedInstanceState == null || TextUtils.isEmpty(savedInstanceState.getString(SELECTED_TAB))) {
            loadWorldIndices();
        }
        //nvDrawer.getMenu().performIdentifierAction(R.id.default_watchlist, 0);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(SELECTED_TAB, "WI");
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
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
           loadWatchlist();
        }
        else if(menuItem.getItemId() == R.id.drawer_portfolio)
        {
            loadPortfolio();
        }
        else if(menuItem.getItemId() ==R.id.drawer_backup_restore)
        {
            Intent intent = new Intent(this, BackupActivity.class);
            startActivity(intent);
        }
        else if(menuItem.getItemId() ==R.id.world_indices){
            loadWorldIndices();

        }
        else if(menuItem.getItemId() ==R.id.blog){
            loadBlogs();

        }
        else if(menuItem.getTitle().equals(getString(R.string.settings)))
        {
            fragment = UserSettingsFragment.newInstance(this,null);
            replaceFragment(fragment, getString(R.string.settings));
        }
        menuItem.setChecked(true);

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
        loadWatchlist();
    }
    public void onEvent(WatchlistDeleteEvent event){
        loadWatchlist();
    }
    public void onEvent(PortfolioNameChangedEvent event){
        loadPortfolio();
    }
    public void onEvent(PortfolioDeleteEvent event){
       loadPortfolio();
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

    public void onEvent(ViewBlogPostEvent event){
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra(WebviewActivity.CONTENT, event.getBlogPost().getContent());
        intent.putExtra(WebviewActivity.TITLE, event.getBlogPost().getTitle());
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

    private void loadWatchlist(){
        Fragment fragment = WatchlistFragment.newInstance(this);
        replaceFragment(fragment, getString(R.string.watchlist));
    }

    private void loadPortfolio(){
        Fragment fragment = PortfolioFragment.newInstance(this);
        replaceFragment(fragment, getString(R.string.portfolio));
    }
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
                        replaceFragment(fragment, getString(R.string.world_indices));
                    }
                });
            }
        });
    }

    private void loadBlogs() {
        showProgressDialog(R.string.loading);
        Fragment fragment = BlogFragment.newInstance(this);
        replaceFragment(fragment, getString(R.string.value_picks));
    }

    @Override
    protected void replaceFragment(Fragment fragment, String title){
        super.replaceFragment(fragment,title);
        mDrawer.closeDrawers();
    }
}