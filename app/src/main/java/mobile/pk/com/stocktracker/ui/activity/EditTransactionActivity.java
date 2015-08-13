package mobile.pk.com.stocktracker.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.event.PortfolioChangeEvent;
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.event.WatchlistChangeEvent;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.EditPortfolioActivity;
import mobile.pk.com.stocktracker.ui.EditWatchlistActivity;
import mobile.pk.com.stocktracker.utils.StockSearchTextView;

/**
 * Created by hello on 8/11/2015.
 */
public class EditTransactionActivity extends AppCompatActivity {

    public static final String PORTFOLIO_ID = "PORTFOLIO_ID";
    public static final String TRANSACTION_ID = "TRANSACTION_ID";
    public static final String STOCK_ID = "STOCK_ID";
    private Long portfolioId;
    private Portfolio portfolio;

    @InjectView(R.id.stock)
    StockSearchTextView stockSearchTextView;
    @InjectView(R.id.quantity)
    TextView quantity;

    @InjectView(R.id.price)
    TextView price;

    @InjectView(R.id.radio_long)
    RadioButton radioLong;

    @InjectView(R.id.radio_short)
    RadioButton radioShort;

    @InjectView(R.id.title)
    TextView title;

    @InjectView(R.id.trade_date)
    EditText tradeDate;
    @InjectView(R.id.btn_delete)
    BootstrapButton deleteButton;
    private long transactionId;
    private UserTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        portfolioId = getIntent().getLongExtra(PORTFOLIO_ID, 0);
        transactionId = getIntent().getLongExtra(TRANSACTION_ID, 0);
        ButterKnife.inject(this);
        if(portfolioId == 0 && transactionId ==0) {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        } else {

            if(transactionId == 0)
            {
                portfolio = Watchlist.findById(Portfolio.class, portfolioId);
                transaction = new UserTransaction();
                transaction.setPortfolio(portfolio);
            }
            else
            {
                transaction = UserTransaction.findById(UserTransaction.class, transactionId);
                portfolio = transaction.getPortfolio();
                title.setText(getString(R.string.edit_transaction));
                deleteButton.setVisibility(View.VISIBLE);
                stockSearchTextView.setText(transaction.getStock().getName());
                quantity.setText(String.valueOf(transaction.getQuantity()));
                price.setText(String.valueOf(transaction.getPrice()));
                tradeDate.setText(DateUtils.formatDateTime(this, transaction.getTransactionDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR) );
                radioLong.setChecked(transaction.isLong());
                radioShort.setChecked(transaction.isShort());
            }
            if(transaction.getStock() == null)
                stockSearchTextView.requestFocus();
            else {
                stockSearchTextView.setEnabled(false);
                quantity.requestFocus();
            }
        }

    }

    @OnClick(R.id.btn_done)
    public void onDone()
    {
        if(stockSearchTextView.isEnabled() && stockSearchTextView.getMatch() != null) {
            transaction.setStock(Stock.from(stockSearchTextView.getMatch()));
        }
        transaction.setPortfolio(portfolio);
        transaction.setLongShortInd(radioLong.isChecked() ? 1 : 2);
        transaction.setPrice(Double.parseDouble(price.getText().toString()));
        transaction.setQuantity(Double.parseDouble(quantity.getText().toString()));
        transaction.setTransactionDate(Date.parse(tradeDate.getText().toString()));
        transaction.save();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(TRANSACTION_ID, transaction.getId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @OnClick(R.id.btn_delete)
    public void onDelete()
    {
        transaction.delete();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(STOCK_ID, transaction.getStock().getId());
        resultIntent.putExtra(PORTFOLIO_ID, transaction.getPortfolio().getId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        setResult(RESULT_CANCELED);
        finish();
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
}
