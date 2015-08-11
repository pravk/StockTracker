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

/**
 * Created by hello on 8/10/2015.
 */
public class SelectStockDialog extends DialogFragment  {

    public interface SelectStockDialogListener
    {
        void onStockSelect(Stock  stock);
        void onCancel();

    }

    private DelayAutoCompleteTextView tickerSearchView;
    private StockAutoCompleteAdapter adapter;
    private SelectStockDialogListener listener;
    private TickerSearchService.Match match;

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
        tickerSearchView = (DelayAutoCompleteTextView) view.findViewById(R.id.ticker_search);
        String title = getArguments().getString("title", "Add Stock");
        getDialog().setTitle(title);
        // Show soft keyboard automatically
        tickerSearchView.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        adapter = new StockAutoCompleteAdapter(getActivity(), ((Application)getActivity().getApplication()).getRestClient().getTickerSearchService());
        tickerSearchView.setAdapter(adapter);

        tickerSearchView.setLoadingIndicator((android.widget.ProgressBar) view.findViewById(R.id.pb_loading_indicator));
        tickerSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                match = (TickerSearchService.Match) adapterView.getItemAtPosition(position);
                tickerSearchView.setText(match.getName());
            }
        });

        Button button = (Button)view.findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Stock s = Stock.from(match);
                listener.onStockSelect(s);
                getDialog().dismiss();
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
