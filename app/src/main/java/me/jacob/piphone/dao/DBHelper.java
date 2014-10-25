package me.jacob.piphone.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by lenovo on 2014/9/6.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "piphone.db";

    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //DashDataHelper.DashDBInfo.TABLE.create(db);
        //ImagesDataHelper.ImagesDBInfo.TABLE.create(db);
        //ItemsDataHelper.ItemsDBInfo.TABLE.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
