package mobile.pk.com.stocktracker.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.adapters.PagerAdapter.PortfolioFragmentPagerAdapter;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.event.PortfolioNameChangedEvent;
import mobile.pk.com.stocktracker.event.PortfolioDeleteEvent;
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.EditPortfolioActivity;
import mobile.pk.com.stocktracker.ui.activity.EditTransactionActivity;

/**
 * Created by hello on 8/20/2015.
 */
public class PortfolioFragment extends ContainerFragment {

    public static PortfolioFragment newInstance(Context context) {
        PortfolioFragment fragment = new PortfolioFragment();
        fragment.setContext(context);
        EventBus.getDefault().register(fragment);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseActivity.EDIT_PORTFOLIO_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Long portfolioId = data.getLongExtra(EditPortfolioActivity.PORTFOLIO_ID, 0);
                Portfolio portfolio = Portfolio.findById(Portfolio.class, portfolioId);
                if(portfolio != null)
                    EventBus.getDefault().post(new PortfolioNameChangedEvent(portfolio));
                else
                    EventBus.getDefault().post(new PortfolioDeleteEvent(portfolioId));
            }

        } else if (requestCode == BaseActivity.ADD_PORTFOLIO_TRANSACTION) {
            if (resultCode == Activity.RESULT_OK) {
                Long transactionId = data.getLongExtra(EditTransactionActivity.TRANSACTION_ID, 0);
                UserTransaction transaction = UserTransaction.findById(UserTransaction.class, transactionId);
                EventBus.getDefault().post(new TransactionChangedEvent(transaction));
            }
        }
    }

    public void onEvent(PortfolioPositionFragment.OnCreateEvent event){
        Intent intent = new Intent(getActivity(), EditPortfolioActivity.class);
        startActivityForResult(intent, BaseActivity.EDIT_PORTFOLIO_REQUEST);
    }
    public void onEvent(PortfolioPositionFragment.OnEditEvent event){
        Intent intent = new Intent(getActivity(), EditPortfolioActivity.class);
        intent.putExtra(EditPortfolioActivity.PORTFOLIO_ID, event.getData().getId());
        startActivityForResult(intent, BaseActivity.EDIT_PORTFOLIO_REQUEST);
    }

    public void onEvent(PortfolioPositionFragment.CreatePortfolioTransactionEvent event){
        Intent intent = new Intent(getActivity(),EditTransactionActivity.class );
        intent.putExtra(EditTransactionActivity.PORTFOLIO_ID, event.getPortfolio().getId());
        startActivityForResult(intent, BaseActivity.ADD_PORTFOLIO_TRANSACTION);
    }

    @Override
    protected FragmentStatePagerAdapter getAdapter(FragmentManager fragmentManager, Context context) {
        return new PortfolioFragmentPagerAdapter(fragmentManager,context);
    }
}
