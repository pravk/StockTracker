package mobile.pk.com.stocktracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.event.WatchlistChangeEvent;
import mobile.pk.com.stocktracker.event.WatchlistDeleteEvent;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;

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

        Menu menu = navigationView.getMenu();
        menu.clear();

        Iterator<Watchlist> iterator = Watchlist.findAll(Watchlist.class);
        while (iterator != null && iterator.hasNext())
        {
            Watchlist watchlist = iterator.next();
            MenuItem subMenu = menu.add(R.id.watchlist_group, watchlist.getId().intValue(), 0, watchlist.getWatchlistName());
            subMenu.setIcon(getResources().getDrawable(R.drawable.ic_menu_black_24dp));
            Intent intent = new Intent();
            intent.putExtra("watchlistId", watchlist.getId());
            subMenu.setIntent(intent);
        }

        menu.add(R.id.watchlist_group, 0, 0, R.string.create_new_watchlist);

        menu.setGroupCheckable(R.id.watchlist_group,true, true );

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;
        Fragment fragmentClass = null;
        if(menuItem.getTitle().equals(getString(R.string.create_new_watchlist)))
        {
            Intent intent = new Intent(this, EditWatchlistActivity.class);
            startActivityForResult(intent, EDIT_WATCHLIST_REQUEST );
        }
        else
        {
            Intent intent = menuItem.getIntent();
            if(intent != null)
            {
                long watchlistId = intent.getLongExtra("watchlistId", 0);
                fragmentClass = WatchlistFragment.newInstance(watchlistId);

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
                else
                {

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

    // This method will be called when a MessageEvent is posted
    public void onEvent(WatchlistChangeEvent event){
        setupDrawerContent(nvDrawer);
        nvDrawer.getMenu().performIdentifierAction(event.getWatchlist().getId().intValue(), 0);
    }
    // This method will be called when a MessageEvent is posted
    public void onEvent(WatchlistDeleteEvent event){
        setupDrawerContent(nvDrawer);
        showHome();
    }

    public void showHome()
    {
        HomeFragment fragment = HomeFragment.newInstance(null,null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        setTitle(R.string.app_name);
    }
}