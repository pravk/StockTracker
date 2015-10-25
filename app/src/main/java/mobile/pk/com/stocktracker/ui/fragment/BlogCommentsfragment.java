package mobile.pk.com.stocktracker.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.GenericRVAdapter;
import mobile.pk.com.stocktracker.adapters.viewholder.PortfolioPositionViewHolder;
import mobile.pk.com.stocktracker.common.Application;
import mobile.pk.com.stocktracker.dao.BlogComment;
import mobile.pk.com.stocktracker.dao.BlogPost;
import mobile.pk.com.stocktracker.dao.Stock;

/**
 * Created by praveen on 03/10/15.
 */
public class BlogCommentsfragment extends GenericRVFragment<BlogComment> {
    public static final String BLOG = "BLOG";
    private BlogPost blogPost;

    public static BlogCommentsfragment newInstance(BlogPost blogPost){
        BlogCommentsfragment fragment = new BlogCommentsfragment();
        Bundle args = new Bundle();
        args.putSerializable(BLOG, blogPost);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            blogPost = (BlogPost) getArguments().getSerializable(BLOG);
        }
    }
    @Override
    protected boolean onEditView() {
        return false;
    }

    @Override
    protected boolean showEditAction() {
        return false;
    }

    @Override
    protected boolean showRefreshAction() {
        return false;
    }

    @Override
    protected GenericRVAdapter<BlogComment> getAdapter() {
        return new GenericRVAdapter<BlogComment>(getContext()) {
            @Override
            protected void onBindViewHolderInternal(RecyclerView.ViewHolder holder, int i) {
                BlogCommentViewHolder viewHolder = (BlogCommentViewHolder) holder;
                BlogComment blogComment = getDataList().get(i);

                viewHolder.comments.setText(blogComment.getComment());
                viewHolder.name.setText(blogComment.getUser());
                viewHolder.time.setText(DateUtils.formatDateTime(Application.getInstance().getApplicationContext(), blogComment.getCommentDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME));
            }

            @Override
            protected RecyclerView.ViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_blogcomments_item, viewGroup, false);

                final BlogCommentViewHolder viewHolder = new BlogCommentViewHolder(view);

                return viewHolder;
            }

            @Override
            protected List<BlogComment> refreshDataInternal() {
                if(blogPost.getComments() != null)
                    return blogPost.getComments();
                else
                    return new ArrayList<BlogComment>();
            }

            @Override
            protected Stock getUnderlyingStock(BlogComment data) {
                return null;
            }
        };
    }

    @Override
    protected void onAddNewItem() {

    }

    public static class BlogCommentViewHolder extends RecyclerView.ViewHolder{

        TextView comments;
        TextView name;
        TextView time;
        ImageView avatar;

        public BlogCommentViewHolder(View itemView) {
            super(itemView);
            comments = (TextView) itemView.findViewById(R.id.comment);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
        }
    }
}
