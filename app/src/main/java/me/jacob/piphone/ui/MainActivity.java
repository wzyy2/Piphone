package me.jacob.piphone.ui;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jacob.piphone.model.Category;
import me.jacob.piphone.ui.fragment.BaseFragment;
import me.jacob.piphone.ui.fragment.DrawerFragment;
import me.jacob.piphone.R;
import me.jacob.piphone.util.sharedpreference.Athority;


public class MainActivity extends BaseActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.container)
    FrameLayout contentLayout;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerFragment mDrawerFragment;
    private Category mCategory;
    private Menu mMenu;
    public SharedPreferences mShared;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mShared = this.getSharedPreferences(
                Athority.ACCOUNT_INFORMATION, Context.MODE_PRIVATE);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                setTitle(R.string.app_name);
            }

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle(mCategory.getDisplayName());
            }
        };
        mDrawerFragment = new DrawerFragment();
        replaceFragment(R.id.navigation_drawer, new DrawerFragment());
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setCategory(Category.Dashboard);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void replaceFragment(int viewId, BaseFragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_signout:
                Athority.addOther("login", "no");
                Intent intent = new Intent(this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCategory(Category category) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (mCategory == category) {
            return;
        }
        mCategory = category;
        setTitle(mCategory.getDisplayName());
        BaseFragment mContentFragment = category.getFragment();
        replaceFragment(R.id.container, mContentFragment);
    }

}
