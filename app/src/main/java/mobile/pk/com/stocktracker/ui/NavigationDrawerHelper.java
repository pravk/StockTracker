package mobile.pk.com.stocktracker.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Iterator;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.event.DrawerSelectionChangeEvent;

/**
 * Created by hello on 8/12/2015.
 */
public class NavigationDrawerHelper {

    private Context context;

    public NavigationDrawerHelper(Context context) {
        this.context = context;
    }

    public void setupContent(NavigationView navigationView)
    {
        /*Menu menu = navigationView.getMenu();
        menu.clear();

        Iterator<Watchlist> iterator = Watchlist.findAll(Watchlist.class);
        while (iterator != null && iterator.hasNext())
        {
            Watchlist watchlist = iterator.next();
            MenuItem subMenu = menu.add(R.id.watchlist_group, watchlist.getId().intValue(), 0, watchlist.getWatchlistName());
            subMenu.setIcon(context.getResources().getDrawable(R.drawable.ic_menu_black_24dp));
            Intent intent = new Intent();
            intent.putExtra("watchlistId", watchlist.getId());
            subMenu.setIntent(intent);
        }

        menu.add(R.id.watchlist_group, 0, 0, R.string.create_new_watchlist);


        Iterator<Portfolio> portfolioIterator = Portfolio.findAll(Portfolio.class);
        while (portfolioIterator != null && portfolioIterator.hasNext())
        {
            Portfolio portfolio = portfolioIterator.next();
            MenuItem subMenu = menu.add(R.id.portfolio_group, portfolio.getId().intValue(), 0, portfolio.getPortfolioName());
            subMenu.setIcon(context.getResources().getDrawable(R.drawable.ic_menu_black_24dp));
            Intent intent = new Intent();
            intent.putExtra("portfolioId", portfolio.getId());
            subMenu.setIntent(intent);
        }

        menu.add(R.id.portfolio_group, 0, 0, R.string.create_new_portfolio);

        //Extras
        menu.add(R.id.extras_group, 0, 0, R.string.settings);
        menu.add(R.id.extras_group, 0, 0, R.string.about);


        menu.setGroupCheckable(R.id.watchlist_group, true, true);
        menu.setGroupCheckable(R.id.portfolio_group,true, true );
        menu.setGroupCheckable(R.id.extras_group,true, true );
*/

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        EventBus.getDefault().post(new DrawerSelectionChangeEvent(menuItem));
                        //selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

}
