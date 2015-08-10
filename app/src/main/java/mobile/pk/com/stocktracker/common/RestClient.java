package mobile.pk.com.stocktracker.common;

import android.content.Context;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.service.TickerSearchService;
import retrofit.RestAdapter;

/**
 * Created by hello on 8/1/2015.
 */
public class RestClient {

    private TickerSearchService tickerSearchService;

    public RestClient(Context context)
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(context.getString(R.string.server_url))
                .build();

        tickerSearchService = restAdapter.create(TickerSearchService.class);

    }

    public TickerSearchService getTickerSearchService() {
        return tickerSearchService;
    }
}
