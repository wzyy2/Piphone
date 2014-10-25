package me.jacob.piphone.ui.fragment.settings;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jacob.piphone.R;
import me.jacob.piphone.api.Api;
import me.jacob.piphone.model.status.ServerInfo;
import me.jacob.piphone.ui.MainActivity;
import me.jacob.piphone.ui.fragment.BaseFragment;
import me.jacob.piphone.view.LinkTextView;
import me.jacob.piphone.view.LoadingProgressBar;

/**
 * Created by lenovo on 2014/9/18.
 */
public class GeneralFragment  extends BaseFragment {
    private MainActivity mActivity;

    @InjectView(R.id.webView1)
    WebView myWebView;

    @Override
    public CharSequence getTitle() {
        return "General";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_web, null);
        ButterKnife.inject(this, contentView);

        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(Api.SETTINGS_GENERAL);


        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
