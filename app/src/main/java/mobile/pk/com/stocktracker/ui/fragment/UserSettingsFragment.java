package mobile.pk.com.stocktracker.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import mobile.pk.com.stocktracker.R;

/**
 * Created by hello on 8/19/2015.
 */
public class UserSettingsFragment extends mobile.pk.com.stocktracker.ui.fragment.PreferenceFragment {

    private Context context;

    public static UserSettingsFragment newInstance(Context context, Bundle args) {
        UserSettingsFragment fragment = new UserSettingsFragment();
        fragment.setContext(context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
