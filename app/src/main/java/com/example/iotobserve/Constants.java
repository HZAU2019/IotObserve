package com.example.iotobserve;

import com.baidu.mapapi.model.LatLng;
import com.example.iotobserve.okhttp.GetSeeds;
import com.example.iotobserve.okhttp.TableName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    //千粒重
    public static float thousandSeed = 4f;
    //作业机具行距
    public static float rowSpace = 0.25f;
    //作业机具行进速度
    public static double speed = 0;
    //亩播量
    public static int muBoLiang = 120;
    //实时数据标号
    public static int i = 0;
    public static int i_last = i -1;
    //开始时间
    public static long start = 0;
    //结束时间
    public static long end = 0;
    //是否存在网络延迟
    public static boolean isDelay = false;
    //网络延时时间
    public static float delay = 0;
    //开始时选择的平台及设备号
    public static String device = "430";
    public static String device_id = "1";
    //在获取到历史表名的时候，所点击选择的表名
    public static String select_table_name;
    //查看历史数据时选择的通道和选择参数
    public static int history_tongdao = 0;
    public static Boolean history_isFrequency = false;
    //查看历史数据时选择的图表类型
    public static Boolean history_isColumnChart = true;
    public static int history_max_tongao;
    //获取当天中存储实时数据的表名，给个表名防止空指针错误
    public static String current_table_name = "table_20200630_430_1_test1";
    //获取的历史图标的最后一条数据
    public static GetSeeds last_HistoryColumn = new GetSeeds();
    //获取到的实时数据以一个对象的形式存储
    public static GetSeeds  sGetSeeds = new GetSeeds();
    //获取上一个实时数据对象用来比对机器是否在工作
    public static GetSeeds sGetSeeds_last = new GetSeeds();
    //获取下一条实时数据
    public static GetSeeds sGetSeeds_next = new GetSeeds();
    //获取基础数据
    public static GetSeeds sGetSeeds_base = new GetSeeds();
    //暂存当前实时数据对象用于下一次赋值给sGetSeeds_last
    public static GetSeeds sGetSeeds_temporary = new GetSeeds();
    //获取到的一串历史数据以list列表的形式存储
    public static List<Integer> sList = new ArrayList<Integer>() ;
    //将获取到的历史数据表名以list形式存储
    public static List<TableName> history_table_name = new ArrayList<>();
    //向服务器提交搜索某一天搜索所有表名时的基本表名
    public static String search_basic_table_neme = null;
    //设置chart表建造过程是否停止
    public static Boolean time_chart_stop = false;
    //设置某一历史表中历史播种信息的集合
    public static List<GetSeeds> history_seeds_collection = new ArrayList<>();
    //设置历史坐标中不合格的集合下标
    public static List<Integer> shitpoint = new ArrayList<>();
    //设置历史坐标的list集合
    public static List<LatLng> points = new ArrayList<LatLng>();
    //设置当前坐标的list集合
    public static List<LatLng> points_current = new ArrayList<>();
    //设置选中的坐标之前的坐标集合
    public static List<LatLng> points_tem = new ArrayList<LatLng>();
    //历史数据中的某个位置的播种频率，播种量，东经北纬度数
    public static float his_fre = 543f;
    public static int his_amount = 56461345;
    public static double his_lat = 30.46934f;
    public static double his_lon = 114.35477f;
    //设置实时监测时是否报警
    public static boolean isAlarm = false;
    //设置实时监测时的标准值
    public static int standardSeedNumberNow = 0;

    //设置实时播种频率最低达标阈值

    public static float setStandardNow = 0;
    //设置开启报警时是否需要报警
    public static boolean didi = false;

    //设置历史查询时的标准值
    public static int standardSeedNumberHis = 0;
    //设置历史播种频率最低达标阈值
    public static float setStandardHis = 0;
    public static List<Float> standardHis = new ArrayList<>();

    //设置历史查询时的按钮选择是作业路径还是合格标准
    public static Boolean isRoute = false;
    //设置历史查询时的不同选择下的seekbar位置
    public static int positionHistory = 0;
    //设置历史数据查询时的图像中心位置
    public static LatLng center;
    //历史数据的漏播率
    public static float missRate = 0;
    //要选取的历史数据圆心
    public static LatLng selectLocation = new LatLng(his_lat,his_lon);
    //要选取的历史播种地图表的频率
    public static boolean isHistoryFre = false;






//    public static int number_1 = 0;
//    public static int rate_1 = 0;
//    public static int number_2 = 0;
//    public static int rate_3 = 0;
//    public static int number_2 = 0;
//    public static int rate_1 = 0;
//    public static int number_3 = 0;
//    public static int rate_1 = 0;
//    public static int number_4 = 0;
//    public static int rate_1 = 0;
//    public static int number_5 = 0;
//    public static int rate_1 = 0;
//    public static int number_6 = 0;
//    public static int rate_1 = 0;
//    public static int number_7 = 0;
//    public static int rate_1 = 0;
//    public static int number_8 = 0;
//    public static int rate_1 = 0;
//    public static int number_9 = 0;
//    public static int rate_1 = 0;
//    public static int number_10 = 0;
//    public static int rate_1 = 0;
//    public static int number_11 = 0;
//    public static int rate_1 = 0;
//    public static int number_12 = 0;
//    public static int rate_1 = 0;
//    public static int number_0 = 0;
//    public static int rate_0 = 0;

}
