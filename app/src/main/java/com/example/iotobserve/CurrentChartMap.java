package com.example.iotobserve;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.iotobserve.okhttp.GetSeeds;
import com.example.iotobserve.okhttp.OkHttp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentChartMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentChartMap extends Fragment {
    private ComboLineColumnChartView columnChartView;
   // private LineChartData columnChartData_fre;
    private ColumnChartData columnChartData_num;
    private ComboLineColumnChartData mComboLineColumnChartData;
    private boolean isChartMapStop = false;
    private Axis axisX,axisY,axisY1;
    private TextView isFre;
    //private Viewport viewport = new Viewport();
    //判断是频率还是播量的柱状图
    private boolean isFrequency = false;
    //百度地图相关参数
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private TextView seed_amount,seed_fre,dongjing,beiwei;
    private Button mButton_pingmian,mButton_weixing;
    private float zoomnumber = 18f;
    private int fre_max = 0,num_max = 0;
    private boolean isFirst = true;
    private Handler handler;
    private List<PointValue> columns_fre = new ArrayList<>();
    List<Column> columns = new ArrayList<>();
    private boolean isSatellite = false;

    //报警提示相关的变量

    boolean isAlarm = false;//是否开启报警功能
    private TextView standardNumber, alarmStatus;
    private SeekBar mSeekBar;
    private Switch mSwitch;
    private boolean didididi = false;
    //设置本程序的循环
    public static Timer timer ;


    public CurrentChartMap() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CurrentChartMap newInstance(String param1, String param2) {
        CurrentChartMap fragment = new CurrentChartMap();
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
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
               if(event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                   timer.cancel();
                  // TimeObserve.timer.cancel();
                   Alarm.openAlarm(false);
               }
                return false;

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_chart_map, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isFre = getView().findViewById(R.id.textView45);
        isFre.setText("播量");
        //设置报警
        mSwitch = getView().findViewById(R.id.switch3);
        mSeekBar = getView().findViewById(R.id.seekBar3);
        standardNumber = getView().findViewById(R.id.textView18);
        alarmStatus = getView().findViewById(R.id.textView19);
        standardNumber.setText("亩播量:" + Constants.standardSeedNumberNow+"g");
        if(Constants.isAlarm){
            alarmStatus.setText("报警状态：开");
        }else {
            alarmStatus.setText("报警状态：关");
        }
        mSeekBar.setProgress(Constants.standardSeedNumberNow);
        mSwitch.setChecked(Constants.isAlarm);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Constants.standardSeedNumberNow = progress;
                standardNumber.setText("亩播量："+Constants.standardSeedNumberNow+"g");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constants.isAlarm = isChecked;
                if(Constants.isAlarm){
                    alarmStatus.setText("报警状态：开");
                    Toast.makeText(getContext(),"报警功能已打开",Toast.LENGTH_SHORT).show();
                }else {
                    alarmStatus.setText("报警状态：关");
                    Toast.makeText(getContext(), "报警功能已关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Alarm.initializeAlarm();
        //openAlarm(false);
        //设置地图
        mMapView = getView().findViewById(R.id.mapView7);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE );
        seed_fre = getView().findViewById(R.id.textView13);
        seed_amount = getView().findViewById(R.id.textView14);
        dongjing = getView().findViewById(R.id.textView17);
        beiwei = getView().findViewById(R.id.textView16);
        mButton_pingmian = getView().findViewById(R.id.button10);
        mButton_weixing = getView().findViewById(R.id.button11);
        //设置柱状图
        columnChartView = getView().findViewById(R.id.ColumnChart3);
        columnChartView.setOnValueTouchListener(new ComboLineColumnChartOnValueSelectListener() {
            @Override
            public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                CreateTimeHelloChart.tongdao = columnIndex + 1;
                CreateTimeHelloChart.isFrequency = isFrequency;
                //timer.cancel();
                TimeObserve.timer.cancel();
                //timer.cancel();
                isFirst = true;
                Alarm.openAlarm(false);
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_currentChartMap_to_currentChart);
            }

            @Override
            public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value) {

            }

            @Override
            public void onValueDeselected() {

            }
        });
        //设置地图缩放响应
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                zoomnumber = mapStatus.zoom;
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        beiwei.setText("北纬："+ new DecimalFormat("#.00000")
                                .format(Constants.sGetSeeds.getLatitude()));
                        dongjing.setText("东经："+ new DecimalFormat("#.00000")
                                .format(Constants.sGetSeeds.getLongitude()));
                        seed_fre.setText("播种频率（粒/秒）："+Constants.sGetSeeds.getRow_frequency());
                        seed_amount.setText("播种总量（粒）："+Constants.sGetSeeds.getRow_amount());
                }
            }
        };
        dongjing.setText("经度："+Constants.sGetSeeds.getLongitude());
        beiwei.setText("纬度："+Constants.sGetSeeds.getLatitude());
        seed_amount.setText("播种总量（粒）："+Constants.sGetSeeds.getRow_amount());
        seed_fre.setText("播种频率（粒/秒）："+Constants.sGetSeeds.getRow_frequency());
        mButton_pingmian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSatellite){
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    LatLng ll = Constants.points_current.get(Constants.points_current.size() - 1);
                    //LatLng ll = new LatLng();
                    MapStatusUpdate mUpdate = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(mUpdate);
                }else {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    LatLng ll = Constants.points_current.get(Constants.points_current.size() - 1);
                    //LatLng ll = new LatLng();
                    MapStatusUpdate mUpdate = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(mUpdate);
                }
                isSatellite = !isSatellite;
            }
        });
        mButton_weixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrequency = !isFrequency;
                if(isFrequency){
                    isFre.setText("频率");
                }else{
                    isFre.setText("播量");
                }
                if(isFrequency){
                    Viewport viewport = new Viewport();
                    viewport.left =  -0.5f;
                    viewport.right =  5.5f;
                    viewport.bottom = 0;
                    viewport.top = fre_max;
                    mComboLineColumnChartData.setAxisYLeft(axisY);
                    columnChartView.setCurrentViewport(viewport);
                }else {
                    Viewport viewport = new Viewport();
                    viewport.left = -0.5f;
                    viewport.right = 5.5f;
                    viewport.bottom = 0;
                    viewport.top = num_max;
                    mComboLineColumnChartData.setAxisYLeft(axisY);
                    columnChartView.setCurrentViewport(viewport);
                }
                //isFrequency = !isFrequency;
            }
        });
        initializeBaiduMap();
        timer = new Timer();
        isFirst = true;
        Log.d("判断值", isFirst+"");

        timer.schedule(new TimerTask() {
            Viewport viewport = new Viewport();
            //初始比较坐标
            LatLng latLng_last = new LatLng(5,5);
            //当前条和上一条的时间差
            int a = 0,b=1;
            @Override
            public void run() {
                if(isChartMapStop){
                    timer.cancel();
                }
                try {
                    Constants.sGetSeeds = OkHttp.TimeObserve(Constants.current_table_name);
                    //Constants.sGetSeeds = OkHttp.TimeObserve("table_20201220_430_1");
                    // 重置报警
                didididi = false;
                try {
                    viewport = columnChartView.getCurrentViewport();
                    if(!isFirst){
                        //获取并重设边界与之前一样
                        Log.d("定位", isFirst+"");
                        float left = viewport.left,right = viewport.right;
                        float bottom = viewport.bottom,top = viewport.top;
                        columns.clear();
                        setColumnChart();
                        if(isFrequency){
                            columnChartView.setCurrentViewport(new Viewport(left, fre_max, right, 0));
                        }else {
                            columnChartView.setCurrentViewport(new Viewport(left, num_max, right, 0));
                        }


                       // columnChartView.setCurrentViewport(new Viewport(left,top,right,bottom));
                        //设置百度地图
                        LatLng ll = new LatLng(Constants.sGetSeeds.getLatitude(),Constants.sGetSeeds.getLongitude());
                        if(0 < ll.latitude&&ll.latitude < 57
                                &&70<ll.longitude&&ll.longitude<140){
                            Constants.points_current.add(ll);
                            //计算该点最低播种频率合格值
                            double distance = DistanceUtil.getDistance(ll,latLng_last);
                            b = Integer.parseInt(Constants.sGetSeeds.getTime().substring(6,8));
                            if(b - a > 0){
                                Constants.speed = distance/(b - a);
                                Constants.setStandardNow = (float) (Constants.standardSeedNumberNow*15*Constants
                                        .speed*Constants.rowSpace/10/Constants.thousandSeed);
                            }else {
                                Constants.speed = distance/(60 + b - a);
                                Constants.setStandardNow = (float) (Constants.standardSeedNumberNow*15*Constants
                                        .speed*Constants.rowSpace/10/Constants.thousandSeed);
                            }

                            MyLocationData.Builder locationbuilder = new MyLocationData.Builder();
                            locationbuilder.latitude(ll.latitude);
                            locationbuilder.longitude(ll.longitude);
                            MyLocationData myLocationData = locationbuilder.build();
                            mBaiduMap.setMyLocationData(myLocationData);
                            Constants.points_current.add(ll);
                            OverlayOptions line = new PolylineOptions()
                                    .color(0xAA00FF00)
                                    .width(10)
                                    .points(Constants.points_current);
                            mBaiduMap.addOverlay(line);
                            //将当前值赋值给上一个
                            latLng_last = ll;
                            a = b;
                        }

                        handler.sendEmptyMessage(1);

                    }else{
                        columns.clear();
                        setColumnChart();
                        if(isFrequency){
                            columnChartView.setCurrentViewport(new Viewport(-0.5f, fre_max,5.5f, 0));
                        }else {
                            columnChartView.setCurrentViewport(new Viewport(-0.5f, num_max,5.5f, 0));
                        }

                        Log.d("定位刷新", "run: ");
                       // LatLng ll = new LatLng(Constants.sGetSeeds.getLatitude(),Constants.sGetSeeds.getLongitude());
                       // LatLng ll = new LatLng();
                        //设置地图
                        try {
                            Constants.points_current.clear();
                            List<GetSeeds> getSeeds = OkHttp.SelectTable(Constants.current_table_name);
                            for(GetSeeds getSeeds1:getSeeds){
                                if(0 < getSeeds1.getLatitude()&&getSeeds1.getLatitude() < 57
                                        &&70<getSeeds1.getLongitude()&&getSeeds1.getLongitude()<140){
                                    LatLng latLng = new LatLng(getSeeds1.getLatitude(),getSeeds1.getLongitude());
                                    Constants.points_current.add(latLng);

                                }
                            }
                            if(Constants.points_current.size()>=2){
                                //定位到相应点，并以该点为中心
                                LatLng ll = Constants.points_current.get(Constants.points_current.size() - 1);
                                MapStatusUpdate mUpdate = MapStatusUpdateFactory.newLatLng(ll);
                                mBaiduMap.animateMapStatus(mUpdate);
                                mUpdate = MapStatusUpdateFactory.zoomTo(zoomnumber);
                                mBaiduMap.animateMapStatus(mUpdate);
                                Constants.points_current.add(ll);
                                LatLng latLng = Constants.points_current.get(0);
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                                        .fromResource(R.drawable.icon_st);
                                OverlayOptions options = new MarkerOptions()
                                        .position(latLng)
                                        .icon(bitmapDescriptor);
                                mBaiduMap.addOverlay(options);

                                MyLocationData.Builder locationbuilder = new MyLocationData.Builder();
                                locationbuilder.latitude(ll.latitude);
                                locationbuilder.longitude(ll.longitude);
                                MyLocationData myLocationData = locationbuilder.build();
                                mBaiduMap.setMyLocationData(myLocationData);
                                Constants.points_current.add(ll);
                                OverlayOptions line = new PolylineOptions()
                                        .color(0xAA00FF00)
                                        .width(10)
                                        .points(Constants.points_current);
                                mBaiduMap.addOverlay(line);
                                handler.sendEmptyMessage(1);
                                isFirst = false;
                            }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                    //Constants.sGetSeeds = OkHttp.TimeObserve(Constants.current_table_name);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        },0,1000);

    }

    public void initializeBaiduMap(){
        dongjing.setText("经度："+Constants.sGetSeeds.getLongitude());
        beiwei.setText("纬度："+Constants.sGetSeeds.getLatitude());
        seed_amount.setText("播种总量（粒）："+Constants.sGetSeeds.getRow_amount());
        seed_fre.setText("播种频率（粒/秒）："+Constants.sGetSeeds.getRow_frequency());
        mLocationClient = new LocationClient(GetApplicationContext.getContext());
       // mBaiduMap.setMyLocationEnabled(true);

        //定位到相应视图
        LatLng latLng = new LatLng(Constants.sGetSeeds.getLatitude(),Constants.sGetSeeds.getLongitude());
        MapStatusUpdate mUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(mUpdate);
        mUpdate = MapStatusUpdateFactory.zoomTo(16f);
        mBaiduMap.animateMapStatus(mUpdate);

        //显示定位点
        MyLocationData.Builder locationbuilder = new MyLocationData.Builder();
        locationbuilder.latitude(latLng.latitude);
        locationbuilder.longitude(latLng.longitude);
        MyLocationData myLocationData = locationbuilder.build();
        mBaiduMap.setMyLocationData(myLocationData);


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void setColumnChart() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int numSubColumns = 1;
        int numColumns = 12;
        //设置x轴，y轴的值

        List<AxisValue> axisValues = new ArrayList<>();
        List<SubcolumnValue> fre_subcolumnValues = new ArrayList<>();
        List<SubcolumnValue> num_subcolumnValues = new ArrayList<>();
        //设置底部坐标轴
        List<String> titles = new ArrayList<>();
        for(int i = 1; i <= numColumns; i++){
            titles.add(i+"");
        }
        //利用反射，将获取来的解析过的json值根据通道数进行分配，执行不同的get
        Class c = Class.forName("com.example.iotobserve.okhttp.GetSeeds");
        float value = 0;
        int numLength = String.valueOf(c.getMethod("getRow_amount").invoke(Constants.sGetSeeds)).length();
        for(int i = 0; i < numColumns; i++){
            Method method_fre = c.getMethod("getRow_frequency"+String.valueOf(i+1));
            Method method_num = c.getMethod("getRow_amount"+String.valueOf(i+1));
            axisValues.add(new AxisValue(i).setLabel(titles.get(i)));
            fre_subcolumnValues = new ArrayList<>();
            num_subcolumnValues = new ArrayList<>();
            for(int j = 0; j < numSubColumns; j++){
                //columns_fre.add(new PointValue(i,(float)method_fre.invoke(Constants.sGetSeeds)));
                if(isFrequency){
                    if((float)method_fre.invoke(Constants.sGetSeeds) < Constants.setStandardNow){
                        num_subcolumnValues.add(new SubcolumnValue((float)method_fre.invoke(Constants.sGetSeeds),Color.parseColor("#FF0000")));
                        didididi = true;
                    }else {
                        num_subcolumnValues.add(new SubcolumnValue((float)method_fre.invoke(Constants.sGetSeeds),Color.parseColor("#ADADAD")));
                    }

                }else{
                    if((float)method_fre.invoke(Constants.sGetSeeds) < Constants.setStandardNow){
                        num_subcolumnValues.add(new SubcolumnValue((int)method_num.invoke(Constants.sGetSeeds),Color.parseColor("#FF0000")));
                        didididi = true;
                    }else {
                        num_subcolumnValues.add(new SubcolumnValue((int)method_num.invoke(Constants.sGetSeeds),Color.parseColor("#ADADAD")));
                    }


                }

                if(fre_max < (float)method_fre.invoke(Constants.sGetSeeds)){
                    float n;
                    n = (float)method_fre.invoke(Constants.sGetSeeds);
                    fre_max = (int)n;
                }
                if(num_max < (int)method_num.invoke(Constants.sGetSeeds)){
                    num_max = (int)method_num.invoke(Constants.sGetSeeds);
                }
            }

            //PointValue column_fre = new PointValue(fre_subcolumnValues);
            Column column_num = new Column(num_subcolumnValues);
           // column_fre.setHasLabels(true);
            column_num.setHasLabels(true);
           // columns_fre.add(column_fre);
            columns.add(column_num);
        }
        fre_max += 10;
        num_max += 10;
        //columnChartData_fre.setStacked(true);
        columnChartData_num = new ColumnChartData(columns);
       // columnChartData_num.setStacked(true);
        //设置x轴，y轴
        axisX = new Axis(axisValues).setHasLines(true);
        axisY = new Axis().setHasLines(true);
        axisY1 = new Axis().setHasLines(true);
        List<AxisValue> values = new ArrayList<>();
        for(int i = 0; i < 100; i = i + 10){
            AxisValue axisValue = new AxisValue(i);
            String lable = i+"";
            axisValue.setLabel(lable);
            values.add(axisValue);
        }
        axisY1.setValues(values);
        axisY1.setMaxLabelChars(6);
        axisX.setName("通道");
        if(isFrequency){
            axisY.setName("播种频率");
        }else {
            axisY.setName("播种总量");
        }

        axisY1.setName("播种频率");
        axisX.setTextColor(Color.parseColor("#000000"));
        axisY.setTextColor(Color.parseColor("#000000"));
        axisY1.setTextColor(Color.parseColor("#000000"));
        axisX.setTextSize(15);
        axisY.setTextSize(15);
        axisY1.setTextColor(15);
        axisY.setMaxLabelChars(numLength);
        columnChartData_num.setAxisYLeft(axisY);
        columnChartData_num.setAxisXBottom(axisX);

        //是否报警提示
        if(Constants.isAlarm){
            if(didididi){
                Alarm.openAlarm(true);
            }else {
                Alarm.openAlarm(false);
                //initializeAlarm();
            }
        }else {
            Alarm.openAlarm(false);
            //initializeAlarm();
        }
        //设置柱状图的数据
        mComboLineColumnChartData = new ComboLineColumnChartData();
       // mComboLineColumnChartData.setLineChartData(columnChartData_fre);
        mComboLineColumnChartData.setColumnChartData(columnChartData_num);
        mComboLineColumnChartData.setAxisXBottom(axisX);
        mComboLineColumnChartData.setAxisYLeft(axisY);

        //mComboLineColumnChartData.setAxisYRight(axisY1);
        columnChartView.setComboLineColumnChartData(mComboLineColumnChartData);
        //columnChartView.setColumnChartData(columnChartData_num);

       //设置柱状图的各项属性
       columnChartView.setZoomType(ZoomType.HORIZONTAL);
       columnChartView.setEnabled(true);
       columnChartView.setInteractive(true);
       columnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
       columnChartView.setMaxZoom(1000000);
       columnChartView.setVisibility(View.VISIBLE);

    }

}