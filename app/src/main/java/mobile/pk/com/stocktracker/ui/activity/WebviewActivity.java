package mobile.pk.com.stocktracker.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.widget.Toast;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.BlogPost;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.fragment.BlogCommentsfragment;
import mobile.pk.com.stocktracker.ui.fragment.BlogPostfragment;
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
                setupViewPager(blogPost);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WebviewActivity.this, "Failed to load contents", Toast.LENGTH_LONG);
            }
        });

    }

    protected void setupViewPager(final BlogPost blogPost){

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        return BlogPostfragment.newInstance(blogPost);
                    case 1:
                        return BlogCommentsfragment.newInstance(blogPost);
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        ViewPager pager = (ViewPager) findViewById(R.id.vpPager);
        pager.setAdapter(adapter);
    }

}
