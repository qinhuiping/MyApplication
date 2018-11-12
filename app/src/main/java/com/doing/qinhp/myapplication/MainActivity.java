package com.doing.qinhp.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;

    private TextView tvLongitude;
    private TextView tvLatitude;
    private int GPS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.open_gps);
        tvLongitude= findViewById(R.id.tv_longitude);
        tvLatitude=findViewById(R.id.tv_latitude);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GaodeLocationUtils gaodeLocationUtils = new GaodeLocationUtils();
                gaodeLocationUtils.startLocation(MainActivity.this, onLocationListenter);
            }
        });
    }
    /**
     * 定位成功/失败
     */
    GaodeLocationUtils.OnLocationListenter onLocationListenter = new GaodeLocationUtils.OnLocationListenter() {
        @Override
        public void getLatitudeAndLongitude(double longitude, double latitude) {
            if (longitude == 0 && latitude == 0) {
//                Toast.makeText(MainActivity.this, "获取位置信息失败，请检查是否有获取位置权限", Toast.LENGTH_SHORT).show();
                openGPSSEtting();
            } else {
                Toast.makeText(MainActivity.this, "获取位置信息成功", Toast.LENGTH_SHORT).show();

            }
            tvLongitude.setText("经度:"+longitude);
            tvLatitude.setText("纬度:"+latitude);
        }
    };

    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private void openGPSSEtting() {
        if (!checkGpsIsOpen()){

            new AlertDialog.Builder(this).setTitle("open GPS")
                    .setMessage("go to open")
                    .setNegativeButton("cancel",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "close", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent,GPS_REQUEST_CODE);
                            dialogInterface.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==GPS_REQUEST_CODE){

            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

        }
    }
}
