package mobile.pk.com.stocktracker.adapters;

import android.content.Context;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.WatchlistStockViewHolder;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.dao.WatchlistStock;
import mobile.pk.com.stocktracker.dao.tasks.PriceLoadTask;
import mobile.pk.com.stocktracker.dao.tasks.ServerPriceRefreshTask;

import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hello on 8/1/2015.
 */
public class WatchListStockAdapter extends GenericRVAdapter<WatchlistStockViewHolder, WatchlistStock> {

    private Watchlist watchlist;

    public WatchListStockAdapter(Context context, Watchlist watchlist) {
        super(context);
        this.watchlist = watchlist;
    }

    @Override
    public WatchlistStockViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_watchlist_stock_item, viewGroup, false);

        final WatchlistStockViewHolder viewHolder = new WatchlistStockViewHolder(view);

        return viewHolder;
    }

    @Override
    protected List<WatchlistStock> refreshDataInternal() {
        return WatchlistStock.find(WatchlistStock.class,  "watchlist = ?", String.valueOf(watchlist.getId()) );
    }

    @Override
    protected Stock getUnderlyingStock(WatchlistStock data) {
        return data.getStock();
    }

    @Override
    public void onBindViewHolderInternal(final WatchlistStockViewHolder stockViewHolder, int i) {
        final WatchlistStock watchlistStock = getDataList().get(i);


        //Setting text view title
        stockViewHolder.ticker.setText(watchlistStock.getStock().getExchange() + ":" + watchlistStock.getStock().getTicker());
        stockViewHolder.name.setText(watchlistStock.getStock().getName());
        if(watchlistStock.getStock().getPrice() != null)
        {
            stockViewHolder.lastTradePrice.setText(String.format(PRICE_FORMAT_WITHOUT_CURRENCY,  watchlistStock.getStock().getPrice().getLastPrice()));
            stockViewHolder.change.setText(String.format(PRICE_CHANGE_FORMAT, watchlistStock.getStock().getPrice().getChange()));
            stockViewHolder.changePercent.setText(String.format(PRICE_CHANGE_PERCENT_FORMAT, watchlistStock.getStock().getPrice().getChangePercent()));
            stockViewHolder.lastTradeTime.setText( DateUtils.getRelativeTimeSpanString(watchlistStock.getStock().getPrice().getLastTradeTime()));
            if(watchlistStock.getStock().getPrice().getChange()<0) {
                stockViewHolder.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            else {
                stockViewHolder.setTextColor(mContext.getResources().getColor(R.color.green));
            }
        }
        stockViewHolder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.remove:
                        watchlistStock.delete();
                        getDataList().remove(watchlistStock);
                        notifyDataSetChanged();
                        Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    /*public void addStock(Stock stock) {
        if(stockWatchList == null)
            stockWatchList = new ArrayList<>();

        WatchlistStock watchlistStock = WatchlistStock.from(stock, watchlist);

        stockWatchList.add(watchlistStock);
        refreshPrices(Arrays.asList( new WatchlistStock[]{watchlistStock}));
        notifyDataSetChanged();
    }*/

}
