package mobile.pk.com.stocktracker.transaction.handler;

import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.UserTransaction;

/**
 * Created by hello on 8/25/2015.
 */
public class SellTransactionHandler implements TransactionHandler{

    @Override
    public void apply(Position position, UserTransaction userTransaction) {
        double quantity = position.getQuantity() - userTransaction.getQuantity();
        if(quantity <0)
            position.setError("Incorrect transaction " + userTransaction);
        double realizedGainLoss = (userTransaction.getPrice() - position.getAveragePrice())*userTransaction.getQuantity();
        userTransaction.setRealizedGainLoss(realizedGainLoss);
        userTransaction.save();
        position.setQuantity(quantity);
        position.setNetRealizedGainLoss(position.getNetRealizedGainLoss() + userTransaction.getRealizedGainLoss());
    }
}
