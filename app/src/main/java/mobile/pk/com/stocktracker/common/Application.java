package mobile.pk.com.stocktracker.common;

/**
 * Created by hello on 8/1/2015.
 */
public class Application extends android.app.Application {

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
