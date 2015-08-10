package mobile.pk.com.stocktracker.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by hello on 8/10/2015.
 */
public interface TickerSearchService {

    @GET("/match?matchtype=matchall")
    public TickerSearchResponse searchTicker(@Query("q") String ticker);

    public class Match {

        @SerializedName("t")
        @Expose
        private String ticker;
        @SerializedName("n")
        @Expose
        private String name;
        @SerializedName("e")
        @Expose
        private String exchange;
        @SerializedName("id")
        @Expose
        private String id;

        /**
         *
         * @return
         * The id
         */
        public String getId() {
            return id;
        }

        /**
         *
         * @param id
         * The id
         */
        public void setId(String id) {
            this.id = id;
        }

        public String getTicker() {
            return ticker;
        }

        public void setTicker(String ticker) {
            this.ticker = ticker;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }
    }

    public class TickerSearchResponse {

        @Expose
        private List<Match> matches = new ArrayList<Match>();

        /**
         *
         * @return
         * The matches
         */
        public List<Match> getMatches() {
            return matches;
        }

        /**
         *
         * @param matches
         * The matches
         */
        public void setMatches(List<Match> matches) {
            this.matches = matches;
        }

    }
}
