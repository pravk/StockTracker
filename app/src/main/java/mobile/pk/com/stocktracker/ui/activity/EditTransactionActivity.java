package mobile.pk.com.stocktracker.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
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

import org.w3c.dom.Text;

import java.util.Calendar;
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
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.utils.StockSearchTextView;

/**
 * Created by hello on 8/11/2015.
 */
public class EditTransactionActivity extends BaseActivity {

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
        setupToolbar();
        if(portfolioId == 0 && transactionId ==0) {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        } else {

            if(transactionId == 0)
            {
                setTitle(getString(R.string.create_new_transaction));
                deleteButton.setVisibility(View.GONE);
                radioLong.setChecked(true);
                portfolio = Watchlist.findById(Portfolio.class, portfolioId);
                transaction = new UserTransaction();
                transaction.setTransactionDate(Calendar.getInstance().getTimeInMillis());
                transaction.setPortfolio(portfolio);
            }
            else
            {
                setTitle(getString(R.string.edit_transaction));
                transaction = UserTransaction.findById(UserTransaction.class, transactionId);
                portfolio = transaction.getPortfolio();
                title.setText(getString(R.string.edit_transaction));
                deleteButton.setVisibility(View.VISIBLE);
                stockSearchTextView.setText(transaction.getStock().getName());
                quantity.setText(String.valueOf(transaction.getQuantity()));
                price.setText(String.valueOf(transaction.getPrice()));

                //tradeDate.setText(DateUtils.formatDateTime(this, transaction.getTransactionDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR) );
                radioLong.setChecked(transaction.isLong());
                radioShort.setChecked(transaction.isShort());
            }
            if(transaction.getStock() == null)
                stockSearchTextView.requestFocus();
            else {
                stockSearchTextView.setEnabled(false);
                quantity.requestFocus();
            }
            setTradeDate(transaction.getTransactionDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(transaction.getTransactionDate());

            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);

            tradeDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.set(year,monthOfYear,dayOfMonth);
                            setTradeDate(calendar1.getTimeInMillis());

                        }
                    },  year, month, day);
                    datePickerDialog.show();
                }
            });

        }

    }

    protected void setTradeDate(long timeInMillis)
    {
        tradeDate.setText(DateUtils.formatDateTime(this, timeInMillis, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
        transaction.setTransactionDate(timeInMillis);
    }

    @OnClick(R.id.btn_done)
    public void onDone()
    {
        if(!validate())
            return;
        if(stockSearchTextView.isEnabled() && stockSearchTextView.getMatch() != null) {
            transaction.setStock(Stock.from(stockSearchTextView.getMatch()));
        }
        transaction.setPortfolio(portfolio);
        transaction.setLongShortInd(radioLong.isChecked() ? UserTransaction.LONG : UserTransaction.SHORT);
        transaction.setPrice(Double.parseDouble(price.getText().toString()));
        transaction.setQuantity(Double.parseDouble(quantity.getText().toString()));
        //transaction.setTransactionDate(Date.parse(tradeDate.getText().toString()));
        transaction.save();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(TRANSACTION_ID, transaction.getId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private boolean validate()
    {
        boolean isValid = true;

        if(stockSearchTextView.isEnabled() && stockSearchTextView.getMatch() == null)
        {
            stockSearchTextView.setError("Please select a stock");
            return false;
        }
        else
        {
            stockSearchTextView.setError(null);
        }
        if(TextUtils.isEmpty(quantity.getText().toString())){
            quantity.setError("Please enter quantity");
            return false;
        }
        else
            quantity.setError(null);

        if(TextUtils.isEmpty(price.getText().toString())){
            price.setError("Please enter price");
            return false;
        }
        else
            price.setError(null);

        return true;
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
