package mobile.pk.com.stocktracker.dao.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.orm.SugarRecord;

import java.util.List;

import de.greenrobot.event.EventBus;
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
public class DeSerializerTask extends AsyncTask<String, Void,Void>{

    @Override
    protected Void doInBackground(String... params) {
        executeSync(params[0]);
        return null;
    }

    private <D extends SugarRecord<D>> void saveRecordList(List<D> records, Class<D> klass)
    {
        if(records == null)
            return;
        D.deleteAll(klass);
        for (D record :
                records) {
            record.setId(null);
            record.save();
        }
        //D.saveInTx(records);
    }

    protected Stock getStockFromDB(Stock stock)
    {
        List<Stock> stockList = Stock.find(Stock.class, "client_id=?", stock.getClientId());
        if(stockList != null && stockList.size()>0)
            return stockList.get(0);
        return null;
    }
    protected Portfolio getPortfolioFromDB(Portfolio portfolio)
    {
        List<Portfolio> portfolioList = Portfolio.find(Portfolio.class, "portfolio_name=?", portfolio.getPortfolioName());
        if(portfolioList != null && portfolioList.size()>0)
            return portfolioList.get(0);
        return null;
    }

    public void executeSync(String data) {
        Gson gson = new Gson();

        StockTrackerApp stockTrackerApp = gson.fromJson(data, StockTrackerApp.class);

        saveRecordList(stockTrackerApp.getStockList(), Stock.class);
        saveRecordList(stockTrackerApp.getStockPriceList(), StockPrice.class);
        saveRecordList(stockTrackerApp.getWatchlistList(), Watchlist.class);
        saveRecordList(stockTrackerApp.getPortfolioList(), Portfolio.class);

        if(stockTrackerApp.getUserTransactionList() != null) {
            for (UserTransaction userTransaction :
                    stockTrackerApp.getUserTransactionList()) {
                userTransaction.setStock(getStockFromDB(userTransaction.getStock()));
                userTransaction.setPortfolio(getPortfolioFromDB(userTransaction.getPortfolio()));
            }
            saveRecordList(stockTrackerApp.getUserTransactionList(), UserTransaction.class);
        }

        //stockTrackerApp.setPositionList(IteratorUtils.toList(Position.findAll(Position.class)));

        if(stockTrackerApp.getWatchlistStockList() != null) {
            for (WatchlistStock watchlistStock :
                    stockTrackerApp.getWatchlistStockList()) {
                if (watchlistStock.getStock() == null) {
                    continue;
                }
                watchlistStock.setStock(getStockFromDB(watchlistStock.getStock()));
            }
            saveRecordList(stockTrackerApp.getWatchlistStockList(), WatchlistStock.class);
        }
        if(stockTrackerApp.getPositionList() != null) {
            for (Position position :
                    stockTrackerApp.getPositionList()) {
                if (position.getStock() == null || position.getPortfolio() == null) {
                    continue;
                }
                position.setStock(getStockFromDB(position.getStock()));
                position.setPortfolio(getPortfolioFromDB(position.getPortfolio()));
            }
            saveRecordList(stockTrackerApp.getPositionList(), Position.class);
            EventBus.getDefault().post(new Position.PositionChangeEvent(stockTrackerApp.getPositionList().get(0)));
        }

    }
}
