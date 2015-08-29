package mobile.pk.com.stocktracker.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import mobile.pk.com.stocktracker.adapters.StockAutoCompleteAdapter;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.service.TickerSearchService;

/**
 * Created by hello on 8/12/2015.
 */
public class StockSearchTextView extends DelayAutoCompleteTextView {

    private StockAutoCompleteAdapter adapter;

    public TickerSearchService.Match getMatch() {
        return match;
    }

    private TickerSearchService.Match match;

    private MatchSelectListener matchSelectListener;

    public StockSearchTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) {
            adapter = new StockAutoCompleteAdapter(context, RestClient.getDefault().getTickerSearchService());
            setAdapter(adapter);

            setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    match = (TickerSearchService.Match) adapterView.getItemAtPosition(position);
                    setText(match.getName());
                    if(matchSelectListener != null)
                        matchSelectListener.onMatchSelect(match);
                }
            });
        }
    }

    public MatchSelectListener getMatchSelectListener() {
        return matchSelectListener;
    }

    public void setMatchSelectListener(MatchSelectListener matchSelectListener) {
        this.matchSelectListener = matchSelectListener;
    }

    public interface MatchSelectListener
    {
        public void onMatchSelect(TickerSearchService.Match match);
    }

}
