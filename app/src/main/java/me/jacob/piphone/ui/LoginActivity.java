package me.jacob.piphone.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import me.jacob.piphone.R;
import me.jacob.piphone.data.CustomerHttpClient;
import me.jacob.piphone.util.TaskUtils;
import me.jacob.piphone.util.sharedpreference.Athority;
import me.jacob.piphone.api.Api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends Activity {


    private Context mContext;

    @InjectView(R.id.username)
    public EditText usernameText;

    @InjectView(R.id.password)
    public EditText passwordText;

    @InjectView(R.id.confirm)
    public Button confirm;

    private ProgressDialog progressDialog;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.inject(this);
        mContext = this;
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        final SharedPreferences mShared = mContext.getSharedPreferences(
                Athority.ACCOUNT_INFORMATION, Context.MODE_PRIVATE);


        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(mContext, "用户名不能为空！", Toast.LENGTH_SHORT)
                            .show();
                } else if (password.equals("")) {
                    Toast.makeText(mContext, "密码不能为空！", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    /*MD5 m = new MD5();
                    password = m.getMD5ofStr(password)*/;
                    progressDialog = ProgressDialog.show(mContext, null, "Please Waiting");
                    TaskUtils.executeAsyncTask(new AsyncTask<Void, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            return isLoginSuccess();
                        }

                        @Override
                        protected void onPostExecute(Boolean success) {
                            super.onPostExecute(success);
                            if (!success) {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT)
                                        .show();
                                usernameText.setText("");
                                passwordText.setText("");
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Success!", Toast.LENGTH_SHORT)
                                        .show();
                                Intent intent = new Intent(mContext, MainActivity.class);
                                mContext.startActivity(intent);
                                Toast toast=Toast.makeText(getApplicationContext(), "Hello,  " + mShared.getString("firstname", ""), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    private boolean isLoginSuccess() {
        return CustomerHttpClient.login(username, password);
    }

}
