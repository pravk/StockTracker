package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.WatchlistViewHolder;
import mobile.pk.com.stocktracker.dao.Watchlist;
import mobile.pk.com.stocktracker.event.EditTransactionEvent;

/**
 * Created by hello on 8/20/2015.
 */
public class WatchlistAdapter extends GenericRVAdapter<WatchlistViewHolder, Watchlist> {

    public WatchlistAdapter(Context context) {
        super(context);
    }

    @Override
    protected WatchlistViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_watchlist_item, viewGroup, false);

        final WatchlistViewHolder viewHolder = new WatchlistViewHolder(view);

        return viewHolder;
    }

    @Override
    protected List<Watchlist> refreshDataInternal() {
        return Watchlist.find(Watchlist.class, null);
    }

    @Override
    public void onBindViewHolder(WatchlistViewHolder holder, int position) {
        final Watchlist watchlist = getDataList().get(position);

        holder.watchlistName.setText(watchlist.getWatchlistName());
        holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.remove:
                        watchlist.delete();
                        reset();
                        Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.edit:
                        EventBus.getDefault().post(new EditWatchListEvent(watchlist));
                        break;

                }
                return true;
            }
        });
        holder.cardView.setOnClickListener(new CardView.OnClickListener(){

            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OpenWatchlistEvent(watchlist));
            }
        });
    }

    public class EditWatchListEvent {
        private Watchlist watchlist;
        public EditWatchListEvent(Watchlist watchlist) {
            this.watchlist = watchlist;
        }

        public Watchlist getWatchlist() {
            return watchlist;
        }
    }

    public class OpenWatchlistEvent {
        private Watchlist watchlist;
        public OpenWatchlistEvent(Watchlist watchlist) {
            this.watchlist = watchlist;
        }

        public Watchlist getWatchlist() {
            return watchlist;
        }
    }
}
