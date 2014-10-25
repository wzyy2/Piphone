package me.jacob.piphone.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import me.jacob.piphone.ui.fragment.BaseFragment;


/**
 * Created by lenovo on 2014/9/6.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment> fragmentsList;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsList.get(position).getTitle();
    }
}