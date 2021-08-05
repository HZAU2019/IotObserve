package com.example.iotobserve;

import android.graphics.Color;
import android.util.Log;

import com.example.iotobserve.okhttp.OkHttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class CreateTimeHelloChart {
    public static Boolean isFrequency;
    public static int tongdao;
    public Boolean isXianshi = false;
    public Boolean Danshuang = true;               //设计每镉一个点显示一个标点数字
    private LineChartView lineChartView;
    private LineChartData lineChartData;		//折线图显示的数据（包括坐标上的点）
    private List<Line> linesList;
    private List<PointValue> points;			//要显示的点
    private List<PointValue> points_xianshi ;  //要对改变进行显示的点
    private List<PointValue> pointValueList;
    private List<PointValue> pointValueList_xianshi;//要对改变进行显示的点
    private int position = 0;
    private int y_max = 0;
    private Timer timer;						//定时刷新折线图
    private boolean isFinish = false;
    //private Socket_client client;

    private List<Integer> data = new ArrayList<Integer>();//接收的数据
    private LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(2);//设置标注点的保留小数位数

    private String colorString = "#FFCD41";

    private int number_1 = 0, rate_1 = 0, number_2 = 0, rate_2 = 0, number_3 = 0, rate_3 = 0,
            number_4 = 0, rate_4 = 0, number_5 = 0, rate_5 = 0, number_6 = 0, rate_6 = 0,
            number_7 = 0, rate_7 = 0, number_8 = 0, rate_8 = 0, number_9 = 0, rate_9 = 0,
            number_10 = 0, rate_10 = 0, number_11 = 0, rate_11 = 0, number_12 = 0, rate_12 = 0,
            number_all = 0,rate_all = 0;//需要接收并显示的数据

    private Axis axisX;							//X轴
    private Axis axisY;							//Y轴
    private int number_max = 1000000;

    //预判初始化x坐标轴的时间逻辑
    String time = Constants.sGetSeeds.getTime();
    String s = time.substring(time.length()-2,time.length());
    String m = time.substring(time.length()-5,time.length()-3);
    String h = time.substring(time.length()-8,time.length()-6);
    int M =Integer.valueOf(m);
    int S = Integer.valueOf(s);
    int H =Integer.valueOf(h);



    CreateTimeHelloChart(LineChartView lineChartView, int tongdao, Boolean isFrequency){
        this.lineChartView = lineChartView;
        this.tongdao = tongdao;
        this.isFrequency = isFrequency;

        initAxisView();							//初始化坐标轴


        showMovingLineChart();					//动态显示折线变化


    }


    /**
     * 初始化显示坐标轴
     */
    private void initAxisView() {

        pointValueList = new ArrayList<PointValue>();
        linesList = new ArrayList<Line>();

        /** 初始化Y轴 */
        axisY = new Axis();
    //    axisY.setName("单位：粒");						//添加Y轴的名称
        axisY.setHasLines(true);							//Y轴分割线
        axisY.setTextSize(10);								//设置字体大小
//        axisY.setTextColor(Color.parseColor("#AFEEEE"));	//设置Y轴颜色，默认浅灰色
        axisY.setMaxLabelChars(10);  //设置最多显示的y坐标个数，不设置只会显示三位
        lineChartData = new LineChartData(linesList);
        lineChartData.setAxisYLeft(axisY);					//设置Y轴在左边

        /** 初始化X轴 */
        axisX = new Axis();
        axisX.setHasTiltedLabels(false);  					//X坐标轴字体是斜的显示还是直的，true是斜的显示
//        axisX.setTextColor(Color.CYAN);  					//设置X轴颜色
    //    axisX.setName("单位：个");  						//X轴名称
        axisX.setHasLines(true);							//X轴分割线
        axisX.setTextSize(10);								//设置字体大小
        axisX.setMaxLabelChars(0); 							//设置0的话X轴坐标值就间隔为1


        List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
        for (int i = 0; i < number_max; i++) {
            if(i<=10){
                switch (i){
                    case 0:
                        mAxisXValues.add(new AxisValue(i).setLabel(Constants.sGetSeeds.getTime()));
                        break;
                    case 1:
                        mAxisXValues.add(new AxisValue(i).setLabel(""));
                        break;
                    case 2:
                        S =S+2;
                        if(S>60){
                            S=S-60;
                            M = M+1;
                            if(M > 60){
                                M=M-60;
                                H = H+1;
                                if(H >24){
                                    H =H-24;
                                }
                            }
                        }
                        String a_time = H +":" +M +":"+S;
                        mAxisXValues.add(new AxisValue(i).setLabel(a_time));
                        break;
                    case 3:
                        mAxisXValues.add(new AxisValue(i).setLabel(""));
                        break;
                    case 4:
                        S =S+2;
                        if(S>60){
                            S=S-60;
                            M = M+1;
                            if(M > 60){
                                M=M-60;
                                H = H+1;
                                if(H >24){
                                    H =H-24;
                                }
                            }
                        }
                        String b_time = H +":" +M +":"+S;
                        mAxisXValues.add(new AxisValue(i).setLabel(b_time));
                        break;
                    case 5:
                        mAxisXValues.add(new AxisValue(i).setLabel(""));
                        break;
                    case 6:
                        S =S+2;
                        if(S>60){
                            S=S-60;
                            M = M+1;
                            if(M > 60){
                                M=M-60;
                                H = H+1;
                                if(H >24){
                                    H =H-24;
                                }
                            }
                        }
                        String c_time = H +":" +M +":"+S;
                        mAxisXValues.add(new AxisValue(i).setLabel(c_time));
                        break;
                    case 7:
                        mAxisXValues.add(new AxisValue(i).setLabel(""));
                        break;
                    case 8:
                        S =S+2;
                        if(S>60){
                            S=S-60;
                            M = M+1;
                            if(M > 60){
                                M=M-60;
                                H = H+1;
                                if(H >24){
                                    H =H-24;
                                }
                            }
                        }
                        String d_time = H +":" +M +":"+S;
                        mAxisXValues.add(new AxisValue(i).setLabel(d_time));
                        break;
                    case 9:
                        mAxisXValues.add(new AxisValue(i).setLabel(""));
                        break;
                    case 10:
                        S =S+2;
                        if(S>60){
                            S=S-60;
                            M = M+1;
                            if(M > 60){
                                M=M-60;
                                H = H+1;
                                if(H >24){
                                    H =H-24;
                                }
                            }
                        }
                        String e_time = H +":" +M +":"+S;
                        mAxisXValues.add(new AxisValue(i).setLabel(e_time));
                        break;


                }



            }
            else {
                if(i%2==0){
                    if (Constants.sGetSeeds.getTime().length()==0){
                        mAxisXValues.add(new AxisValue(i).setLabel(i + ""));
                    }
                    mAxisXValues.add(new AxisValue(i).setLabel(Constants.sGetSeeds.getTime()));
                }
                if (i%2==1){
                    mAxisXValues.add(new AxisValue(i).setLabel(""));
                }
            }




        }
        axisX.setValues(mAxisXValues);  					//填充X轴的坐标名称
        lineChartData.setAxisXBottom(axisX); 				//X轴在底部

        lineChartView.setLineChartData(lineChartData);

        Viewport port = initViewPort(0,10,120,0);					//初始化X轴10个间隔坐标
        lineChartView.setCurrentViewportWithAnimation(port);
        lineChartView.setInteractive(true);				//设置不可交互
        lineChartView.setScrollEnabled(false);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setZoomEnabled(false);
        lineChartView.setMaxZoom((float) 3);//最大方法比例
        lineChartView.setValueTouchEnabled(true);
        lineChartView.setFocusableInTouchMode(false);
        lineChartView.setViewportCalculationEnabled(false);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.startDataAnimation();

        loadData();											//加载待显示数据
    }

    private Viewport initViewPort(float left,float right,int top,int bottom) {
        Viewport port = new Viewport();
        port.top = top;				//Y轴上限，固定(不固定上下限的话，Y轴坐标值可自适应变化)
        port.bottom = bottom;			//Y轴下限，固定
        port.left = left;			//X轴左边界，变化
        port.right = right;			//X轴右边界，变化
        return port;
    }

    /**
     * 初始化数据点
     */
    private void loadData() {
        points = new ArrayList<PointValue>();
        points_xianshi = new ArrayList<PointValue>();
        pointValueList_xianshi = new ArrayList<PointValue>();
//        for (int i = 0; i < 30; i++) {
//            points.add(new PointValue(i + 1, i % 5 * 10 + 30));
//        }
    }

    /**
     * 数据点动态刷新
     */

    private void showMovingLineChart() {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(Constants.time_chart_stop){
                    timer.cancel();
                }
                Log.d("position位置", String.valueOf(position));
                if(!isFinish){

                    //data = Constants.sList;
                    try {
                        Constants.sGetSeeds = OkHttp.TimeObserve(Constants.current_table_name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //把数据取出来
                    number_1 = Constants.sGetSeeds.getRow_amount1()+1;
                    rate_1 = (int) Constants.sGetSeeds.getRow_frequency1()+1;
                    number_2 = Constants.sGetSeeds.getRow_amount2()+1;
                    rate_2 =(int) Constants.sGetSeeds.getRow_frequency2()+1;
                    number_3 = Constants.sGetSeeds.getRow_amount3()+1;
                    rate_3 = (int) Constants.sGetSeeds.getRow_frequency3()+1;
                    number_4 = Constants.sGetSeeds.getRow_amount4()+1;
                    rate_4 = (int) Constants.sGetSeeds.getRow_frequency4()+1;
                    number_5 = Constants.sGetSeeds.getRow_amount5()+1;
                    rate_5 = (int) Constants.sGetSeeds.getRow_frequency5()+1;
                    number_6 =Constants.sGetSeeds.getRow_amount6()+1;
                    rate_6 = (int) Constants.sGetSeeds.getRow_frequency6()+1;
                    number_7 = Constants.sGetSeeds.getRow_amount7()+1;
                    rate_7 = (int) Constants.sGetSeeds.getRow_frequency7()+1;
                    number_8 = Constants.sGetSeeds.getRow_amount8()+1;
                    rate_8 = (int) Constants.sGetSeeds.getRow_frequency8()+1;
                    number_9 = Constants.sGetSeeds.getRow_amount9()+1;
                    rate_9 = (int) Constants.sGetSeeds.getRow_frequency9()+1;
                    number_10 =Constants.sGetSeeds.getRow_amount10()+1;
                    rate_10 = (int) Constants.sGetSeeds.getRow_frequency10()+1;
                    number_11 = Constants.sGetSeeds.getRow_amount11()+1;
                    rate_11 = (int) Constants.sGetSeeds.getRow_frequency11()+1;
                    number_12 = Constants.sGetSeeds.getRow_amount12()+1;
                    rate_12 = (int) Constants.sGetSeeds.getRow_frequency12()+1;
                    number_all = Constants.sGetSeeds.getRow_amount()+1;
                    rate_all = (int) Constants.sGetSeeds.getRow_frequency()+1;
                    Log.d("是否执行socket",String.valueOf(rate_all));

                    //判断是要频率还是要数量
                    if(isFrequency){
                       switch (tongdao){
                           case 0:
                               points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_frequency()));
                               colorString = "#FF4040";
                               //判断是否更换y坐标
                               if (rate_all > y_max|y_max/(rate_all+1) > 6){
                                   y_max = rate_all;
                               }
                               Log.d("第一个数字",String.valueOf(rate_all));
                               break;
                           case 1:
                               points.add(new PointValue(position + 1,Constants.sGetSeeds.getRow_frequency1()));
                               colorString = "#FFCD41";
                               //判断是否更换y坐标
                               if (rate_1 > y_max | y_max / rate_1 > 6){
                                   y_max = rate_1;
                               }
                               break;
                           case 2:
                               points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_frequency2()));
                               colorString = "#7B68EE";
                               //判断是否更换y坐标
                               if(rate_2 > y_max | y_max / rate_2 > 6){
                                   y_max = rate_2;
                               }
                               break;
                           case 3:
                               points.add(new PointValue(position + 1,Constants.sGetSeeds.getRow_frequency3()));
                               colorString = "#00EE00";
                               //判断是否更换y坐标
                               if(rate_3 > y_max | y_max / rate_3 > 6){
                                   y_max = rate_3;
                               }
                               break;
                           case 4:
                               points.add(new PointValue(position + 1,Constants.sGetSeeds.getRow_frequency4()));
                               colorString = "#BF3EFF";
                               //判断是否更换y坐标
                               if(rate_4 > y_max | y_max / rate_4 > 6){
                                   y_max = rate_4;
                               }
                               break;
                           case 5:
                               points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_frequency5()));
                               colorString = "#AEEEEE";
                               //判断是否更换y坐标
                               if(rate_5 > y_max | y_max / rate_5 > 6){
                                   y_max = rate_5;
                               }
                               break;
                           case 6:
                               points.add(new PointValue(position + 1,Constants.sGetSeeds.getRow_frequency6()));
                               colorString = "#FFC0CB";
                               //判断是否更换y坐标
                               if(rate_6 > y_max | y_max / rate_6 > 6){
                                   y_max = rate_6;
                               }
                               break;

                           case 7:

                               points.add(new PointValue(position + 1,Constants.sGetSeeds.getRow_frequency7()));
                               colorString = "#FFCD41";
                               //判断是否更换y坐标
                               if (rate_1 > y_max | y_max / rate_1 > 6){
                                   y_max = rate_1;
                               }
                               break;
                           case 8:
                               points.add(new PointValue(position + 1,Constants.sGetSeeds.getRow_frequency8()));
                               colorString = "#7B68EE";
                               //判断是否更换y坐标
                               if(rate_2 > y_max | y_max / rate_2 > 6){
                                   y_max = rate_2;
                               }
                               break;
                           case 9:
                               points.add(new PointValue(position + 1,Constants.sGetSeeds.getRow_frequency9()));
                               colorString = "#00EE00";
                               //判断是否更换y坐标
                               if(rate_3 > y_max | y_max / rate_3 > 6){
                                   y_max = rate_3;
                               }
                               break;
                           case 10:
                               points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_frequency10()));
                               colorString = "#BF3EFF";
                               //判断是否更换y坐标
                               if(rate_4 > y_max | y_max / rate_4 > 6){
                                   y_max = rate_4;
                               }
                               break;
                           case 11:
                               points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_frequency11()));
                               colorString = "#AEEEEE";
                               //判断是否更换y坐标
                               if(rate_5 > y_max | y_max / rate_5 > 6){
                                   y_max = rate_5;
                               }
                               break;
                           case 12:
                               points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_frequency12()));
                               colorString = "#FFC0CB";
                               //判断是否更换y坐标
                               if(rate_6 > y_max | y_max / rate_6 > 6){
                                   y_max = rate_6;
                               }
                               break;

                       }
                    }
                    else {
                        switch (tongdao) {
                            case 0:
                                points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_amount()));
                                colorString = "#FF4040";
                                //判断是否更换y坐标
                                if (number_all > y_max | y_max / number_all > 6){
                                    y_max = number_all;
                                }
                                break;
                            case 1:
                                points.add(new PointValue(position + 1,Constants.sGetSeeds.getRow_amount1()));
                                colorString = "#FFCD41";
                                //判断是否更换y坐标
                                if (number_1 > y_max | y_max / number_1 > 6){
                                    y_max = number_1;
                                }
                                break;
                            case 2:
                                points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_amount2()));
                                colorString = "#7B68EE";
                                //判断是否更换y坐标
                                if (number_2 > y_max | y_max / number_2 > 6){
                                    y_max = number_2;
                                }
                                break;
                            case 3:
                                points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_amount3()));
                                colorString = "#00EE00";
                                //判断是否更换y坐标
                                if (number_3 > y_max | y_max / number_3 > 6){
                                    y_max = number_3;
                                }
                                break;
                            case 4:
                                points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_amount4()));
                                colorString = "#BF3EFF";
                                //判断是否更换y坐标
                                if (number_4 > y_max | y_max / number_4 > 6){
                                    y_max = number_4;
                                }
                                break;
                            case 5:
                                points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_amount5()));
                                colorString = "#AEEEEE";
                                //判断是否更换y坐标
                                if (number_5 > y_max | y_max / number_5 > 6){
                                    y_max = number_5;
                                }
                                break;
                            case 6:
                                points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_amount6()));
                                colorString = "#FFC0CB";
                                //判断是否更换y坐标
                                if (number_6 > y_max | y_max / number_6 > 6){
                                    y_max = number_6;
                                }
                                break;
                            case 7:
                                points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_amount7()));
                                colorString = "#FFCD41";
                                //判断是否更换y坐标
                                if (number_1 > y_max | y_max / number_1 > 6){
                                    y_max = number_1;
                                }
                                break;
                            case 8:
                                points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_amount8()));
                                colorString = "#7B68EE";
                                //判断是否更换y坐标
                                if (number_2 > y_max | y_max / number_2 > 6){
                                    y_max = number_2;
                                }
                                break;
                            case 9:
                                points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_amount9()));
                                colorString = "#00EE00";
                                //判断是否更换y坐标
                                if (number_3 > y_max | y_max / number_3 > 6){
                                    y_max = number_3;
                                }
                                break;
                            case 10:
                                points.add(new PointValue(position + 1, Constants.sGetSeeds.getRow_amount10()));
                                colorString = "#BF3EFF";
                                //判断是否更换y坐标
                                if (number_4 > y_max | y_max / number_4 > 6){
                                    y_max = number_4;
                                }
                                break;
                            case 11:
                                points.add(new PointValue(position + 1,Constants.sGetSeeds.getRow_amount11()));
                                colorString = "#AEEEEE";
                                //判断是否更换y坐标
                                if (number_5 > y_max | y_max / number_5 > 6){
                                    y_max = number_5;
                                }
                                break;
                            case 12:
                                points.add(new PointValue(position + 1,Constants.sGetSeeds.getRow_amount12()));
                                colorString = "#FFC0CB";
                                //判断是否更换y坐标
                                if (number_6 > y_max | y_max / number_6 > 6){
                                    y_max = number_6;
                                }
                                break;
                        }
                    }


                    //points.add(new PointValue(position + 1, rate_1));

                    pointValueList.add(points.get(position));		//实时添加新的点


                    if (position + 1 > 10 + 1) {
                        pointValueList.remove(0);
                        int a = 0;
                        for (int i = position - 11; i < position; i++){
                            if(points.get(i).getY() > a){
                                Log.d("近10个的值为", String.valueOf(points.get(i).getY()));
                                a = (int) points.get(i).getY();
                            }
                        }
                        y_max = a;
                        Log.d("最大值为", String.valueOf(y_max));
                    }
                    //判断是否在底部增加一个点来显示切换通道的痕迹
                    if(isXianshi){
                       // points_xianshi.remove(0)
                        points_xianshi.add(new PointValue(position + 1,0));
                       // pointValueList_xianshi.add(points_xianshi.get(position));
                        Line line_xianshi = new Line(points_xianshi);
                        line_xianshi.setColor(Color.parseColor(colorString));		//设置折线颜色
                        line_xianshi.setShape(ValueShape.CIRCLE);				//设置折线图上数据点形状为 圆形 （共有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
                        line_xianshi.setCubic(false);							//曲线是否平滑，true是平滑曲线，false是折线
                        line_xianshi.setHasLabels(true);						//数据是否有标注
//                    line.setHasLabelsOnlyForSelected(true);		//点击数据坐标提示数据,设置了line.setHasLabels(true);之后点击无效
                        line_xianshi.setHasLines(true);							//是否用线显示，如果为false则没有曲线只有点显示
                        line_xianshi .setHasPoints(true);						//是否显示圆点 ，如果为false则没有原点只有点显示（每个数据点都是个大圆点）
                        linesList.add(line_xianshi);
                        linesList.remove(0);
                        isXianshi = false;
                    }
                    //根据新的点的集合画出新的线
                    Line line = new Line(pointValueList);
                    if(isFrequency){
                        line.setFormatter(chartValueFormatter);//设置折线上标点保留的小数位数
                    }
                    line.setColor(Color.parseColor(colorString));		//设置折线颜色
                    line.setShape(ValueShape.CIRCLE);				//设置折线图上数据点形状为 圆形 （共有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
                    line.setCubic(false);							//曲线是否平滑，true是平滑曲线，false是折线
                    ///Danshuang = !Danshuang;                         //单双数设计标注
                    line.setHasLabels(Danshuang);						//数据是否有标注
