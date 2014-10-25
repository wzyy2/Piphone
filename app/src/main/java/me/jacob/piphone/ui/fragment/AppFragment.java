package me.jacob.piphone.ui.fragment;

import android.app.ActionBar;
import android.os.Bundle;

import me.jacob.piphone.ui.MainActivity;

/**
 * Created by lenovo on 2014/9/8.
 */
public class AppFragment extends BaseFragment {

    MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActivity = (MainActivity) getActivity();
    }

}
