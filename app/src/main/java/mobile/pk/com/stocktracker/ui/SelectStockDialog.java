package mobile.pk.com.stocktracker.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.StockAutoCompleteAdapter;
import mobile.pk.com.stocktracker.common.Application;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.service.TickerSearchService;
import mobile.pk.com.stocktracker.utils.DelayAutoCompleteTextView;
import mobile.pk.com.stocktracker.utils.StockSearchTextView;

/**
 * Created by hello on 8/10/2015.
 */
public class SelectStockDialog extends DialogFragment  {

    public interface SelectStockDialogListener
    {
        void onStockSelect(Stock  stock);
        void onCancel();

    }

    private StockSearchTextView tickerSearchView;
    private StockAutoCompleteAdapter adapter;
    private SelectStockDialogListener listener;

    public static SelectStockDialog newInstance(String title, SelectStockDialogListener listener) {
        SelectStockDialog frag = new SelectStockDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.listener = listener;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_stock_watchlist, container);
        tickerSearchView = (StockSearchTextView) view.findViewById(R.id.ticker_search);
        String title = getArguments().getString("title", "Add Stock");
        getDialog().setTitle(title);
        // Show soft keyboard automatically
        tickerSearchView.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        Button button = (Button)view.findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(tickerSearchView.getMatch()!= null) {
                    Stock s = Stock.from(tickerSearchView.getMatch());
                    listener.onStockSelect(s);
                    getDialog().dismiss();
                }
            }
        });

        Button cancel = (Button)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onCancel();
                getDialog().dismiss();
            }
        });
        return view;
    }
}
