package mobile.pk.com.stocktracker.adapters.viewholder;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mobile.pk.com.stocktracker.R;

/**
 * Created by hello on 8/1/2015.
 */
public class PortfolioPositionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView ticker;
    public TextView name;
    public TextView lastTradePrice;
    public TextView quantity;
    public TextView avgPrice;
    public TextView change;
    public TextView marketValue;
    public TextView error;
    public ViewGroup errorLayout;
    public ViewGroup detailLayout;
    public TextView gainLoss;
    public Toolbar toolbar;
    public CardView cardView;

    public PortfolioPositionViewHolder(View itemView) {
        super(itemView);
        ticker = (TextView) itemView.findViewById(R.id.ticker);
        name = (TextView) itemView.findViewById(R.id.name);
        lastTradePrice = (TextView) itemView.findViewById(R.id.last_trade_price);
        change = (TextView) itemView.findViewById(R.id.change);
        toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
        if(toolbar != null)
            toolbar.inflateMenu(R.menu.portfolio_item_menu);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        quantity = (TextView) itemView.findViewById(R.id.quantity);
        avgPrice =(TextView) itemView.findViewById(R.id.avg_price);
        marketValue = (TextView) itemView.findViewById(R.id.market_value);
        gainLoss = (TextView) itemView.findViewById(R.id.gain_loss);
        error = (TextView) itemView.findViewById(R.id.position_error);
        errorLayout = (ViewGroup) itemView.findViewById(R.id.error_layout);
        detailLayout = (ViewGroup) itemView.findViewById(R.id.detail_layout);
    }

    public void setColor(int color)
    {
        gainLoss.setTextColor(color);
        change.setTextColor(color);
    }

    @Override
    public void onClick(View v) {

    }

    public static class PortfolioPositionViewHolderHeader extends PortfolioPositionViewHolder{

        public TextView positionType;
        public PortfolioPositionViewHolderHeader(View itemView) {
            super(itemView);
            positionType = (TextView) itemView.findViewById(R.id.position_type);
        }

        @Override
        public void setColor(int color)
        {
            ticker.setTextColor(color);
            name.setTextColor(color);
            lastTradePrice.setTextColor(color);
            change.setTextColor(color);
            quantity.setTextColor(color);
            avgPrice.setTextColor(color);
            marketValue.setTextColor(color);
            gainLoss.setTextColor(color);
        }
    }
}
