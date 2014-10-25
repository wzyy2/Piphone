/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */

package me.jacob.piphone.view.chart;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import me.jacob.piphone.App;
import me.jacob.piphone.view.GraphicalView;

/**
 * @ClassName PieChart02View
 * @Description  平面饼图的例子
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */

public class PieChart02View extends GraphicalView {

    private String TAG = "PieChart02View";
    private PieChart chart = new PieChart();
    public LinkedList<PieData> chartData = new LinkedList<PieData>();
    Context mcontext;

    public PieChart02View(Context context) {
        super(context);
        mcontext = context;
        initView();
    }

    public PieChart02View(Context context, AttributeSet attrs){
        super(context, attrs);
        initView();
    }

    public PieChart02View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView()
    {
        chartDataSet();
        chartRender();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w,h);
    }


    private void chartRender()
    {
        try {
            //标签显示(隐藏，显示在中间，显示在扇区外面,折线注释方式)
            chart.setLabelPosition(XEnum.SliceLabelPosition.INSIDE);

            //图的内边距
            //注释折线较长，缩进要多些
            int [] ltrb = new int[4];
            ltrb[0] = DensityUtil.dip2px(mcontext, 30); //left
            ltrb[1] = DensityUtil.dip2px(mcontext, 15); //top
            ltrb[2] = DensityUtil.dip2px(mcontext, 30); //right
            ltrb[3] = DensityUtil.dip2px(mcontext, 15); //bottom

            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            //设定数据源
            chart.setDataSource(chartData);

            //标题
            //chart.setTitle("擂茶配方比");
            //chart.addSubtitle("(XCL-Charts Demo)");
            //chart.setTitleVerticalAlign(XEnum.VerticalAlign.MIDDLE);

            //隐藏渲染效果
            chart.hideGradient();
            //显示边框
            //chart.showRoundBorder();

            //激活点击监听
            //chart.ActiveListenItemClick();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }

    private void chartDataSet()
    {
        //设置图表数据源
        chartData.add(new PieData("id",100,(int)Color.rgb(00, 0xc0, 0xaf)));
        chartData.add(new PieData("us",0,(int)Color.rgb(0x3c, 0x8d, 0xbc)));
    }

    public void charDataclear()
    {
        chartData.clear();
    }

    public void charDataadd(PieData data)
    {
        chartData.add(data);
    }

    @Override
    public void render(Canvas canvas) {
        // TODO Auto-generated method stub
        try{
            chart.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        //super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            triggerClick(event.getX(),event.getY());
        }
        return true;
    }


    //触发监听
    private void triggerClick(float x,float y)
    {

        ArcPosition record = chart.getPositionRecord(x,y);
        if( null == record) return;

        PieData pData = chartData.get(record.getDataID());
        Toast.makeText(mcontext,
                " key:" +  pData.getKey() +
                        " Label:" + pData.getLabel() ,
                Toast.LENGTH_SHORT).show();

    }

}
