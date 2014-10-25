package me.jacob.piphone.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jacob.piphone.App;
import me.jacob.piphone.R;
import me.jacob.piphone.ui.adapter.TableAdapter;

/**
 * Created by lenovo on 2014/9/6.
 */
public class Tablelistview  extends LinearLayout {

    @InjectView(R.id.box_right_button)
    Button rightButton;

    @InjectView(R.id.box_left_button)
    Button leftButton;

    @InjectView(R.id.title)
    TextView title;

    @InjectView(R.id.box_body)
    LinearLayout box_body;

    @InjectView(R.id.box_foot)
    LinearLayout box_foot;

    public SimpleAdapter adapter;
    public ListView listview;

    public Tablelistview(Context context) {
        this(context, null);
    }

    public Tablelistview(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.tableboxview, this, true);
        ButterKnife.inject(this);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Tablelistview);

        //title
        int color = a.getColor(R.styleable.Tablelistview_title_Color,android.graphics.Color.WHITE);
        String text = a.getString(R.styleable.Tablelistview_title_text);
        title.setText(text);
        title.setTextColor(color);
        int bar_color = a.getColor(R.styleable.Tablelistview_bar_Color, getResources().getColor(R.color.olive));
        GradientDrawable myGrad = (GradientDrawable)title.getBackground();
        myGrad.setColor(bar_color);

        //button
        if (a.getBoolean(R.styleable.Tablelistview_need_button, false)) {
            color = a.getColor(R.styleable.Tablelistview_button_left_Color,android.graphics.Color.WHITE);
            text = a.getString(R.styleable.Tablelistview_button_left_text);
            Drawable button_shape = a.getDrawable(R.styleable.Tablelistview_button_shape );
            leftButton.setText(text);
            leftButton.setTextColor(color);
            if (button_shape == null)
                button_shape = getResources().getDrawable(R.drawable.button_shape);
            leftButton.setBackground(button_shape);

            color = a.getColor(R.styleable.Tablelistview_button_right_Color, android.graphics.Color.WHITE);
            text = a.getString(R.styleable.Tablelistview_button_right_text);
            button_shape = a.getDrawable(R.styleable.Tablelistview_button_shape);
            rightButton.setText(text);
            rightButton.setTextColor(color);
            if (button_shape == null)
                button_shape = getResources().getDrawable(R.drawable.button_shape);
            rightButton.setBackground(button_shape);
        }
        else {
            leftButton.setVisibility(View.GONE);
            rightButton.setVisibility(View.GONE);
        }

    }

    public void setButtonListener(View.OnClickListener listener) {
        rightButton.setOnClickListener(listener);
        leftButton.setOnClickListener(listener);
    }

    public int getTableWidth() {
        return box_body.getWidth();
    }

    public  void addList1(List<Map<String, Object>> data) {
        listview = new ListView(App.getContext());
        box_body.addView(listview,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        adapter = new SimpleAdapter(App.getContext(),data,R.layout.tablebox_list1,
                new String[]{"title","info"},
                new int[]{R.id.tablebox_list1_title,R.id.tablebox_list1_info});
        listview.setAdapter(adapter);
        listview.setPadding(0, 10, 0, 0);
        //simple.notifyDataSetChanged();
    }

    public  void addList2(List<Map<String, Object>> data) {
        listview = new ListView(App.getContext());
        box_body.addView(listview,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        adapter = new SimpleAdapter(App.getContext(),data,R.layout.tablebox_list2,
                new String[]{"0","1","2","3"},
                new int[]{R.id.tablebox_list2_0,R.id.tablebox_list2_1,R.id.tablebox_list2_2,R.id.tablebox_list2_3});
        listview.setAdapter(adapter);
        listview.setDivider(getResources().getDrawable(R.drawable.divider));
        listview.setDividerHeight(3);
        LinearLayout title =   (LinearLayout)  LayoutInflater.from(App.getContext()).inflate(R.layout.tablebox_list2, null);
        listview.addHeaderView(title);
        //listview.setBackgroundResource(R.drawable.table_list_shape);
        //simple.notifyDataSetChanged();
    }

    public  void addBodyItem(View body) {
        box_body.addView(body,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public  void setBodyHeight(int height) {
        box_body.setMinimumHeight(height);
    }

    public  void addFootItem(View foot) {
        box_foot.addView(foot,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public  void setTableAdapter(TableAdapter adapter) {

    }

    public  void notifyList() {
        adapter.notifyDataSetChanged();
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        params.height = totalHeight
                + (listview.getDividerHeight() * (adapter.getCount() - 1));
        if (listview.getHeaderViewsCount() > 0)
            params.height += 10;
        listview.setLayoutParams(params);

    }
}
