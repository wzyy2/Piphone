package me.jacob.piphone.ui.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.jacob.piphone.R;
import me.jacob.piphone.ui.MainActivity;
import me.jacob.piphone.ui.WebviewActivity;

/**
 * Created by lenovo on 2014/9/6.
 */
public class NasFragment extends BaseFragment  {;
    MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_nas, null);

        return contentView;
    }

    @Override
    public  void  onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_download).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_download:
                Intent intent = new Intent(mActivity.getApplicationContext(), WebviewActivity.class);
                startActivity(intent);
                break;
            default: break;
        }
        return false;//false表示继续传递到父类处理
    }

}
