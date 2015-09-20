package mobile.pk.com.stocktracker.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.webkit.WebView;

import butterknife.ButterKnife;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Position;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.fragment.UserTransactionFragment;


public class WebviewActivity extends BaseActivity {

   public static final String CONTENT = "CONTENT";

    public static final String TITLE = "TITLE";

    public WebviewActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        setupToolbar();
        String content = getIntent().getStringExtra(CONTENT);
        String title = getIntent().getStringExtra(TITLE);

        setTitle(title);

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
    }

}
