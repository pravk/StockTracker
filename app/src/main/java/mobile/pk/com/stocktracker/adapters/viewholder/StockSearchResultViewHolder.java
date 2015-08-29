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
public class StockSearchResultViewHolder extends RecyclerView.ViewHolder{

    public TextView ticker;
    public TextView name;
    public TextView exchange;

    public StockSearchResultViewHolder(View itemView) {
        super(itemView);
        ticker = (TextView) itemView.findViewById(R.id.ticker);
        name = (TextView) itemView.findViewById(R.id.name);
        exchange = (TextView) itemView.findViewById(R.id.exchange);
    }

}
