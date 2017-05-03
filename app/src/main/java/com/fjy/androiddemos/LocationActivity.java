package com.fjy.androiddemos;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

/**
 * Demo描述:
 * 利用LocationManager实现定位功能
 * 1 实时更新经度,纬度
 * 2 根据经度和纬度获取地理信息(比如:国家,街道等)(略过)
 * <p>
 * <p>
 * 注意事项:
 * 0 在测试GPS定位时最好在较为宽广的空间,否则影响定位
 * 1 利用mLocationManager.getLastKnownLocation(GPSProvider)获取Location时常为null.
 * 因为设备定位是需要一定时间的,所以把定位逻辑放在LocationManager的requestLocationUpdates()方法
 * <p>
 * 2 LocationManager.requestLocationUpdates
 * (String provider, long minTime, float minDistance, LocationListener listener)
 * 第一个参数:位置信息的provider,比如GPS
 * 第二个参数:更新位置信息的时间间隔,单位毫秒
 * 第三个参数:更新位置信息的距离间隔,单位米
 * 第四个参数:位置信息变化时的回调
 * <p>
 * 3 LocationListener中最重要的回调方法onLocationChanged()
 * 当minTime和minDistance同时满足时会调用该方法.文档说明:
 * The minDistance parameter can also be used to control the
 * frequency of location updates. If it is greater than 0 then the
 * location provider will only send your application an update when
 * the location has changed by at least minDistance meters, AND
 * at least minTime milliseconds have passed.
 * 比如间隔时间(minTime)到了3秒并且移动的距离(minDistance)大于了5米
 * 那么就会调用该方法.
 * <p>
 * 4 在Activity的onDestroy()时取消地理位置的更新.
 * <p>
 * <p>
 * 权限配置:
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 * <uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION"/>
 * <uses-permission android:name="android.permission.INTERNET"/>
 */
public class LocationActivity extends AppCompatActivity {

    private Context mContext;
    private TextView mTextView;
    private LocationManager mLocationManager;
    private LocationListenerImpl mLocationListenerImpl;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initViews();
        initLocationManager(mContext);
    }

    private void initViews() {
        mContext = this;
        mTextView = (TextView) findViewById(R.id.textView);
    }

    private void initLocationManager(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取可用的位置信息Provider.即passive,network,gps中的一个或几个
        List<String> providerList = mLocationManager.getProviders(true);
        for (Iterator<String> iterator = providerList.iterator(); iterator.hasNext(); ) {
            String provider = (String) iterator.next();
            System.out.println("provider=" + provider);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    private void getLocation() {
        //在此采用GPS的方式获取位置信息
        String GPSProvider = LocationManager.NETWORK_PROVIDER;
        Location location = mLocationManager.getLastKnownLocation(GPSProvider);
        if (location != null) {
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            System.out.println("longitude=" + longitude + ",altitude=" + altitude);
        } else {
            System.out.println("location==null");
        }

        //注册位置监听
        mLocationListenerImpl = new LocationListenerImpl();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, mLocationListenerImpl);
    }

    private void showLocation(Location location) {
        // 获取经度
        double longitude = location.getLongitude();
        // 获取纬度
        double altitude = location.getAltitude();
        String message = "经度为:" + longitude + "\n" + "纬度为:" + altitude;
        mTextView.setText(message);

    }

    private class LocationListenerImpl implements LocationListener {
        //当设备位置发生变化时调用该方法
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                showLocation(location);
            }
        }

        //当provider的状态发生变化时调用该方法.比如GPS从可用变为不可用.
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        //当provider被打开的瞬间调用该方法.比如用户打开GPS
        @Override
        public void onProviderEnabled(String provider) {

        }

        //当provider被关闭的瞬间调用该方法.比如关闭打开GPS
        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        getLocation();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(LocationActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListenerImpl);
        }
    }


}
