package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Stock;

/**
 * Created by hello on 8/11/2015.
 */
public class TransactionDeleteEvent {
    private Stock stock;
    private Portfolio portfolio;


    public TransactionDeleteEvent(Stock stock, Portfolio portfolio) {
        this.stock = stock;
        this.portfolio = portfolio;
    }

    public Stock getStock() {
        return stock;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }
}
