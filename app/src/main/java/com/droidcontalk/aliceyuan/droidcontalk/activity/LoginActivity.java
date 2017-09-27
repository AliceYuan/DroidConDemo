package com.droidcontalk.aliceyuan.droidcontalk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.droidcontalk.aliceyuan.droidcontalk.R;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean DEBUG = true;
    private PDKClient pdkClient;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(this);
        pdkClient = PDKClient.configureInstance(this, getResources().getString(R.string.pdk_api_key));
        pdkClient.onConnect(this);
        pdkClient.setDebugMode(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pdkClient.onOauthResponse(requestCode, resultCode,
                data);
    }

    private void onLogin() {
        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PRIVATE);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PRIVATE);

        setContentView(new FrameLayout(getBaseContext()));
        pdkClient.login(this, scopes, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(getClass().getName(), response.getData().toString());
                onLoginSuccess();
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.login:
                onLogin();
                break;
        }
    }

    private void onLoginSuccess() {
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
