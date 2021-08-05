package com.example.iotobserve;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.iotobserve.okhttp.OkHttp;
import com.example.iotobserve.okhttp.TableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class GetTableName extends Fragment {
    RecyclerView recyclerView;
    SeekBar mSeekBar2;
    Switch mSwitch1,mSwitch2;
    TextView mTextView_tonddao,mTextView_isFrequency;
    String basic_table_name;
    String basic_table_name_now;

    public GetTableName() {
        // Required empty public constructor
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    TableNameAdapter tableNameAdapter = null;
                    Log.d("结果", "大哥大嫂过年好");
                    tableNameAdapter = new TableNameAdapter(Constants.history_table_name);

                    Log.d("共有", String.valueOf(tableNameAdapter.getItemCount()));
                    recyclerView.setAdapter(tableNameAdapter);

            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_table_name, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        List<String> table_name_history = new ArrayList<>();
//        table_name_history.add("table_20200630_430_1_test1");
//        table_name_history.add("table_20200630_430_1_test2");
//        table_name_history.add("table_20200630_430_1_test3");
//        table_name_history.add("table_20200630_430_2_test1");
//        table_name_history.add("table_20200630_430_2_test2");
//        table_name_history.add("table_20200630_430_2_test3");
//        table_name_history.add("table_20200630_430_2_test4");
//        table_name_history.add("table_20200630_430_2_test5");

        mSeekBar2 = getView().findViewById(R.id.seekBar2);
        mSwitch1 = getView().findViewById(R.id.switch1);
        mSwitch2 = getView().findViewById(R.id.switch2);
        mTextView_tonddao = getView().findViewById(R.id.textView29);
        if(Constants.history_tongdao==0){
            mTextView_tonddao.setText("选择通道："+"总通道");
        }else {
            mTextView_tonddao.setText("选择通道："+Constants.history_tongdao+"通道");
        }
        mTextView_isFrequency = getView().findViewById(R.id.textView28);
        mSeekBar2.setProgress(Constants.history_tongdao);
        mSeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    Constants.history_tongdao = 0;
                    mTextView_tonddao.setText("选择通道："+"总通道");
                }else {
                    Constants.history_tongdao = progress;
                    mTextView_tonddao.setText("选择通道："+progress+"通道");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Constants.history_isFrequency = false;
                    mTextView_isFrequency.setText("选择参数：播量");
                }else {
                    Constants.history_isFrequency = true;
                    mTextView_isFrequency.setText("选择参数：频率");
                }
            }
        });
        mSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Constants.history_isColumnChart = true;
                }else {
                    Constants.history_isColumnChart = false;
                }
            }
        });


        final Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Constants.history_table_name = OkHttp.GetTableName(Constants.search_basic_table_neme);
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) +1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if(calendar.get(Calendar.MONTH)+1<10){
            month = "0"+month;
        }
        if(calendar.get(Calendar.DAY_OF_MONTH)<10){
            day = "0"+day;
        }
        basic_table_name_now = "'table_"+year+month+day+"_%';";

        recyclerView = getView().findViewById(R.id.RecycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        if(Constants.search_basic_table_neme != null){

            TableNameAdapter tableNameAdapter = new TableNameAdapter(Constants.history_table_name);
            recyclerView.setAdapter(tableNameAdapter);
        }
        else {
            Constants.search_basic_table_neme = basic_table_name_now;
            Log.d("走到这里了", Constants.search_basic_table_neme);
            t4.start();

        }

        final EditText editText = getView().findViewById(R.id.editText2);
        Button button_search = getView().findViewById(R.id.button3);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.getText().toString().length()==0){

                    String a = "'table_20200630_%';";
                    Constants.search_basic_table_neme = basic_table_name_now;
                    //Log.d("空搜索", a);
                    Log.d("空搜索", Constants.search_basic_table_neme);
                }
                else{
                    Constants.search_basic_table_neme = "'table_"+ editText.getText().toString()  +"_%';";
                    Log.d("组合名", Constants.search_basic_table_neme);
                    Log.d("空不空", String.valueOf(editText.getText().toString().length()));
                   // basic_table_name = "'table_20200630_%';";
                }
                Thread t5 = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Constants.history_table_name = OkHttp.GetTableName(Constants.search_basic_table_neme);
                            Log.d("空搜索", Constants.search_basic_table_neme);
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t5.start();
            }
        });


    }
}
