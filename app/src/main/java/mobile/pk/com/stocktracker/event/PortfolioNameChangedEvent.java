package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.Portfolio;

/**
 * Created by hello on 8/21/2015.
 */
public class PortfolioNameChangedEvent {
    private final Portfolio portfolio;

    public PortfolioNameChangedEvent(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }
}
