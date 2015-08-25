package mobile.pk.com.stocktracker.transaction.handler;

import java.util.List;

import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.dao.UserTransaction;
import mobile.pk.com.stocktracker.transaction.processor.TransactionProcessor;

/**
 * Created by hello on 8/25/2015.
 */
public class BuyToCoverTransactionHandler implements TransactionHandler{

    @Override
    public void apply(Position position, UserTransaction userTransaction) {

        List<UserTransaction> transactionList = TransactionProcessor.getInstance().getOpenUserTransactions(position,"SHORT-SELL", userTransaction.getTransactionDate());

        if(transactionList == null || transactionList.size()==0) {
            position.setError("Invalid Txn: " + userTransaction);
            return;
        }
        double totalShortSellQantity = 0;
        for (UserTransaction shortTransaction :
                transactionList) {
            totalShortSellQantity = totalShortSellQantity + shortTransaction.getQuantity();
        }

        if(userTransaction.getQuantity()>totalShortSellQantity){
            position.setError("Invalid Txn: " + userTransaction);
            return;
        }

        double quantity = userTransaction.getQuantity();
        double realizedGainLoss = 0;
        for(UserTransaction shortSellTransaction: transactionList)
        {
            if(shortSellTransaction.getQuantity()<= quantity)
            {
                realizedGainLoss += shortSellTransaction.getQuantity() * (shortSellTransaction.getPrice()-userTransaction.getPrice());
                quantity = quantity -shortSellTransaction.getQuantity();
                shortSellTransaction.setClosed(true);
                shortSellTransaction.save();
            }
            else
            {
                realizedGainLoss += quantity * (shortSellTransaction.getPrice()-userTransaction.getPrice());
                break;
            }
        }
        position.setQuantity(position.getQuantity()+userTransaction.getQuantity());
        userTransaction.setRealizedGainLoss(realizedGainLoss);
        userTransaction.save();
        position.setNetRealizedGainLoss(position.getNetRealizedGainLoss() + userTransaction.getRealizedGainLoss());
    }
}
