package mobile.pk.com.stocktracker.processor;

import android.database.Cursor;

import com.orm.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.common.Application;
import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.PortfolioCurrencySummary;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.event.RefreshPositionEvent;
import mobile.pk.com.stocktracker.event.TransactionChangedEvent;
import mobile.pk.com.stocktracker.event.TransactionDeleteEvent;

/**
 * Created by hello on 8/24/2015.
 */
public class TransactionProcessor {
    private static TransactionProcessor instance;
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
    public TransactionProcessor()
    {
        EventBus.getDefault().register(this);
        instance = this;
    }
    public static TransactionProcessor getInstance() {
        return instance;
    }

    public void onEvent(TransactionChangedEvent event){
        if(event.getTransaction().isShort())
        {
            event.getTransaction().setRealizedGainLoss(computeUnrealizedGain(event.getTransaction()));
            event.getTransaction().save();
        }
        updatePosition(event.getTransaction().getStock(), event.getTransaction().getPortfolio());
    }

    public void onEvent(TransactionDeleteEvent event){
        updatePosition(event.getStock(), event.getPortfolio());
    }
    public void onEvent(RefreshPositionEvent event){
        updatePosition(event.getPosition().getStock(), event.getPosition().getPortfolio());
    }

    protected double computeUnrealizedGain(UserTransaction userTransaction){
        double[] avgPriceAndQuantity = computeAveragePurchasePrice(userTransaction.getStock(), userTransaction.getPortfolio(), userTransaction.getTransactionDate());
        double avgPrice = avgPriceAndQuantity[0];
        return userTransaction.getQuantity() * (userTransaction.getPrice()- avgPrice);
    }

    protected Position updatePosition(Stock stock, Portfolio portfolio) {
        Position position = getPosition(stock, portfolio);
        if(position == null)
        {
            position = new Position();
            position.setStock(stock);
            position.setPortfolio(portfolio);
        }
        double[] avgPriceAndQuantity = computeAveragePurchasePrice(stock, portfolio, Calendar.getInstance().getTimeInMillis());

        position.setQuantity(avgPriceAndQuantity[1]);
        position.setAveragePrice(avgPriceAndQuantity[0]);
        position.setNetRealizedGainLoss(avgPriceAndQuantity[2]);
        position.setLongShortInd(position.getQuantity()<0?1:2);
        position.setTotalPrice(position.getQuantity() * position.getAveragePrice());
        position.save();
        EventBus.getDefault().post(new Position.PositionChangeEvent(position));
        return position;
    }

    public Position getPosition(Stock stock, Portfolio portfolio)
    {
        List<Position> positionList = Position.find(Position.class, "stock=? and portfolio=?", String.valueOf(stock.getId()), String.valueOf(portfolio.getId()));
        Position position = null;
        if(positionList == null || positionList.size()==0)
        {
            return null;
        }
        else
        {
            return positionList.get(0);
        }
    }

    public double[] computeAveragePurchasePrice(Stock stock, Portfolio portfolio, Long date){
        double avgPrice = 0;
        double quantity = 0;
        double realizedGainLoss = 0;
        List<UserTransaction> userTransactionList = getUserTransactions(stock,portfolio);
        if(userTransactionList == null || userTransactionList.size()==0)
            return new double[]{0,0,0};
        for(UserTransaction userTransaction: userTransactionList)
        {
            if(userTransaction.getTransactionDate() < date)
            {
                if(userTransaction.isShort())
                {
                    quantity = quantity - userTransaction.getQuantity();
                    realizedGainLoss = realizedGainLoss + userTransaction.getRealizedGainLoss();
                }
                else
                {

                    avgPrice = ((avgPrice * quantity)  + (userTransaction.getPrice() * userTransaction.getQuantity()))/ (quantity + userTransaction.getQuantity());
                    quantity = quantity + userTransaction.getQuantity();

                }
            }
        }
        return new double[] {avgPrice, quantity, realizedGainLoss};
    }


    public List<UserTransaction> getUserTransactions(Stock stock, Portfolio portfolio)
    {
        return UserTransaction.find(UserTransaction.class, "stock=? and portfolio = ?", new String[]{String.valueOf(stock.getId()), String.valueOf(portfolio.getId())}, null, StringUtil.toSQLName("transactionDate"), null);
    }



    public List<PortfolioCurrencySummary> getPortfolioSummary(){

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
                portfolioCurrencySummary.setUnrealizedGainLoss(portfolioCurrencySummary.getNetAsset() - portfolioCurrencySummary.getInvestmentAmount());
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

}
