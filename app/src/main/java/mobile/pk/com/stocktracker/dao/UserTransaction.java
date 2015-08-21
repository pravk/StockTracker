package mobile.pk.com.stocktracker.dao;

import com.orm.SugarRecord;

import java.util.Date;

import mobile.pk.com.stocktracker.dao.tasks.HasStock;

/**
 * Created by hello on 8/10/2015.
 */
public class UserTransaction extends SugarRecord<UserTransaction> implements HasStock{

    private Long transactionDate;
    private double quantity;
    private double price;
    private Stock stock;
    private int longShortInd;
    private Portfolio portfolio;

    public Long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Long transactionDate) {
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

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public int getLongShortInd() {
        return longShortInd;
    }

    public void setLongShortInd(int longShortInd) {
        this.longShortInd = longShortInd;
    }

    public boolean isLong() {
        return getLongShortInd() == 1;
    }
    public boolean isShort() {
        return getLongShortInd() == 2;
    }
}
