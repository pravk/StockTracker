package mobile.pk.com.stocktracker.adapters.viewholder;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import mobile.pk.com.stocktracker.R;

/**
 * Created by hello on 8/1/2015.
 */
public class PortfolioSummaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView portfolio;
    public TextView changePercent;
    public TextView realizedGain;
    public TextView unrealizedGain;
    public TextView netAsset;
    public FontAwesomeText upIcon;
    public FontAwesomeText downIcon;
    public Toolbar toolbar;
    public CardView cardView;

    public PortfolioSummaryViewHolder(View itemView) {
        super(itemView);
        portfolio = (TextView) itemView.findViewById(R.id.portfolio_name);
        changePercent = (TextView) itemView.findViewById(R.id.change_percent);
        realizedGain = (TextView) itemView.findViewById(R.id.realized_gain);
        unrealizedGain = (TextView) itemView.findViewById(R.id.unrealized_gain);
        upIcon = (FontAwesomeText) itemView.findViewById(R.id.up_icon);
        downIcon = (FontAwesomeText) itemView.findViewById(R.id.down_icon);
        netAsset = (TextView) itemView.findViewById(R.id.net_asset);
        toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
    }

    @Override
    public void onClick(View v) {

    }
}
