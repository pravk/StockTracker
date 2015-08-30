package mobile.pk.com.stocktracker.adapters.viewholder;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;

import mobile.pk.com.stocktracker.R;

/**
 * Created by hello on 8/1/2015.
 */
public class PortfolioSummaryViewHolder extends RecyclerView.ViewHolder {

    public TextView returns;
    public TextView realizedGain;
    public TextView unrealizedGain;
    public TextView netAsset;
    public PieChart chart;
    public CardView cardView;

    public PortfolioSummaryViewHolder(View itemView) {
        super(itemView);
        returns = (TextView) itemView.findViewById(R.id.returns);
        realizedGain = (TextView) itemView.findViewById(R.id.realized_gain);
        unrealizedGain = (TextView) itemView.findViewById(R.id.unrealized_gain);
        netAsset = (TextView) itemView.findViewById(R.id.net_asset);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        chart = (PieChart) itemView.findViewById(R.id.chart);
        chart.setDescription("");
        chart.setUsePercentValues(true);
        chart.setDragDecelerationFrictionCoef(0.95f);

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setWordWrapEnabled(true);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }
}
