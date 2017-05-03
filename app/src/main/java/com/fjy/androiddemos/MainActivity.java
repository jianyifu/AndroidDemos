package com.fjy.androiddemos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView openLocationDemo;
    private TextView openUnzipDemo;
    private TextView openServerDemo;
    private TextView openWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews() {
        openLocationDemo = (TextView) findViewById(R.id.open_location_demo);
        openUnzipDemo = (TextView) findViewById(R.id.open_unzip_demo);
        openServerDemo = (TextView) findViewById(R.id.open_server_demo);
        openWebView = (TextView) findViewById(R.id.open_web_view);
    }

    private void initListeners() {
        openLocationDemo.setOnClickListener(this);
        openUnzipDemo.setOnClickListener(this);
        openServerDemo.setOnClickListener(this);
        openWebView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.open_location_demo:
                intent = new Intent(this,LocationActivity.class);
                startActivity(intent);
                break;
            case R.id.open_unzip_demo:
                intent = new Intent(this,UnZipActivity.class);
                startActivity(intent);
                break;
            case R.id.open_server_demo:
                intent = new Intent(this,WebServerActivity.class);
                startActivity(intent);
                break;
            case R.id.open_web_view:
                intent = new Intent(this,WebViewActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
