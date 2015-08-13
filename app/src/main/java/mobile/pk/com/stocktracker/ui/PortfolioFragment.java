package mobile.pk.com.stocktracker.ui;

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
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.event.PortfolioChangeEvent;
import mobile.pk.com.stocktracker.event.PortfolioDeleteEvent;
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.event.TransactionDeleteEvent;
import mobile.pk.com.stocktracker.ui.activity.EditTransactionActivity;


public class PortfolioFragment extends Fragment {

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

    public PortfolioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            portfolioId = getArguments().getLong(PORTFOLIO_ID);
        }
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        ButterKnife.inject(this, view);

        final RecyclerView recyclerView =   (RecyclerView) view.findViewById(R.id.watchlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Portfolio portfolio = Portfolio.findById(Portfolio.class, portfolioId);
        portfolioAdapter = new PortfolioAdapter(getActivity(), portfolio, RestClient.getDefault().getPricingService() );
        recyclerView.setAdapter(portfolioAdapter);

        return view;
    }

    @OnClick(R.id.fab_add_new_transaction)
    public void onAddNewTransaction(){

        Intent intent = new Intent(getActivity(),EditTransactionActivity.class );
        intent.putExtra(EditTransactionActivity.PORTFOLIO_ID, portfolioId);
        startActivityForResult(intent, BaseActivity.ADD_PORTFOLIO_TRANSACTION);

    }

    @Override
    public void onCreateOptionsMenu(
        Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.watchlist_toolbar_menu, menu);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.refresh_view:
                portfolioAdapter.refreshPrices(portfolioAdapter.getPositionList());
                return true;
            case R.id.edit_view:
                Intent intent = new Intent(getActivity(), EditPortfolioActivity.class);
                intent.putExtra(EditPortfolioActivity.PORTFOLIO_ID, portfolioId);
                startActivityForResult(intent, BaseActivity.EDIT_PORTFOLIO_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
