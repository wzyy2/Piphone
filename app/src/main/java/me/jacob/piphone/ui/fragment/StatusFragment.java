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
import me.jacob.piphone.ui.fragment.status.ClientFragment;
import me.jacob.piphone.ui.fragment.status.ServerFragment;

/**
 * Created by lenovo on 2014/9/6.
 */
public class StatusFragment extends BaseFragment {

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
        // Initialize the ViewPager and set an adapter
        ArrayList<BaseFragment> fragmentsList = new ArrayList<BaseFragment>();
        BaseFragment clientFragment  = new ClientFragment();
        BaseFragment serverFragment = new ServerFragment();
        pager = (ViewPager) contentView.findViewById(R.id.pager);
        fragmentsList.add(clientFragment);
        fragmentsList.add(serverFragment);
        pager.setAdapter(new FragmentAdapter(getChildFragmentManager(), fragmentsList));
        pager.setCurrentItem(currentPageSelected);

        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) contentView.findViewById(R.id.tabs);
        tabs.setViewPager(pager);


        //tabs.setOnPageChangeListener(new PageChangeListener());
        return contentView;
    }



}
