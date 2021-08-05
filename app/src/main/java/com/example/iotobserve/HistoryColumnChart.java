package com.example.iotobserve;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iotobserve.okhttp.OkHttp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;


public class HistoryColumnChart extends Fragment {
    ColumnChartView mColumnChartView_fre,mColumnChartView_num;
    ColumnChartData mColumnChartData_fre,mColumnChartData_num;


    public HistoryColumnChart() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_column_chart, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //为wifi设计的固有数据
        Constants.last_HistoryColumn.setRow_amount(89);
        Constants.last_HistoryColumn.setRow_amount1(13);
        Constants.last_HistoryColumn.setRow_amount2(16);
        Constants.last_HistoryColumn.setRow_amount3(15);
        Constants.last_HistoryColumn.setRow_amount4(13);
        Constants.last_HistoryColumn.setRow_amount5(15);
        Constants.last_HistoryColumn.setRow_amount6(17);
        Constants.last_HistoryColumn.setRow_frequency(72002);
        Constants.last_HistoryColumn.setRow_frequency1(12133);
        Constants.last_HistoryColumn.setRow_frequency2(11957);
        Constants.last_HistoryColumn.setRow_frequency3(12020);
        Constants.last_HistoryColumn.setRow_frequency4(11847);
        Constants.last_HistoryColumn.setRow_frequency5(11994);
        Constants.last_HistoryColumn.setRow_frequency6(12051);
        //获取历史表名的最新一条网络数据
        mColumnChartView_fre = getView().findViewById(R.id.ColumnChartView2);
        mColumnChartView_num = getView().findViewById(R.id.ColumnChartView);

