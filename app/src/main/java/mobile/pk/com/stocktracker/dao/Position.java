package mobile.pk.com.stocktracker.dao;

import com.orm.SugarRecord;

/**
 * Created by hello on 8/10/2015.
 */
public class Position extends SugarRecord<Position> {

    private Stock stock;
    private double quantity;
    private Portfolio portfolio;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
