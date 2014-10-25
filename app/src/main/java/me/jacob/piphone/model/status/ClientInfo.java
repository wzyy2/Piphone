package me.jacob.piphone.model.status;

import android.util.Log;

import me.jacob.piphone.model.BaseModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/9/14.
 */
public class ClientInfo extends BaseModel {

    public String uptime;
    public String name;
    public String kernel_version;
    public double cpu;

    public MemoryInfo memory_info;
    public Loadavg loadavg;
    public List<FilesystemInfo> filesystem_info;


    public class Loadavg {
        public Double value0;
        public Double value1;
        public Double value2;
    }

    public class MemoryInfo {
        public int used;
        public int cached;
        public int free;
        public int shared;
        public int total;
        public int buffers;
    }

    public class FilesystemInfo {
        public long available;
        public String  mount_point;
        public int used;
        public String filesystem;
    }


    public static ClientInfo fromJson(String json) {
        ClientInfo ret = new Gson().fromJson(json, ClientInfo.class);
        try {
            JSONObject root = new JSONObject(json);
            root = root.getJSONObject("loadavg");
            ret.loadavg.value0 = root.getDouble("0");
            ret.loadavg.value1 = root.getDouble("1");
            ret.loadavg.value2 = root.getDouble("2");
        } catch (Exception e) {
            // JSon解析出错
            Log.w("debug", "connect error");
        }
        return ret;
    }

    public void bindSystemData (List<Map<String, Object>> data)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "Pi Name");
        map.put("info", name);
        data.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "Kernel Version");
        map.put("info", kernel_version);
        data.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "Uptime");
        map.put("info", uptime);
        data.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "Load Average");
        map.put("info", String.valueOf(loadavg.value0) + " " + String.valueOf(loadavg.value1) + " " + String.valueOf(loadavg.value2));
        data.add(map);
    }

    public void bindFileSystemData (List<Map<String, Object>> data)
    {
        for(FilesystemInfo i : filesystem_info){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("0", i.filesystem);
            map.put("1", i.mount_point);
            map.put("2", String.valueOf(i.available) + "KB");
            map.put("3", i.used);
            data.add(map);
        }

    }

}
