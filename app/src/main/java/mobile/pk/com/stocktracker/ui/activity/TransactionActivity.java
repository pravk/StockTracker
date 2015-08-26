package mobile.pk.com.stocktracker.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import butterknife.ButterKnife;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.fragment.UserTransactionFragment;


public class TransactionActivity extends BaseActivity {

   public static final String POSITION_ID = "POSITION_ID";

    // TODO: Rename and change types of parameters
   private long positionId;

    public TransactionActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        setupToolbar();
        positionId = getIntent().getLongExtra(POSITION_ID, 0);
        ButterKnife.inject(this);

        Position position = Position.findById(Position.class, positionId);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_layout, UserTransactionFragment.newInstance(position, this)).commit();

        setTitle(position.getStock().getName() + "@" + position.getPortfolio().getPortfolioName());

    }

}
