package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.Watchlist;

/**
 * Created by hello on 8/11/2015.
 */
public class WatchlistNameChangedEvent {
    private Watchlist watchlist;

    public WatchlistNameChangedEvent(Watchlist watchlist)
    {
        this.watchlist = watchlist;
    }
    public Watchlist getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
    }
}
