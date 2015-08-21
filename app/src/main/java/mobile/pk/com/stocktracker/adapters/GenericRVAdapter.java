package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.StockPrice;
import mobile.pk.com.stocktracker.dao.tasks.HasStock;
import mobile.pk.com.stocktracker.dao.tasks.PriceLoadTask;
import mobile.pk.com.stocktracker.dao.tasks.ServerPriceRefreshTask;

/**
 * Created by hello on 8/20/2015.
 */
public abstract class GenericRVAdapter<T extends RecyclerView.ViewHolder, D extends SugarRecord> extends RecyclerView.Adapter<T> {

    protected Context mContext;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    protected static final String PRICE_CHANGE_FORMAT = "%s (%s%%)";
    protected static final String PRICE_FORMAT = "%s %2$,.2f";

    private List<D> dataList;

    public GenericRVAdapter(Context context)
    {
        this.mContext = context;
    }

    @Override
    public T onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_HEADER)
        {
            return onCreateViewHolderHeaderInternal(viewGroup, viewType);
        }
        else
        {
            return onCreateViewHolderInternal(viewGroup, viewType);
        }
    }

    protected T onCreateViewHolderHeaderInternal(ViewGroup viewGroup, int viewType){
        throw new RuntimeException("Not supported by this class");
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        if(isPositionHeader(position))
        {
            onBindViewHolderHeaderInternal(holder, position);
        }
        else
        {
            if(hasHeader())
                position = position-1;
            onBindViewHolderInternal(holder,position);
        }
    }

    protected void onBindViewHolderHeaderInternal(T holder, int i){
        //Do nothing
    }

    protected abstract void onBindViewHolderInternal(T holder, int i);

    public void reset()
    {
        List<D> newData = refreshDataInternal();
        getDataList().clear();
        getDataList().addAll(newData);
        populatePrices();
    }

    public void addItem(D item) {
        getDataList().add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(hasHeader())
            return getDataList().size() + 1;
        else
            return getDataList().size();
    }

    public List<D> getDataList(){
        if(dataList==null)
            dataList = new ArrayList<>();
        return dataList;
    }

    protected abstract T onCreateViewHolderInternal(ViewGroup viewGroup, int i);

    protected abstract List<D> refreshDataInternal();

    public void populatePrices(){
        List<D> dataList = getDataList();
        final List<Stock> stockList = new ArrayList<>();
        for(D data : dataList)
        {
            if(data instanceof HasStock)
                stockList.add(((HasStock) data).getStock());

        }
        new PriceLoadTask(){
            @Override
            protected void onPostExecute(List<StockPrice> result) {
                if(result != null && result.size()==stockList.size())
                    notifyDataSetChanged();
                else
                    refreshPrices();
            }

        }.execute(stockList.toArray(new Stock[stockList.size()]));

    }

    public void refreshPrices(){
        List<D> dataList = getDataList();
        final List<Stock> stockList = new ArrayList<>();
        for(D data : dataList)
        {
            if(data instanceof HasStock)
                stockList.add(((HasStock) data).getStock());

        }

        new ServerPriceRefreshTask(RestClient.getDefault().getPricingService()) {
            @Override
            protected void onPostExecute(Void result) {
                if(getException() == null) {
                    populatePrices();
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
        return TYPE_ITEM;
    }

    protected boolean hasHeader(){
        return false;
    }

    protected boolean isPositionHeader(int position)
    {
        return hasHeader() && position == 0;
    }
}
