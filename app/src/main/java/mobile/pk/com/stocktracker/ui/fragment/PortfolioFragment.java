package mobile.pk.com.stocktracker.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.PortfolioAdapter;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioViewHolder;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.event.PortfolioChangeEvent;
import mobile.pk.com.stocktracker.event.PortfolioDeleteEvent;
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.event.TransactionDeleteEvent;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.EditPortfolioActivity;
import mobile.pk.com.stocktracker.ui.activity.EditTransactionActivity;


public class PortfolioFragment extends GenericRVFragment<PortfolioViewHolder> {

   private static final String PORTFOLIO_ID = "PORTFOLIO_ID";

    // TODO: Rename and change types of parameters
   private long portfolioId;
   private PortfolioAdapter portfolioAdapter;

    public static PortfolioFragment newInstance(Long portfolioId) {
        PortfolioFragment fragment = new PortfolioFragment();
        Bundle args = new Bundle();
        args.putLong(PORTFOLIO_ID, portfolioId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean hasMenuOptions() {
        return true;
    }

    @Override
    protected RecyclerView.Adapter<PortfolioViewHolder> getAdapter() {
        if(portfolioAdapter == null) {
            Portfolio portfolio = Portfolio.findById(Portfolio.class, portfolioId);
            portfolioAdapter = new PortfolioAdapter(getActivity(), portfolio);
        }
        return portfolioAdapter;
    }

    @Override
    protected void onAddNewClick() {
        Intent intent = new Intent(getActivity(),EditTransactionActivity.class );
        intent.putExtra(EditTransactionActivity.PORTFOLIO_ID, portfolioId);
        startActivityForResult(intent, BaseActivity.ADD_PORTFOLIO_TRANSACTION);
    }

    @Override
    public void onCreateOptionsMenu(
        Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.watchlist_toolbar_menu, menu);
    }

    @Override
    protected boolean showEditAction() {
        return true;
    }

    @Override
    protected boolean showRefreshAction() {
        return true;
    }

    @Override
    protected boolean onEditView() {
        Intent intent = new Intent(getActivity(), EditPortfolioActivity.class);
        intent.putExtra(EditPortfolioActivity.PORTFOLIO_ID, portfolioId);
        startActivityForResult(intent, BaseActivity.EDIT_PORTFOLIO_REQUEST);
        return true;
    }

    @Override
    protected boolean onRefreshView() {
        portfolioAdapter.refreshPrices();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseActivity.EDIT_PORTFOLIO_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Long portfolioId = data.getLongExtra(EditPortfolioActivity.PORTFOLIO_ID, 0);
                Portfolio portfolio = Portfolio.findById(Portfolio.class, portfolioId);
                if(portfolio != null)
                    EventBus.getDefault().post(new PortfolioChangeEvent(portfolio));
                else
                    EventBus.getDefault().post(new PortfolioDeleteEvent(portfolioId));
            }
        }
        else if (requestCode == BaseActivity.ADD_PORTFOLIO_TRANSACTION) {
            if (resultCode == Activity.RESULT_OK) {
                Long transactionId = data.getLongExtra(EditTransactionActivity.TRANSACTION_ID, 0);
                UserTransaction transaction = UserTransaction.findById(UserTransaction.class, transactionId);
                if(transaction != null)
                    EventBus.getDefault().post(new TransactionChangedEvent(transaction));
                else {
                    Long stockId = data.getLongExtra(EditTransactionActivity.STOCK_ID, 0);
                    Long portfolioId= data.getLongExtra(EditTransactionActivity.PORTFOLIO_ID, 0);
                    Stock stock = Stock.findById(Stock.class, stockId);
                    Portfolio portfolio = Portfolio.findById(Portfolio.class, portfolioId);
                    EventBus.getDefault().post(new TransactionDeleteEvent(stock,portfolio));
                }
            }
        }

    }

    public void onEvent(TransactionChangedEvent transactionChangeEvent){
        Position.reeval(transactionChangeEvent.getTransaction().getStock(), transactionChangeEvent.getTransaction().getPortfolio());
        portfolioAdapter.reset();
    }

    public void onEvent(TransactionDeleteEvent transactionDeleteEvent){
        Position.reeval(transactionDeleteEvent.getStock(), transactionDeleteEvent.getPortfolio());
        portfolioAdapter.reset();
    }
}
