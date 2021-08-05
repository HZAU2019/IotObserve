package com.example.iotobserve;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.example.iotobserve.okhttp.GetSeeds;
import com.example.iotobserve.okhttp.OkHttp;

import java.io.IOException;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryChart extends Fragment {
    float[] score_frequency;
    float[] score_amount;

    private LineChartView lineChart;
    private TextView mTextView_tongdao,mTextView_isFrequency;
    String[] date;

    public HistoryChart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_chart, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTextView_tongdao = getView().findViewById(R.id.textView26);
        mTextView_isFrequency = getView().findViewById(R.id.textView27);
        if(Constants.history_tongdao!=0){
            mTextView_tongdao.setText("当前通道："+Constants.history_tongdao+"通道");
        }else {
            mTextView_tongdao.setText("当前通道：总通道");
        }

        if(Constants.history_isFrequency){
            mTextView_isFrequency.setText("监测参数：频率");
        }else {
            mTextView_isFrequency.setText("监测参数：播量");
        }


        lineChart = getView().findViewById(R.id.line_chart);
        //设置触摸相应点的监听事件
        lineChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {

                int index = pointIndex;
                Constants.his_lat = Constants.points.get(index).latitude;
                Constants.his_lon = Constants.points.get(index).longitude;

                Constants.points_tem.addAll(Constants.points.subList(0,index+1));

                Constants.his_fre = score_frequency[index];
                Constants.his_amount = (int) score_amount[index];
               // NavController navController = Navigation.findNavController(getView());
                //navController.navigate(R.id.action_historyChart_to_historyBaiduMap);
            }

            @Override
            public void onValueDeselected() {

            }
        });
        final CreateHelloChart createHelloChart = new CreateHelloChart();
        Log.d("是频率么", String.valueOf(Constants.history_isFrequency));

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // Log.d("开始时间", "run: ");
                // List<GetSeeds> getSeeds = OkHttp.SelectTable(Constants.select_table_name);
                // Log.d("结束时间", "run: ");
                int id_amount = Constants.history_seeds_collection.size();
                score_frequency = new float[id_amount];
                score_amount = new float[id_amount];
                date = new String[id_amount];
                LatLng latLng;
                int i = 0;
                switch (Constants.history_tongdao){
                    case 0:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency();
                            score_amount[i] = getseed.getRow_amount();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 1:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency1();
                            score_amount[i] = getseed.getRow_amount1();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 2:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency2();
                            score_amount[i] = getseed.getRow_amount2();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 3:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency3();
                            score_amount[i] = getseed.getRow_amount3();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 4:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency4();
                            score_amount[i] = getseed.getRow_amount4();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 5:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency5();
                            score_amount[i] = getseed.getRow_amount5();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 6:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency6();
                            score_amount[i] = getseed.getRow_amount6();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 7:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency7();
                            score_amount[i] = getseed.getRow_amount7();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 8:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency8();
                            score_amount[i] = getseed.getRow_amount8();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 9:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency9();
                            score_amount[i] = getseed.getRow_amount9();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 10:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency10();
                            score_amount[i] = getseed.getRow_amount10();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 11:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency11();
                            score_amount[i] = getseed.getRow_amount11();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;
                    case 12:
                        for(GetSeeds getseed :Constants.history_seeds_collection){
                            score_frequency[i] = getseed.getRow_frequency12();
                            score_amount[i] = getseed.getRow_amount12();
                            date[i] = getseed.getTime();
                            latLng = new LatLng(getseed.getLatitude(),getseed.getLongitude());
                            Constants.points.add(latLng);
                            i++;
                        }
                        break;


                }

                if(Constants.history_isFrequency){
                    createHelloChart.run(score_frequency,date,lineChart);
                }
                else {
                    createHelloChart.run(score_amount,date,lineChart);
                }


            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<GetSeeds> getSeeds = OkHttp.SelectTable(Constants.select_table_name);
                    int id_amount = getSeeds.size();
                    Log.d("数据条数为：", String.valueOf(id_amount));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }
}
