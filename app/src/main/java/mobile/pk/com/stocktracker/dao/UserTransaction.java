package mobile.pk.com.stocktracker.dao;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by hello on 8/10/2015.
 */
public class UserTransaction extends SugarRecord<UserTransaction> {

    private Date transactionDate;
    private double quantity;
    private double price;
    private Stock stock;
    private int buySellInd;
    private Portfolio portfolio;

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getBuySellInd() {
        return buySellInd;
    }

    public void setBuySellInd(int buySellInd) {
        this.buySellInd = buySellInd;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
