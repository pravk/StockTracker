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
import mobile.pk.com.stocktracker.dao.tasks.HasStock;
import mobile.pk.com.stocktracker.dao.tasks.PriceLoadTask;
import mobile.pk.com.stocktracker.dao.tasks.ServerPriceRefreshTask;

/**
 * Created by hello on 8/20/2015.
 */
public abstract class GenericRVAdapter<T extends RecyclerView.ViewHolder, D extends SugarRecord> extends RecyclerView.Adapter<T> {

    protected Context mContext;

    private List<D> dataList;

    public GenericRVAdapter(Context context)
    {
        this.mContext = context;
    }

    @Override
    public T onCreateViewHolder(ViewGroup viewGroup, int i) {
        return onCreateViewHolderInternal(viewGroup, i);
    }

    public void reset()
    {
        List<D> newData = refreshDataInternal();
        getDataList().clear();
        getDataList().addAll(newData);
        notifyDataSetChanged();
    }

    public void addItem(D item) {
        getDataList().add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return getDataList().size();
    }

    public List<D> getDataList(){
        if(dataList==null)
            dataList = new ArrayList<>();
        return dataList;
    }

    protected abstract T onCreateViewHolderInternal(ViewGroup viewGroup, int i);

    protected abstract List<D> refreshDataInternal();

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
                    new PriceLoadTask(){
                        @Override
                        protected void onPostExecute(Void result) {
                            notifyDataSetChanged();
                        }

                    }.execute(stockList.toArray(new Stock[stockList.size()]));
                }
                else
                {
                    Toast.makeText(mContext, getException().getMessage(), Toast.LENGTH_SHORT);
                }
            }

        }.execute(stockList.toArray(new Stock[stockList.size()]));
    }

}
