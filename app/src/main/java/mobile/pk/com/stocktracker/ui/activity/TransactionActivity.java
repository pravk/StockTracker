package mobile.pk.com.stocktracker.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.TransactionAdapter;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.event.EditTransactionEvent;
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.event.TransactionDeleteEvent;
import mobile.pk.com.stocktracker.ui.BaseActivity;


public class TransactionActivity extends BaseActivity {

   public static final String POSITION_ID = "POSITION_ID";

    // TODO: Rename and change types of parameters
   private long positionId;
   private TransactionAdapter adapter;

    public TransactionActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        positionId = getIntent().getLongExtra(POSITION_ID, 0);
        ButterKnife.inject(this);

        final RecyclerView recyclerView =   (RecyclerView) findViewById(R.id.watchlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Position position = Position.findById(Position.class, positionId);
        adapter = new TransactionAdapter(this, position, RestClient.getDefault().getPricingService() );
        recyclerView.setAdapter(adapter);
        setTitle(position.getStock().getName() + "@" + position.getPortfolio().getPortfolioName());
        EventBus.getDefault().register(this);
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.fab_add_new_transaction)
    public void onAddNewTransaction(){
/*

        Intent intent = new Intent(getActivity(),EditTransactionActivity.class );
        intent.putExtra(EditTransactionActivity.PORTFOLIO_ID, portfolioId);
        startActivityForResult(intent, BaseActivity.ADD_PORTFOLIO_TRANSACTION);
*/

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
                adapter.refreshPrices(adapter.getTransactionList());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        }
        }

    }

    public void onEvent(EditTransactionEvent event){
        Intent intent = new Intent(this, EditTransactionActivity.class);
        intent.putExtra(EditTransactionActivity.TRANSACTION_ID, event.getTransaction().getId());
        startActivityForResult(intent, EDIT_USER_TRANSACTION);
    }

    public void onEvent(TransactionChangedEvent transactionChangeEvent){
        adapter.reset();
    }

    public void onEvent(TransactionDeleteEvent transactionDeleteEvent){
        adapter.reset();
    }
}
