package com.fjy.androiddemos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView openLocationDemo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews() {
        openLocationDemo = (TextView) findViewById(R.id.open_location_demo);
    }

    private void initListeners() {
        openLocationDemo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.open_location_demo:
                Intent intent = new Intent(this,LocationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
