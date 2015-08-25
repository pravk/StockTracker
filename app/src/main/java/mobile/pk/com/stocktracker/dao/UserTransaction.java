package mobile.pk.com.stocktracker.dao;

import android.text.format.DateUtils;

import com.orm.SugarRecord;

import mobile.pk.com.stocktracker.dao.tasks.HasStock;
import mobile.pk.com.stocktracker.utils.NumberUtils;

/**
 * Created by hello on 8/10/2015.
 */
public class UserTransaction extends SugarRecord<UserTransaction> implements HasStock {

    private Long transactionDate;
    private double quantity;
    private double price;
    private Stock stock;
    private Portfolio portfolio;
    private double realizedGainLoss;
    private String transactionType;

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

    public double getRealizedGainLoss() {
        return realizedGainLoss;
    }

    public void setRealizedGainLoss(double realizedGainLoss) {
        this.realizedGainLoss = realizedGainLoss;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString(){
        return String.format("%s,%s", getTransactionType(), DateUtils.getRelativeTimeSpanString(getTransactionDate()));
    }
}