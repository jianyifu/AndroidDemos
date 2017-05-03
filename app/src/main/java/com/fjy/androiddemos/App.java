package com.fjy.androiddemos;

import android.app.Application;

import java.io.IOException;

/**
 * Created by fujianyi on 2017/5/3.
 */

public class App extends Application {
    private MyServer server;
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            server = new MyServer(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
