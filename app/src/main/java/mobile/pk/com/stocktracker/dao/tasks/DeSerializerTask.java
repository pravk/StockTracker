package mobile.pk.com.stocktracker.dao.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.orm.SugarRecord;

import java.util.Iterator;
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

    protected Watchlist getWatchlistFromDB(Watchlist watchlist)
    {
        List<Watchlist> watchlistList = Watchlist.find(Watchlist.class, "watchlist_name=?", watchlist.getWatchlistName());
        if(watchlistList != null && watchlistList.size()>0)
            return watchlistList.get(0);
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
            Iterator<UserTransaction> iteratorTxn = stockTrackerApp.getUserTransactionList().iterator();
            while (iteratorTxn.hasNext()) {
                UserTransaction userTransaction = iteratorTxn.next();
                userTransaction.setStock(getStockFromDB(userTransaction.getStock()));
                userTransaction.setPortfolio(getPortfolioFromDB(userTransaction.getPortfolio()));
            }
            saveRecordList(stockTrackerApp.getUserTransactionList(), UserTransaction.class);
        }

        //stockTrackerApp.setPositionList(IteratorUtils.toList(Position.findAll(Position.class)));

        if(stockTrackerApp.getWatchlistStockList() != null) {
            Iterator<WatchlistStock> iterator = stockTrackerApp.getWatchlistStockList().iterator();
            while (iterator.hasNext()) {
                WatchlistStock watchlistStock = iterator.next();
                if (watchlistStock.getStock() == null || watchlistStock.getWatchlist() == null) {
                    iterator.remove();
                    continue;
                }
                else {
                    watchlistStock.setStock(getStockFromDB(watchlistStock.getStock()));
                    watchlistStock.setWatchlist(getWatchlistFromDB(watchlistStock.getWatchlist()));
                }
            }
            saveRecordList(stockTrackerApp.getWatchlistStockList(), WatchlistStock.class);
        }
        if(stockTrackerApp.getPositionList() != null) {
            Iterator<Position> iteratorPosition = stockTrackerApp.getPositionList().iterator();
            while (iteratorPosition.hasNext()) {
                Position position = iteratorPosition.next();
                if (position.getStock() == null || position.getPortfolio() == null) {
                    iteratorPosition.remove();
                    continue;
                }
                else {
                    position.setStock(getStockFromDB(position.getStock()));
                    position.setPortfolio(getPortfolioFromDB(position.getPortfolio()));
                }
            }
            if(stockTrackerApp.getPositionList().size() >0) {
                saveRecordList(stockTrackerApp.getPositionList(), Position.class);
                EventBus.getDefault().post(new Position.PositionChangeEvent(stockTrackerApp.getPositionList().get(0)));
            }
        }

    }
}
