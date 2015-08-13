package mobile.pk.com.stocktracker.utils;

import java.text.NumberFormat;

/**
 * Created by hello on 8/13/2015.
 */
public class NumberUtils {
    public static String formatAsMoney(double value)
    {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(value);
        return moneyString;
    }
}
