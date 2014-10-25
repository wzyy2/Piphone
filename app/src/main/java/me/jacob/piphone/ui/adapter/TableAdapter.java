package me.jacob.piphone.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;

import me.jacob.piphone.view.Tablelistview;

/**
 * Created by lenovo on 2014/9/7.
 */
public class TableAdapter {

    private Tablelistview mtableview;
    private LayoutInflater mLayoutInflater;
    private Resources mResource;

    public TableAdapter(Context context, Tablelistview tableview) {

        mResource = context.getResources();
        mLayoutInflater = LayoutInflater.from(context);
        mtableview = tableview;
    }




}

