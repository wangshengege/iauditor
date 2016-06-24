package com.jointeach.iauditor.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.jointeach.iauditor.R;
import com.jointeach.iauditor.common.JKApplication;
import com.jointeach.iauditor.common.LocationService;
import com.jointeach.iauditor.entity.LocationBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.utils.Tools;

import de.greenrobot.event.EventBus;

public class MapActivity extends AbstractBaseActivity {
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.map)
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationService loactation;
    @ViewInject(R.id.tv_loc)
    private TextView tv_loc;
    @ViewInject(R.id.tv_lng)
    private TextView tv_lng;
    private LocationBack locationBack=new LocationBack();
    //构建Marker图标
   private BitmapDescriptor bitmap = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_openmap_mark);
    public static void startAction(Context ctx) {
        Tools.toActivity(ctx, MapActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_map);
        toolbar.setTitle("位置");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView() {
        loactation = ((JKApplication) getApplication()).locationService;
        loactation.registerListener(bdListener);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        loactation.start();
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                baiduMap.clear();
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(mapPoi.getPosition())
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                baiduMap.addOverlay(option);
                locationBack.location=mapPoi.getName();
                locationBack.latitude=mapPoi.getPosition().latitude;
                locationBack.longitude=mapPoi.getPosition().longitude;
                tv_lng.setText(String.format("[%s,%s]",mapPoi.getPosition().latitude+"", mapPoi.getPosition().longitude+""));
                tv_loc.setText(mapPoi.getName());
                return true;
            }
        });
    }

    private BDLocationListener bdListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            loactation.stop();
            tv_loc.setText(bdLocation.getAddrStr());
            tv_lng.setText(String.format("[%s,%s]",bdLocation.getLatitude()+"", bdLocation.getLongitude()+""));
            locationBack.location=bdLocation.getAddrStr();
            locationBack.latitude=bdLocation.getLatitude();
            locationBack.longitude=bdLocation.getLongitude();
            //定义Maker坐标点
            LatLng point = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.zoom(17).target(point);//放大到级别
            // 改变地图状态
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(builder.build());
            baiduMap.setMapStatus(mMapStatusUpdate);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            baiduMap.addOverlay(option);
        }
    };
    @OnClick(R.id.fab)
    private void getLoaction(View v){
        baiduMap.clear();
        loactation.start();
    }
    @OnClick(R.id.tv_use)
    private void use(View v){
        EventBus.getDefault().post(locationBack);
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
        loactation.unregisterListener(bdListener);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
       mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        // activity 销毁时同时销毁地图控件
        mapView.onDestroy();
        super.onDestroy();
    }
}
