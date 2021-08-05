package com.example.iotobserve;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.baidu.location.LocationClient;
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
import com.baidu.mapapi.utils.AreaUtil;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.iotobserve.okhttp.GetSeeds;
import com.example.iotobserve.okhttp.OkHttp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryChartMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryChartMap extends Fragment {
    //设置图表的相关参数
    private ComboLineColumnChartView comboLineColumnChartView;
    private ComboLineColumnChartData comboLineColumnChartData;
    private ColumnChartData mColumnChartData;
    private Boolean isFrequency = false;
    private List<Column> mColumns;
    private int fre_max = 0,num_max = 0;
    //private boolean isFre = false;
    private boolean isStatelite = false;
    private Axis axisx,axisy;
    private TextView isFre;
    //设置百度地图相关参数
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private TextView longitude,latitude,fre,num;
    private Button button_pingmian,button_satellite;
    //设置合格指标
    private SeekBar seekBar;
    private Switch mSwitch;
    private TextView standardText,missRate;
    public HistoryChartMap() {
        // Required empty public constructor
    }


    public static HistoryChartMap newInstance(String param1, String param2) {
        HistoryChartMap fragment = new HistoryChartMap();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_chart_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //清除之前的值
        Constants.points.clear();
        //设置漏播标准相关
        isFre = getView().findViewById(R.id.textView43);
        standardText = getView().findViewById(R.id.textView18);
        missRate = getView().findViewById(R.id.textView19);
        seekBar = getView().findViewById(R.id.seekBar3);
        mSwitch = getView().findViewById(R.id.switch3);
        seekBar.setVisibility(View.INVISIBLE);
        mSwitch.setVisibility(View.INVISIBLE);
        if(Constants.isRoute){
            mSwitch.setChecked(true);
            seekBar.setMax(Constants.history_seeds_collection.size()-1);
            seekBar.setProgress(Constants.positionHistory);
            standardText.setText("进度值："+Constants.positionHistory);

        }else {
            mSwitch.setChecked(false);
            seekBar.setMax(1000);
            seekBar.setProgress(Constants.standardSeedNumberHis);
            standardText.setText("亩播量："+ Constants.standardSeedNumberHis + "g");

        }
        standardText.setText("亩播量："+ Constants.standardSeedNumberHis+"g");
        missRate.setText("达标率："+ new DecimalFormat("#.00").format(100 - Constants.missRate*100) + "%");
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constants.isRoute = isChecked;
                if(Constants.isRoute){
                    mSwitch.setChecked(true);
                    int a = Constants.positionHistory;
                    seekBar.setMax(Constants.history_seeds_collection.size()-1);
                    Constants.positionHistory = a;
                    Log.d("第几个", Constants.positionHistory+"");
                    seekBar.setProgress(Constants.positionHistory);
                    standardText.setText("进度值："+Constants.positionHistory);

                }else {
                    mSwitch.setChecked(false);
                    int a = Constants.standardSeedNumberHis;
                    seekBar.setMax(1000);
                    Constants.standardSeedNumberHis = a;
                    seekBar.setProgress(Constants.standardSeedNumberHis);
                    standardText.setText("亩播量："+ Constants.standardSeedNumberHis + "g");
                }

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(Constants.isRoute){
                    standardText.setText("进度值：" + progress);
                    Constants.selectLocation = new LatLng(
                            Constants.history_seeds_collection.get(progress).getLatitude(),
                            Constants.history_seeds_collection.get(progress).getLongitude());
                   // LatLng latLng = new LatLng(Constants.his_lat,Constants.his_lon);
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(Constants.selectLocation);
                   // mBaiduMap.animateMapStatus(update);
                    if(isNormal(Constants.selectLocation)){
                        MapStatusUpdate update1 = MapStatusUpdateFactory.zoomTo(20f);
                        mBaiduMap.animateMapStatus(update1);
                        MyLocationData.Builder locationbuilder = new MyLocationData.Builder();
                        locationbuilder.latitude(Constants.selectLocation.latitude);
                        locationbuilder.longitude(Constants.selectLocation.longitude);
                        MyLocationData myLocationData = locationbuilder.build();
                        mBaiduMap.setMyLocationData(myLocationData);
                    }

                    Constants.positionHistory = progress;
                    Log.d("已经监听过", "onProgressChanged: ");
                    //设置地图上的文字
                    if(isNormal(Constants.selectLocation)){
                        longitude.setText("经度："+ new DecimalFormat("#.00000")
                                .format(Constants.history_seeds_collection.get(Constants.positionHistory).getLongitude()));
                        latitude.setText("纬度：" + new DecimalFormat("#.00000")
                                .format(Constants.history_seeds_collection.get(Constants.positionHistory).getLatitude()));
                    }

                    fre.setText("播种频率（粒/秒）" + Constants.history_seeds_collection.get(Constants.positionHistory)
                            .getRow_frequency());
                    num.setText("播种总量（粒）：" + Constants.history_seeds_collection.get(Constants.positionHistory)
                            .getRow_amount());
                    //设置柱状图的变化
                    Constants.last_HistoryColumn = Constants.history_seeds_collection.get(Constants.positionHistory);
                    try {
                        setComboLineColumnChartView();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }else {
                    Constants.standardSeedNumberHis = progress;
                    standardText.setText("亩播量："+ Constants.standardSeedNumberHis + "g");
                    try {
                        Log.d("当前速度", Constants.standardSeedNumberHis + "");
                        initializeBaiduMap();
                        setComboLineColumnChartView();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if(114 - Constants.missRate*100>=100){
                        missRate.setText("达标率："+"100%");
                    }else{
                        missRate.setText("达标率："+new DecimalFormat("#.00").format(114 - Constants.missRate*100) +"%");
                    }


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(!Constants.isRoute){

                }
            }
        });

        //百度地图
        mMapView = getView().findViewById(R.id.mapView7);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        longitude = getView().findViewById(R.id.textView17);
        latitude = getView().findViewById(R.id.textView16);
        fre = getView().findViewById(R.id.textView13);
        num = getView().findViewById(R.id.textView14);
        button_pingmian = getView().findViewById(R.id.button10);
        button_satellite = getView().findViewById(R.id.button11);
        //设置按键
        button_pingmian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStatelite){
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                }else {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                }
                isStatelite = !isStatelite;
            }
        });
        button_satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               isFrequency = !isFrequency;
               if(isFrequency){
                   isFre.setText("频率");
               }else {
                   isFre.setText("播量");
               }
                try {
                    setComboLineColumnChartView();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });


        //柱图表
        comboLineColumnChartView = getView().findViewById(R.id.ColumnChart3);
        comboLineColumnChartView.setOnValueTouchListener(new ComboLineColumnChartOnValueSelectListener() {
            @Override
            public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                /*
                if(isFrequency){
                    Viewport viewport = new Viewport();
                    viewport.left = -0.5f;
                    viewport.right = 11.5f;
                    viewport.top = fre_max;
                    viewport.bottom = 0;
                    axisy.setName("播种频率");
                    comboLineColumnChartView.setCurrentViewport(viewport);
                }else {
                    Viewport viewport = new Viewport();
                    viewport.left = -0.5f;
                    viewport.right = 11.5f;
                    viewport.top = num_max;
                    viewport.bottom = 0;
                    axisy.setName("播种总量");
                    comboLineColumnChartView.setCurrentViewport(viewport);
                }
                 */
                Constants.history_isFrequency = isFrequency;
                Constants.history_tongdao = columnIndex + 1;
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_historyChartMap_to_historyChart);
            }

            @Override
            public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value) {

            }

            @Override
            public void onValueDeselected() {

            }
        });


        //开启线程执行网络请求获得柱状图的最后一条数据
        class ChartTask extends AsyncTask<String,String,Integer>{
            @Override
            protected Integer doInBackground(String... params) {
                try {
                    Constants.last_HistoryColumn = OkHttp.TimeObserve(Constants.select_table_name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                try {
                    setComboLineColumnChartView();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


            }
        }


        //开启线程执行网络请求获得所需数据表的全部数据以加载百度地图
        class MapTask extends AsyncTask<String,Integer,String>{
            @Override
            protected String doInBackground(String... strings) {
                try {
                    Constants.history_seeds_collection = OkHttp.SelectTable(Constants.select_table_name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                try {

                    if(Constants.history_seeds_collection.size() >= 1){
                        Constants.positionHistory = Constants.history_seeds_collection.size()-1;
                        if(Constants.isRoute){
                            seekBar.setProgress(Constants.positionHistory);
                        }
                        boolean isXianShi = false;
                        int a = 0;
                        int j = 0;
                        while(true){
                            LatLng ll = new LatLng(Constants.history_seeds_collection.get(a).getLatitude(),Constants.history_seeds_collection.get(a).getLongitude());
                            if(isNormal(ll)){
                                j++;
                            }
                            Log.d("j的值", "合格点"+j + "个数"+a);
                            if(j>=2){
                                isXianShi = true;
                                break;
                            }
                            if(a >= Constants.history_seeds_collection.size()-1){
                                break;
                            }
                            a++;
                        }
                        Log.d("执行后第几个", Constants.positionHistory+"");
                        longitude.setText("经度："+ new DecimalFormat("#.00000")
                                .format(Constants.history_seeds_collection.get(Constants.positionHistory).getLongitude()));
                        latitude.setText("纬度：" + new DecimalFormat("#.00000")
                                .format(Constants.history_seeds_collection.get(Constants.positionHistory).getLatitude()));
                        fre.setText("播种频率（粒/秒）" + Constants.history_seeds_collection.get(Constants.positionHistory)
                                .getRow_frequency());
                        num.setText("播种总量（粒）：" + Constants.history_seeds_collection.get(Constants.positionHistory)
                                .getRow_amount());
                        // Log.d("漏播率：", (int)Constants.missRate*100+"%"+Constants.missRate*100);
                        if(isXianShi){
                            seekBar.setVisibility(View.VISIBLE);
                            mSwitch.setVisibility(View.VISIBLE);
                            initializeBaiduMap();
                            missRate.setText("达标率：" + new DecimalFormat("#.00")
                                    .format(110 - Constants.missRate*100) +"%");
                        }

                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        MapTask mapTask = new MapTask();
        mapTask.execute();
        ChartTask chartTask = new ChartTask();
        chartTask.execute();
    }


    private void initializeBaiduMap() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //定位
        mLocationClient = new LocationClient(GetApplicationContext.getContext());
        mBaiduMap.setMyLocationEnabled(true);
        //获取不合格历史数据点
        HashMap<Integer,LatLng> bad = new HashMap<Integer,LatLng>();
        List<Integer> bad_number = new ArrayList<>();
        //合理绘图点
        List<LatLng> latLngs = new ArrayList<>();
        //反射求每个通道频率
        Class command = Class.forName("com.example.iotobserve.okhttp.GetSeeds");
        //获取历史数据表的点
        Constants.standardHis.clear();

        for(int j = 1; j < Constants.history_seeds_collection.size(); j++){
            GetSeeds getSeed = Constants.history_seeds_collection.get(j);
            GetSeeds getSeed_last = Constants.history_seeds_collection.get(j-1);
            LatLng latLng_last = new LatLng(getSeed_last.getLatitude(),getSeed_last.getLongitude());
            LatLng latLng1 = new LatLng(getSeed.getLatitude(),getSeed.getLongitude());
            if(j == 1){
                Constants.points.add(latLng_last);
            }
            Constants.points.add(latLng1);
            //判断定位点是否是合理点
            if(isNormal(latLng1)){
                latLngs.add(latLng1);
            }
            //计算每个点的最低播种频率合格阈值
            double distance = DistanceUtil.getDistance(latLng1,latLng_last);
            int a = Integer.parseInt(getSeed_last.getTime().substring(6,8));
            int b = Integer.parseInt(getSeed.getTime().substring(6,8));
            if(b - a > 0){
                Constants.speed = distance/(b - a);

                Constants.setStandardHis = (float) (Constants.standardSeedNumberHis*15*Constants
                        .speed*Constants.rowSpace/10/Constants.thousandSeed);
                Log.d("速度0", Constants.standardSeedNumberHis + "xxx" + Constants.speed + "xxx" + Constants.rowSpace);
                Log.d("速度1", Constants.standardSeedNumberHis*15*
                        Constants.speed*Constants.rowSpace/10/Constants.thousandSeed+"");
            }else {
                Constants.speed = distance/(60 + b - a);
                Constants.setStandardHis = (float) (Constants.standardSeedNumberHis*15*Constants
                        .speed*Constants.rowSpace/10/Constants.thousandSeed);
            }
            if(j == 1){
                Constants.standardHis.add(Constants.setStandardHis);
            }
            Log.d("速度", Constants.setStandardHis+"");
            Constants.standardHis.add(Constants.setStandardHis);
            Log.d("播种轨迹长度", Constants.points.size()+"");
            for(int i = 1; i <= getSeed.getMax_tongdao(); i++){
                Method method = command.getMethod("getRow_frequency"+i);
                float fre  = (float)method.invoke(getSeed);
                if(fre < Constants.setStandardHis){
                    if(isNormal(latLng1)){
                        bad.put(getSeed.getRapeSeed_id(),latLng1);
                        bad_number.add(getSeed.getRapeSeed_id());
                        Log.d("不合格个数", bad_number.size()+"");
                        Log.d("不合格种子", fre+"");
                        Log.d("当前i的值为", i+"");
                    }
                    break;
                }
            }
        }
        /*
        for(GetSeeds getSeed:Constants.history_seeds_collection){
            LatLng latLng1 = new LatLng(getSeed.getLatitude(),getSeed.getLongitude());
            Constants.points.add(latLng1);
            //判断定位点是否是合理点
            if(isNormal(latLng1)){
                latLngs.add(latLng1);
            }

            Log.d("播种轨迹长度", Constants.points.size()+"");
            for(int i = 1; i <= getSeed.getMax_tongdao(); i++){
                Method method = command.getMethod("getRow_frequency"+i);
                float fre  = (float)method.invoke(getSeed);
                if(fre < Constants.standardSeedNumberHis){
                    if(isNormal(latLng1)){
                        bad.put(getSeed.getRapeSeed_id(),latLng1);
                        bad_number.add(getSeed.getRapeSeed_id());
                        Log.d("不合格个数", bad_number.size()+"");
                        Log.d("不合格种子", fre+"");
                        Log.d("当前i的值为", i+"");
                    }
                    break;
                }
            }
        }

         */
        //计算不合格值
        Constants.missRate =(bad_number.size()/(float)Constants.history_seeds_collection.size());
        Log.d("不合格率",Constants.missRate+"+"+bad_number.size()+"+"+Constants.history_seeds_collection.size());
        Log.d("合格率：", new DecimalFormat("#.00").format(110 - Constants.missRate*100)+"%");

        //获取起始点与终点的中点
        Constants.his_lat = (latLngs.get(0).latitude
                + latLngs.get(latLngs.size()-1).latitude)/2;
        Constants.his_lon = (latLngs.get(0).longitude
                + latLngs.get(latLngs.size()-1).longitude)/2;
        //定位到相应视图
        LatLng latLng = new LatLng(Constants.his_lat,Constants.his_lon);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(20f);
        mBaiduMap.animateMapStatus(update);

        //绘制总体轨迹
        mMapView.getMap().clear();
        OverlayOptions guidelines = new PolylineOptions()
                .color(0xAA00FF00)
                .width(10)
                .points(latLngs);
        mBaiduMap.addOverlay(guidelines);
        Log.d("坐标点个数", latLngs.size()+"和"+Constants.points.size());
        BitmapDescriptor startBD = new BitmapDescriptorFactory()
                .fromResource(R.drawable.icon_st);
        BitmapDescriptor endBD = new BitmapDescriptorFactory()
                .fromResource(R.drawable.icon_en);
        OverlayOptions maka_start = new MarkerOptions()
                .position(latLngs.get(0))
                .icon(startBD);
        OverlayOptions maka_end = new MarkerOptions()
                .position(latLngs.get(latLngs.size()-1))
                .icon(endBD);
        mBaiduMap.addOverlay(maka_start);
        mBaiduMap.addOverlay(maka_end);
        //标记最后一个点为中心

        int i = Constants.positionHistory;
        Log.d("第第第", Constants.points.size() + "第" + "i:"+i+"position:"+Constants.positionHistory);
        while(!isNormal(Constants.points.get(i))){
            i = i + 1;
            if(i >= Constants.points.size()){
                i = Constants.positionHistory;
                while (!isNormal(Constants.points.get(i))){
                    i = i - 1;
                }
                break;
            }
        }
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(Constants.points.get(i).latitude);
        builder.longitude(Constants.points.get(i).longitude);
        MyLocationData myLocationData = builder.build();
        mBaiduMap.setMyLocationData(myLocationData);

        //绘制不合格轨迹
        List<List<LatLng>> split = new ArrayList<List<LatLng>>();
        List<LatLng> split1 = new ArrayList<>();
        for(int j = 0; j < bad_number.size()-1;j++){
            if(bad_number.get(j+1) - bad_number.get(j) == 1){
                split1.add(bad.get(bad_number.get(j)));
                Log.d("轨迹", bad_number.get(j)+"");
            }else {
                split1.add(bad.get(bad_number.get(j)));
                split.add(split1);
                split1 = new ArrayList<>();
                Log.d("不合格轨迹", bad_number.get(j)+"频率"+bad.get(bad_number.get(j)));
            }
        }

        if(!split1.isEmpty()){
            split.add(split1);
        }
        Log.d("共几条轨迹", split.size()+"");
        for(int m = 0; m < split.size(); m++){
            if(split.get(m).size() >= 2){
                OverlayOptions bad_line = new PolylineOptions()
                        .color(0xAAFF0000)
                        .width(10)
                        .points(split.get(m));
                mBaiduMap.addOverlay(bad_line);
            }

            Log.d("第"+m+1+"条轨迹", String.valueOf(split.get(m)));
            Log.d("播种轨迹长度", Constants.points.size()+"+"+split.get(0).size());
        }
        Constants.points.clear();
    }

    public void setComboLineColumnChartView() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int numSubColumns = 1;
        int numColumns = 12;
        //设置x轴y轴的值
        List<AxisValue> axisValues = new ArrayList<>();
        List<SubcolumnValue> subcolumnValues;
        mColumns = new ArrayList<>();
        //设置x轴的具体值
        List<String> titles = new ArrayList<>();
        for(int i = 1; i <= numColumns; i++){
            titles.add(i+"");
        }
        Log.d("合格阈值", "合格阈值"+Constants.standardHis.get(Constants.positionHistory));
        //利用反射，将获取来的json数据按照不同通道进行分配
        Class command = Class.forName("com.example.iotobserve.okhttp.GetSeeds");
        for(int i = 0; i < numColumns; i++){
            Method method_fre = command.getMethod("getRow_frequency"+String.valueOf(i+1));
            Method method_num = command.getMethod("getRow_amount"+String.valueOf(i+1));
            axisValues.add(new AxisValue(i).setLabel(titles.get(i)));
            //设定
            subcolumnValues = new ArrayList<>();
            for(int j = 0; j < numSubColumns; j ++){
                if(isFrequency){
                    if((float)method_fre.invoke(Constants.last_HistoryColumn) < Constants.standardHis.get(Constants.positionHistory)){
                        subcolumnValues.add(new SubcolumnValue((float) method_fre.invoke(Constants.last_HistoryColumn), Color.parseColor("#FF0000")));
                    }else{
                        subcolumnValues.add(new SubcolumnValue((float) method_fre.invoke(Constants.last_HistoryColumn), Color.parseColor("#ADADAD")));
                    }
                }else {
                    if((float)method_fre.invoke(Constants.last_HistoryColumn) < Constants.standardHis.get(Constants.positionHistory)){
                        subcolumnValues.add(new SubcolumnValue((int)method_num.invoke(Constants.last_HistoryColumn),Color.parseColor("#FF0000")));
                    }else {
                        subcolumnValues.add(new SubcolumnValue((int)method_num.invoke(Constants.last_HistoryColumn),Color.parseColor("#ADADAD")));
                    }
                }
                if(fre_max < (float)method_fre.invoke(Constants.last_HistoryColumn)){
                    float n = (float) method_fre.invoke(Constants.last_HistoryColumn);
                    fre_max = (int) n;
                }
                if(num_max < (int)method_num.invoke(Constants.last_HistoryColumn)){
                    num_max = (int)method_num.invoke(Constants.last_HistoryColumn);
                }

            }
            Column column = new Column(subcolumnValues);
            column.setHasLabels(true);
            mColumns.add(column);
        }
        fre_max += 10;
        num_max += 10;
        mColumnChartData = new ColumnChartData(mColumns);
        //设置x轴，y轴
        axisx = new Axis(axisValues).setHasLines(true);
        axisy = new Axis().setHasLines(true);
        axisx.setName("通道");
        if(isFrequency){
            axisy.setName("播种频率");
        }else {
            axisy.setName("播种总量");
        }
        axisx.setTextColor(Color.parseColor("#000000"));
        axisy.setTextColor(Color.parseColor("#000000"));
        axisx.setTextSize(15);
        axisy.setTextSize(15);

        mColumnChartData.setAxisYLeft(axisy);
        mColumnChartData.setAxisXBottom(axisx);
        //设置柱状图的数据
        comboLineColumnChartData = new ComboLineColumnChartData();
        comboLineColumnChartData.setColumnChartData(mColumnChartData);
        comboLineColumnChartData.setAxisYLeft(axisy);
        comboLineColumnChartData.setAxisXBottom(axisx);
        comboLineColumnChartView.setComboLineColumnChartData(comboLineColumnChartData);
        //设置柱状图的各项属性
        comboLineColumnChartView.setZoomType(ZoomType.HORIZONTAL);
        comboLineColumnChartView.setEnabled(true);
        comboLineColumnChartView.setInteractive(true);
        comboLineColumnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        comboLineColumnChartView.setMaxZoom(1000000);
        comboLineColumnChartView.setVisibility(View.VISIBLE);

    }
    public boolean isNormal(LatLng latLng){
        if(0 < latLng.latitude&&latLng.latitude < 57
                &&70<latLng.longitude&&latLng.latitude<140){
           return true;
        }
        return false;
    }
}