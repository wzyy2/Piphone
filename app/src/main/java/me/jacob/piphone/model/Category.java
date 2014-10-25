package me.jacob.piphone.model;

import me.jacob.piphone.ui.fragment.AppFragment;
import me.jacob.piphone.ui.fragment.BaseFragment;
import me.jacob.piphone.ui.fragment.DashboardFragment;
import me.jacob.piphone.ui.fragment.NasFragment;
import me.jacob.piphone.ui.fragment.SettingsFragment;
import me.jacob.piphone.ui.fragment.StatusFragment;

/**
 * Created by storm on 14-3-25.
 */
public enum Category {
    Dashboard("Dashboard"), Application("Application"), Nas("Nas"), Status("Status"), Settings("Settings");
    private String mDisplayName;

    Category(String displayName) {
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public BaseFragment getFragment() {
        BaseFragment fragment;

        if (mDisplayName.equals("Settings"))
            fragment = new SettingsFragment();
        else if (mDisplayName.equals("Application"))
            fragment = new AppFragment();
        else if (mDisplayName.equals("Nas"))
            fragment = new NasFragment();
        else if (mDisplayName.equals("Status"))
            fragment = new StatusFragment();
        else
            fragment = new DashboardFragment();

        return fragment;
    }

}
