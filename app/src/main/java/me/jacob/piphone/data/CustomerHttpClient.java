package me.jacob.piphone.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.jacob.piphone.App;
import me.jacob.piphone.api.Api;
import me.jacob.piphone.util.sharedpreference.Athority;

/**
 * Created by lenovo on 2014/9/5.
 */

public class CustomerHttpClient {
    private static final String CHARSET = HTTP.UTF_8;
    private static DefaultHttpClient customerHttpClient;
    private static final String TAG = "CustomerHttpClient";
    static final SharedPreferences mShared = App.getContext().getSharedPreferences(
            Athority.ACCOUNT_INFORMATION, Context.MODE_PRIVATE);

    private CustomerHttpClient() {
    }

    public static synchronized DefaultHttpClient getHttpClient() {
        if (null == customerHttpClient) {
            HttpParams params = new BasicHttpParams();
            // 设置一些基本参数
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);
            HttpProtocolParams.setUserAgent(
                    params, "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                            + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
            // 超时设置
            /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(params, 1000);
            /* 连接超时 */
            HttpConnectionParams.setConnectionTimeout(params, 4000);
            /* 请求超时 */
            HttpConnectionParams.setSoTimeout(params, 4000);

            // 设置我们的HttpClient支持HTTP和HTTPS两种模式
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

            // 使用线程安全的连接管理来创建HttpClient
            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
            customerHttpClient = new org.apache.http.impl.client.DefaultHttpClient(conMgr, params);
        }
        return customerHttpClient;
    }

    public static String post(String url, NameValuePair... params) {
        DefaultHttpClient client;
        HttpResponse response;
        HttpPost request;

        try {
            // 编码参数
            List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
            for (NameValuePair p : params) {
                formparams.add(p);
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,CHARSET);

            // 创建POST请求
            request = new HttpPost(url);

            CookieStore cookieStore = Athority.getCookie();
            client = getHttpClient();
            client.setCookieStore(cookieStore);

            request.setEntity(entity);
            response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity resEntity = response.getEntity();
            return (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, e.getMessage());
            return null;
        } catch (ClientProtocolException e) {
            Log.w(TAG, e.getMessage());
            return null;
        } catch (IOException e) {
            throw new RuntimeException("连接失败", e);
        }
    }

    public static String get(String url, Map<String, String> map) {
        DefaultHttpClient client;
        HttpResponse response;
        HttpGet request;
        String key;
        List params = new ArrayList();

        try {
            String param;
            Set keys = map.keySet( );
            if(keys != null) {
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    params.add(new BasicNameValuePair(key, map.get(key)));
                }
            }
            param = URLEncodedUtils.format(params, "UTF-8");
            url += "?" + param;
            Log.w(TAG, url);
            request = new HttpGet(url);

            CookieStore cookieStore = Athority.getCookie();
            client = getHttpClient();
            client.setCookieStore(cookieStore);

            response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity resEntity = response.getEntity();
            return (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, e.getMessage());
            return null;
        } catch (ClientProtocolException e) {
            Log.w(TAG, e.getMessage());
            return null;
        } catch (IOException e) {
            throw new RuntimeException("连接失败", e);
        }
    }

    public static String get(String url) {
        DefaultHttpClient client;
        HttpResponse response;
        HttpGet request;
        String key;

        try {
            request = new HttpGet(url);
            // 发送请求

            CookieStore cookieStore = Athority.getCookie();
            client = getHttpClient();
            client.setCookieStore(cookieStore);

            response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity resEntity = response.getEntity();
            return (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, e.getMessage());
            return null;
        } catch (ClientProtocolException e) {
            Log.w(TAG, e.getMessage());
            return null;
        } catch (IOException e) {
            throw new RuntimeException("连接失败", e);
        }
    }



    public static boolean login(String username, String password) {
        NameValuePair param1 = new BasicNameValuePair("username", username);
        NameValuePair param2 = new BasicNameValuePair("password", password);
        try {
            // 编码参数
            List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
            formparams.add(param1);
            formparams.add(param2);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, CHARSET);
            // 创建POST请求
            HttpPost request = new HttpPost(Api.LOGIN);
            request.setEntity(entity);
            // 发送请求
            DefaultHttpClient client = (DefaultHttpClient) CustomerHttpClient.getHttpClient();
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return false;
            }
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String login_response = EntityUtils.toString(resEntity, CHARSET);
                JSONObject root = new JSONObject(login_response);
                String ret = root.getString("msg");
                String firstname = root.getString("firstname");
                String lastname = root.getString("lastname");

                CookieStore cookieStore = client.getCookieStore();
                Athority.setCookie(cookieStore);

                List<Cookie> cookies = cookieStore.getCookies();

                if (! cookies.isEmpty()){
                    CookieSyncManager.createInstance(App.getContext());
                    CookieManager cookieManager = CookieManager.getInstance();
                    //sync all the cookies in the httpclient with the webview by generating cookie string
                    for (Cookie cookie : cookies){
                        String cookieString = cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain();
                        cookieManager.setCookie(Api.HOST, cookieString);
                        CookieSyncManager.getInstance().sync();
                    }
                }

                if (ret.equals("ok")) {
                    Athority.addOther("login", "yes");
                    Athority.addOther("firstname", firstname);
                    Athority.addOther("lastname", lastname);
                    Athority.addOther("username", username);
                    Athority.addOther("password", password);
                    return true;
                }
            }
        } catch (UnsupportedEncodingException e) {
            Log.w("debug", "connect error 1");
        } catch (ClientProtocolException e) {
            Log.w("debug", "connect error 2");
        } catch (IOException e) {
            Log.w("debug", "connect error 3");
        } catch (Exception e) {
            // JSon解析出错
            e.printStackTrace();
        }
        return false;
    }

}