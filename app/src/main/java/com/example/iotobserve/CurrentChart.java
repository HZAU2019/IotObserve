package com.example.iotobserve;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentChart extends Fragment {
    private int[] score;
    private String[] date;
    private LineChartView lineChart;
    private SeekBar mSeekBar;
    private Switch mSwitch;
    private TextView mTextView_tongdao,mTextView_isFrequency;
   // public static Boolean isFrequency = false;
  //  public static int tongdao = 0;
    public CurrentChart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_chart, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Constants.time_chart_stop = false;
        lineChart = getView().findViewById(R.id.line_chart);
        score = new int[]{28,56,42,91,87,82,64,79,43};
        date = new String[]{"16:42:23","16:42:24","16:42:25","16:42:26","16:42:27","16:42:28","16:42:29","16:42:30","16:42:31"};
        //CreateHelloChart createHelloChart = new CreateHelloChart();
        //createHelloChart.run(score,date,lineChart);
        mSeekBar = getView().findViewById(R.id.seekBar);
        mSwitch = getView().findViewById(R.id.switch1);
        mTextView_tongdao = getView().findViewById(R.id.textView26);
        mTextView_isFrequency = getView().findViewById(R.id.textView27);
        if(CreateTimeHelloChart.tongdao!=0){
            mTextView_tongdao.setText("当前通道：" + CreateTimeHelloChart.tongdao + "通道");
        }else {
            mTextView_tongdao.setText("当前通道：总通道");
        }
        if(CreateTimeHelloChart.isFrequency){
            mTextView_isFrequency.setText("监测参数：频率");
        }else {
            mTextView_isFrequency.setText("监测参数：播量");
        }

        mSeekBar.setProgress(CreateTimeHelloChart.tongdao);
        mSwitch.setChecked(!CreateTimeHelloChart.isFrequency);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    CreateTimeHelloChart.tongdao = 0;
                    mTextView_tongdao.setText("当前通道：总通道");
                }
                else {
                    CreateTimeHelloChart.tongdao = progress;
                    mTextView_tongdao.setText("当前通道:"+progress+"通道");
                }
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
                if(isChecked){
                    CreateTimeHelloChart.isFrequency = false;
                    mTextView_isFrequency.setText("监测参数：播量");
                }else {
                    CreateTimeHelloChart.isFrequency = true;
                    mTextView_isFrequency.setText("监测参数：频率");
                }
            }
        });

        lineChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Constants.time_chart_stop = true;
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_currentChart_to_currentBaiduMap);
            }

            @Override
            public void onValueDeselected() {

            }
        });
        //设置按钮的点击事件
        CreateTimeHelloChart createTimeHelloChart = new CreateTimeHelloChart(lineChart,CreateTimeHelloChart.tongdao,CreateTimeHelloChart.isFrequency);

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
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                    Constants.time_chart_stop = true;
                    CurrentChartMap.timer.cancel();
                    Alarm.openAlarm(false);
                return false;
            }
        });
    }
}
