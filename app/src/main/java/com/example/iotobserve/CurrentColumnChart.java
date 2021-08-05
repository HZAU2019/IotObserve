package com.example.iotobserve;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iotobserve.okhttp.OkHttp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentColumnChart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentColumnChart extends Fragment {
    ColumnChartView viewColumnChart_fre,viewColumnChart_num;
    ColumnChartData dataColumnChart_fre,dataColumnChart_num;
    List<Column> mColumns_fre = new ArrayList<>();
    List<Column> mColumns_num = new ArrayList<>();
    public static Boolean isColumnStop = false;
    Boolean isFirst = true;
    int newLeft = 7,newRight = 13,oldLeft = 0,oldRight = 6;
    float zoomLevel = 3;
    public CurrentColumnChart() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CurrentColumnChart newInstance(String param1, String param2) {
        CurrentColumnChart fragment = new CurrentColumnChart();
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
                if(event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                    isColumnStop = true;

                }
                return false;

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_column_chart, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isColumnStop = false;
        viewColumnChart_fre = getView().findViewById(R.id.ColumnChartView2);
        viewColumnChart_num = getView().findViewById(R.id.ColumnChartView);
        viewColumnChart_fre.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                if(columnIndex >= 1){
                    CreateTimeHelloChart.tongdao = columnIndex-1;
                    CreateTimeHelloChart.isFrequency = true;
                    isColumnStop = true;
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_currentChartMap_to_currentChart);
                }

            }

            @Override
            public void onValueDeselected() {

            }
        });
        viewColumnChart_num.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                if(columnIndex >= 1){
                    CreateTimeHelloChart.tongdao = columnIndex-1;
                    CreateTimeHelloChart.isFrequency = false;
                    isColumnStop = true;
                    NavController navController = Navigation.findNavController(getView());
                   // navController.navigate(R.id.action_currentColumnChart_to_currentChart);
                }

            }

            @Override
            public void onValueDeselected() {

            }
        });


        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            Viewport viewport_fre = new Viewport();
            Viewport viewport_num = new Viewport();
            @Override
            public void run() {
                viewport_fre = viewColumnChart_fre.getCurrentViewport();
                viewport_num = viewColumnChart_num.getCurrentViewport();
                if(isColumnStop){
                    timer.cancel();
                }
                try {
                    if(!isFirst){
                        Log.d("左右", viewport_fre.left+"+"+ viewport_fre.right+"+"+ viewport_fre.bottom+"+"+ viewport_fre.top);
                        float left_fre = viewport_fre.left,left_num = viewport_num.left;
                        float right_fre = viewport_fre.right,right_num = viewport_num.right;
                        float bottom_fre = viewport_fre.bottom,bottom_num = viewport_num.bottom;
                        float top_fre = viewport_fre.top,top_num = viewport_num.top;

                        Constants.sGetSeeds = OkHttp.TimeObserve(Constants.current_table_name);
                        mColumns_fre.clear();
                        mColumns_num.clear();
                        setViewColumnChart_num();
                        setViewColumnChart_fre();
                        viewColumnChart_fre.setCurrentViewport(new Viewport(left_fre,top_fre,right_fre,bottom_fre));
                        viewColumnChart_num.setCurrentViewport(new Viewport(left_num,top_num,right_num,bottom_num));
                        Log.d("左右12", viewport_fre.left+"+"+ viewport_fre.right+"+"+ viewport_fre.bottom+"+"+ viewport_fre.top);

                    }else {
                        Constants.sGetSeeds = OkHttp.TimeObserve(Constants.current_table_name);
                        mColumns_fre.clear();
                        mColumns_num.clear();
                        setViewColumnChart_fre();
                        setViewColumnChart_num();

                        isFirst = false;

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        },0,1000);

    }
    private void setViewColumnChart_fre() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int numSubColumns = 1;
        int numColumns = 14;
        //设置x轴，y轴的值
        List<AxisValue> axisValues = new ArrayList<>();
        List<SubcolumnValue> values = new ArrayList<>();
       // List<Column> columns = new ArrayList<>();
        //添加底部坐标轴的值
        List<String> titles = new ArrayList<>();
        titles.add("通道数");
        titles.add("总量");
        for(int i = 2;i < 14;i++){
            titles.add(String.valueOf(i-1));
        }
        //利用反射，将获取回来的json解析值放到图标中对应的列
        Class c = Class.forName("com.example.iotobserve.okhttp.GetSeeds");
        float value = 0;
        int numLength = 2;
        for(int i = 0; i < numColumns; i++){
            if(i > 1){
                Method method = c.getMethod("getRow_frequency" + String.valueOf(i-1));
                value = (float) method.invoke(Constants.sGetSeeds);
            }else {
                if(i == 1){
                    Method method = c.getMethod("getRow_frequency");
                    value = (float) method.invoke(Constants.sGetSeeds);
                    numLength = String.valueOf(value).length();
                }else {
                    Method method = c.getMethod("getMax_tongdao");
                    value = (int) method.invoke(Constants.sGetSeeds);
                }

            }
            axisValues.add(new AxisValue(i).setLabel(titles.get(i)));
            values = new ArrayList<>();
            for(int j = 0; j < numSubColumns; j++){
                values.add(new SubcolumnValue(value, ChartUtils.pickColor()));
            }
            Column column = new Column(values);
            column.setHasLabels(true);
            mColumns_fre.add(column);
        }
        dataColumnChart_fre = new ColumnChartData(mColumns_fre);

        Axis axisX = new Axis(axisValues).setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("通道");
        axisY.setName("播种频率");
        axisY.setMaxLabelChars(numLength);
        dataColumnChart_fre.setAxisYLeft(axisY);
        dataColumnChart_fre.setAxisXBottom(axisX);
        //设置柱状图动画属性
        viewColumnChart_fre.setColumnChartData(dataColumnChart_fre);
        viewColumnChart_fre.setZoomType(ZoomType.HORIZONTAL);
        viewColumnChart_fre.setZoomEnabled(true);
        viewColumnChart_fre.setInteractive(true);
        viewColumnChart_fre.setMaxZoom(30000);
        viewColumnChart_fre.setScrollEnabled(true);
        viewColumnChart_fre.setScrollContainer(true);
        viewColumnChart_fre.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        viewColumnChart_fre.setVisibility(View.VISIBLE);

        Log.d("版号", Build.VERSION.SDK_INT+"  "+Build.VERSION_CODES.M);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("戴师兄", "有变化");
            viewColumnChart_fre.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    Log.d("戴师兄是个狗", "有变化");
                    newLeft = scrollX;
                    newRight = scrollY;
                }
            });
        }
    }
    private void setViewColumnChart_num() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int numSubColumn = 1;
        int numColumn = 14;
        //设置x轴，y轴的值
        List<AxisValue> axisValues = new ArrayList<>();
        List<SubcolumnValue> values = new ArrayList<>();
        //List<Column> columns = new ArrayList<>();
        //设置底部坐标轴的值
        List<String> titles = new ArrayList<>();
        titles.add("通道数");
        titles.add("总量");
        for(int i = 2; i < numColumn; i++){
            titles.add(String.valueOf(i-1));
        }
        //将获得的json格式的数据解析后根据柱状图的通道进行数据分配
        int value;
        int numLength = 2;
        Class c = Class.forName("com.example.iotobserve.okhttp.GetSeeds");
        for(int i = 0;i < numColumn;i++){
            if(i > 1){
                Method method = c.getMethod("getRow_amount" + String.valueOf(i-1));
                value = (int) method.invoke(Constants.sGetSeeds);
            }else {
                if(i == 0){
                    Method method = c.getMethod("getMax_tongdao");
                    value = (int) method.invoke(Constants.sGetSeeds);
                }else {
                    Method method = c.getMethod("getRow_amount");
                    value = (int) method.invoke(Constants.sGetSeeds);
                    numLength = String.valueOf(value).length();
                }
            }
            values = new ArrayList<>();
            axisValues.add(new AxisValue(i).setLabel(titles.get(i)));
            for(int j = 0;j < numSubColumn;j++){
                values.add(new SubcolumnValue(value, ChartUtils.pickColor()));
            }
            Column column = new Column(values);
            column.setHasLabels(true);
            mColumns_num.add(column);
        }
        dataColumnChart_num = new ColumnChartData(mColumns_num);
        //设置x,y坐标轴
        Axis axisX = new Axis(axisValues).setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("通道");
        axisY.setName("播种总量");
        axisY.setMaxLabelChars(numLength);
        dataColumnChart_num.setAxisXBottom(axisX);
        dataColumnChart_num.setAxisYLeft(axisY);
        //设置柱状图的各项属性
        viewColumnChart_num.setColumnChartData(dataColumnChart_num);
        viewColumnChart_num.setZoomType(ZoomType.HORIZONTAL);
        viewColumnChart_num.setZoomEnabled(true);
        viewColumnChart_num.setInteractive(true);
        viewColumnChart_num.setContainerScrollEnabled(true,ContainerScrollType.HORIZONTAL);
        viewColumnChart_num.setMaxZoom(30000);
        viewColumnChart_num.setVisibility(View.VISIBLE);

    }



}