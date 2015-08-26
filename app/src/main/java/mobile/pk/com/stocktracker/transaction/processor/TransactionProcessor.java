package mobile.pk.com.stocktracker.transaction.processor;

import android.database.Cursor;
import android.text.TextUtils;

import com.orm.StringUtil;

import java.util.ArrayList;
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
import mobile.pk.com.stocktracker.transaction.handler.TransactionFactory;
import mobile.pk.com.stocktracker.transaction.handler.TransactionHandler;

/**
 * Created by hello on 8/24/2015.
 */
public class TransactionProcessor {
    private static TransactionProcessor instance;
    private static final String PORTFOLIO_SUMMARY_QUERY = "SELECT port.id as portfolioId, port.portfolio_name as portfolioName, sp.currency as currency, " +
            " SUM( pos.quantity*sp.last_price) as netAsset, " +
            " SUM(pos.quantity*pos.average_price) as investmentAmount," +
            " SUM(pos.net_realized_gain_loss) as realizedGainLoss" +
            " FROM Portfolio port " +
            " JOIN Position pos on pos.portfolio = port.id " +
            " JOIN Stock s on pos.stock = s.id  " +
            " JOIN Stock_price sp on s.client_id = sp.client_id " +
            " GROUP BY port.id, sp.currency " +
            " --HAVING SUM(pos.quantity*pos.average_price)>0      ";
    public TransactionProcessor()
    {
        EventBus.getDefault().register(this);
        instance = this;
    }
    public static TransactionProcessor getInstance() {
        return instance;
    }

    public void onEvent(TransactionChangedEvent event){
        refreshPosition(getOpenPosition(event.getTransaction().getStock(), event.getTransaction().getPortfolio()));
    }

    public void onEvent(TransactionDeleteEvent event){
        refreshPosition(getOpenPosition(event.getStock(), event.getPortfolio()));
    }

    public void onEvent(RefreshPositionEvent event){
        refreshPosition(event.getPosition());
    }

    protected void refreshPosition(Position position){
        List<UserTransaction> userTransactionList = getUserTransactions(position);
        position.reset();
        for(UserTransaction userTransaction: userTransactionList)
        {
            TransactionHandler handler = TransactionFactory.getHandler(userTransaction.getTransactionType());
            if(handler != null)
            {
               handler.apply(position, userTransaction);

            }
            else
            {
                position.setError("Not handler found for transaction type " + userTransaction.getTransactionType());
            }

            if(!TextUtils.isEmpty(position.getError()))
                break;
        }

        position.save();
        EventBus.getDefault().post(new Position.PositionChangeEvent(position));
    }

    public Position getOpenPosition(Stock stock, Portfolio portfolio)
    {
        List<Position> positionList = Position.find(Position.class, "stock=? and portfolio=?", String.valueOf(stock.getId()), String.valueOf(portfolio.getId()));
        Position position = null;
        if(positionList == null || positionList.size()==0)
        {
            position = new Position();
            position.setStock(stock);
            position.setPortfolio(portfolio);
            portfolio.save();
        }
        else
        {
            return positionList.get(0);
        }
        return position;
    }

    public List<Position> getOpenPositions(Portfolio portfolio){
        return Position.find(Position.class,  "portfolio = ? and quantity != 0", String.valueOf(portfolio.getId()) );
    }
    public List<Position> getClosedPositions(Portfolio portfolio){
        return Position.find(Position.class,  "portfolio = ? and quantity = 0", String.valueOf(portfolio.getId()) );
    }

    public List<UserTransaction> getUserTransactions(Position position){
        return UserTransaction.find(UserTransaction.class, "stock=? and portfolio = ?", new String []{ String.valueOf(position.getStock().getId()), String.valueOf(position.getPortfolio().getId())} ,null, StringUtil.toSQLName("transactionDate"), null );
    }

    public List<UserTransaction> getOpenUserTransactions(Position position, String transactionType, long before){
        return UserTransaction.find(UserTransaction.class, "stock=? and portfolio = ? and transaction_type=? and transaction_date<?"
                , new String []{ String.valueOf(position.getStock().getId()), String.valueOf(position.getPortfolio().getId()), transactionType, String.valueOf(before)} ,null, StringUtil.toSQLName("transactionDate"), null );
    }

    public List<PortfolioCurrencySummary> getPortfolioSummary(){

        Cursor c = Application.getInstance().executeQuery(PORTFOLIO_SUMMARY_QUERY);
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
