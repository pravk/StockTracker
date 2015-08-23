package mobile.pk.com.stocktracker.dao;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

import mobile.pk.com.stocktracker.dao.tasks.HasStock;

/**
 * Created by hello on 8/10/2015.
 */
public class UserTransaction extends SugarRecord<UserTransaction> implements HasStock {

    public static final int LONG = 1;
    public static final int SHORT = -1;

    private Long transactionDate;
    private double quantity;
    private double price;
    private Stock stock;
    private int longShortInd;
    private Portfolio portfolio;
    private double realizedGainLoss;

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
        return getLongShortInd() == LONG;
    }

    public boolean isShort() {
        return getLongShortInd() == SHORT;
    }

    public double getRealizedGainLoss() {
        return realizedGainLoss;
    }

    public void setRealizedGainLoss(double realizedGainLoss) {
        this.realizedGainLoss = realizedGainLoss;
    }

    @Override
    public void save() {
        if (this.isShort()) {
            double[] avgPriceAndQuantity = Position.getAveragePurchasePrice(stock, portfolio, getTransactionDate());
            double avgPrice = avgPriceAndQuantity[0];
            //double quantity = avgPriceAndQuantity[1];

            realizedGainLoss = this.quantity * (price - avgPrice);
        }
        else
        {
            realizedGainLoss = 0;
        }
        super.save();
    }
}