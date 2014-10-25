package me.jacob.piphone.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * Created by lenovo on 2014/9/12.
 */
public class LoadingProgressBar extends ProgressBar {

    public LoadingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public LoadingProgressBar(Context context) {
        super(context);
    }

    public void set(Activity  activity, FrameLayout rootContainer, Drawable customIndeterminateDrawable) {
        // 给progressbar准备一个FrameLayout的LayoutParams
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置对其方式为：屏幕居中对其
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        setVisibility(View.GONE);
        setLayoutParams(lp);
        // 自定义小菊花
        if (customIndeterminateDrawable != null) {
            setIndeterminateDrawable(customIndeterminateDrawable);
        }
        // 将菊花添加到FrameLayout中
        rootContainer.addView(this);
    }
}
