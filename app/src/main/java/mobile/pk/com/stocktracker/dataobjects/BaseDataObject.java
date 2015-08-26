package mobile.pk.com.stocktracker.dataobjects;

import mobile.pk.com.stocktracker.dao.Stock;

/**
 * Created by hello on 8/26/2015.
 */
public class BaseDataObject {

    protected static final String PRICE_CHANGE_FORMAT = "%s (%s%%)";
    protected static final String PRICE_FORMAT = "%s %2$,.2f";

    public String getStockName(Stock stock) {
        return stock.getName();
    }

    public String getTicker(Stock stock) {
        return stock.getTicker();
    }

    public String formatCurrencyAmount(String currency, double amount) {
        return String.format(PRICE_FORMAT, currency, amount);
    }
}
