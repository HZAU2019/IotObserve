package com.example.iotobserve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
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
import com.baidu.mapapi.model.LatLng;
import com.example.iotobserve.okhttp.GetSeeds;
import com.example.iotobserve.okhttp.OkHttp;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentBaiduMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentBaiduMap extends Fragment {
    //百度地图页面
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private TextView seed_amount,seed_fre,dongjing,beiwei;
    private Button normal,satellite;
    private Boolean isfirstlocation = true;
    private Boolean isRunning = true;
    private float zoomnumber = 20f;


    public CurrentBaiduMap() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentBaiduMap.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentBaiduMap newInstance(String param1, String param2) {
        CurrentBaiduMap fragment = new CurrentBaiduMap();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    isRunning = false;
                    Constants.points_current.clear();
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SDKInitializer.initialize(GetApplicationContext.getContext());
        return inflater.inflate(R.layout.fragment_current_baidu_map, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapView = getView().findViewById(R.id.mapview_current);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        seed_amount = getView().findViewById(R.id.textView5);
        seed_fre = getView().findViewById(R.id.textView4);
        dongjing = getView().findViewById(R.id.textView6);
        beiwei = getView().findViewById(R.id.textView7);
        normal = getView().findViewById(R.id.button8);
        satellite = getView().findViewById(R.id.button9);

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            }
        });
        satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            }
        });

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        beiwei.setText("北纬："+Constants.sGetSeeds.getLatitude());
                        dongjing.setText("东经："+Constants.sGetSeeds.getLongitude());
                        seed_fre.setText("播种频率（粒/秒）："+Constants.sGetSeeds.getRow_frequency());
                        seed_amount.setText("播种总量（粒）："+Constants.sGetSeeds.getRow_amount());
                }
            }
        };


        //定位
        mLocationClient = new LocationClient(getContext());

        //每隔一秒钟获取一个坐标点并在地图上显示
        final Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if(!isRunning){
                            timer.cancel();
                        }
                        try {
                            Constants.sGetSeeds = OkHttp.TimeObserve(Constants.current_table_name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LatLng ll = new LatLng(Constants.sGetSeeds.getLatitude(),Constants.sGetSeeds.getLongitude());
                        if(isfirstlocation){
                            try {
                                Constants.points_current.clear();
                                List<GetSeeds> getSeeds = OkHttp.SelectTable(Constants.current_table_name);
                                for(GetSeeds getSeeds1:getSeeds){
                                    LatLng latLng = new LatLng(getSeeds1.getLatitude(),getSeeds1.getLongitude());
                                    Constants.points_current.add(latLng);
                                }
                                //定位到相应点，并以该点为中心

                                MapStatusUpdate mUpdate = MapStatusUpdateFactory.newLatLng(ll);
                                mBaiduMap.animateMapStatus(mUpdate);
                                mUpdate = MapStatusUpdateFactory.zoomTo(zoomnumber);
                                mBaiduMap.animateMapStatus(mUpdate);
                                isfirstlocation = false;
                                Constants.points_current.add(ll);
                                LatLng latLng = Constants.points_current.get(0);
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                                        .fromResource(R.drawable.icon_st);
                                OverlayOptions options = new MarkerOptions()
                                        .position(latLng)
                                        .icon(bitmapDescriptor);
                                mBaiduMap.addOverlay(options);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                        MyLocationData.Builder locationbuilder = new MyLocationData.Builder();
                        locationbuilder.latitude(Constants.sGetSeeds.getLatitude());
                        locationbuilder.longitude(Constants.sGetSeeds.getLongitude());
                        MyLocationData myLocationData = locationbuilder.build();
                        mBaiduMap.setMyLocationData(myLocationData);
                        Constants.points_current.add(ll);
                        handler.sendEmptyMessage(1);

                    }
                },0,1000);
    }
}