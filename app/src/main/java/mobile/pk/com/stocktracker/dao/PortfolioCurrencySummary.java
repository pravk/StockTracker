package mobile.pk.com.stocktracker.dao;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import mobile.pk.com.stocktracker.common.Application;

/**
 * Created by hello on 8/21/2015.
 */
public class PortfolioCurrencySummary {

    private String portfolioId;
    private String portfolioName;
    private String currency;
    private double netGainLoss;
    private double realizedGainLoss;
    private double unrealizedGainLoss;
    private double investmentAmount;
    private double netAsset;
    private double returnPercent;


    private static final String PORTFOLIO_SUMMARY_QUERY = "SELECT port.id as portfolioId, port.portfolio_name as portfolioName, sp.currency as currency, " +
                                                            " SUM(pos.quantity*sp.last_price) as netAsset, " +
                                                            " SUM(pos.quantity*pos.average_price) as investmentAmount," +
                                                            " SUM(pos.net_realized_gain_loss) as realizedGainLoss" +
                                                            " FROM Portfolio port " +
                                                            " JOIN Position pos on pos.portfolio = port.id " +
                                                            " JOIN Stock s on pos.stock = s.id  " +
                                                            " JOIN Stock_price sp on s.client_id = sp.client_id " +
                                                            " GROUP BY port.id, sp.currency " +
                                                            " HAVING SUM(pos.quantity*pos.average_price)>0      ";

    public static List<PortfolioCurrencySummary> getAll(){

        Cursor c = Application.getInstance().executeQuery(PORTFOLIO_SUMMARY_QUERY,null);
        List<PortfolioCurrencySummary> portfolioCurrencySummaryList = new ArrayList<>();
        try {
            while(c.moveToNext()) {
                PortfolioCurrencySummary portfolioCurrencySummary = new PortfolioCurrencySummary();
                portfolioCurrencySummary.setPortfolioId(c.getString(c.getColumnIndex("portfolioId")));
                portfolioCurrencySummary.setPortfolioName(c.getString(c.getColumnIndex("portfolioName")));
                portfolioCurrencySummary.setCurrency(c.getString(c.getColumnIndex("currency")));
                portfolioCurrencySummary.setInvestmentAmount(c.getDouble(c.getColumnIndex("investmentAmount")));
                portfolioCurrencySummary.setNetAsset(c.getDouble(c.getColumnIndex("netAsset")));
                portfolioCurrencySummary.setUnrealizedGainLoss(portfolioCurrencySummary.netAsset - portfolioCurrencySummary.getInvestmentAmount());
                portfolioCurrencySummary.setRealizedGainLoss(c.getDouble(c.getColumnIndex("realizedGainLoss")));
                portfolioCurrencySummary.setReturnPercent(portfolioCurrencySummary.getUnrealizedGainLoss()*100/portfolioCurrencySummary.getInvestmentAmount());
                portfolioCurrencySummaryList.add(portfolioCurrencySummary);

            }
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            c.close();
        }
        return portfolioCurrencySummaryList;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getNetGainLoss() {
        return netGainLoss;
    }

    public void setNetGainLoss(double netGainLoss) {
        this.netGainLoss = netGainLoss;
    }

    public double getRealizedGainLoss() {
        return realizedGainLoss;
    }

    public void setRealizedGainLoss(double realizedGainLoss) {
        this.realizedGainLoss = realizedGainLoss;
    }

    public double getUnrealizedGainLoss() {
        return unrealizedGainLoss;
    }

    public void setUnrealizedGainLoss(double unrealizedGainLoss) {
        this.unrealizedGainLoss = unrealizedGainLoss;
    }

    public double getNetAsset() {
        return netAsset;
    }

    public void setNetAsset(double netAsset) {
        this.netAsset = netAsset;
    }

    public double getReturnPercent() {
        return returnPercent;
    }

    public void setReturnPercent(double returnPercent) {
        this.returnPercent = returnPercent;
    }

    public double getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(double investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }
}
