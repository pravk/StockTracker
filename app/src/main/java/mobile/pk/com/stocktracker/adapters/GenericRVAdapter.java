package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioViewHolder;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.WatchlistStock;

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
}
