package me.jacob.piphone.api;

/**
 * Created by lenovo on 2014/9/5.
 */
public class Api {
    public static final String HOST = "http://192.168.10.105:8000";

    public static final String API = HOST + "/API";

    public static final String ACCOUNT = API + "/accounts";
    public static final String LOGIN = ACCOUNT + "/login";
    public static final String CHECK_LOGIN = ACCOUNT + "/check/login";
    public static final String NAS = API + "/PiApp/nas";
    public static final String SERVER = API + "/PiApp/server";
    public static final String CLIENT = API + "/PiApp/status/client";
    public static final String DASH = API + "/PiApp/dashboard";
    public static final String SETTINGS_GENERAL = API + "/PiApp/settings/general";
    public static final String SETTINGS_ACCOUNT = API + "/PiApp/settings/account";


    public static final String ARIA2 = "http://192.168.10.105:8000" + "/static/yaaw-master/index.html";

    public static final int MSG_SUCCESS = 0;
    public static final int MSG_FAILURE = 1;
    public static final int MSG_ERROR = 2;
}
