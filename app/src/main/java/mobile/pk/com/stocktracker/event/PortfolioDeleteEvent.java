package mobile.pk.com.stocktracker.event;

/**
 * Created by hello on 8/11/2015.
 */
public class PortfolioDeleteEvent {
    private Long portfolioId;


    public PortfolioDeleteEvent(Long portfolioId) {
        this.portfolioId = portfolioId;
    }
}
