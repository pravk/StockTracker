package mobile.pk.com.stocktracker.dao;

import com.orm.StringUtil;
import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.dao.tasks.HasStock;

/**
 * Created by hello on 8/10/2015.
 */
public class Position extends SugarRecord<Position> implements HasStock {

    private Portfolio portfolio;
    private Stock stock;
    private double quantity;
    private double averagePrice;
    private double totalPrice;
    private int longShortInd;
    private double netRealizedGainLoss;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getLongShortInd() {
        return longShortInd;
    }

    public void setLongShortInd(int longShortInd) {
        this.longShortInd = longShortInd;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<UserTransaction> getUserTransactions(){
        return UserTransaction.find(UserTransaction.class, "stock=? and portfolio = ?", new String []{ String.valueOf(stock.getId()), String.valueOf(portfolio.getId())} ,null, StringUtil.toSQLName("transactionDate"), null );
    }

    public static Position reEvaluate(Stock stock, Portfolio portfolio) {
        Position position = getPosition(stock, portfolio);
        if(position == null)
        {
            position = new Position();
            position.setStock(stock);
            position.setPortfolio(portfolio);
        }
        double[] avgPriceAndQuantity = getAveragePurchasePrice(stock, portfolio, Calendar.getInstance().getTimeInMillis());

        position.setQuantity(avgPriceAndQuantity[1]);
        position.setAveragePrice(avgPriceAndQuantity[0]);
        position.setNetRealizedGainLoss(avgPriceAndQuantity[2]);
        position.setLongShortInd(position.getQuantity()<0?1:2);
        position.setTotalPrice(position.getQuantity() * position.getAveragePrice());
        position.save();
        EventBus.getDefault().post(new PositionChangeEvent(position));
        return position;
    }

    public static double[] getAveragePurchasePrice(Stock stock, Portfolio portfolio, Long date){
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

    public static List<UserTransaction> getUserTransactions(Stock stock, Portfolio portfolio)
    {
        return UserTransaction.find(UserTransaction.class, "stock=? and portfolio = ?", new String[]{String.valueOf(stock.getId()), String.valueOf(portfolio.getId())}, null, StringUtil.toSQLName("transactionDate"), null);
    }

    public static Position getPosition(Stock stock, Portfolio portfolio)
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

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getGainLoss() {
        return getQuantity() * (getStock().getPrice().getLastPrice() - averagePrice);
    }

    public double getMarketValue() {
        return getQuantity() * getStock().getPrice().getLastPrice();
    }

    public double getNetRealizedGainLoss() {
        return netRealizedGainLoss;
    }

    public void setNetRealizedGainLoss(double netRealizedGainLoss) {
        this.netRealizedGainLoss = netRealizedGainLoss;
    }

    public static class PositionChangeEvent {
        private final Position position;

        public PositionChangeEvent(Position position) {
            this.position = position;
        }

        public Position getPosition() {
            return position;
        }
    }
}
