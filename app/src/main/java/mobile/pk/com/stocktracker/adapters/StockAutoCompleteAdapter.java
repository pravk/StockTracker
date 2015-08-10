package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.service.TickerSearchService;

public class StockAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private final TickerSearchService service;
    private Context mContext;
    private List<TickerSearchService.Match> resultList = new ArrayList<TickerSearchService.Match>();

    public StockAutoCompleteAdapter(Context context, TickerSearchService service) {
        mContext = context;
        this.service = service;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public TickerSearchService.Match getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_dropdown_item_2line, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position).getName());
        ((TextView) convertView.findViewById(R.id.text2)).setText(getItem(position).getExchange() + ", " +getItem(position).getTicker());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<TickerSearchService.Match> matches = findMatch(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = matches;
                    filterResults.count = matches.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<TickerSearchService.Match>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    private List<TickerSearchService.Match> findMatch(Context context, String ticker) {
        TickerSearchService.TickerSearchResponse response = service.searchTicker(ticker);
        if(response != null)
            return response.getMatches();
        return null;
    }
}