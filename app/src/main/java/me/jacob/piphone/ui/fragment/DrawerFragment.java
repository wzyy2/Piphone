package me.jacob.piphone.ui.fragment;


import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import me.jacob.piphone.R;
import me.jacob.piphone.model.Category;
import me.jacob.piphone.ui.MainActivity;
import me.jacob.piphone.ui.adapter.DrawerAdapter;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class DrawerFragment extends BaseFragment {

    public ListView mDrawerListView;
    private DrawerAdapter mAdapter;
    private MainActivity mActivity;

    public DrawerFragment() {
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
//        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
//        mDrawerListView = (ListView) view.findViewById(R.id.fragment_navigation_drawer_view);
        View contentView = inflater.inflate(R.layout.fragment_drawer, null);
        mDrawerListView = (ListView) contentView.findViewById(R.id.listView);
        mAdapter = new DrawerAdapter(mDrawerListView);
        mDrawerListView.setAdapter(mAdapter);
        mDrawerListView.setItemChecked(0, true);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerListView.setItemChecked(position, true);
                mActivity.setCategory(Category.values()[position]);
            }
        });

        return contentView;
    }

    public void setItemChecked(int position) {
        mDrawerListView.setItemChecked(position, true);
    }
}
