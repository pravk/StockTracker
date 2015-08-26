package mobile.pk.com.stocktracker.dao;

import android.text.TextUtils;

import com.orm.StringUtil;
import com.orm.SugarRecord;

import java.util.List;

import mobile.pk.com.stocktracker.service.PricingService;

/**
 * Created by hello on 8/11/2015.
 */
public class StockPrice extends SugarRecord<StockPrice>{

    private String clientId;
    private String ticker;
    private String exchange;
    private double lastPrice;
    private String lastPriceWithCurrency;
    private double change;
    private double changePercent;
    private String lastTrade;
    private String currency;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getLastPriceWithCurrency() {
        return lastPriceWithCurrency;
    }

    public void setLastPriceWithCurrency(String lastPriceWithCurrency) {
        this.lastPriceWithCurrency = lastPriceWithCurrency;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public String getLastTrade() {
        return lastTrade;
    }

    public void setLastTrade(String lastTrade) {
        this.lastTrade = lastTrade;
    }

    public static StockPrice from(PricingService.StockPrice serverPrice)
    {
        List<StockPrice> stockPriceList = find(StockPrice.class, StringUtil.toSQLName("clientId") + "=?", serverPrice.getId());
        StockPrice stockPrice = null;
        if(stockPriceList == null || stockPriceList.size()==0)
        {
            stockPrice = new StockPrice();
            stockPrice.setTicker(serverPrice.getTicker());
            stockPrice.setExchange(serverPrice.getExchange());
            stockPrice.setClientId(serverPrice.getId());
        }
        else
        {
            stockPrice = stockPriceList.get(0);

        }
        if(!TextUtils.isEmpty( serverPrice.getChange()))
            stockPrice.setChange( Double.parseDouble(serverPrice.getChange()));
        if(!TextUtils.isEmpty( serverPrice.getChangePercent()))
            stockPrice.setChangePercent( Double.parseDouble(serverPrice.getChangePercent()));
        if(!TextUtils.isEmpty( serverPrice.getLastPrice()))
            stockPrice.setLastPrice(Double.parseDouble(serverPrice.getLastPrice()));
        stockPrice.setLastPriceWithCurrency(serverPrice.getLastPriceWithCurrency());
        stockPrice.setLastTrade(serverPrice.getLastTrade());
        stockPrice.setCurrency(serverPrice.getLastPriceWithCurrency().replace(serverPrice.getLastPriceFormatted(), ""));
        if( TextUtils.isEmpty(stockPrice.getCurrency()))
            stockPrice.setCurrency("$");
        stockPrice.save();

        return stockPrice;

    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
