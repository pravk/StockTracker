package mobile.pk.com.stocktracker.adapters.PagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.ui.fragment.PortfolioPositionFragment;
import mobile.pk.com.stocktracker.ui.fragment.WatchlistStockFragment;

/**
 * Created by hello on 8/21/2015.
 */
public class PortfolioFragmentPagerAdapter extends BaseFragmentPagerAdapter<Portfolio> {

    List<Watchlist> watchlistList;

    public PortfolioFragmentPagerAdapter(FragmentManager fragmentManager, Context context)
    {
        super(fragmentManager, context);
    }

    private void createDefaultPotfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.setPortfolioName(getContext().getString(R.string.default_portfolio_name));
        portfolio.save();
    }

    @Override
    protected Fragment getFragment(Portfolio portfolio) {
        return PortfolioPositionFragment.newInstance(portfolio.getId());
    }

    @Override
    protected CharSequence getPageTitle(Portfolio portfolio) {
        return portfolio.getPortfolioName();
    }

    @Override
    protected List<Portfolio> getData() {
        List<Portfolio> portfolioList = Portfolio.find(Portfolio.class, null);
        if(portfolioList == null || portfolioList.size()==0)
        {
            createDefaultPotfolio();
            portfolioList = Portfolio.find(Portfolio.class, null);
        }
        return portfolioList;
    }
}
