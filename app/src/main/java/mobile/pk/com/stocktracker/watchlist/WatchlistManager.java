package mobile.pk.com.stocktracker.watchlist;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.common.Application;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.dao.WatchlistStock;
import mobile.pk.com.stocktracker.event.WatchlistRefreshEvent;
import mobile.pk.com.stocktracker.service.TickerSearchService;

/**
 * Created by hello on 8/27/2015.
 */
public class WatchlistManager {

    public static WatchlistManager instance;

    public static final String WORLD_INDICES_NAME = "WORLD INDICES";


    public static WatchlistManager getInstance(){
        if(instance==null) {
            instance = new WatchlistManager();
        }
        return instance;
    }

    public void getWorldMarketWatchlist(Context context, final WorldMarketLoadComplete worldMarketLoadComplete)
    {
        List<Watchlist> watchlistList = Watchlist.find(Watchlist.class, "watchlist_name=? and is_system=1", WORLD_INDICES_NAME);
        if(watchlistList == null || watchlistList.size()==0)
        {
            createWorldIndicesWatchlist(context);
            watchlistList = Watchlist.find(Watchlist.class, "watchlist_name=? and is_system=1", WORLD_INDICES_NAME);

            final List<Watchlist> finalWatchlistList = watchlistList;
            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... params) {
                    initWorldIndices(finalWatchlistList.get(0));
                    worldMarketLoadComplete.loadComplete(finalWatchlistList.get(0));
                    EventBus.getDefault().post(new WatchlistRefreshEvent(finalWatchlistList.get(0)));
                    return null;
                }
            }.execute();
        }
        else
        {
            worldMarketLoadComplete.loadComplete(watchlistList.get(0));
        }
        //return watchlistList.get(0);
    }

    public static interface WorldMarketLoadComplete
    {
        void loadComplete(Watchlist watchlist);
    }

    private void createWorldIndicesWatchlist(Context context) {
        Watchlist watchlist = new Watchlist();
        watchlist.setWatchlistName(WORLD_INDICES_NAME);
        watchlist.setIsSystem(true);

        watchlist.save();



    }

    private void initWorldIndices(Watchlist watchlist){
        long time = Calendar.getInstance().getTimeInMillis();

        String [] indices = Application.getInstance().getApplicationContext().getResources().getStringArray(R.array.world_indices);

        for (String index : indices) {
            WatchlistStock watchlistStock = new WatchlistStock();
            watchlistStock.setWatchlist(watchlist);
            watchlistStock.setStock(getStock(index));

            if (watchlistStock.getStock() != null)
                watchlistStock.save();

        }
    }

    public Stock getStock(String ticker){
        TickerSearchService.TickerSearchResponse tickerSearchResponse = RestClient.getDefault().getTickerSearchService().searchTicker(ticker);
        if(tickerSearchResponse.getMatches() != null && tickerSearchResponse.getMatches().size()>0)
            return Stock.from(tickerSearchResponse.getMatches().get(0));
        else
            return null;
    }

    public List<Watchlist> getUserWatchlists(Context context)
    {
        List<Watchlist> watchlistList = Watchlist.find(Watchlist.class, "is_system=0");
        if(watchlistList == null || watchlistList.size()==0)
        {
            createDefaultWatchList(context);
            watchlistList = Watchlist.find(Watchlist.class, "is_system=0");
        }
        return watchlistList;
    }

    protected void createDefaultWatchList(Context context) {
        Watchlist watchlist = new Watchlist();
        watchlist.setWatchlistName(context.getString(R.string.default_watchlist_name));
        watchlist.save();
    }
}
