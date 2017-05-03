package com.fjy.androiddemos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fjy.androiddemos.util.FileUtils;

import net.lingala.zip4j.core.ZipFile;

public class UnZipActivity extends AppCompatActivity implements View.OnClickListener {
    private Button clickToExtract;
    private final int BUFF_SIZE = 4096;

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
            ZipFile zFile = new ZipFile(getExternalFilesDir("zip").getAbsolutePath()+"/163949.zip");
            zFile.setRunInThread(true); //true 在子线程中进行解压 , false主线程中解压
            zFile.extractAll(getExternalFilesDir("unzip").getAbsolutePath()); //将压缩文件解压到filePath中...
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