//                    line.setHasLabelsOnlyForSelected(true);		//点击数据坐标提示数据,设置了line.setHasLabels(true);之后点击无效
                    line.setHasLines(true);							//是否用线显示，如果为false则没有曲线只有点显示
                    line.setHasPoints(true);						//是否显示圆点 ，如果为false则没有原点只有点显示（每个数据点都是个大圆点）

                    linesList.add(line);


                    if (position + 1 > 10) {
                        linesList.remove(0);

                    }

                    lineChartData = new LineChartData(linesList);
                    lineChartData.setAxisYLeft(axisY);					//设置Y轴在左
                    lineChartData.setAxisXBottom(axisX); 				//X轴在底部
                    lineChartView.setLineChartData(lineChartData);

                    float xAxisValue = points.get(position).getX();
                    //根据点的横坐标实时变换X坐标轴的视图范围
                    Viewport port;
                    if(xAxisValue > 10){
//                        port1 = new Viewport(lineChartView.getMaximumViewport());
//                        port1.left = xAxisValue - 10;
//                        port1.right = xAxisValue;
//                        port1.bottom = 0;
//                        port1.top = lineChartView.getMaximumViewport().top;
                        port = initViewPort(xAxisValue - 7,xAxisValue,y_max,0);
                    }
                    else {
//                        port1 = new Viewport(lineChartView.getMaximumViewport());
//                        port1.left = 10;
//                        port1.right = 0;
//                        port1.bottom = 0;
//                        port1.top = lineChartView.getMaximumViewport().top;
                          port = initViewPort(0,7,y_max,0);
                    }
                    lineChartView.setMaximumViewport(port);
                    lineChartView.setCurrentViewport(port);

                    position = position + 1;
                    //i = i + 1;

                    if(position > number_max-1) {
                        isFinish = true;
                    }
                }
            }
        },1000,1000);
    }




}
