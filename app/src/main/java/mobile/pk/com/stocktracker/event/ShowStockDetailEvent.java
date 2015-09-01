package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.Stock;

/**
 * Created by hello on 8/31/2015.
 */
public class ShowStockDetailEvent {
    private final Stock stock;

    public ShowStockDetailEvent(Stock stock) {
        this.stock = stock;
    }

    public Stock getStock() {
        return stock;
    }
}
