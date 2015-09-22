package mobile.pk.com.stocktracker.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mobile.pk.com.stocktracker.R;

/**
 * Created by hello on 8/31/2015.
 */
public class BlogViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView description;
    public TextView dateandauthor;
    public Button comments;
    public Button more;
    public ImageView image;

    public BlogViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        dateandauthor = (TextView) itemView.findViewById(R.id.dateandauthor);
        description = (TextView) itemView.findViewById(R.id.description);
        more = (Button) itemView.findViewById(R.id.more_button);
        comments = (Button) itemView.findViewById(R.id.comment_count);
        image = (ImageView) itemView.findViewById(R.id.image);
    }
}
