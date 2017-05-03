package com.fjy.androiddemos;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fjy.androiddemos.util.CompressKeys;
import com.fjy.androiddemos.util.CompressStatus;
import com.fjy.androiddemos.util.FileUtils;

import java.io.File;

public class UnZipActivity extends AppCompatActivity implements View.OnClickListener {
    private final int BUFF_SIZE = 4096;

    private Button clickToExtract;
    private TextView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_zip);
        initViews();
        initListeners();
    }

    private void initListeners() {
        clickToExtract.setOnClickListener(this);
    }

    private void initViews() {
        clickToExtract = (Button) findViewById(R.id.click_to_extract);
        progress = (TextView) findViewById(R.id.progress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.click_to_extract:
                doExtract();
                break;
            default:
                break;
        }
    }

    private void doExtract() {

        try {
            FileUtils.CopyAssets(this,"zip",getExternalFilesDir("zip").getAbsolutePath());
            FileUtils.unzip(new File(getExternalFilesDir("zip").getAbsolutePath()+"/163949.zip"),getExternalFilesDir("unzip").getAbsolutePath(),"password",true, false, _handler);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private Handler _handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CompressStatus.START:
                {
                    setText("Start...");
                    break;
                }
                case CompressStatus.HANDLING:
                {
                    Bundle bundle=msg.getData();
                    int percent=bundle.getInt(CompressKeys.PERCENT);
                    setText(percent+"%");
                    break;
                }
                case CompressStatus.ERROR:
                {
                    Bundle bundle=msg.getData();
                    String error=bundle.getString(CompressKeys.ERROR);
                    progress.setText(error);
                    break;
                }
                case CompressStatus.COMPLETED:
                {
                    setText("Completed");
//                    byte[] data=FileSp.read(tempFilePath);
//                    try {
//                        String dataStr=new String(data,"UTF-8");
//                        _info_textView.setText(dataStr);
//                    } catch (UnsupportedEncodingException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
                    break;
                }
                default:
                    break;
            }

        };
    };

    private void setText(String s) {
        if(progress!=null){
            progress.setText(s+"");
        }
    }

}
