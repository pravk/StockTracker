package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.Watchlist;

/**
 * Created by hello on 8/11/2015.
 */
public class PortfolioChangeEvent {
    private Portfolio portfolio;

    public PortfolioChangeEvent(Portfolio portfolio)
    {
        this.portfolio = portfolio;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
