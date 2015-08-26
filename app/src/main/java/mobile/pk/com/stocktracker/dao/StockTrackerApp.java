package mobile.pk.com.stocktracker.dao;

import java.util.List;

/**
 * Created by hello on 8/26/2015.
 */
public class StockTrackerApp {

    private List<Position> positionList;
    private List<UserTransaction> userTransactionList;
    private List<Portfolio> portfolioList;
    private List<Watchlist> watchlistList;
    private List<WatchlistStock> watchlistStockList;
    private List<Stock> stockList;
    private List<StockPrice> stockPriceList;

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public List<UserTransaction> getUserTransactionList() {
        return userTransactionList;
    }

    public void setUserTransactionList(List<UserTransaction> userTransactionList) {
        this.userTransactionList = userTransactionList;
    }

    public List<Portfolio> getPortfolioList() {
        return portfolioList;
    }

    public void setPortfolioList(List<Portfolio> portfolioList) {
        this.portfolioList = portfolioList;
    }

    public List<Watchlist> getWatchlistList() {
        return watchlistList;
    }

    public void setWatchlistList(List<Watchlist> watchlistList) {
        this.watchlistList = watchlistList;
    }

    public List<WatchlistStock> getWatchlistStockList() {
        return watchlistStockList;
    }

    public void setWatchlistStockList(List<WatchlistStock> watchlistStockList) {
        this.watchlistStockList = watchlistStockList;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public List<StockPrice> getStockPriceList() {
        return stockPriceList;
    }

    public void setStockPriceList(List<StockPrice> stockPriceList) {
        this.stockPriceList = stockPriceList;
    }
}
