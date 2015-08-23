package mobile.pk.com.stocktracker.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.GenericRVAdapter;
import mobile.pk.com.stocktracker.adapters.PortfolioPositionAdapter;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioPositionViewHolder;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.event.PortfolioChangeEvent;
import mobile.pk.com.stocktracker.event.PortfolioDeleteEvent;
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.event.TransactionDeleteEvent;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.EditPortfolioActivity;
import mobile.pk.com.stocktracker.ui.activity.EditTransactionActivity;


public class PortfolioPositionFragment extends GenericRVFragment<PortfolioPositionViewHolder> {

   private static final String PORTFOLIO_ID = "PORTFOLIO_ID";

    // TODO: Rename and change types of parameters
   private long portfolioId;
    private Portfolio portfolio;
   private PortfolioPositionAdapter portfolioAdapter;

    public static PortfolioPositionFragment newInstance(Long portfolioId) {
        PortfolioPositionFragment fragment = new PortfolioPositionFragment();
        Bundle args = new Bundle();
        args.putLong(PORTFOLIO_ID, portfolioId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            portfolioId = getArguments().getLong(PORTFOLIO_ID);
            portfolio = Portfolio.findById(Portfolio.class, portfolioId);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected GenericRVAdapter getAdapter() {
        if(portfolioAdapter == null) {

            portfolioAdapter = new PortfolioPositionAdapter(getActivity(), portfolio);
        }
        return portfolioAdapter;
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
    protected void onAddNewItem() {
        EventBus.getDefault().post(new CreatePortfolioTransactionEvent(portfolio));
        /*Intent intent = new Intent(getActivity(),EditTransactionActivity.class );
        intent.putExtra(EditTransactionActivity.PORTFOLIO_ID, portfolioId);
        startActivityForResult(intent, BaseActivity.ADD_PORTFOLIO_TRANSACTION);*/
    }

    @Override
    protected boolean showCreateAction() {
        return true;
    }

    @Override
    protected boolean onEditView() {
        EventBus.getDefault().post(new OnEditEvent(portfolio));
        return true;
    }
    @Override
    protected  boolean onCreateNew(){
        EventBus.getDefault().post(new OnCreateEvent());
        return true;
    }

    @Override
    protected boolean onRefreshView() {
        portfolioAdapter.refreshPrices();
        return true;
    }

    public void onEvent(Position.PositionChangeEvent event){
        getAdapter().reset();
    }

    public class OnCreateEvent {

    }

    public class OnEditEvent{
        private final Portfolio data;

        public OnEditEvent(Portfolio data)
        {
            this.data = data;
        }


        public Portfolio getData() {
            return data;
        }
    }
    public class OnDeleteEvent {
        private final Portfolio data;

        public OnDeleteEvent(Portfolio data)
        {
            this.data = data;
        }
    }

    public class CreatePortfolioTransactionEvent {
        private final Portfolio portfolio;

        public CreatePortfolioTransactionEvent(Portfolio portfolio) {
            this.portfolio = portfolio;
        }

        public Portfolio getPortfolio() {
            return portfolio;
        }
    }
}
