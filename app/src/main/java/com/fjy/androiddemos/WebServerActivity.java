package com.fjy.androiddemos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

public class WebServerActivity extends AppCompatActivity {
    private MyServer server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_server);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            server = new MyServer(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if(server != null) {
            server.stop();
        }
    }
}
