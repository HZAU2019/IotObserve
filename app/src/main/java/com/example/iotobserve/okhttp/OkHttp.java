package com.example.iotobserve.okhttp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttp {
    private static final String TAG = "返回结果为：";

    public static GetSeeds TimeObserve(String table_name) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("table_name",table_name)
                .build();

        Request request = new Request.Builder()
                .url("http://39.106.74.235:8080/WriteReadFromDatabasewenbo/TimeObserve")
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        Log.d(TAG, "成功了"+response.code());
        String body = response.body().string();

        Log.d(TAG, "返回内容为："+body);
        GetSeeds getSeeds = parsetimeobservewithgson(body);
        return getSeeds;


    }

    private static GetSeeds parsetimeobservewithgson(String body) {
        Gson gson = new Gson();
        GetSeeds getSeeds = gson.fromJson(body,GetSeeds.class);
        return getSeeds;

    }

    public static List<TableName> GetTableName(String basic_table_name) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .build();
        Log.d(TAG, basic_table_name);
        RequestBody requestBody = new FormBody.Builder()
                .add("basic_table_name",basic_table_name)
                .build();
        Request request = new Request.Builder()
                .url("http://39.106.74.235:8080/WriteReadFromDatabasewenbo/GetTableName")
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String body = response.body().string();
        Log.d(TAG, body);
        List<TableName> get_table_name = parsetablenamewithgson(body);
        return get_table_name;
    }

    private static List<TableName> parsetablenamewithgson(String body) {
        Gson gson = new Gson();
        List<TableName> get_table_name = gson.fromJson(body,new TypeToken<List<TableName>>(){}.getType());
        for(TableName tableName : get_table_name){
            Log.d(TAG, "表名为："+tableName.getTableName());
        }
        return get_table_name;
    }


    public static List<GetSeeds> SelectTable(String select_table) throws IOException {
        //Log.d(TAG, "开始时间");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("select_table_name",select_table)
                .build();
        Request request = new Request.Builder()
                .url("http://39.106.74.235:8080/WriteReadFromDatabasewenbo/SelectTable")
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String body = response.body().string();
        //Log.d(TAG, "结束时间");
        List<GetSeeds> getSeeds = parseselecttablewithgson(body);
        return getSeeds;
    }

    private static List<GetSeeds> parseselecttablewithgson(String body){
        Gson gson = new Gson();
        List<GetSeeds> getSeeds = gson.fromJson(body,new TypeToken<List<GetSeeds>>(){}.getType());
        return getSeeds;
    }

}
