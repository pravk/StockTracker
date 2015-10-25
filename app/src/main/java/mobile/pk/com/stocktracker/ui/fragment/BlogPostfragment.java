package mobile.pk.com.stocktracker.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import butterknife.ButterKnife;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.BlogPost;

/**
 * Created by praveen on 03/10/15.
 */
public class BlogPostfragment extends Fragment {

    public static final String BLOG = "BLOG";

    public static final String TITLE = "TITLE";
    public static final String SUBTITLE = "Subtitle";
    private BlogPost blogPost;

    public static BlogPostfragment newInstance(BlogPost blogPost){
        BlogPostfragment fragment = new BlogPostfragment();
        Bundle args = new Bundle();
        args.putSerializable(BLOG, blogPost);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            blogPost = (BlogPost)getArguments().getSerializable(BLOG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blogpost, container, false);
        ButterKnife.inject(this, view);
        WebView webView = (WebView) view.findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadDataWithBaseURL("", blogPost.getContent(), "text/html", "UTF-8", "");

        return view;
    }
}
