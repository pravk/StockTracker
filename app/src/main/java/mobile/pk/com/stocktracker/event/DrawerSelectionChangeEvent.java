package mobile.pk.com.stocktracker.event;

import android.view.MenuItem;

/**
 * Created by hello on 8/12/2015.
 */
public class DrawerSelectionChangeEvent {
    private MenuItem menuItem;

    public DrawerSelectionChangeEvent(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
}
