package mobile.pk.com.stocktracker.transaction.handler;

import java.util.List;

import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.transaction.processor.TransactionProcessor;

/**
 * Created by hello on 8/25/2015.
 */
public class SellTransactionHandler implements TransactionHandler{

    @Override
    public void apply(Position position, UserTransaction userTransaction) {

        List<UserTransaction> transactionList = TransactionProcessor.getInstance().getOpenUserTransactions(position,"BUY", userTransaction.getTransactionDate());

        double totalLongQantity = 0;
        for (UserTransaction shortTransaction :
                transactionList) {
            totalLongQantity = totalLongQantity + shortTransaction.getQuantity();
        }

        if(totalLongQantity <0)
            position.setError("Incorrect transaction " + userTransaction);

        double realizedGainLoss = (userTransaction.getPrice() - position.getAveragePrice())*userTransaction.getQuantity();
        userTransaction.setRealizedGainLoss(realizedGainLoss);
        userTransaction.save();
        position.setQuantity(position.getQuantity()-userTransaction.getQuantity());
        position.setNetRealizedGainLoss(position.getNetRealizedGainLoss() + userTransaction.getRealizedGainLoss());
    }
}
