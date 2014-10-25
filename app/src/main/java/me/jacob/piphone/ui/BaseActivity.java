package me.jacob.piphone.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.Toast;

import me.jacob.piphone.R;

/**
 * Created by storm on 14-3-24.
 */
public abstract class BaseActivity extends FragmentActivity {
    protected ActionBar actionBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    private void initActionBar() {
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
//        ActionBar.Tab edit = actionBar.newTab().setText("Tab B");
//        actionBar.addTab(edit);
    }

    public void setTitle(int resId) {
        actionBar.setTitle(resId);
    }

    public void setTitle(CharSequence text) {
        actionBar.setTitle(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//            case R.id.action_settings:
//                startActivity(new Intent(this, PreferenceActivity.class));
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
