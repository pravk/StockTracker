package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioPositionViewHolder;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.event.ShowPositionDetailEvent;
import mobile.pk.com.stocktracker.utils.NumberUtils;

/**
 * Created by hello on 8/1/2015.
 */
public class PortfolioPositionAdapter extends GenericRVAdapter<PortfolioPositionViewHolder, Position> {

    private Portfolio portfolio;

    public PortfolioPositionAdapter(Context context, Portfolio portfolio) {
        super(context);
        this.portfolio = portfolio;
        reset();
        refreshPrices();
    }


    @Override
    public PortfolioPositionViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_portfolio_position_item, viewGroup, false);

        final PortfolioPositionViewHolder viewHolder = new PortfolioPositionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PortfolioPositionViewHolder portfolioViewHolder, int i) {
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
