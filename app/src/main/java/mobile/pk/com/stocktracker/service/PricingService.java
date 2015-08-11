package mobile.pk.com.stocktracker.service;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by hello on 8/11/2015.
 */
public interface PricingService {
    @GET("/info?client=ig")
    public Response getStockPrices(@Query("q") String csListOfStocks);

    public static class StockPrice
    {
        private String id;
        @SerializedName("t")
        private String ticker;
        @SerializedName("e")
        private String exchange;
        @SerializedName("l_fix")
        private double lastPrice;
        @SerializedName("l_cur")
        private String lastPriceWithCurrency;
        @SerializedName("c")
        private double change;
        @SerializedName("cp")
        private double changePercent;
        @SerializedName("lt")
        private String lastTrade;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    }

}
