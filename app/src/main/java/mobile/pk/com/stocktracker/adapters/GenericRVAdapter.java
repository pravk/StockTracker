package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.StockPrice;
import mobile.pk.com.stocktracker.dao.tasks.PriceLoadTask;
import mobile.pk.com.stocktracker.dao.tasks.ServerPriceRefreshTask;

/**
 * Created by hello on 8/20/2015.
 */
public abstract class GenericRVAdapter<D> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_SUMMARY = 2;

    protected static final String PRICE_CHANGE_WITH_PERCENT_FORMAT = "%s (%s%%)";
    protected static final String PRICE_CHANGE_FORMAT = "%s";
    protected static final String PRICE_CHANGE_PERCENT_FORMAT = "%s%%";
    protected static final String PRICE_FORMAT = "%s %2$,.2f";
    protected static final String PRICE_FORMAT_WITHOUT_CURRENCY = "%1$,.2f";

    private List<D> dataList;

    public GenericRVAdapter(Context context)
    {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_HEADER)
        {
            return onCreateViewHolderHeaderInternal(viewGroup, viewType);
        }
        else if(viewType == TYPE_SUMMARY)
        {
            return onCreateViewHolderSummary(viewGroup,viewType);
        }
        else
        {
            return onCreateViewHolderInternal(viewGroup, viewType);
        }
    }

    protected RecyclerView.ViewHolder onCreateViewHolderSummary(ViewGroup viewGroup, int viewType) {
        throw new RuntimeException("Not supported by this class");
    }

    protected RecyclerView.ViewHolder onCreateViewHolderHeaderInternal(ViewGroup viewGroup, int viewType){
        throw new RuntimeException("Not supported by this class");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isPositionHeader(position))
        {
            onBindViewHolderHeaderInternal(holder, position);
        }
        else if (isPositionSummary(position))
        {
            onBindViewHolderSummary(holder, position);
        }
        else
        {
            onBindViewHolderInternal(holder,position);
        }
    }

    protected void onBindViewHolderSummary(RecyclerView.ViewHolder holder, int position) {
        throw new RuntimeException("Not supported by this class");

    }

    protected void onBindViewHolderHeaderInternal(RecyclerView.ViewHolder holder, int i){
        throw new RuntimeException("Not supported by this class");

    }

    protected abstract void onBindViewHolderInternal(RecyclerView.ViewHolder holder, int i);

    public void refreshData()
    {
        List<D> newData = refreshDataInternal();
        getDataList().clear();
        getDataList().addAll(newData);
    }

    /*public void addItem(D item) {
        getDataList().add(item);
        reset();
        notifyDataSetChanged();

    }
*/
    @Override
    public int getItemCount() {
        return getDataList().size();
    }

    public List<D> getDataList(){
        if(dataList==null)
            dataList = new ArrayList<>();
        return dataList;
    }

    protected abstract RecyclerView.ViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i);

    protected abstract List<D> refreshDataInternal();

    public void populatePrices(final boolean force){
        List<D> dataList = getDataList();
        final List<Stock> stockList = new ArrayList<>();
        for(D data : dataList)
        {
            Stock stock= getUnderlyingStock(data);
            if(stock != null)
                stockList.add(stock);
        }

        new PriceLoadTask(){
            @Override
            protected void onPostExecute(List<StockPrice> result) {
                if(result != null)
                    notifyDataSetChanged();
                if(force)
                    refreshPrices(stockList);
            }

        }.execute(stockList.toArray(new Stock[stockList.size()]));

    }

    protected abstract Stock getUnderlyingStock(D data);/*{
        if(data instanceof HasStock)
            return ((HasStock) data).getStock();
        else
            return  null;
    }*/

    public void refreshPrices(List<Stock> stockList){

        new ServerPriceRefreshTask(RestClient.getDefault().getPricingService()) {
            @Override
            protected void onPostExecute(List<StockPrice> result) {
                if(getException() == null) {
                    populatePrices(false);
                }
                else
                {
                    Toast.makeText(mContext, getException().getMessage(), Toast.LENGTH_SHORT);
                }
            }

        }.execute(stockList.toArray(new Stock[stockList.size()]));
    }

    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        if(hasHeader() && isPositionHeader(position))
            return TYPE_HEADER;
        else if (hasSummary() && isPositionSummary(position))
            return TYPE_SUMMARY;
        return TYPE_ITEM;
    }

    protected boolean isPositionSummary(int position) {
        return false;
    }

    protected boolean hasSummary() {
        return false;
    }

    protected boolean hasHeader(){
        return false;
    }

    protected boolean isPositionHeader(int position)
    {
        return hasHeader() && position == 0;
    }
}
