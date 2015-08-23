package mobile.pk.com.stocktracker.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.adapters.PortfolioSummaryAdapter;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioSummaryViewHolder;

/**
 * Created by hello on 8/22/2015.
 */
public class PortfolioSummaryFragment extends GenericRVFragment<PortfolioSummaryViewHolder> {

    PortfolioSummaryAdapter adapter;

    @Override
    protected boolean onEditView() {
        return false;
    }

    @Override
    protected boolean onRefreshView() {
        return false;
    }

    @Override
    protected boolean showEditAction() {
        return false;
    }

    @Override
    protected boolean showRefreshAction() {
        return false;
    }

    @Override
    protected boolean showAddNewItem(){
        return false;
    }
    @Override
    protected PortfolioSummaryAdapter getAdapter() {
        if(adapter == null)
            adapter = new PortfolioSummaryAdapter(getActivity());
        return adapter;
    }

    @Override
    protected void onAddNewItem() {

    }

    public static Fragment newInstance(Context context) {
        PortfolioSummaryFragment fragment = new PortfolioSummaryFragment();
        fragment.setContext(context);
        return fragment;
    }
}
