package com.example.iotobserve;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WifiMode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WifiMode extends Fragment {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    TextView textView9;
    TextView textView10;
    TextView textView11;
    TextView textView12;
    TextView textView13;
    TextView textView14;
    Button button1;
    Button button2;
    Timer timer;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            String string = String.valueOf(msg.obj);
            Log.d("数字", string+ "序号"+msg.what);

            switch(msg.what){
                case 1:
                    textView1.setText(string);
                    break;
                case 2:
                    textView2.setText(string);
                    break;
                case 3:
                    textView3.setText(string);;
                    break;
                case 4:
                    textView4.setText(string);
                    break;
                case 5:
                    textView5.setText(string);
                    break;
                case 6:
                    textView6.setText(string);
                    break;
                case 7:
                    textView7.setText(string);
                    break;
                case 8:
                    textView8.setText(string);
                    break;
                case 9:
                    textView9.setText(string);
                    break;
                case 10:
                    textView10.setText(string);
                    break;
                case 11:
                    textView11.setText(string);
                    break;
                case 12:
                    textView12.setText(string);
                    break;
                case 13:
                    textView13.setText(string);
                    break;
                case 14:
                    textView14.setText(string);
                    break;
            }
        }
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WifiMode() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WifiMode.
     */
    // TODO: Rename and change types and number of parameters
    public static WifiMode newInstance(String param1, String param2) {
        WifiMode fragment = new WifiMode();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP&&event.getKeyCode() == KeyEvent.KEYCODE_BACK){


                }
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wifi_mode, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView1 = getView().findViewById(R.id.textView4);
        textView2 = getView().findViewById(R.id.textView11);
        textView3 = getView().findViewById(R.id.textView13);
        textView4 = getView().findViewById(R.id.textView15);
        textView5 = getView().findViewById(R.id.textView17);
        textView6 = getView().findViewById(R.id.textView19);
        textView7 = getView().findViewById(R.id.textView3);
        textView8 = getView().findViewById(R.id.textView10);
        textView9 = getView().findViewById(R.id.textView12);
        textView10 = getView().findViewById(R.id.textView14);
        textView11 = getView().findViewById(R.id.textView16);
        textView12 = getView().findViewById(R.id.textView18);
        textView13 = getView().findViewById(R.id.textView41);
        textView14 = getView().findViewById(R.id.textView42);

        button1 = getView().findViewById(R.id.button);
        button2 = getView().findViewById(R.id.button2);
        textView1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView5.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView6.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView7.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView8.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView9.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView10.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView11.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView12.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView13.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView14.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //三句确定连接状态
                Log.d("connect", "开始连接");
                Socket socket = null;
                try {
                    socket = new Socket("10.10.100.254", 8899);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("connect", "连接成功");

                //开始接受数据
                InputStream is = null;
                try {
                    is = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedInputStream bis = new BufferedInputStream(is);
                //把需要创建的对象放在前面，避免循环时多次创建对象影响速度
                int a;
                int c = 0;
                int b = 0;
                int fre_total = 0;
                int num_total = 0;
                //每18个数字一个循环，3个数字为一组，前两个为16进制，代表播种总量
                // 后一个代表播种频率
                for(int i=0;i<18;i++) {
                    Message msg = handler.obtainMessage();
                    try {
                        b = bis.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if((i+1)%3 == 1){
                        c = b;
                    }
                    if((i+1)%3 == 2){
                        b = c * 256 + b;
                        msg.what = (i+2)/3;
                        msg.obj = b;
                        Log.d("总数", String.valueOf(b));
                        handler.sendMessage(msg);
                        num_total = num_total + b;

                    }
                    if ((i+1)%3 == 0){
                        msg.what = (i+1)/3+6;
                        msg.obj = b;
                        Log.d("频率", String.valueOf(b));
                        handler.sendMessage(msg);
                        fre_total = fre_total + b;
                    }
                }
                Message msg1 = new Message();
                msg1.obj = fre_total;
                msg1.what = 13;
                Message msg2 = new Message();
                msg2.obj = num_total;
                msg2.what = 14;
                handler.sendMessage(msg1);
                handler.sendMessage(msg2);
            }
        },0,1000);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                NavController navController = Navigation.findNavController(v);
                //navController.navigate(R.id.action_wifiMode_to_historyColumnChart);
            }
        });

    }
}