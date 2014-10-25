package me.jacob.piphone.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import me.jacob.piphone.R;
import me.jacob.piphone.api.Api;
import me.jacob.piphone.data.CustomerHttpClient;
import me.jacob.piphone.util.TaskUtils;
import me.jacob.piphone.util.sharedpreference.Athority;


public class LaunchActivity extends Activity {

    private Context mContext;
    SharedPreferences mShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getActionBar().hide();
        setContentView(R.layout.launch);

        mShared = mContext.getSharedPreferences(
                Athority.ACCOUNT_INFORMATION, Context.MODE_PRIVATE);

        if (mShared.getString("login", "no").equals("yes")) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    TestLogin();
                }
            }, 100);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(mContext,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }

    }

    public void TestLogin()
    {
        TaskUtils.executeAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                String username = mShared.getString("username", "blank");
                String password = mShared.getString("password", "blank");
                return CustomerHttpClient.login(username, password);
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                if (success) {
                    Intent intent = new Intent(mContext,
                            MainActivity.class);
                    startActivity(intent);
                    Toast toast=Toast.makeText(getApplicationContext(), "Hello,  " + mShared.getString("firstname", ""), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    finish();
                } else {
                    Athority.addOther("login", "no");
                    Intent intent = new Intent(mContext,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
