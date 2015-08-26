package mobile.pk.com.stocktracker.transaction.handler;

import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.UserTransaction;

/**
 * Created by hello on 8/25/2015.
 */
public class BuyTransactionHandler implements TransactionHandler{


    @Override
    public void apply(Position position, UserTransaction userTransaction) {
        int quantity = position.getQuantity() + userTransaction.getQuantity();
        double avgPrice = ((position.getAveragePrice() * position.getQuantity())  + (userTransaction.getPrice() * userTransaction.getQuantity()))/ (quantity);
        position.setQuantity(quantity);
        position.setAveragePrice(avgPrice);
    }
}
