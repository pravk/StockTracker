package mobile.pk.com.stocktracker.dao;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by hello on 8/10/2015.
 */
public class WatchlistStock extends SugarRecord<Watchlist>{

    private Stock stock;
    private Watchlist watchlist;
    private Date creationTS;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Watchlist getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
    }

    public Date getCreationTS() {
        return creationTS;
    }

    public void setCreationTS(Date creationTS) {
        this.creationTS = creationTS;
    }

    public static WatchlistStock from(Stock stock, Watchlist watchlist) {
        WatchlistStock watchlistStock = new WatchlistStock();
        watchlistStock.setStock(stock);
        watchlistStock.setWatchlist(watchlist);
        watchlistStock.setCreationTS(new Date());
        watchlistStock.save();
        return watchlistStock;
    }
}

