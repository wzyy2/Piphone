package me.jacob.piphone.ui.fragment.status;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jacob.piphone.R;
import me.jacob.piphone.api.Api;
import me.jacob.piphone.data.CustomerHttpClient;
import me.jacob.piphone.model.status.ClientInfo;
import me.jacob.piphone.model.status.ServerInfo;
import me.jacob.piphone.ui.MainActivity;
import me.jacob.piphone.ui.fragment.BaseFragment;
import me.jacob.piphone.util.TaskUtils;
import me.jacob.piphone.view.LinkTextView;
import me.jacob.piphone.view.LoadingProgressBar;
import me.jacob.piphone.view.Tablelistview;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by lenovo on 2014/9/6.
 */
public class ServerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.server_swiplayout)
    SwipeRefreshLayout mSwipeLayout;

    @InjectView(R.id.server_body)
    LinearLayout server_body;

    @InjectView(R.id.about_box)
    Tablelistview about_box;

    private MainActivity mActivity;
    private String version;
    private ServerInfo info;
    List<Map<String, Object>> about_list;

    @Override
    public CharSequence getTitle() {
        return "Server";
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
        View contentView = inflater.inflate(R.layout.fragment_status_server, null);
        ButterKnife.inject(this, contentView);

        loading_bar = new LoadingProgressBar(mActivity.getApplicationContext());
        loading_bar.set(mActivity, (FrameLayout) contentView, null);
        loading_bar.setVisibility(View.VISIBLE);

        mSwipeLayout.setOnRefreshListener(this);

        LinkTextView about_foot = new LinkTextView(mActivity.getApplicationContext());
        about_foot.setUrl(mActivity,"https://github.com/wzyy2");
        about_foot.setText(getResources().getString(R.string.check_new));
        about_foot.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        about_foot.setTextColor(Color.BLUE);
        about_foot.setGravity(Gravity.CENTER);
        about_box.addFootItem(about_foot);

        about_list = new ArrayList<Map<String, Object>>();
        about_box.addList1(about_list);
        GetInfo();

        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onRefresh() {
        GetInfo();
        setRefreshing(false);
    }

    private void setRefreshing(boolean refreshing) {
        mSwipeLayout.setRefreshing(refreshing);
    }

    public void GetInfo()
    {
        TaskUtils.executeAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    String response = CustomerHttpClient.get(Api.SERVER);
                    info = ServerInfo.fromJson(response);
                    return true;
                } catch (RuntimeException e) {
                    // 请求失败或者连接失败
                    Log.w("debug", "connect error");
                } catch (Exception e) {
                    // JSon解析出错
                    Log.w("debug", "connect error");
                }
                return false;

            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                about_list.clear();
                loading_bar.setVisibility(View.GONE);
                if (success) {
                    info.bindAboutData(about_list);
                } else {

                }
                about_box.adapter.notifyDataSetChanged();
            }
        });
    }
}
