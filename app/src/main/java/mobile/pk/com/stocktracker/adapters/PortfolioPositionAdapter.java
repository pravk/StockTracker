package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioPositionViewHolder;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioSummaryViewHolder;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.PortfolioCurrencySummary;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dataobjects.PositionData;
import mobile.pk.com.stocktracker.event.RefreshPositionEvent;
import mobile.pk.com.stocktracker.event.ShowPositionDetailEvent;
import mobile.pk.com.stocktracker.transaction.processor.TransactionProcessor;

/**
 * Created by hello on 8/1/2015.
 */
public class PortfolioPositionAdapter extends GenericRVAdapter<PositionData> {

    private Portfolio portfolio;

    public PortfolioPositionAdapter(Context context, Portfolio portfolio) {
        super(context);
        this.portfolio = portfolio;
    }

    protected RecyclerView.ViewHolder onCreateViewHolderSummary(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_portfolio_position_summary, viewGroup, false);

        final PortfolioSummaryViewHolder viewHolder = new PortfolioSummaryViewHolder(view);

        return viewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_portfolio_position_item, viewGroup, false);

            final PortfolioPositionViewHolder viewHolder = new PortfolioPositionViewHolder(view);

            return viewHolder;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderHeaderInternal(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_portfolio_position_item_header, viewGroup, false);

        PortfolioPositionViewHolder.PortfolioPositionViewHolderHeader viewHolder = new PortfolioPositionViewHolder.PortfolioPositionViewHolderHeader(view);

        return viewHolder;
    }

    @Override
    protected void onBindViewHolderSummary(RecyclerView.ViewHolder holder, int position) {
        PortfolioSummaryViewHolder viewHolder = (PortfolioSummaryViewHolder) holder;
        PositionData.PortfolioSummaryData summaryData = (PositionData.PortfolioSummaryData) getDataList().get(position);
        viewHolder.returns.setText(summaryData.getReturnPercent());
        viewHolder.realizedGain.setText(summaryData.getRealizedGain());
        viewHolder.unrealizedGain.setText(summaryData.getUnRealizedGain());
        viewHolder.netAsset.setText(summaryData.getNetAsset());
        //viewHolder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.primary_light));

    }

    @Override
    protected void onBindViewHolderHeaderInternal(RecyclerView.ViewHolder viewHolder, int i) {
        PortfolioPositionViewHolder.PortfolioPositionViewHolderHeader holder = (PortfolioPositionViewHolder.PortfolioPositionViewHolderHeader) viewHolder;
        final PositionData position = getDataList().get(i);

        holder.gainLoss.setText(position.getGainLoss());
        holder.lastTradePrice.setText(position.getLastTradePrice());
        holder.ticker.setText(position.getTicker());
        holder.avgPrice.setText(position.getAveragePrice());
        holder.change.setText(position.getChange());
        holder.marketValue.setText(position.getMarketValue());
        holder.name.setText(position.getStockName());
        holder.quantity.setText(position.getQuantity());
        ((PortfolioPositionViewHolder.PortfolioPositionViewHolderHeader) holder).positionType.setText( ((PositionData.PositionHeaderData)position).getPositionDesc() );
        holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(((PositionData.PositionHeaderData)position).getCardColor()));
        holder.setColor(mContext.getResources().getColor(R.color.white));
    }

    @Override
    public void onBindViewHolderInternal(final RecyclerView.ViewHolder viewHolder, int i) {
        PortfolioPositionViewHolder portfolioViewHolder = (PortfolioPositionViewHolder) viewHolder;
        final PositionData position = getDataList().get(i);

        //Setting text view title
       // portfolioViewHolder.ticker.setText(position.getStock().getExchange() + ":"+ position.getStock().getTicker());
        portfolioViewHolder.name.setText(position.getStockName());
        portfolioViewHolder.ticker.setText(position.getTicker());
        portfolioViewHolder.error.setText(position.getError());
        if(!TextUtils.isEmpty(position.getError()))
        {
            portfolioViewHolder.errorLayout.setVisibility(View.VISIBLE);
            portfolioViewHolder.detailLayout.setVisibility(View.GONE);
        }
        else {
            portfolioViewHolder.errorLayout.setVisibility(View.GONE);
            portfolioViewHolder.detailLayout.setVisibility(View.VISIBLE);
            portfolioViewHolder.quantity.setText(position.getQuantity());
            portfolioViewHolder.avgPrice.setText(position.getAveragePrice());
            portfolioViewHolder.lastTradePrice.setText(position.getLastTradePrice());
            portfolioViewHolder.change.setText(position.getChange());
            portfolioViewHolder.gainLoss.setText(position.getGainLoss());
            portfolioViewHolder.marketValue.setText(position.getMarketValue());
            portfolioViewHolder.change.setTextColor(mContext.getResources().getColor(position.getChangeTextColor()));
            portfolioViewHolder.gainLoss.setTextColor(mContext.getResources().getColor(position.getGainLossTextColor()));
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
                            if(position.getPosition() != null)
                                EventBus.getDefault().post(new RefreshPositionEvent( position.getPosition()));
                            refreshData();
                            break;
                        case R.id.transactions:
                            if(position.getPosition() != null)
                                EventBus.getDefault().post(new ShowPositionDetailEvent(position.getPosition()));
                            break;
                    }
                    return true;
                }
            });
    }

    @Override
    public List<PositionData> refreshDataInternal() {
        List<Position> positionList = TransactionProcessor.getInstance().getOpenPositions(portfolio);
        List<PositionData> positionDataList= new ArrayList<>();
        positionDataList.add(new PositionData.PositionHeaderData(mContext.getResources().getString(R.string.open_positions), R.color.green));
        for (Position position :
                positionList) {
            positionDataList.add(new PositionData(position));
        }
        positionList = TransactionProcessor.getInstance().getClosedPositions(portfolio);
        if(positionList.size()>0)
            positionDataList.add(new PositionData.PositionHeaderData(mContext.getResources().getString(R.string.closed_positions), R.color.teal));
        for (Position position :
                positionList) {
            positionDataList.add(new PositionData.ClosedPositionData(position));
        }

        List<PortfolioCurrencySummary> portfolioCurrencySummaryList = TransactionProcessor.getInstance().getPortfolioSummary(portfolio);
        if(portfolioCurrencySummaryList != null && portfolioCurrencySummaryList.size()>0)
            positionDataList.add(new PositionData.PortfolioSummaryData(portfolioCurrencySummaryList.get(0)));

        return positionDataList;
    }

    protected boolean hasHeader(){
        return true;
    }

    protected boolean isPositionHeader(int position)
    {
        return getDataList().get(position) instanceof PositionData.PositionHeaderData;
    }
    protected boolean isPositionSummary(int position) {
        return getDataList().get(position) instanceof PositionData.PortfolioSummaryData;
    }

    protected boolean hasSummary() {
        return true;
    }


    @Override
    protected Stock getUnderlyingStock(PositionData data){
        if(data.getPosition() != null)
            return data.getPosition().getStock();
        else
            return  null;
    }
}
