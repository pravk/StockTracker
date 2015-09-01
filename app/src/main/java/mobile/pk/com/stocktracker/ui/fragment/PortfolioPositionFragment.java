package mobile.pk.com.stocktracker.ui.fragment;

import android.os.Bundle;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.adapters.PortfolioPositionAdapter;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dataobjects.PositionData;


public class PortfolioPositionFragment extends GenericRVFragment<PositionData> {

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
    protected PortfolioPositionAdapter getAdapter() {
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

   /* @Override
    protected boolean onRefreshView() {
        portfolioAdapter.populatePrices(true);
        return true;
    }*/

    public void onEvent(Position.PositionChangeEvent event){
        reset();
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
