package me.jacob.piphone.ui.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jacob.piphone.R;
import me.jacob.piphone.api.Api;
import me.jacob.piphone.data.CustomerHttpClient;
import me.jacob.piphone.model.Category;
import me.jacob.piphone.model.DashboardInfo;
import me.jacob.piphone.ui.LoginActivity;
import me.jacob.piphone.ui.MainActivity;
import me.jacob.piphone.util.TaskUtils;
import me.jacob.piphone.util.sharedpreference.Athority;
import me.jacob.piphone.view.DashTab;
import me.jacob.piphone.view.LoadingProgressBar;
/**
 * Created by lenovo on 2014/9/6.
 */
public class DashboardFragment extends BaseFragment implements View.OnTouchListener, GestureDetector.OnGestureListener {
    MainActivity mActivity;
    private GestureDetector mGestureDetector;
    private int mCurrentLayoutState;
    private static final int FLING_MIN_DISTANCE = 100;
    private static final int FLING_MIN_VELOCITY = 200;
    DashboardInfo info;

    @InjectView(R.id.viewflipper)
    ViewFlipper viewflipper;

    @InjectView(R.id.conn_tab)
    DashTab conn_tab;

    @InjectView(R.id.nas_tab)
    DashTab nas_tab;

    @InjectView(R.id.app_tab)
    DashTab app_tab;

    @InjectView(R.id.user_tab)
    DashTab user_tab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_dashboard, null);

        loading_bar = new LoadingProgressBar(mActivity.getApplicationContext());
        loading_bar.set(mActivity, (FrameLayout) contentView, null);
        loading_bar.setVisibility(View.VISIBLE);

        //contentView.setVisibility(View.GONE);
        ButterKnife.inject(this, contentView);

        viewflipper.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this);
        mCurrentLayoutState = 0;
        viewflipper.setLongClickable(true);
        viewflipper.setFlipInterval(5000);
        viewflipper.setInAnimation(AnimationUtils.loadAnimation(mActivity.getApplicationContext(),
                R.anim.push_left_in));
        // 设置View退出屏幕时候使用的动画
        viewflipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity.getApplicationContext(),
                R.anim.push_left_out));
        viewflipper.startFlipping();

        conn_tab.setButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.setCategory(Category.Settings);
            }

        });
        nas_tab.setButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.setCategory(Category.Nas);
            }
        });
        app_tab.setButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.setCategory(Category.Application);
            }
        });
        user_tab.setButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.setCategory(Category.Dashboard);
            }
        });

        GetInfo();

        return contentView;
    }


    public boolean onDown(MotionEvent e) {

        return false;
    }

    /**
     * 用户按下触摸屏、快速移动后松开即触发这个事件 e1：第1个ACTION_DOWN MotionEvent e2：最后一个ACTION_MOVE
     * MotionEvent velocityX：X轴上的移动速度，像素/秒 velocityY：Y轴上的移动速度，像素/秒 触发条件 ：
     * X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
     */
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // 当像左侧滑动的时候
            // 设置View进入屏幕时候使用的动画
            viewflipper.setInAnimation(AnimationUtils.loadAnimation(mActivity.getApplicationContext(),
                    R.anim.push_left_in));
            // 设置View退出屏幕时候使用的动画
            viewflipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity.getApplicationContext(),
                    R.anim.push_left_out));
            viewflipper.showNext();
        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // 当像右侧滑动的时候
            viewflipper.setInAnimation(AnimationUtils.loadAnimation(mActivity.getApplicationContext(),
                    R.anim.push_right_in));
            viewflipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity.getApplicationContext(),
                    R.anim.push_right_out));
            viewflipper.showPrevious();
        }
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onTouch(View v, MotionEvent event) {
        // 一定要将触屏事件交给手势识别类去处理（自己处理会很麻烦的）
        return mGestureDetector.onTouchEvent(event);
    }


    public void GetInfo()
    {
        TaskUtils.executeAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    String response = CustomerHttpClient.get(Api.DASH);
                    info = DashboardInfo.fromJson(response);
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
                loading_bar.setVisibility(View.GONE);
                if (success) {
                    conn_tab.setText(info.connection);
                    nas_tab.setText(info.nas);
                    app_tab.setText(String .valueOf(info.app_num));
                    user_tab.setText(String .valueOf(info.user_count));

                } else {

                }

            }
        });
    }

}