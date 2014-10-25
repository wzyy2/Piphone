package me.jacob.piphone.util.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.http.client.CookieStore;

import me.jacob.piphone.App;

import java.util.Iterator;
import java.util.Map;


public class Athority {

    public static final String ACCOUNT_INFORMATION = "accout_information";
    private static SharedPreferences mShared;
    private static CookieStore cookies;

    public static SharedPreferences getSharedPreference() {
        if (mShared == null) {
            mShared = App.getContext().getSharedPreferences(ACCOUNT_INFORMATION, Context.MODE_PRIVATE);
        }
        return mShared;
    }

    public static CookieStore getCookie(){
        return cookies;
    }
    public static void setCookie(CookieStore cks){
        cookies = cks;
    }

    public static void addOther(String key, String value) {
        if (mShared == null) {
            getSharedPreference();
        }
        SharedPreferences.Editor editor = mShared.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getInformation(String key) {
        String information = "";
        if (mShared == null) {
            getSharedPreference();
        }
        information = mShared.getString(key, "no");
        return information;
    }

}
