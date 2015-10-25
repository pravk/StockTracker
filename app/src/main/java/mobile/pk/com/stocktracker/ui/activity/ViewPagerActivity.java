package mobile.pk.com.stocktracker.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.common.VerticalViewPager;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.fragment.BlogSummaryFragment;

/**
 * Created by praveen on 24/10/15.
 */
public class ViewPagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vertical_viewpager);
        setupToolbar();
        EventBus.getDefault().register(this);

        // Set the menu icon instead of the launcher icon.
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        VerticalViewPager pager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        PagerAdapter adapter= new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        //pager.setPageTransformer(true, new DefaultTransformer());
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            final Bundle bundle = new Bundle();
            bundle.putInt(BlogSummaryFragment.EXTRA_POSITION, position);

            final BlogSummaryFragment fragment = new BlogSummaryFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
    }
}
