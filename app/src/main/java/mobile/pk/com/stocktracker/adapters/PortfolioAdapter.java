package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioViewHolder;
import mobile.pk.com.stocktracker.adapters.viewholder.WatchlistStockViewHolder;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.dao.WatchlistStock;
import mobile.pk.com.stocktracker.dao.tasks.PriceLoadTask;
import mobile.pk.com.stocktracker.dao.tasks.ServerPriceRefreshTask;
import mobile.pk.com.stocktracker.event.ShowPositionDetailEvent;
import mobile.pk.com.stocktracker.service.PricingService;
import mobile.pk.com.stocktracker.ui.activity.TransactionActivity;
import mobile.pk.com.stocktracker.utils.NumberUtils;

/**
 * Created by hello on 8/1/2015.
 */
public class PortfolioAdapter extends GenericRVAdapter<PortfolioViewHolder, Position> {


    private Portfolio portfolio;

    public PortfolioAdapter(Context context, Portfolio portfolio) {
        super(context);
        this.portfolio = portfolio;
        reset();
        updateModelAndUI();
        refreshPrices();
    }

    public void refreshPrices(){
        List<Position> positionList = getDataList();
        List<Stock> stockList = new ArrayList<>();
        for(Position position: positionList)
        {
            stockList.add(position.getStock());
        }
            new ServerPriceRefreshTask(RestClient.getDefault().getPricingService()) {
                @Override
                protected void onPostExecute(Void result) {
                    if(getException() == null) {
                        updateModelAndUI();
                    }
                    else
                    {
                        Toast.makeText(mContext,getException().getMessage(), Toast.LENGTH_SHORT);
                    }
                }

            }.execute(stockList.toArray(new Stock[stockList.size()]));
       }


    public void updateModelAndUI(){
        List<Position> positionList = getDataList();
        List<Stock> stockList = new ArrayList<>();
        for(Position position: positionList)
        {
            stockList.add(position.getStock());
        }
        new PriceLoadTask(){
            @Override
            protected void onPostExecute(Void result) {
                notifyDataSetChanged();
            }

        }.execute(stockList.toArray(new Stock[stockList.size()]));
    }

    @Override
    public PortfolioViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_portfolio_item, viewGroup, false);

        final PortfolioViewHolder viewHolder = new PortfolioViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PortfolioViewHolder portfolioViewHolder, int i) {
        final Position position = getDataList().get(i);

        //Setting text view title
       // portfolioViewHolder.ticker.setText(position.getStock().getExchange() + ":"+ position.getStock().getTicker());
        portfolioViewHolder.name.setText(position.getStock().getName());
        portfolioViewHolder.avgPrice.setText( mContext.getString(R.string.avg_price) + ": " + NumberUtils.formatAsMoney(position.getAveragePrice()) );
        portfolioViewHolder.quantity.setText( mContext.getString(R.string.quantity) + ": " + position.getQuantity() );
        if(position.getStock().getPrice() != null)
        {
            portfolioViewHolder.lastTradePrice.setText(String.valueOf(position.getStock().getPrice().getLastPrice()));
            portfolioViewHolder.change.setText(String.valueOf(position.getStock().getPrice().getChange()));
            if(position.getStock().getPrice().getChange()<0) {
                portfolioViewHolder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.red));
                portfolioViewHolder.setTextColor(mContext.getResources().getColor(R.color.white));
            }
            else {
                portfolioViewHolder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.green));
                portfolioViewHolder.setTextColor(mContext.getResources().getColor(R.color.white));
            }
        }
        portfolioViewHolder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    /*case R.id.remove:
                        watchlistStock.delete();
                        positionList.remove(watchlistStock);
                        notifyDataSetChanged();
                        Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                        break;*/
                    case R.id.refresh:
                        Position.reeval(position.getStock(), position.getPortfolio());
                        reset();
                        break;
                    case R.id.transactions:
                        EventBus.getDefault().post(new ShowPositionDetailEvent(position));
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

    @Override
    public List<Position> refreshDataInternal() {
        return Position.find(Position.class,  "portfolio = ?", String.valueOf(portfolio.getId()) );
    }
}
