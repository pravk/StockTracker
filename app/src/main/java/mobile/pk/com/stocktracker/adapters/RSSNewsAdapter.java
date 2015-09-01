package mobile.pk.com.stocktracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.adapters.viewholder.RSSNewsViewHolder;
import mobile.pk.com.stocktracker.adapters.viewholder.StockSearchResultViewHolder;
import mobile.pk.com.stocktracker.dao.Stock;
import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

/**
 * Created by hello on 8/31/2015.
 */
public class RSSNewsAdapter extends GenericRVAdapter<RssItem> {

    private static final String RSS_NEWS_FORMAT = "https://www.google.com/finance/company_news?q=%s:%s&output=rss";
    private final Stock stock;

    public RSSNewsAdapter(Context context, Stock stock) {
        super(context);
        this.stock = stock;
    }

    @Override
    protected void onBindViewHolderInternal(RecyclerView.ViewHolder holder, int i) {
        RssItem rssItem = getDataList().get(i);

        RSSNewsViewHolder viewHolder = (RSSNewsViewHolder) holder;

        viewHolder.title.setText(rssItem.getTitle());
        viewHolder.description.setText(Html.fromHtml(rssItem.getDescription()));
        viewHolder.description.setMovementMethod(LinkMovementMethod.getInstance());
        //Picasso.with(mContext).load(rssItem.ge).into(imageView);

    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderInternal(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_stock_news_item, viewGroup, false);

        final RSSNewsViewHolder viewHolder = new RSSNewsViewHolder(view);

        return viewHolder;
    }

    @Override
    protected List<RssItem> refreshDataInternal() {
        RssFeed feed = null;
        try {
            URL url = new URL( String.format(RSS_NEWS_FORMAT, stock.getExchange(), stock.getTicker()) );
            feed = RssReader.read(url);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        if(feed != null)
            return feed.getRssItems();
        return new ArrayList<>();
    }

    @Override
    protected Stock getUnderlyingStock(RssItem data) {
        return stock;
    }
}
