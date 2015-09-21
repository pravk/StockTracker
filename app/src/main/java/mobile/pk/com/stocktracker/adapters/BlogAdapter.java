package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.GenericRVAdapter;
import mobile.pk.com.stocktracker.adapters.viewholder.BlogViewHolder;
import mobile.pk.com.stocktracker.common.RestClient;
import mobile.pk.com.stocktracker.dao.BlogPost;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.event.ViewBlogPostEvent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hello on 8/31/2015.
 */
public class BlogAdapter extends GenericRVAdapter<BlogPost> {

    public BlogAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindViewHolderInternal(RecyclerView.ViewHolder holder, int i) {
        final BlogPost rssItem = getDataList().get(i);

        BlogViewHolder viewHolder = (BlogViewHolder) holder;

        viewHolder.title.setText(rssItem.getTitle());
        viewHolder.description.setText(Html.fromHtml(rssItem.getSummary()));
        viewHolder.date.setText(DateUtils.formatDateTime(mContext, rssItem.getLastModified(), DateUtils.FORMAT_SHOW_DATE));

        viewHolder.description.setMovementMethod(LinkMovementMethod.getInstance());

        viewHolder.comments.setText(String.format("%d comments", rssItem.getComments() !=null?rssItem.getComments().size():0));

        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ViewBlogPostEvent(rssItem));
            }
        });

        //Picasso.with(mContext).load(rssItem.ge).into(imageView);

    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_blog_item, viewGroup, false);

        BlogViewHolder viewHolder = new BlogViewHolder(view);

        return viewHolder;
    }

    @Override
    protected List<BlogPost> refreshDataInternal() {
        return RestClient.getDefault().getBlogService().getRecentBlogPosts();
    }

    @Override
    protected Stock getUnderlyingStock(BlogPost data) {
        return null;
    }

    public void loadPage(int currentPage) {
        RestClient.getDefault().getBlogService().getRecentBlogPosts(currentPage, 5, new Callback<List<BlogPost>>() {
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
}
