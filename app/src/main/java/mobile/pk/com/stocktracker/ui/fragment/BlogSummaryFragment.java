package mobile.pk.com.stocktracker.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.BlogPost;
import mobile.pk.com.stocktracker.event.ViewBlogPostEvent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by praveen on 24/10/15.
 */
public class BlogSummaryFragment extends Fragment {

    public static final String EXTRA_POSITION = "EXTRA_POSITION";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int position = getArguments().getInt(EXTRA_POSITION);
        final View view = inflater.inflate(R.layout.fragment_blog_summary, container, false);

        RestClient.getDefault().getBlogService().getRecentBlogPosts(position, 1, new Callback<List<BlogPost>>() {
            @Override
            public void success(final List<BlogPost> blogPosts, Response response) {

                boolean hasImage = false;
                ImageView imageView = (ImageView) view.findViewById(R.id.blog_image);
                if(blogPosts.get(0).getImageUrlList() != null
                        && blogPosts.get(0).getImageUrlList().size()>0)
                {
                    Picasso.with(getActivity()).load(blogPosts.get(0).getImageUrlList().get(0)).fit().into(imageView);
                    hasImage = true;
                }
                else
                {
                    imageView.setVisibility(View.GONE);
                }

                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText(blogPosts.get(0).getTitle());

                TextView summary = (TextView) view.findViewById(R.id.summary);
                String summaryText = hasImage ? blogPosts.get(0).getSummary50() : blogPosts.get(0).getSummary200();
                if(summaryText != null && summaryText.length()>0)
                  summary.setText(Html.fromHtml(summaryText));

                TextView authorAndDate = (TextView) view.findViewById(R.id.author_and_date);
                authorAndDate.setText(String.format(" ~ by %s / %s", blogPosts.get(0).getAuthor(), DateUtils.formatDateTime(getActivity(), blogPosts.get(0).getLastModified(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME)));

                Button fullArticleButton = (Button) view.findViewById(R.id.full_article);
                fullArticleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new ViewBlogPostEvent(blogPosts.get(0)));
                    }
                });

                Button shareButton = (Button) view.findViewById(R.id.share);
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = blogPosts.get(0).getSummary();
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, blogPosts.get(0).getTitle());
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
