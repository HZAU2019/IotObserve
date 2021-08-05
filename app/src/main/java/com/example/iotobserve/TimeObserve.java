package com.example.iotobserve;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.iotobserve.okhttp.GetSeeds;
import com.example.iotobserve.okhttp.OkHttp;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeObserve extends Fragment {
    public static Boolean isStart = true;
    private RecyclerView mRecyclerView;
    private TimeObserveAdapter timeObserveAdapter;
    TextView textview_rate_all,textview_number_all;
    public static Timer timer = null;
    private Ringtone alarm;
    private Vibrator vibrator;
    private boolean isThis = true;
    private boolean isRealData = false;

    public TimeObserve() {
        // Required empty public constructor
    }
    private Handler mHandler = new Handler(){
        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    /*
                    Constants.sList.add(Constants.sGetSeeds.getMax_tongdao());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount1());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency1());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount2());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency2());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount3());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency3());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount4());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency4());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount5());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency5());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount6());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency6());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount7());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency7());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount8());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency8());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount9());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency9());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount10());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency10());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount11());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency11());
                    Constants.sList.add(Constants.sGetSeeds.getRow_amount12());
                    Constants.sList.add(Constants.sGetSeeds.getRow_frequency12());
                    int number_all = 0;
                    int rate_all = 0;
                    for(int i =1;i<=12;i++){
                        number_all = number_all + Constants.sList.get(2*i-1);
                        rate_all = rate_all + Constants.sList.get(2*i);
                    }
                    Constants.sList.add(number_all);
                    Constants.sList.add(rate_all);
                    */

                    //timeObserveAdapter = new TimeObserveAdapter(Constants.sGetSeeds);
                    //mRecyclerView.setAdapter(timeObserveAdapter);

                    timeObserveAdapter.getseeds = Constants.sGetSeeds;
                    timeObserveAdapter.notifyDataSetChanged();
                    Log.d("是否报警", String.valueOf(Constants.isAlarm) + Constants.didi+isThis);
                    //是否报警提示
                    if(Constants.isAlarm&&isThis){
                        if(Constants.didi){
                            //initailizeAlarm();
                            Alarm.openAlarm(true);
                        }else {
                            Alarm.openAlarm(false);
                            //initailizeAlarm();
                        }
                    }else {
                        Alarm.openAlarm(false);
                        //initailizeAlarm();
                    }
                    textview_rate_all.setText(String.valueOf(Constants.sGetSeeds.getRow_frequency()));
                    if(Constants.didi){
                        textview_rate_all.setTextColor(Color.parseColor("#FF0000"));
                    }else {
                        textview_rate_all.setTextColor(0xFF888888);
                    }
                    textview_number_all.setText(String.valueOf(Constants.sGetSeeds.getRow_amount()));
                    Log.d("设置结果", "设置成功");
                    Constants.didi = false;
                    break;
                case 2:
                    float t = (float) msg.obj;
                    Log.d("延时", "handleMessage: "+(Constants.end - Constants.start));
                    Toast.makeText(getContext(),"当前网络延迟"+ new DecimalFormat("#.00")
                            .format(t/1000)+"秒",Toast.LENGTH_SHORT).show();
                    Constants.isDelay = false;
                    break;
            }

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_observe, container, false);
    }
    //设置back键
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    isStart = false;
                    timer.cancel();
                    Alarm.openAlarm(false);
                }
                return false;
            }


        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置报警初始化
        Alarm.initializeAlarm();
        Constants.didi = false;
        isStart =true;
        mRecyclerView = getView().findViewById(R.id.recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        timeObserveAdapter = new TimeObserveAdapter(Constants.sGetSeeds);
        mRecyclerView.setAdapter(timeObserveAdapter);


        showlivlingdata();

        textview_number_all = getView().findViewById(R.id.textView22);
        textview_rate_all = getView().findViewById(R.id.textView15);

        Button button_history = getView().findViewById(R.id.button6);
        Button button_switch = getView().findViewById(R.id.button2);
        Button button_current_chart = getView().findViewById(R.id.button5);
        final Button button_on_off = getView().findViewById(R.id.button);

        button_history.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                isStart = false;
                timer.cancel();
                Alarm.openAlarm(false);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_timeObserve_to_getTableName);

            }
        });

        button_switch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                isStart = false;
                timer.cancel();
                Alarm.openAlarm(false);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_timeObserve_to_selectDevice);
                }


        });

        button_current_chart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                Alarm.openAlarm(false);
                try{
                    if(Constants.sGetSeeds.getTime().length()!=0) {
                        if(isRealData){
                            timer.cancel();
                            isThis = false;
                            Constants.time_chart_stop = false;
                            timer.cancel();
                            NavController navController = Navigation.findNavController(v);
                            navController.navigate(R.id.action_timeObserve_to_currentChartMap);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if(Constants.isAlarm){
            button_on_off.setText("报警开启");
        }else{
            button_on_off.setText("报警关闭");
        }

        button_on_off.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
//                isStart = false;
//                showlivlingdata();

                Constants.isAlarm = !Constants.isAlarm;
                if(Constants.isAlarm){
                    button_on_off.setText("报警开启");
                }else{
                    button_on_off.setText("报警关闭");
                }


                //openAlarm(false);
                NavController navController = Navigation.findNavController(v);
              //  navController.navigate(R.id.action_timeObserve_to_wifiMode);


                Thread t3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Constants.sGetSeeds = OkHttp.TimeObserve(Constants.current_table_name);
                            Message message = new Message();
                            message.what = 1;
                            message.obj = Constants.sGetSeeds;
                            mHandler.sendMessage(message);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
               // t3.start();
            }
        });
    }

    public void showlivlingdata(){
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        if(calendar.get(Calendar.MONTH)+1<10){
            month = "0"+month;
        }
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if(calendar.get(Calendar.DAY_OF_MONTH)<10){
            day = "0"+day;
        }
        String current_table_name = "table_"+ year+month+day+"_"+Constants.device+"_"+Constants.device_id;
        Log.d("当前要访问的表名为：", current_table_name);
        Constants.current_table_name = current_table_name;

        timer = new Timer();
        isThis = true;
        timer.schedule(new TimerTask() {
            int i = 0;
            int a = 0,b = 1;
            LatLng latLng_last = new LatLng(5,5);
            @Override
            public void run() {
                if(!isStart){
                    timer.cancel();
                }
                try {
                    GetSeeds getSeeds;
                    Constants.start = System.currentTimeMillis();
                    getSeeds = OkHttp.TimeObserve(Constants.current_table_name);
                    Constants.end = System.currentTimeMillis();
                    if(Constants.end - Constants.start > 2500){
                        Constants.isDelay = true;
                        Constants.delay = (float) (Constants.end - Constants.start);
                    }
                    //getSeeds = OkHttp.TimeObserve("table_20201111_430_1");
                    if(getSeeds.getTime().length()!=0){
                        isRealData = true;
                        Constants.sGetSeeds_last = Constants.sGetSeeds_temporary;
                        Constants.sGetSeeds = getSeeds;
                        Constants.sGetSeeds_temporary = Constants.sGetSeeds;
                        //计算该点最低播种频率合格值
                        LatLng ll = new LatLng(Constants.sGetSeeds.getLatitude(),Constants.sGetSeeds.getLongitude());
                        latLng_last = new LatLng(Constants.sGetSeeds_last.getLatitude(),Constants.sGetSeeds.getLongitude());
                        double distance = DistanceUtil.getDistance(ll,latLng_last);
                        b = Integer.parseInt(Constants.sGetSeeds.getTime().substring(6,8));
                        if(b - a > 0){
                            Constants.speed = distance/(b - a);
                            Constants.setStandardNow = (float) (Constants.standardSeedNumberNow*15*Constants
                                    .speed*Constants.rowSpace/10/Constants.thousandSeed);
                        }else {
                            Constants.speed = distance/(60 + b - a);
                            Constants.setStandardNow = (float) (Constants.standardSeedNumberNow*15*
                                    Constants.speed*Constants.rowSpace/10/Constants.thousandSeed);
                        }
                        a = b;
                        if(Constants.sGetSeeds_last.getRow_amount() == Constants.sGetSeeds.getRow_amount()){
                            i++;
                            if(i>=3){
                                Constants.sGetSeeds.setRow_frequency(0.0f);
                                Constants.sGetSeeds.setRow_frequency1(0.0f);
                                Constants.sGetSeeds.setRow_frequency2(0.0f);
                                Constants.sGetSeeds.setRow_frequency3(0.0f);
                                Constants.sGetSeeds.setRow_frequency4(0.0f);
                                Constants.sGetSeeds.setRow_frequency5(0.0f);
                                Constants.sGetSeeds.setRow_frequency6(0.0f);
                                Constants.sGetSeeds.setRow_frequency7(0.0f);
                                Constants.sGetSeeds.setRow_frequency8(0.0f);
                                Constants.sGetSeeds.setRow_frequency9(0.0f);
                                Constants.sGetSeeds.setRow_frequency10(0.0f);
                                Constants.sGetSeeds.setRow_frequency11(0.0f);
                                Constants.sGetSeeds.setRow_frequency12(0.0f);
                            }

                        }else{
                            i = 0;
                        }
                    }
                    else {

                       // Constants.sGetSeeds_last = Constants.sGetSeeds_temporary;
                       // Constants.sGetSeeds = OkHttp.TimeObserve("table_20200630_430_1_test1");
                        Constants.sGetSeeds = OkHttp.TimeObserve("table_20201111_430_1");
                       // Constants.sGetSeeds_temporary = Constants.sGetSeeds;
                      //  if(Constants.sGetSeeds_last.getRow_amount() == Constants.sGetSeeds.getRow_amount()){

                            Constants.sGetSeeds.setRow_frequency(0.0f);
                            Constants.sGetSeeds.setRow_frequency1(0.0f);
                            Constants.sGetSeeds.setRow_frequency2(0.0f);
                            Constants.sGetSeeds.setRow_frequency3(0.0f);
                            Constants.sGetSeeds.setRow_frequency4(0.0f);
                            Constants.sGetSeeds.setRow_frequency5(0.0f);
                            Constants.sGetSeeds.setRow_frequency6(0.0f);
                            Constants.sGetSeeds.setRow_frequency7(0.0f);
                            Constants.sGetSeeds.setRow_frequency8(0.0f);
                            Constants.sGetSeeds.setRow_frequency9(0.0f);
                            Constants.sGetSeeds.setRow_frequency10(0.0f);
                            Constants.sGetSeeds.setRow_frequency11(0.0f);
                            Constants.sGetSeeds.setRow_frequency12(0.0f);
                            Constants.sGetSeeds.setRow_amount(0);
                            Constants.sGetSeeds.setRow_amount1(0);
                            Constants.sGetSeeds.setRow_amount2(0);
                            Constants.sGetSeeds.setRow_amount3(0);
                            Constants.sGetSeeds.setRow_amount4(0);
                            Constants.sGetSeeds.setRow_amount5(0);
                            Constants.sGetSeeds.setRow_amount6(0);
                            Constants.sGetSeeds.setRow_amount7(0);
                            Constants.sGetSeeds.setRow_amount8(0);
                            Constants.sGetSeeds.setRow_amount9(0);
                            Constants.sGetSeeds.setRow_amount10(0);
                            Constants.sGetSeeds.setRow_amount11(0);
                            Constants.sGetSeeds.setRow_amount12(0);

                       // }
                    }
//                    LatLng ll = new LatLng(Constants.sGetSeeds.getLatitude(),Constants.sGetSeeds.getLongitude());
//                    LatLng last = new LatLng(Constants.sGetSeeds_last.getLatitude(),Constants.sGetSeeds.getLongitude());
//                    int speed = DistanceUtil.getDistance(ll,last);
//                    Constants.standardNowFre = 150*Constants.standardSeedNumberNow*

                    Message message = new Message();
                    message.what = 1;
                    message.obj = Constants.sGetSeeds;
                    mHandler.sendMessage(message);
                    Message message_delay = new Message();
                    if(Constants.isDelay){
                        message_delay.what = 2;
                        message_delay.obj = Constants.delay;
                        mHandler.sendMessage(message_delay);
                    }


                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        },0,1000);

    }


}
