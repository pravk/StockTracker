package mobile.pk.com.stocktracker.ui.activity;

import android.app.SearchManager;
import android.os.Bundle;

import java.util.List;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.BlogAdapter;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.BlogPost;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.fragment.BlogFragment;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hello on 8/29/2015.
 */
public class BlogSearchActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();

        final String query = getIntent().getStringExtra(SearchManager.QUERY);
        BlogFragment fragment = BlogFragment.newInstance(this);

        fragment.setAdapter(new BlogAdapter(this){
            @Override
            public void loadPage(int currentPage) {
                RestClient.getDefault().getBlogService().findBlogPosts(query, currentPage, 5, new Callback<List<BlogPost>>() {
                    @Override
                    public void success(List<BlogPost> blogPosts, Response response) {
                        getDataList().addAll(blogPosts);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }

            @Override
            protected List<BlogPost> refreshDataInternal() {
                return RestClient.getDefault().getBlogService().findBlogPosts(query);
            }

        });

        replaceFragment(fragment, "Search Results:" + query);
    }

}
