package me.jacob.piphone.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jacob.piphone.R;

/**
 * Created by lenovo on 2014/9/7.
 */
public class DashTab extends LinearLayout {

    @InjectView(R.id.font_awesome)
    TextView font_awesome;

    @InjectView(R.id.tab_textView0)
    TextView text0;

    @InjectView(R.id.tab_textView1)
    TextView text1;

    @InjectView(R.id.tab_textView2)
    Button text2;

    @InjectView(R.id.tab_body)
    LinearLayout body;

    public DashTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.dashtab, this, true);
        ButterKnife.inject(this);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.DashTab);
        int color = a.getColor(R.styleable.DashTab_DashTab_background,getResources().getColor(R.color.olive));

        //body.setClickable(false);
        //font_awesome.setClickable(false);
        //text0.setClickable(false);
        //text1.setClickable(false);

        body.setBackgroundColor(color - 0x20000000);
        text2.setBackgroundColor(color);

        String font_aws = a.getString(R.styleable.DashTab_font_aws);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        font_awesome.setTypeface(font);
        font_awesome.setText(font_aws);

        String text = a.getString(R.styleable.DashTab_tab_text_how);
        text0.setText(text);
        text = a.getString(R.styleable.DashTab_tab_text_what);
        text1.setText(text);
        text = a.getString(R.styleable.DashTab_tab_text_button);
        text2.setText(text + " " + getResources().getString(R.string.fa_arrow_circle_right));
        text2.setTypeface(font);

    }

    public void setButtonListener(View.OnClickListener listener) {
        text2.setOnClickListener(listener);
    }

    public void setText(String get) {
        text0.setText(get);
    }

}
