package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.TransactionViewHolder;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.dao.tasks.PriceLoadTask;
import mobile.pk.com.stocktracker.dao.tasks.ServerPriceRefreshTask;
import mobile.pk.com.stocktracker.event.EditTransactionEvent;
import mobile.pk.com.stocktracker.service.PricingService;
import mobile.pk.com.stocktracker.utils.NumberUtils;

/**
 * Created by hello on 8/1/2015.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {

    private Context mContext;

    private Position position1;
    private List<UserTransaction> transactionList;
    private PricingService service;

    public TransactionAdapter(Context context, Position position, PricingService service) {
        this.mContext = context;
        this.position1= position;
        this.service = service;
        reset();
        updateModelAndUI(transactionList);
        refreshPrices(transactionList);
    }

    public void refreshPrices(final List<UserTransaction> userTransactionList){
        List<Stock> stockList = new ArrayList<>();
        for(UserTransaction userTransaction: userTransactionList)
        {
            stockList.add(userTransaction.getStock());
        }
            new ServerPriceRefreshTask(service) {
                @Override
                protected void onPostExecute(Void result) {
                    if(getException() == null) {
                        updateModelAndUI(userTransactionList);
                    }
                    else
                    {
                        Toast.makeText(mContext,getException().getMessage(), Toast.LENGTH_SHORT);
                    }
                }

            }.execute(stockList.toArray(new Stock[stockList.size()]));
       }


    public void updateModelAndUI(List<UserTransaction> userTransactionList){
        List<Stock> stockList = new ArrayList<>();
        for(UserTransaction userTransaction: userTransactionList)
        {
            stockList.add(userTransaction.getStock());
        }
        new PriceLoadTask(){
            @Override
            protected void onPostExecute(Void result) {
                notifyDataSetChanged();
            }

        }.execute(stockList.toArray(new Stock[stockList.size()]));
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_transaction_item, viewGroup, false);

        final TransactionViewHolder viewHolder = new TransactionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TransactionViewHolder transactionViewHolder, int i) {
        final UserTransaction userTransaction = transactionList.get(i);


        //Setting text view title
       // portfolioViewHolder.ticker.setText(position.getStock().getExchange() + ":"+ position.getStock().getTicker());
        transactionViewHolder.price.setText( NumberUtils.formatAsMoney(userTransaction.getPrice()) );
        transactionViewHolder.quantity.setText(  String.valueOf(userTransaction.getQuantity()));
        transactionViewHolder.buySell.setText(userTransaction.isLong()?"Buy":"Sell");
        transactionViewHolder.transactionDate.setText(DateUtils.formatDateTime(mContext, userTransaction.getTransactionDate(), DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
        transactionViewHolder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    /*case R.id.remove:
                        watchlistStock.delete();
                        positionList.remove(watchlistStock);
                        notifyDataSetChanged();
                        Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                        break;*/
                    case R.id.edit:
                        EventBus.getDefault().post(new EditTransactionEvent(userTransaction));
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(transactionList== null)
            return 0;
        return transactionList.size();
    }



    public void reset() {
        transactionList = position1.getUserTransactions();
        notifyDataSetChanged();
    }

    public List<UserTransaction> getTransactionList() {
        return transactionList;
    }
}
