package mobile.pk.com.stocktracker.dao;

import com.orm.StringUtil;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by hello on 8/10/2015.
 */
public class Watchlist extends SugarRecord<Watchlist> {
    private String watchlistName;
    private boolean isSystem;

    public static Watchlist getDefaultWatchList() {
        List<Watchlist> watchlistList = find(Watchlist.class, StringUtil.toSQLName("isSystem") + "=?", "1");
        if(watchlistList == null || watchlistList.size()==0)
        {
            Watchlist watchlist = new Watchlist();
            watchlist.watchlistName = "Default Watchlist";
            watchlist.isSystem = true;
            watchlist.save();
            return watchlist;
        }
        else
        {
            return watchlistList.get(0);
        }
    }
}
