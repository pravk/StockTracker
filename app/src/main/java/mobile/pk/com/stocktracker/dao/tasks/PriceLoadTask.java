package mobile.pk.com.stocktracker.dao.tasks;

import android.os.AsyncTask;

import com.orm.StringUtil;

import java.util.List;

import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.StockPrice;
import mobile.pk.com.stocktracker.service.PricingService;

/**
 * Created by hello on 8/11/2015.
 */
public class PriceLoadTask extends AsyncTask<Stock, Void, List<StockPrice>> {


    @Override
    protected List<StockPrice> doInBackground(Stock... params) {

        if(params == null || params.length==0)
            return null;

        String [] clientIdArray = new String[params.length];
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(StringUtil.toSQLName("clientId") + " in (");
        int index = 0;
        for (Stock stock : params)
        {
            whereClause.append("?,");
            clientIdArray[index++] = stock.getClientId();
        }
        whereClause.deleteCharAt(whereClause.length()-1);
        whereClause.append(")");

        List<StockPrice> stockPriceList = StockPrice.find(StockPrice.class, whereClause.toString(), clientIdArray);
        for(StockPrice stockPrice : stockPriceList)
        {
            for(Stock stock: params)
            {
                if(stockPrice.getClientId().equals(stock.getClientId()))
                {
                    stock.setPrice(stockPrice);
                }
            }
        }
        return stockPriceList;
    }
}
