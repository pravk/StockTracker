package mobile.pk.com.stocktracker.dao;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by hello on 8/10/2015.
 */
public class Portfolio extends SugarRecord<Portfolio>{

   private String portfolioName;
   public String getPortfolioName() {
        return portfolioName;
    }

   public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

}
