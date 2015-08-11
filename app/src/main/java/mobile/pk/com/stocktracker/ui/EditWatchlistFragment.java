package mobile.pk.com.stocktracker.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Stock;
import mobile.pk.com.stocktracker.dao.Watchlist;

/**
 * Created by hello on 8/11/2015.
 */
public class EditWatchlistFragment extends Fragment {

    private static final String WATCH_LIST_ID = "WATCH_LIST_ID";
    private Long watchListId;
    private Watchlist watchlist;

    @InjectView(R.id.watchlist_name)
    BootstrapEditText watchListName;
    private EditWatchlistFragment.WatchListUpdateListener watchListUpdateListener;


    public static EditWatchlistFragment newInstance(Long watchListId, WatchListUpdateListener watchListUpdateListener) {
        EditWatchlistFragment fragment = new EditWatchlistFragment();
        Bundle args = new Bundle();
        if(watchListId != null)
            args.putLong(WATCH_LIST_ID, watchListId);
        fragment.setArguments(args);
        fragment.watchListUpdateListener = watchListUpdateListener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            watchListId = getArguments().getLong(WATCH_LIST_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_watchlist, container, false);
        ButterKnife.inject(this, view);

        if(watchListId == 0) {
            watchlist = new Watchlist();
            watchlist.setWatchlistName("");
        }
        else
            watchlist = Watchlist.findById(Watchlist.class, watchListId);
        watchListName.setText(watchlist.getWatchlistName());
        return view;
    }

    @OnClick(R.id.btn_done)
    public void onDone()
    {
        if(TextUtils.isEmpty(watchListName.getText().toString()))
            return;

        watchlist.setWatchlistName(watchListName.getText().toString());
        watchlist.save();
        watchListUpdateListener.onUpdateComplete(watchlist);
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        watchListUpdateListener.onCancel();
    }

    public interface WatchListUpdateListener
    {
        void onUpdateComplete(Watchlist watchlist);
        void onCancel();
    }
}
