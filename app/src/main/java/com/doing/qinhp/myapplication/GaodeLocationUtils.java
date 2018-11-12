package com.doing.qinhp.myapplication;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * 高德定位工具类
 * GaodeLocationUtils
 *
 * @author tianshi
 * @time 2018/1/15 0015 13:16
 */

public class GaodeLocationUtils {
    public OnLocationListenter onLocationListenter;
    public AMapLocationClient aMapLocationClient;

    /**
     * 高德地图参数配置
     *
     * @return
     */
    public AMapLocationClientOption parameterConfiguration() {
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式（采取高精度定位模式）
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        //设置单次定位
        mLocationOption.setOnceLocation(true);
        //获取3s内最精确的一次定位结果
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
//        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        mLocationOption.setHttpTimeOut(20000);
        return mLocationOption;
    }

    /**
     * 开始定位
     *
     * @return
     */
    public void startLocation(Context context, OnLocationListenter onLocationListenter) {
        this.onLocationListenter = onLocationListenter;
        //初始化定位
        aMapLocationClient = new AMapLocationClient(context);
        //设置定位成功监听
        aMapLocationClient.setLocationOption(parameterConfiguration());
        //成功结果监听
        aMapLocationClient.setLocationListener(aMapLocationListener);
        //检测是否有GPS权限
        AndPermission
                .with(context)
                .requestCode(100)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(permissionListener)
                .start();


    }

    /**
     * 定位监听
     */
    public interface OnLocationListenter {
        /**
         * 获取经纬度
         *
         * @param longitude 经度
         * @param latitude  纬度
         */
        void getLatitudeAndLongitude(double longitude, double latitude);
    }

    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (onLocationListenter != null) {
                aMapLocationClient.onDestroy();
                onLocationListenter.getLatitudeAndLongitude(aMapLocation.getLongitude(), aMapLocation.getLatitude());
            }
        }
    };
    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            aMapLocationClient.startLocation();
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            //获取位置失败
            if (onLocationListenter != null) {
                onLocationListenter.getLatitudeAndLongitude(0, 0);
            }
        }
    };

}
