package me.jacob.piphone.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import me.jacob.piphone.view.LoadingProgressBar;


/**
 * Created by storm on 14-3-25.
 */
public abstract class BaseFragment extends Fragment {

    public LoadingProgressBar loading_bar;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public CharSequence getTitle() {
        return null;
    }


}
