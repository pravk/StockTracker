package mobile.pk.com.stocktracker.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.webkit.WebView;
import android.widget.Toast;

import butterknife.ButterKnife;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.BlogPost;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.fragment.UserTransactionFragment;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class WebviewActivity extends BaseActivity {

   public static final String BLOG_ID = "BLOG_ID";

    public static final String TITLE = "TITLE";
    public static final String SUBTITLE = "Subtitle";


    public WebviewActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        setupToolbar();
        final String blogId = getIntent().getStringExtra(BLOG_ID);

        RestClient.getDefault().getBlogService().getBlogPostById(blogId, new Callback<BlogPost>() {
            @Override
            public void success(BlogPost blogPost, Response response) {
                setTitle(blogPost.getTitle());
                getSupportActionBar().setSubtitle(DateUtils.formatDateTime(WebviewActivity.this, blogPost.getLastModified(), DateUtils.FORMAT_SHOW_DATE));

                WebView webView = (WebView) findViewById(R.id.webview);
                webView.getSettings().setJavaScriptEnabled(true);

                webView.loadDataWithBaseURL("", blogPost.getContent(), "text/html", "UTF-8", "");
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WebviewActivity.this,"Failed to load contents", Toast.LENGTH_LONG);
            }
        });

    }

}
