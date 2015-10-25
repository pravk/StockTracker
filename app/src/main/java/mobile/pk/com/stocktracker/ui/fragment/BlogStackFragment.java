package mobile.pk.com.stocktracker.ui.fragment;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.common.VerticalViewPager;
import mobile.pk.com.stocktracker.ui.BaseActivity;
import mobile.pk.com.stocktracker.ui.activity.BlogSearchActivity;

/**
 * Created by praveen on 25/10/15.
 */
public class BlogStackFragment extends Fragment {

    VerticalViewPager pager = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_vertical_viewpager, container, false);
        setHasOptionsMenu(true);
        pager = (VerticalViewPager) view.findViewById(R.id.vertical_viewpager);
        PagerAdapter adapter= new ViewPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        return view;
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

    @Override
    public void onCreateOptionsMenu (Menu menu, final MenuInflater inflater){
        MenuItem item = menu.findItem(R.id.search);
        if(item == null)
            return;
        SearchView sv = new SearchView(((BaseActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");

                Intent intent = new Intent(getActivity(), BlogSearchActivity.class);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {

            case R.id.gototop:
               // pager.setCurrentItem(1);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
