package mobile.pk.com.stocktracker.adapters.PagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Collection;
import java.util.List;

/**
 * Created by hello on 8/21/2015.
 */
public abstract class BaseFragmentPagerAdapter<T> extends FragmentStatePagerAdapter{

    private List<T> data;
    private Context context;

    public BaseFragmentPagerAdapter(FragmentManager fragmentManager, Context context)
    {
        super(fragmentManager);
        this.context = context;
        reset();
    }

    public void reset() {
       if(data != null) {
           data.clear();
       }
        else
       {
           data = getData();
       }

        notifyDataSetChanged();
    }

    protected abstract List<T> getData();

    @Override
    public Fragment getItem(int position) {
        return getFragment(data.get(position));
    }

    protected abstract Fragment getFragment(T t);

    @Override
    public CharSequence getPageTitle(int position) {
        return getPageTitle(data.get(position));
    }

    protected abstract CharSequence getPageTitle(T t);


    @Override
    public int getCount() {
        return data.size();
    }

    public Context getContext() {
        return context;
    }
}
