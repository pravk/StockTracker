package mobile.pk.com.stocktracker.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.adapters.UserTransactionAdapter;
import mobile.pk.com.stocktracker.adapters.viewholder.TransactionViewHolder;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.event.EditTransactionEvent;
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.event.TransactionDeleteEvent;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.activity.EditTransactionActivity;

/**
 * Created by hello on 8/26/2015.
 */
public class UserTransactionFragment extends GenericRVFragment<TransactionViewHolder> {
    private static final String POSITION_ID = "POSITION_ID";
    UserTransactionAdapter adapter;
    Position position;

    public static UserTransactionFragment newInstance(Position position, Context context) {
        UserTransactionFragment fragment = new UserTransactionFragment();
        fragment.setContext(context);
        Bundle args = new Bundle();
        fragment.position = position;
        fragment.setArguments(args);
        EventBus.getDefault().register(fragment);
        return fragment;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected boolean onEditView() {
        return false;
    }

    @Override
    protected boolean onRefreshView() {
        adapter.reset();
        return true;
    }

    @Override
    protected boolean showEditAction() {
        return false;
    }

    @Override
    protected boolean showRefreshAction() {
        return true;
    }

    @Override
    protected UserTransactionAdapter getAdapter() {
        if(adapter == null)
            adapter = new UserTransactionAdapter(getContext(), position);
        return adapter;
    }

    @Override
    protected void onAddNewItem() {

    }

    public void onEvent(TransactionChangedEvent transactionChangeEvent){
        adapter.reset();
    }

    public void onEvent(TransactionDeleteEvent transactionDeleteEvent){
        adapter.reset();
    }
    public void onEvent(EditTransactionEvent event){
        Intent intent = new Intent(getContext(), EditTransactionActivity.class);
        intent.putExtra(EditTransactionActivity.TRANSACTION_ID, event.getTransaction().getId());
        startActivityForResult(intent, BaseActivity.EDIT_USER_TRANSACTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BaseActivity.EDIT_USER_TRANSACTION) {
            if (resultCode == Activity.RESULT_OK) {
                Long transactionId = data.getLongExtra(EditTransactionActivity.TRANSACTION_ID, 0);
                if(transactionId != 0) {
                    UserTransaction userTransaction = UserTransaction.findById(UserTransaction.class, transactionId);
                    EventBus.getDefault().post(new TransactionChangedEvent(userTransaction));
                }
                else
                {
                    long stockId = data.getLongExtra(EditTransactionActivity.STOCK_ID, 0);
                    long portfolioId = data.getLongExtra(EditTransactionActivity.PORTFOLIO_ID, 0);
                    EventBus.getDefault().post(new TransactionDeleteEvent(Stock.findById(Stock.class, stockId), Portfolio.findById(Portfolio.class, portfolioId)));
                }

            }
        }

    }
}
