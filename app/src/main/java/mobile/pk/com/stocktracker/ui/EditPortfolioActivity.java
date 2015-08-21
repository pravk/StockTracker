package mobile.pk.com.stocktracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Watchlist;

/**
 * Created by hello on 8/11/2015.
 */
public class EditPortfolioActivity extends BaseActivity {

    public static final String PORTFOLIO_ID = "PORTFOLIO_ID";
    private Long portfolioId;
    private Portfolio portfolio;

    @InjectView(R.id.portfolio_name)
    BootstrapEditText portfolioName;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.btn_delete)
    BootstrapButton deleteButton;
    //private EditWatchlistActivity.WatchListUpdateListener watchListUpdateListener;


    /*public static EditWatchlistActivity newInstance(Long watchListId, WatchListUpdateListener watchListUpdateListener) {
        EditWatchlistActivity fragment = new EditWatchlistActivity();
        Bundle args = new Bundle();
        if(watchListId != null)
            args.putLong(WATCH_LIST_ID, watchListId);
        fragment.setArguments(args);
        fragment.watchListUpdateListener = watchListUpdateListener;
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_portfolio);
        setupToolbar();
        portfolioId = getIntent().getLongExtra(PORTFOLIO_ID, 0);
        ButterKnife.inject(this);
        if(portfolioId == 0) {
            portfolio = new Portfolio();
            portfolio.setPortfolioName("");
            deleteButton.setVisibility(View.GONE);
            setTitle(getString(R.string.create_new_portfolio));
        }
        else {
            portfolio = Watchlist.findById(Portfolio.class, portfolioId);
            setTitle(getString(R.string.edit_portfolio));
            title.setText(getString(R.string.edit_portfolio));
            deleteButton.setVisibility(View.VISIBLE);
        }
        if(portfolio != null)
            portfolioName.setText(portfolio.getPortfolioName());

    }

    @OnClick(R.id.btn_done)
    public void onDone()
    {
        if(TextUtils.isEmpty(portfolioName.getText().toString()))
            return;

        portfolio.setPortfolioName(portfolioName.getText().toString());
        portfolio.save();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(PORTFOLIO_ID, portfolio.getId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @OnClick(R.id.btn_delete)
    public void onDelete()
    {
        portfolio.delete();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(PORTFOLIO_ID, portfolio.getId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        setResult(RESULT_CANCELED);
        finish();
    }

}
