package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioSummaryViewHolder;
import mobile.pk.com.stocktracker.dao.PortfolioCurrencySummary;
import mobile.pk.com.stocktracker.transaction.processor.TransactionProcessor;

/**
 * Created by hello on 8/21/2015.
 */
public class PortfolioSummaryAdapter extends GenericRVAdapter<PortfolioSummaryViewHolder, PortfolioCurrencySummary> {

    public PortfolioSummaryAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindViewHolderInternal(PortfolioSummaryViewHolder holder, int i) {
        PortfolioCurrencySummary summary = getDataList().get(i);
        holder.portfolio.setText(String.format("%s (%s)", summary.getPortfolioName(), summary.getCurrency()));
        holder.netAsset.setText(String.format(PRICE_FORMAT, summary.getCurrency(), summary.getNetAsset()));
        holder.unrealizedGain.setText(String.format(PRICE_FORMAT, summary.getCurrency(), summary.getUnrealizedGainLoss()));
        holder.realizedGain.setText(String.format(PRICE_FORMAT, summary.getCurrency(), summary.getRealizedGainLoss()));
        holder.changePercent.setText(String.format("%1$,.2f %%", summary.getReturnPercent()));

        if(summary.getReturnPercent() <0) {
            holder.changePercent.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.upIcon.setVisibility(View.GONE);
            holder.downIcon.setVisibility(View.VISIBLE);
        }
        else {
            holder.changePercent.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.downIcon.setVisibility(View.GONE);
            holder.upIcon.setVisibility(View.VISIBLE);
        }

        if(summary.getRealizedGainLoss() <0)
            holder.realizedGain.setTextColor(mContext.getResources().getColor(R.color.red));
        else
            holder.realizedGain.setTextColor(mContext.getResources().getColor(R.color.green));

        if(summary.getUnrealizedGainLoss() <0)
            holder.unrealizedGain.setTextColor(mContext.getResources().getColor(R.color.red));
        else
            holder.unrealizedGain.setTextColor(mContext.getResources().getColor(R.color.green));
    }

    @Override
    protected PortfolioSummaryViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_portfolio_summary_item, viewGroup, false);

        PortfolioSummaryViewHolder viewHolder = new PortfolioSummaryViewHolder(view);

        return viewHolder;
    }

    @Override
    protected List<PortfolioCurrencySummary> refreshDataInternal() {
        return TransactionProcessor.getInstance().getPortfolioSummary();
    }
}
