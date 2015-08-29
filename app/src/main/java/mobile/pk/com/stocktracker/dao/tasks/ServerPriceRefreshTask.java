package mobile.pk.com.stocktracker.dao.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.StockPrice;
import mobile.pk.com.stocktracker.service.PricingService;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by hello on 8/11/2015.
 */
public class ServerPriceRefreshTask extends AsyncTask<Stock, Void, List<StockPrice>> {

    private static final String TAG = ServerPriceRefreshTask.class.getSimpleName();
    private final PricingService service;
    private Exception exception;

    public ServerPriceRefreshTask(PricingService service)
    {
        this.service = service;
    }

    @Override
    protected List<StockPrice> doInBackground(Stock... params) {

        if(params == null || params.length==0)
            return null;

        List<StockPrice> stockPriceList= new ArrayList<>();
        StringBuilder queryValue = new StringBuilder();
        int index = 0;
        for (Stock stock : params)
        {
            queryValue.append(stock.getExchange() +":"+ stock.getTicker());
            if(index<= params.length-1)
                queryValue.append(",");
        }
        try {
            Response response = service.getStockPrices(queryValue.toString());
            String json = new String(((TypedByteArray) response.getBody()).getBytes());
            Type listType = new TypeToken<List<PricingService.StockPrice>>() {
            }.getType();
            Gson gson = new Gson();
            List<PricingService.StockPrice> serverPriceList = gson.fromJson(json.substring(json.indexOf("[")), listType);

            for (PricingService.StockPrice serverPrice : serverPriceList) {
                StockPrice stockPrice = StockPrice.from(serverPrice);
                stockPrice.save();
                stockPriceList.add(stockPrice);
            }
        }catch (Exception e)
        {
            exception = e;
            Log.e(TAG,"Failed to fetch latest prices" ,e);
        }
        return stockPriceList;
    }

    public Exception getException() {
        return exception;
    }
}
