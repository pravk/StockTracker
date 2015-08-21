package mobile.pk.com.stocktracker.dao;

import com.orm.StringUtil;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

import mobile.pk.com.stocktracker.dao.tasks.HasStock;
import mobile.pk.com.stocktracker.service.TickerSearchService;

/**
 * Created by hello on 8/10/2015.
 */
public class Stock extends SugarRecord<Stock>{
    private String name;
    private String ticker;
    private String exchange;
    private String clientId;
    @Ignore
    private StockPrice price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Stock from(TickerSearchService.Match match) {
        List<Stock> stockList = find(Stock.class, StringUtil.toSQLName("clientId") + "=?", match.getId());
        if(stockList == null || stockList.size()==0)
        {
            Stock stock = new Stock();
            stock.setClientId(match.getId());
            stock.setExchange(match.getExchange());
            stock.setName(match.getName());
            stock.setTicker(match.getTicker());
            stock.save();
            return stock;
        }
        return stockList.get(0);
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public StockPrice getPrice() {
        return price;
    }

    public void setPrice(StockPrice price) {
        this.price = price;
    }
}
