package mobile.pk.com.stocktracker.dao;

/**
 * Created by hello on 8/12/2015.
 */
public class PortfolioPosition {
    private Portfolio portfolio;
    private Position position;

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
