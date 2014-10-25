package me.jacob.piphone.model.status;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jacob.piphone.model.BaseModel;

/**
 * Created by lenovo on 2014/9/14.
 */
public class ServerInfo extends BaseModel {

    public String version;


    public static ServerInfo fromJson(String json) {
        return new Gson().fromJson(json, ServerInfo.class);
    }

    public void bindAboutData (List<Map<String, Object>> data)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "Pihome Version");
        map.put("info", version);
        data.add(map);
    }
}
