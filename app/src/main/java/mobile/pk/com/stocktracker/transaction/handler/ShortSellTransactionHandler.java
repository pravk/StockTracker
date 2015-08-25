package mobile.pk.com.stocktracker.transaction.handler;

import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.UserTransaction;

/**
 * Created by hello on 8/25/2015.
 */
public class ShortSellTransactionHandler implements TransactionHandler{

    @Override
    public void apply(Position position, UserTransaction userTransaction) {
        double quantity = position.getQuantity() - userTransaction.getQuantity();
        position.setQuantity(quantity);
    }
}
