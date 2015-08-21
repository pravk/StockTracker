package mobile.pk.com.stocktracker.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.event.EventBus;
import mobile.pk.com.stocktracker.R;

/**
 * Created by hello on 8/21/2015.
 */
public abstract  class ContainerFragment extends Fragment {

    private Context context;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    FragmentStatePagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        FragmentManager fragmentManager = getChildFragmentManager();
        adapter = getAdapter(fragmentManager,context);
        viewPager.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.sliding_tabs);
        tabLayout.setVisibility(View.VISIBLE);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    protected abstract FragmentStatePagerAdapter getAdapter(FragmentManager fragmentManager, Context context);
}
