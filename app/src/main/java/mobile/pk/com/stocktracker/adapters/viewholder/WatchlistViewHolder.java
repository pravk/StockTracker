package mobile.pk.com.stocktracker.adapters.viewholder;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;

/**
 * Created by hello on 8/1/2015.
 */
public class WatchlistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView watchlistName;
    public Toolbar toolbar;
    public CardView cardView;

    public WatchlistViewHolder(View itemView) {
        super(itemView);
        watchlistName = (TextView) itemView.findViewById(R.id.watchlist_name);
        toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.watchlist_item_menu);
        cardView = (CardView) itemView.findViewById(R.id.card_view);

    }

    @Override
    public void onClick(View v) {

    }
}
