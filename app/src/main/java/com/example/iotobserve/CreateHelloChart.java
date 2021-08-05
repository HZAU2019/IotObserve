package com.example.iotobserve;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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

public class  CreateHelloChart {
    private LineChartView lineChart;
    private LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(2);//设置标注点的保留小数位数
    float[] score;//y轴的标注
    String[] date;//X轴的标注
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private String TAG = "爸爸打我";
    private int y_max = 0, x_max = 0;
    public CreateHelloChart(){
//        this.score = score;
//        this.date = date;
//        this.lineChart = lineChartView;
//        getAxisXLables();//获取x轴的标注
//        getAxisPoints();//获取y轴坐标点
//        initLineChart();//初始化
    }
    public void run(float[] score,String[] date,LineChartView lineChartView){
        this.score = score;
        this.date = date;
        this.lineChart = lineChartView;

        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取y轴坐标点
        initLineChart();//初始化
    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables() {
        Log.d(TAG, "x长度为"+String.valueOf(date.length));
        x_max = date.length;
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
            Log.d("小数为：", String.valueOf(score[i]));
            if(y_max < score[i]){
                y_max = (int) score[i]+1;
            }
        }
    }

    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        if(Constants.history_isFrequency){
            line.setFormatter(chartValueFormatter);//设置折线上保留标点的小数位数
        }
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标（扯淡的，只是显示横坐标显示位数而已），意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length，
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setHasLines(true);
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setMaxLabelChars(10);  //设置最多显示的y坐标个数，不设置只会显示三位
       // axisY.setHasTiltedLabels(true);
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setZoomEnabled(true);
        lineChart.setMaxZoom((float) 30000);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        Viewport v = new Viewport();
        v.top = y_max;
        v.bottom = 0;
        v.left = 0;
        v.right = 7;
        Viewport v_max = new Viewport();
        v_max.top = y_max;
        v_max.bottom = 0;
        v_max.left = 0;
        v_max.right = x_max;
        lineChart.setMaximumViewport(v_max);
        lineChart.setCurrentViewport(v);

    }

}
