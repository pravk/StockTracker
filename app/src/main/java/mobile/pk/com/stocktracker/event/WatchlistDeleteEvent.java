package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.Watchlist;

/**
 * Created by hello on 8/11/2015.
 */
public class WatchlistDeleteEvent {
    private Long watchlistId;

    public WatchlistDeleteEvent(Long watchlistId)
    {
        this.watchlistId = watchlistId;
    }

    public Long getWatchlistId() {
        return watchlistId;
    }

    public void setWatchlistId(Long watchlistId) {
        this.watchlistId = watchlistId;
    }
}
