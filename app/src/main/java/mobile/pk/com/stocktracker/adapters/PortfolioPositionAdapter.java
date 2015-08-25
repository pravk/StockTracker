package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import mobile.pk.com.stocktracker.event.RefreshPositionEvent;
import mobile.pk.com.stocktracker.event.ShowPositionDetailEvent;

/**
 * Created by hello on 8/1/2015.
 */
public class PortfolioPositionAdapter extends GenericRVAdapter<PortfolioPositionViewHolder, Position> {

    private Portfolio portfolio;

    public PortfolioPositionAdapter(Context context, Portfolio portfolio) {
        super(context);
        this.portfolio = portfolio;
        reset();

    }

    @Override
    public PortfolioPositionViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_portfolio_position_item, viewGroup, false);

            final PortfolioPositionViewHolder viewHolder = new PortfolioPositionViewHolder(view);

            return viewHolder;
    }

    @Override
    protected PortfolioPositionViewHolder onCreateViewHolderHeaderInternal(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_portfolio_position_item_header, viewGroup, false);

        PortfolioPositionViewHolder.PortfolioPositionViewHolderHeader viewHolder = new PortfolioPositionViewHolder.PortfolioPositionViewHolderHeader(view);

        return viewHolder;
    }

    @Override
    protected void onBindViewHolderHeaderInternal(PortfolioPositionViewHolder holder, int i) {
        holder.gainLoss.setText("Gain/Loss");
        holder.lastTradePrice.setText("Last Price");
        holder.ticker.setText("Ticker /");
        holder.avgPrice.setText("Avg Price");
        holder.change.setText("Change (%)");
        holder.marketValue.setText("Mkt Value");
        holder.name.setText("Company Name");
        holder.quantity.setText("Quantity x");
        holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.green));
        holder.setColor(mContext.getResources().getColor(R.color.white));
        //holder.toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onBindViewHolderInternal(final PortfolioPositionViewHolder portfolioViewHolder, int i) {
        final Position position = getDataList().get(i);

        //Setting text view title
       // portfolioViewHolder.ticker.setText(position.getStock().getExchange() + ":"+ position.getStock().getTicker());
        portfolioViewHolder.name.setText(position.getStock().getName());
        portfolioViewHolder.ticker.setText(position.getStock().getTicker());
        portfolioViewHolder.error.setText(position.getError());
        if(!TextUtils.isEmpty(position.getError()))
        {
            portfolioViewHolder.errorLayout.setVisibility(View.VISIBLE);
            portfolioViewHolder.detailLayout.setVisibility(View.GONE);
        }
        else {
            portfolioViewHolder.errorLayout.setVisibility(View.GONE);
            portfolioViewHolder.detailLayout.setVisibility(View.VISIBLE);
            portfolioViewHolder.quantity.setText(String.format("%d x", (int) position.getQuantity()));
            if (position.getStock().getPrice() != null) {
                double gainLoss = position.getGainLoss();
                double marketValue = position.getMarketValue();
                portfolioViewHolder.avgPrice.setText(String.format(PRICE_FORMAT, position.getStock().getPrice().getCurrency(), position.getAveragePrice()));
                portfolioViewHolder.lastTradePrice.setText(String.format(PRICE_FORMAT, position.getStock().getPrice().getCurrency(), position.getStock().getPrice().getLastPrice()));
                portfolioViewHolder.change.setText(String.format(PRICE_CHANGE_FORMAT, position.getStock().getPrice().getChange(), position.getStock().getPrice().getChangePercent()));
                portfolioViewHolder.gainLoss.setText(String.format(PRICE_FORMAT, position.getStock().getPrice().getCurrency(), gainLoss));
                portfolioViewHolder.marketValue.setText(String.format(PRICE_FORMAT, position.getStock().getPrice().getCurrency(), marketValue));
                //portfolioViewHolder.lastTradePrice.setText(String.valueOf(position.getStock().getPrice().getLastPrice()));
                //portfolioViewHolder.change.setText(String.valueOf(position.getStock().getPrice().getChange()));
                if (position.getStock().getPrice().getChange() < 0) {
                    portfolioViewHolder.change.setTextColor(mContext.getResources().getColor(R.color.red));
                } else {
                    portfolioViewHolder.change.setTextColor(mContext.getResources().getColor(R.color.green));
                }
                if (gainLoss < 0) {
                    portfolioViewHolder.gainLoss.setTextColor(mContext.getResources().getColor(R.color.red));
                } else {
                    portfolioViewHolder.gainLoss.setTextColor(mContext.getResources().getColor(R.color.green));
                }
            }
        }
        if(portfolioViewHolder.toolbar != null)
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
                            EventBus.getDefault().post(new RefreshPositionEvent(position));
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

    @Override
    public List<Position> refreshDataInternal() {
        return Position.find(Position.class,  "portfolio = ? and quantity != 0", String.valueOf(portfolio.getId()) );
    }
    protected boolean hasHeader(){
        return true;
    }

}
