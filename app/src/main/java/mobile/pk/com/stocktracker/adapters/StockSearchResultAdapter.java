package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.StockSearchResultViewHolder;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.service.TickerSearchService;

/**
 * Created by hello on 8/29/2015.
 */
public class StockSearchResultAdapter extends GenericRVAdapter<StockSearchResultViewHolder, TickerSearchService.Match> {

    private String query;

    public StockSearchResultAdapter(Context context, String query) {
        super(context.getApplicationContext());
        this.query = query;
    }

    @Override
    protected void onBindViewHolderInternal(StockSearchResultViewHolder holder, int i) {
        TickerSearchService.Match match = getDataList().get(i);
        if(match != null)
        {
            holder.exchange.setText(match.getExchange());
            holder.name.setText(match.getName());
            holder.ticker.setText(match.getTicker());
        }

    }

    @Override
    protected StockSearchResultViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_stock_search_result_item, viewGroup, false);

        final StockSearchResultViewHolder viewHolder = new StockSearchResultViewHolder(view);

        return viewHolder;
    }

    @Override
    protected List<TickerSearchService.Match> refreshDataInternal() {
        return RestClient.getDefault().getTickerSearchService().searchTicker(getQuery()).getMatches();
    }

    @Override
    protected Stock getUnderlyingStock(TickerSearchService.Match data) {
        return Stock.from(data);
    }

    public String getQuery() {
        return query;
    }
    @Override
    public void populatePrices(final boolean force){
        //ignore
    }
}
