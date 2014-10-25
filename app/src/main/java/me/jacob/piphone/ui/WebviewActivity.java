package me.jacob.piphone.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jacob.piphone.R;
import me.jacob.piphone.api.Api;
import me.jacob.piphone.data.CustomerHttpClient;

/**
 * Created by lenovo on 2014/9/8.
 */
public class WebviewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Menu mMenu;

    @InjectView(R.id.web_swipe_container)
    SwipeRefreshLayout mSwipeLayout;

    @InjectView(R.id.webView)
    WebView myWebView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.inject(this);
        setTitle("Aria2");

        mSwipeLayout.setOnRefreshListener(this);

        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(Api.ARIA2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        switch (item.getItemId()) {
            case R.id.action_open_aria2:
                vibrator.vibrate(new long[]{0,100}, -1);
                Map<String, String> map = new HashMap<String,String>();
                map.put("cmd","open");
                new Thread(new CmdThread(map)).start();
                break;
            case R.id.action_close_aria2:
                Map<String, String> map1 = new HashMap<String,String>();
                map1.put("cmd","close");
                vibrator.vibrate(new long[]{0,100}, -1);
                new Thread(new CmdThread(map1)).start();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private Handler mHandler = new Handler() {
        // 重写handleMessage()方法，此方法在UI线程运行
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 如果成功
                case Api.MSG_SUCCESS:
                    Toast.makeText(getApplication(), "Success", Toast.LENGTH_SHORT).show();
                    myWebView.reload();
                    break;
                // 否则提示失败
                case Api.MSG_FAILURE:
                    Toast.makeText(getApplication(), "Can not connect to device", Toast.LENGTH_SHORT).show();
                case Api.MSG_ERROR:
                    Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    class CmdThread implements Runnable{

        private Map<String, String> map;

        public CmdThread(Map<String, String> gmap) {
            this.map = gmap;
        }

        @Override
        public void run() {
            try {
                String response = CustomerHttpClient.get(Api.NAS, map);
                JSONObject root = new JSONObject(response);
                String result = root.getString("cmd_ret");
                if ( result.equals("ok"))
                    mHandler.obtainMessage(Api.MSG_SUCCESS).sendToTarget();
                else
                    mHandler.obtainMessage(Api.MSG_ERROR).sendToTarget();
            } catch (RuntimeException e) {
                // 请求失败或者连接失败
                mHandler.obtainMessage(Api.MSG_FAILURE).sendToTarget();
            } catch (Exception e) {
                // JSon解析出错
                mHandler.obtainMessage(Api.MSG_ERROR).sendToTarget();
            }
        }
    };


    public void onRefresh() {
        myWebView.reload();
        setRefreshing(false);
    }

    private void setRefreshing(boolean refreshing) {
        mSwipeLayout.setRefreshing(refreshing);
    }

}
