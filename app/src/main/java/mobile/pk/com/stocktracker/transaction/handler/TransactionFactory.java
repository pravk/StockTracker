package mobile.pk.com.stocktracker.transaction.handler;

/**
 * Created by hello on 8/25/2015.
 */
public class TransactionFactory {
    public static TransactionHandler getHandler(String transactionType)
    {
        switch (transactionType)
        {
            case "BUY":
                return new BuyTransactionHandler();
            case "SELL":
                return new SellTransactionHandler();
            case "BUY-TO-COVER":
                return new BuyToCoverTransactionHandler();
            case "SHORT-SELL":
                return new ShortSellTransactionHandler();
        }
        return null;
    }
}
