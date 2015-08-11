package mobile.pk.com.stocktracker.adapters;

import android.content.Context;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.WatchlistStockViewHolder;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.dao.WatchlistStock;
import mobile.pk.com.stocktracker.dao.tasks.PriceLoadTask;
import mobile.pk.com.stocktracker.dao.tasks.ServerPriceRefreshTask;
import mobile.pk.com.stocktracker.service.PricingService;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hello on 8/1/2015.
 */
public class WatchListAdapter extends RecyclerView.Adapter<WatchlistStockViewHolder> {

    private Context mContext;

    private List<WatchlistStock> stockWatchList;
    private Watchlist watchlist;
    private PricingService service;

    public WatchListAdapter(Context context, Watchlist watchlist, PricingService service) {
        this.mContext = context;
        this.watchlist = watchlist;
        stockWatchList = WatchlistStock.find(WatchlistStock.class,  "watchlist = ?", String.valueOf(watchlist.getId()) );
        this.service = service;
        updateModelAndUI(stockWatchList);
        refreshPrices(stockWatchList);
    }

    public void refreshPrices(final List<WatchlistStock> stockWatchList){
        List<Stock> stockList = new ArrayList<>();
        for(WatchlistStock watchlistStock: stockWatchList)
        {
            stockList.add(watchlistStock.getStock());
        }
            new ServerPriceRefreshTask(service) {
                @Override
                protected void onPostExecute(Void result) {
                    if(getException() == null) {
                        updateModelAndUI(stockWatchList);
                    }
                    else
                    {
                        Toast.makeText(mContext,getException().getMessage(), Toast.LENGTH_SHORT);
                    }
                }

            }.execute(stockList.toArray(new Stock[stockList.size()]));
       }


    public void updateModelAndUI(List<WatchlistStock> stockWatchList){
        List<Stock> stockList = new ArrayList<>();
        for(WatchlistStock watchlistStock: stockWatchList)
        {
            stockList.add(watchlistStock.getStock());
        }
        new PriceLoadTask(){
            @Override
            protected void onPostExecute(Void result) {
                notifyDataSetChanged();
            }

        }.execute(stockList.toArray(new Stock[stockList.size()]));
    }

    @Override
    public WatchlistStockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_watchlist_item,  viewGroup, false);

        final WatchlistStockViewHolder viewHolder = new WatchlistStockViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WatchlistStockViewHolder stockViewHolder, int i) {
        final WatchlistStock watchlistStock = stockWatchList.get(i);


        //Setting text view title
        stockViewHolder.ticker.setText(watchlistStock.getStock().getExchange() + ":"+ watchlistStock.getStock().getTicker());
        stockViewHolder.name.setText(watchlistStock.getStock().getName());
        if(watchlistStock.getStock().getPrice() != null)
        {
            stockViewHolder.lastTradePrice.setText(String.valueOf(watchlistStock.getStock().getPrice().getLastPrice()));
            stockViewHolder.change.setText(String.valueOf(watchlistStock.getStock().getPrice().getChange()));
            if(watchlistStock.getStock().getPrice().getChange()<0) {
                stockViewHolder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.red));
                stockViewHolder.setTextColor(mContext.getResources().getColor(R.color.white));
            }
            else {
                stockViewHolder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.green));
                stockViewHolder.setTextColor(mContext.getResources().getColor(R.color.white));
            }
        }
        stockViewHolder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.remove:
                        watchlistStock.delete();
                        stockWatchList.remove(watchlistStock);
                        notifyDataSetChanged();
                        Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(stockWatchList == null)
            return 0;
        return stockWatchList.size();
    }

    public void addStock(Stock stock) {
        if(stockWatchList == null)
            stockWatchList = new ArrayList<>();

        WatchlistStock watchlistStock = WatchlistStock.from(stock, watchlist);

        stockWatchList.add(watchlistStock);
        refreshPrices(Arrays.asList( new WatchlistStock[]{watchlistStock}));
        notifyDataSetChanged();
    }


    public List<WatchlistStock> getWatchListStocks() {
        return stockWatchList;

    }
}
