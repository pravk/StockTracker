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
public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView ticker;
    public TextView name;
    public TextView quantity;
    public TextView price;
    public TextView transactionDate;
    public TextView buySell;
    public Toolbar toolbar;
    public CardView cardView;

    public TransactionViewHolder(View itemView) {
        super(itemView);
        ticker = (TextView) itemView.findViewById(R.id.ticker);
        name = (TextView) itemView.findViewById(R.id.name);
        price = (TextView) itemView.findViewById(R.id.price);
        quantity = (TextView) itemView.findViewById(R.id.quantity);
        buySell = (TextView) itemView.findViewById(R.id.buy_sell);
        transactionDate = (TextView) itemView.findViewById(R.id.transaction_date);
        toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.transaction_item_menu);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
    }

    @Override
    public void onClick(View v) {

    }
}
