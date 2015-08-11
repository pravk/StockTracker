package mobile.pk.com.stocktracker.common;

import com.orm.SugarApp;

/**
 * Created by hello on 8/1/2015.
 */
public class Application extends SugarApp {

    private RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        // SharedObjects.context = this;

        restClient = new RestClient(this);


    }

    public RestClient getRestClient() {
        return restClient;
    }
}
