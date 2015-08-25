package mobile.pk.com.stocktracker.dao;

import com.orm.StringUtil;
import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.dao.tasks.HasStock;

/**
 * Created by hello on 8/10/2015.
 */
public class Position extends SugarRecord<Position> implements HasStock {

    private Portfolio portfolio;
    private Stock stock;
    private double quantity;
    private double averagePrice;
    private double totalPrice;
    private double netRealizedGainLoss;
    private String error;

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

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getGainLoss() {
        return getQuantity() * (getStock().getPrice().getLastPrice() - averagePrice);
    }

    public double getMarketValue() {
        return getQuantity() * getStock().getPrice().getLastPrice();
    }

    public double getNetRealizedGainLoss() {
        return netRealizedGainLoss;
    }

    public void setNetRealizedGainLoss(double netRealizedGainLoss) {
        this.netRealizedGainLoss = netRealizedGainLoss;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void reset() {
        this.setError(null);
        this.setQuantity(0);
        this.setNetRealizedGainLoss(0);
        this.setAveragePrice(0);
        this.setTotalPrice(0);
    }

    public static class PositionChangeEvent {
        private final Position position;

        public PositionChangeEvent(Position position) {
            this.position = position;
        }

        public Position getPosition() {
            return position;
        }
    }
}
