package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.Watchlist;

/**
 * Created by hello on 8/27/2015.
 */
public class WatchlistRefreshEvent {
    private Watchlist watchlist;

    public WatchlistRefreshEvent(Watchlist watchlist) {
        this.watchlist = watchlist;
    }

    public Watchlist getWatchlist() {
        return watchlist;
    }
}