        mColumnChartView_fre.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                if(columnIndex >= 1){
                    Constants.history_isFrequency = true;
                    Constants.history_tongdao = columnIndex - 1;
                  //  NavController navController = Navigation.findNavController(getView());
                 //   navController.navigate(R.id.action_historyColumnChart_to_historyChart);
                }

            }

            @Override
            public void onValueDeselected() {

            }
        });
        mColumnChartView_num.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                if(columnIndex >= 1){
                    Constants.history_isFrequency = false;
                    Constants.history_tongdao = columnIndex - 1;
              //      NavController navController = Navigation.findNavController(getView());
             //       navController.navigate(R.id.action_historyColumnChart_to_historyChart);
                }

            }

            @Override
            public void onValueDeselected() {

            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                try {
                    Constants.last_HistoryColumn = OkHttp.TimeObserve(Constants.select_table_name);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                 */
            }
        });
        try {
            thread.start();
            thread.join();
            setColumnChart_fre(mColumnChartView_fre);
            setColumnChart_num(mColumnChartView_num);

            //mColumnChartView_fre.setMaximumViewport(viewport);
            Log.d("到最后了", "onActivityCreated: ");
        } catch (InterruptedException e) {
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

    public void setColumnChart_fre(ColumnChartView chart) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //设置柱状图列的个数
        int numSubColumns = 1;
        int numColumns = 8;
        List<String> title = new ArrayList<>();
        //x,y轴值list
        List<AxisValue> axisValues = new ArrayList<>();
        List<PointValue> mPointValue = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        //添加底部横坐标的值
        title.add("通道数");
        title.add("总量");
        title.add("1");
        title.add("2");
        title.add("3");
        title.add("4");
        title.add("5");
        title.add("6");
        title.add("7");
        title.add("8");
        title.add("9");
        title.add("10");
        title.add("11");
        title.add("12");
        //color.add(Color.parseColor("#85B74F"));
        Class c = Class.forName("com.example.iotobserve.okhttp.GetSeeds");
        float value = 0;
        for(int i = 0;i < numColumns;i++){
            if(i > 1){
                Method method = c.getMethod("getRow_frequency"+String.valueOf(i-1));
                value = (float) method.invoke(Constants.last_HistoryColumn);
                Log.d(String.valueOf(i)+"通道", String.valueOf(value));
            }else{
                if(i ==1){
                    Method method = c.getMethod("getRow_frequency");
                    value = (float)method.invoke(Constants.last_HistoryColumn);
                    Log.d("总频率", String.valueOf(value));
                    Log.d("总频率", String.valueOf(Constants.last_HistoryColumn.getRow_frequency()));
                }else {
                    Method method = c.getMethod("getMax_tongdao");
                    value = (int)method.invoke(Constants.last_HistoryColumn);
                }

            }

            axisValues.add(new AxisValue(i).setLabel(title.get(i)));
            Log.d("个数", i+"");
            values = new ArrayList<>();
            for(int j = 0; j < numSubColumns;j++){
                values.add(new SubcolumnValue(value, ChartUtils.pickColor()));
            }
            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);
        }

        mColumnChartData_fre = new ColumnChartData(columns);
        Axis axisX = new Axis(axisValues).setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
        Axis axisY1 = new Axis().setHasLines(true);
        axisX.setName("通道");
        axisY.setName("播种频率");
        axisY1.setName("播种总量");
        mColumnChartData_fre.setAxisXBottom(axisX);
        mColumnChartData_fre.setAxisYLeft(axisY);
        mColumnChartData_fre.setAxisYRight(axisY1);
        //图表设置数据
        mColumnChartView_fre.setColumnChartData(mColumnChartData_fre);
        mColumnChartView_fre.setZoomType(ZoomType.HORIZONTAL);
        mColumnChartView_fre.setInteractive(true);
        mColumnChartView_fre.setZoomEnabled(true);
        mColumnChartView_fre.setMaxZoom((float) 5);//最大方法比例
        mColumnChartView_fre.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mColumnChartView_fre.setVisibility(View.VISIBLE);

    }
    public void setColumnChart_num(ColumnChartView columnChartView_num) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int numSubColumns = 1;
        int numColumns = 8;
        List<String> title = new ArrayList<>();
        //x,y轴的值
        List<AxisValue> axisValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values = new ArrayList<>();
        //添加底部横坐标的值
        title.add("通道数");
        title.add("总量");

        for(int i = 2; i < numColumns; i++){
            title.add(String.valueOf(i-1));
        }
        Class c = Class.forName("com.example.iotobserve.okhttp.GetSeeds");
        int value = 0;
        int numLength = 2;
        for(int i = 0; i < numColumns; i++){
            if(i > 1){
                Method method = c.getMethod("getRow_amount"+String.valueOf(i-1));
                value = (int) method.invoke(Constants.last_HistoryColumn);

            }else {
                if(i == 1) {
                    Method method = c.getMethod("getRow_amount");
                    value = (int) method.invoke(Constants.last_HistoryColumn);
                    numLength = String.valueOf(value).length();
                }else {
                    Method method = c.getMethod("getMax_tongdao");
                    value = (int) method.invoke(Constants.last_HistoryColumn);

                }
            }
            axisValues.add(new AxisValue(i).setLabel(title.get(i)));
            values = new ArrayList<>();
            for(int j = 0; j < numSubColumns; j++){
                values.add(new SubcolumnValue(value,ChartUtils.pickColor()));
            }
            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);
        }
        mColumnChartData_num = new ColumnChartData(columns);
        Axis axisX = new Axis(axisValues).setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("通道");
        axisY.setName("播种数量");
        axisY.setMaxLabelChars(numLength);
        mColumnChartData_num.setAxisXBottom(axisX);
        mColumnChartData_num.setAxisYLeft(axisY);
        //设置图表的特性
        mColumnChartView_num.setColumnChartData(mColumnChartData_num);
        mColumnChartView_num.setZoomType(ZoomType.HORIZONTAL);
        mColumnChartView_num.setInteractive(true);
        mColumnChartView_num.setZoomEnabled(true);
        mColumnChartView_num.setMaxZoom(30000);
        mColumnChartView_num.setContainerScrollEnabled(true,ContainerScrollType.HORIZONTAL);
        mColumnChartView_fre.setVisibility(View.VISIBLE);
    }
}