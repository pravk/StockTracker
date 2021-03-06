package mobile.pk.com.stocktracker.adapters.viewholder;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import mobile.pk.com.stocktracker.R;

/**
 * Created by hello on 8/1/2015.
 */
public class WatchlistStockViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView ticker;
    public TextView name;
    public TextView lastTradePrice;
    public TextView lastTradeTime;
    public TextView changePercent;
    public TextView change;
    public Toolbar toolbar;
    public CardView cardView;

    public WatchlistStockViewHolder(View itemView) {
        super(itemView);
        ticker = (TextView) itemView.findViewById(R.id.ticker);
        name = (TextView) itemView.findViewById(R.id.name);
        lastTradePrice = (TextView) itemView.findViewById(R.id.last_trade_price);
        lastTradeTime = (TextView) itemView.findViewById(R.id.last_trade_time);
        changePercent = (TextView) itemView.findViewById(R.id.change_percent);
        change = (TextView) itemView.findViewById(R.id.change);
        toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.watchlist_stock_item_menu);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
    }

    public void setTextColor(int color)
    {
       // ticker.setTextColor(color);
       // name.setTextColor(color);
        //lastTradePrice.setTextColor(color);
        change.setTextColor(color);
        changePercent.setTextColor(color);
    }

    @Override
    public void onClick(View v) {

    }
}
