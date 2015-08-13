package mobile.pk.com.stocktracker.dao;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by hello on 8/10/2015.
 */
public class Position extends SugarRecord<Position> {

    private Portfolio portfolio;
    private Stock stock;
    private double quantity;
    private double averagePrice;
    private double totalPrice;
    private int longShortInd;

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

    public static Position reeval(Stock stock, Portfolio portfolio) {
        List<Position> positionList = Position.find(Position.class, "stock=? and portfolio=?", String.valueOf(stock.getId()), String.valueOf(portfolio.getId()));
        Position position = null;
        if(positionList == null || positionList.size()==0)
        {
            position = new Position();
            position.setStock(stock);
            position.setPortfolio(portfolio);
        }
        else
        {
            position = positionList.get(0);
        }

        List<UserTransaction> userTransactionList = UserTransaction.find(UserTransaction.class, "stock=? and portfolio = ?", String.valueOf(stock.getId()), String.valueOf(portfolio.getId()) );

        position.setQuantity(0);
        position.setAveragePrice(0);
        for(UserTransaction userTransaction: userTransactionList)
        {
            position.setQuantity(position.getQuantity() + userTransaction.getQuantity());
            position.setAveragePrice ((position.getAveragePrice()*position.getQuantity() + userTransaction.getPrice()*userTransaction.getQuantity())/(position.getQuantity() + userTransaction.getQuantity()));
        }
        position.setLongShortInd(position.getQuantity()<0?1:2);
        position.setTotalPrice(position.getQuantity()*position.getAveragePrice());
        position.save();
        return position;
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
}
