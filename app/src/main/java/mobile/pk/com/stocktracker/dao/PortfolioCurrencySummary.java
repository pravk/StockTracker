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
