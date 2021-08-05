package com.example.iotobserve;

import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryBaiduMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryBaiduMap extends Fragment {

    //百度地图页面
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private TextView seed_amount,seed_fre,dongjing,beiwei;
    private Button mButton_pingmian,mButton_weixing;
    private Boolean isfirstlocation = true;
    private Boolean isrunning = true;
    private Polyline mPolyline_whole,mPolyline_temp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryBaiduMap() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryBaiduMap.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryBaiduMap newInstance(String param1, String param2) {
        HistoryBaiduMap fragment = new HistoryBaiduMap();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    //设置back键

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
                    isrunning = false;
                    //清除之前保留下来的点
                    Constants.points_tem.clear();
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SDKInitializer.initialize(GetApplicationContext.getContext());
        return inflater.inflate(R.layout.fragment_history_baidu_map, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //SDK初始化
        //SDKInitializer.initialize(GetApplicationContext.getContext());
        super.onActivityCreated(savedInstanceState);
        mMapView = getView().findViewById(R.id.mapview_history);
        mBaiduMap = mMapView.getMap();
        seed_amount = getView().findViewById(R.id.textView5);
        seed_fre = getView().findViewById(R.id.textView4);
        dongjing = getView().findViewById(R.id.textView6);
        beiwei = getView().findViewById(R.id.textView7);
        seed_amount.setText("播种总量（粒）："+Constants.his_amount);
        seed_fre.setText("播种频率（粒/秒）："+Constants.his_fre);
        DecimalFormat df = new DecimalFormat("#.00000");
        dongjing.setText("东经："+df.format(Constants.his_lon));
        beiwei.setText("北纬："+df.format(Constants.his_lat));
        mButton_pingmian = getView().findViewById(R.id.button4);
        mButton_weixing = getView().findViewById(R.id.button7);
        mButton_pingmian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            }
        });

        mButton_weixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            }
        });

        //定位
        mLocationClient = new LocationClient(GetApplicationContext.getContext());
        mBaiduMap.setMyLocationEnabled(true);
        //定位到相应视图
        LatLng ll = new LatLng(Constants.his_lat,Constants.his_lon);
        MapStatusUpdate mUpdate = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(mUpdate);
        mUpdate = MapStatusUpdateFactory.zoomTo(20f);
        mBaiduMap.animateMapStatus(mUpdate);
        //显示定位点
        MyLocationData.Builder locationbuilder = new MyLocationData.Builder();
        locationbuilder.latitude(ll.latitude);
        locationbuilder.longitude(ll.longitude);
        Log.d("定位点：", String.valueOf(ll.longitude));
        MyLocationData myLocationData = locationbuilder.build();
        mBaiduMap.setMyLocationData(myLocationData);
        //清除之前绘制的线
        mMapView.getMap().clear();
        //根据坐标绘制折线
        OverlayOptions onpolylines = new PolylineOptions()
                .color(0xAAFF0000)
                .width(10)
                .points(Constants.points);
        mPolyline_whole = (Polyline) mBaiduMap.addOverlay(onpolylines);
        OverlayOptions onpolulines_tem = new PolylineOptions()
                .color(0xAA0000FF)
                .width(10)
                .points(Constants.points_tem);
        mPolyline_temp = (Polyline) mBaiduMap.addOverlay(onpolulines_tem);
        BitmapDescriptor startBD = new BitmapDescriptorFactory()
                .fromResource(R.drawable.icon_st);
        BitmapDescriptor finishBD = new BitmapDescriptorFactory()
                .fromResource(R.drawable.icon_en);
        LatLng start = Constants.points.get(0);
        LatLng end = Constants.points.get(Constants.points.size()-1);
        OverlayOptions marka = new MarkerOptions()
                .position(start)
                .icon(startBD);
        OverlayOptions markb = new MarkerOptions()
                .position(end)
                .icon(finishBD);
        mBaiduMap.addOverlay(marka);
        mBaiduMap.addOverlay(markb);


        /*
        //每隔一秒钟获取一个坐标点并在地图上显示0xAAFF0000
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isfirstlocation){
                    LatLng ll = new LatLng(30.46934,114.35477);
                    MapStatusUpdate mUpdate = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(mUpdate);
                    mUpdate = MapStatusUpdateFactory.zoomTo(20f);
                    mBaiduMap.animateMapStatus(mUpdate);
                    isfirstlocation = false;

                }
                if(!isrunning){
                    timer.cancel();
                }
                MyLocationData.Builder locationbuilder = new MyLocationData.Builder();
                locationbuilder.latitude(30.46934);
                locationbuilder.longitude(114.35477);
                MyLocationData myLocationData = locationbuilder.build();
                mBaiduMap.setMyLocationData(myLocationData);
                Log.d("初始定位坐标", "和");

            }
        },0,1000);
        */

    }


}