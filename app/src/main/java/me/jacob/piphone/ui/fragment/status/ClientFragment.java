package me.jacob.piphone.ui.fragment.status;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.xclcharts.chart.BarData;
import org.xclcharts.chart.PieData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jacob.piphone.R;
import me.jacob.piphone.api.Api;
import me.jacob.piphone.data.CustomerHttpClient;
import me.jacob.piphone.model.DashboardInfo;
import me.jacob.piphone.model.status.ClientInfo;
import me.jacob.piphone.ui.MainActivity;
import me.jacob.piphone.ui.fragment.BaseFragment;
import me.jacob.piphone.util.TaskUtils;
import me.jacob.piphone.view.LoadingProgressBar;
import me.jacob.piphone.view.Tablelistview;
import me.jacob.piphone.view.chart.BarChart01View;
import me.jacob.piphone.view.chart.PieChart02View;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by lenovo on 2014/9/6.
 */
public class ClientFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.client_swiplayout)
    SwipeRefreshLayout mSwipeLayout;

    @InjectView(R.id.system_box)
    Tablelistview system_box;

    @InjectView(R.id.client_scrollview)
    ScrollView client_scrollview;

    @InjectView(R.id.client_body)
    LinearLayout client_body;

    @InjectView(R.id.filesystem_box)
    Tablelistview filesystem_box;

    @InjectView(R.id.cpu_box)
    Tablelistview cpu_box;

    @InjectView(R.id.mem_box)
    Tablelistview mem_box;

    private MainActivity mActivity;
    private ClientInfo info;
    private PieChart02View cpu_pie_char;
    private BarChart01View mem_char;

    List<Map<String, Object>> system_list;
    List<Map<String, Object>> filesystem_list;

    @Override
    public CharSequence getTitle() {
        return "Raspberry Pi";
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
        View contentView = inflater.inflate(R.layout.fragment_status_client, null);
        ButterKnife.inject(this, contentView);
        container.addView(new Button(mActivity.getApplicationContext()));

        loading_bar = new LoadingProgressBar(mActivity.getApplicationContext());
        loading_bar.set(mActivity, (FrameLayout) contentView, null);
        loading_bar.setVisibility(View.VISIBLE);

        mSwipeLayout.setOnRefreshListener(this);

        system_box.setButtonListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.box_left_button:
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage("sure to shudown？");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("cmd", "shutdown");
                                        CustomerHttpClient.get(Api.CLIENT, map);
                                    }
                                }).start();

                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        break;
                    case R.id.box_right_button:
                        builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage("sure to reboot？");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("cmd", "reboot");
                                        CustomerHttpClient.get(Api.CLIENT, map);
                                    }
                                }).start();

                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        break;
                }
            }
        });

        int color = getResources().getColor(R.color.ab_background_color);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(color, color, android.graphics.Color.WHITE, android.graphics.Color.WHITE);

        system_list = new ArrayList<Map<String, Object>>();
        system_box.addList1(system_list);

        filesystem_list = new ArrayList<Map<String, Object>>();
        filesystem_box.addList2(filesystem_list);

        FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        frameParm.gravity = Gravity.CENTER;

        cpu_box.setBodyHeight(500);
        cpu_pie_char = new PieChart02View(mActivity.getApplication());
        cpu_box.addBodyItem(cpu_pie_char);
        //container.addView(new Button(mActivity.getApplication()), frameParm);

        mem_box.setBodyHeight(500);
        mem_char = new BarChart01View(mActivity.getApplication());
        mem_box.addBodyItem(mem_char);

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
                    String response = CustomerHttpClient.get(Api.CLIENT);
                    info = ClientInfo.fromJson(response);
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
                system_list.clear();
                filesystem_list.clear();
                cpu_pie_char.charDataclear();
                mem_char.charDataclear();
                loading_bar.setVisibility(View.GONE);
                if (success) {
                    info.bindSystemData(system_list);
                    info.bindFileSystemData(filesystem_list);

                    cpu_pie_char.charDataadd(new PieData("id " + String.valueOf(100 - info.cpu) + "%",100 - info.cpu,(int) Color.rgb(00, 0xc0, 0xaf)));
                    cpu_pie_char.charDataadd(new PieData("us " + String.valueOf(info.cpu) + "%", info.cpu, (int) Color.rgb(0x3c, 0x8d, 0xbc)));

                    List<Double> dataSeriesA= new LinkedList<Double>();
                    dataSeriesA.add((double) info.memory_info.total);
                    dataSeriesA.add((double) info.memory_info.used - info.memory_info.cached - info.memory_info.buffers);
                    dataSeriesA.add((double) info.memory_info.free + info.memory_info.cached + info.memory_info.buffers);
                    dataSeriesA.add((double) info.memory_info.shared);
                    dataSeriesA.add((double) info.memory_info.buffers);
                    dataSeriesA.add((double) info.memory_info.cached);
                    BarData BarDataA = new BarData("111",dataSeriesA,(int)Color.argb(0xc0, 53, 169, 239));
                    mem_char.charDataadd(BarDataA);

                } else {
                }
                mem_char.invalidate();
                cpu_pie_char.invalidate();
                system_box.notifyList();
                filesystem_box.notifyList();
            }
        });
    }

}
