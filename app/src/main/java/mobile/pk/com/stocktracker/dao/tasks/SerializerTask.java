package mobile.pk.com.stocktracker.dao.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.apache.commons.collections4.IteratorUtils;

import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.StockPrice;
import mobile.pk.com.stocktracker.dao.StockTrackerApp;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.dao.WatchlistStock;

/**
 * Created by hello on 8/26/2015.
 */
public class SerializerTask extends AsyncTask<Void, Void,String>{

    @Override
    protected String doInBackground(Void... params) {
       return executeSync(params);
    }

    protected String executeSync(Void...params){

        StockTrackerApp stockTrackerApp = new StockTrackerApp();
        stockTrackerApp.setStockList(IteratorUtils.toList(Stock.findAll(Stock.class)));
        stockTrackerApp.setStockPriceList(IteratorUtils.toList(StockPrice.findAll(StockPrice.class)));
        stockTrackerApp.setWatchlistList(IteratorUtils.toList(Watchlist.findAll(Watchlist.class)));
        stockTrackerApp.setWatchlistStockList(IteratorUtils.toList(WatchlistStock.findAll(WatchlistStock.class)));
        stockTrackerApp.setPortfolioList(IteratorUtils.toList(Portfolio.findAll(Portfolio.class)));
        stockTrackerApp.setUserTransactionList(IteratorUtils.toList(UserTransaction.findAll(UserTransaction.class)));
        stockTrackerApp.setPositionList(IteratorUtils.toList(Position.findAll(Position.class)));

        Gson gson = new Gson();
        return gson.toJson(stockTrackerApp);
    }

}
