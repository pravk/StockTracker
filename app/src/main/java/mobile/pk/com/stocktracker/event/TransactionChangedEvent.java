package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.Portfolio;
import mobile.pk.com.stocktracker.dao.UserTransaction;

/**
 * Created by hello on 8/11/2015.
 */
public class TransactionChangedEvent {
    private UserTransaction transaction;

    public TransactionChangedEvent(UserTransaction transaction) {
        this.transaction = transaction;
    }

    public UserTransaction getTransaction() {
        return transaction;
    }
}
