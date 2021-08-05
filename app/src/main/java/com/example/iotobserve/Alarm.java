package com.example.iotobserve;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;

public class Alarm {
    public static Ringtone alarm;//响铃声
    public static Vibrator vibrator;//震动声
    public static final void initializeAlarm(){
        alarm = RingtoneManager.getRingtone( GetApplicationContext.getContext(),
                Uri.parse("android.resource://"+ GetApplicationContext.getContext().getPackageName()+"/"+R.raw.didi));
        vibrator = (Vibrator) GetApplicationContext.getContext().getSystemService(GetApplicationContext.getContext().VIBRATOR_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static final void openAlarm(boolean switch1){
        if(switch1){
            //vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
            alarm.play();
            alarm.setLooping(true);
            long[] period = {1000,1000,1000,2000};
            vibrator.vibrate(period,0);
        }else{
            alarm.stop();
            vibrator.cancel();
        }
    }
}
