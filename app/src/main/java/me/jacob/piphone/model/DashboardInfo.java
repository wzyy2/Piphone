package me.jacob.piphone.model;

import com.google.gson.Gson;

/**
 * Created by lenovo on 2014/9/14.
 */
public class DashboardInfo extends BaseModel  {

    public String nas;
    public String connection;
    public int user_count;
    public int app_num;



    public static DashboardInfo fromJson(String json) {
        return new Gson().fromJson(json, DashboardInfo.class);
    }
}
