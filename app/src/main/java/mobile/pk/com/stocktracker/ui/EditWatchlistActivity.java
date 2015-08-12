package mobile.pk.com.stocktracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Watchlist;

/**
 * Created by hello on 8/11/2015.
 */
public class EditWatchlistActivity extends AppCompatActivity {

    public static final String WATCH_LIST_ID = "WATCH_LIST_ID";
    private Long watchListId;
    private Watchlist watchlist;

    @InjectView(R.id.watchlist_name)
    BootstrapEditText watchListName;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.btn_delete)
    BootstrapButton deleteButton;
    //private EditWatchlistActivity.WatchListUpdateListener watchListUpdateListener;


    /*public static EditWatchlistActivity newInstance(Long watchListId, WatchListUpdateListener watchListUpdateListener) {
        EditWatchlistActivity fragment = new EditWatchlistActivity();
        Bundle args = new Bundle();
        if(watchListId != null)
            args.putLong(WATCH_LIST_ID, watchListId);
        fragment.setArguments(args);
        fragment.watchListUpdateListener = watchListUpdateListener;
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_watchlist);
        watchListId = getIntent().getLongExtra(WATCH_LIST_ID, 0);
        ButterKnife.inject(this);
        if(watchListId == 0) {
            watchlist = new Watchlist();
            watchlist.setWatchlistName("");
            deleteButton.setVisibility(View.GONE);
        }
        else {
            watchlist = Watchlist.findById(Watchlist.class, watchListId);
            title.setText(getString(R.string.edit_watchlist));
            deleteButton.setVisibility(View.VISIBLE);
        }
        if(watchlist != null)
            watchListName.setText(watchlist.getWatchlistName());

    }

    @OnClick(R.id.btn_done)
    public void onDone()
    {
        if(TextUtils.isEmpty(watchListName.getText().toString()))
            return;

        watchlist.setWatchlistName(watchListName.getText().toString());
        watchlist.save();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(WATCH_LIST_ID, watchlist.getId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @OnClick(R.id.btn_delete)
    public void onDelete()
    {
        watchlist.delete();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(WATCH_LIST_ID, watchlist.getId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        setResult(RESULT_CANCELED);
        finish();
    }

}
