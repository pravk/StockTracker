package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.TransactionViewHolder;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.event.EditTransactionEvent;
import mobile.pk.com.stocktracker.transaction.processor.TransactionProcessor;
import mobile.pk.com.stocktracker.utils.NumberUtils;

/**
 * Created by hello on 8/26/2015.
 */
public class UserTransactionAdapter extends GenericRVAdapter<UserTransaction> {

    private final Position position;

    public UserTransactionAdapter(Context context, Position position) {
        super(context);
        this.position = position;
    }

    @Override
    protected void onBindViewHolderInternal(RecyclerView.ViewHolder viewHolder, int i) {
        TransactionViewHolder transactionViewHolder = (TransactionViewHolder) viewHolder;
        final UserTransaction userTransaction = getDataList().get(i);

        //Setting text view title
        // portfolioViewHolder.ticker.setText(position.getStock().getExchange() + ":"+ position.getStock().getTicker());
        transactionViewHolder.price.setText( NumberUtils.formatAsMoney(userTransaction.getPrice()) );
        transactionViewHolder.quantity.setText(  String.valueOf(userTransaction.getQuantity()));
        transactionViewHolder.transactionDate.setText(DateUtils.formatDateTime(mContext, userTransaction.getTransactionDate(), DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
        transactionViewHolder.buySell.setText(userTransaction.getTransactionType());
        transactionViewHolder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        EventBus.getDefault().post(new EditTransactionEvent(userTransaction));
                        break;

                }
                return true;
            }
        });
    }

    @Override
    protected TransactionViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_transaction_item, viewGroup, false);

        final TransactionViewHolder viewHolder = new TransactionViewHolder(view);

        return viewHolder;
    }

    @Override
    protected List<UserTransaction> refreshDataInternal() {
        return TransactionProcessor.getInstance().getUserTransactions(position);
    }

    @Override
    protected Stock getUnderlyingStock(UserTransaction data) {
        return data.getStock();
    }
}
