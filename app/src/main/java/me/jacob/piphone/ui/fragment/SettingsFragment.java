package me.jacob.piphone.ui.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import me.jacob.piphone.R;
import me.jacob.piphone.ui.MainActivity;
import me.jacob.piphone.ui.adapter.FragmentAdapter;
import me.jacob.piphone.ui.fragment.settings.AccountFragment;
import me.jacob.piphone.ui.fragment.settings.GeneralFragment;
import me.jacob.piphone.ui.fragment.status.ClientFragment;
import me.jacob.piphone.ui.fragment.status.ServerFragment;

/**
 * Created by lenovo on 2014/9/8.
 */
public class SettingsFragment extends BaseFragment  {;
    MainActivity mActivity;
    PagerSlidingTabStrip tabs;
    ViewPager pager;
    private int currentPageSelected = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActivity = (MainActivity) getActivity();
        ActionBar actionBar = mActivity.getActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_base, null);
        ArrayList<BaseFragment> fragmentsList = new ArrayList<BaseFragment>();
        BaseFragment accountFragment  = new AccountFragment();
        BaseFragment generalFragment = new GeneralFragment();
        pager = (ViewPager) contentView.findViewById(R.id.pager);

        fragmentsList.add(generalFragment);
        fragmentsList.add(accountFragment);

        pager.setAdapter(new FragmentAdapter(getChildFragmentManager(), fragmentsList));
        pager.setCurrentItem(currentPageSelected);

        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) contentView.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        return contentView;

    }
}
