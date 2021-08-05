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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectDevice extends Fragment {

    public SelectDevice() {
        // Required empty public constructor
    }
    RadioButton mRadioButton_430,mRadioButton_RPi,mRadioButton_PC;
    RadioButton mRadioButton_1,mRadioButton_2,mRadioButton_3;
    EditText thousandSeed,rowSpace;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_device, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRadioButton_1 = getView().findViewById(R.id.radioButton3);
        mRadioButton_2 = getView().findViewById(R.id.radioButton4);
        mRadioButton_3 = getView().findViewById(R.id.radioButton5);
        mRadioButton_430 = getView().findViewById(R.id.radioButton);
        mRadioButton_RPi = getView().findViewById(R.id.radioButton2);
        mRadioButton_PC = getView().findViewById(R.id.radioButton6);
        thousandSeed = getView().findViewById(R.id.editTextNumber);
        rowSpace = getView().findViewById(R.id.editTextNumber2);
        thousandSeed.setText(String.valueOf(Constants.thousandSeed));
        rowSpace.setText(String.valueOf(Constants.rowSpace));

        Log.d("初始化", Constants.device+Constants.device_id);
        //设置保存之前的按钮状态
        switch (Constants.device){
            case "430":
                mRadioButton_430.setChecked(true);
                break;
            case "RPi":
                mRadioButton_RPi.setChecked(true);
                break;
            case "pc":
                mRadioButton_PC.setChecked(true);
                break;
        }
        switch(Constants.device_id){
            case "1":
                mRadioButton_1.setChecked(true);
                break;
            case "2":
                mRadioButton_2.setChecked(true);
                break;
            case "3":
                mRadioButton_3.setChecked(true);
                break;
        }


        Button button = getView().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thousandSeed.getText().toString().length() != 0 ){
                    Constants.thousandSeed = Float.parseFloat(String.valueOf(thousandSeed.getText()));
                }
                if(rowSpace.getText().toString().length() != 0){
                    Constants.rowSpace = Float.parseFloat(String.valueOf(rowSpace.getText()));
                }
                Log.d("亩播量", "" + Constants.thousandSeed + "和" + Constants.rowSpace);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_selectDevice_to_timeObserve);
            }
        });

        RadioGroup radioGroup_device = getView().findViewById(R.id.radioGroup);
        radioGroup_device.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("选择结果", Constants.device+Constants.device_id);
                if (checkedId == R.id.radioButton){
                    Constants.device = "430";
                    Log.d("选择结果", Constants.device+Constants.device_id);
                }
                else if(checkedId == R.id.radioButton6){
                    Constants.device = "pc";
                    Log.d("选择结果", Constants.device+Constants.device_id);
                }
                else {
                    Constants.device = "RPi";
                    Log.d("选择结果", Constants.device+Constants.device_id);
                }
            }
        });

        RadioGroup radioGroup_id = getView().findViewById(R.id.radioGroup2);
        radioGroup_id.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("选择结果", Constants.device+Constants.device_id);
                if(checkedId == R.id.radioButton3){
                    Constants.device_id = "1";
                    Log.d("选择结果", Constants.device+Constants.device_id);
                }
                if (checkedId == R.id.radioButton4){

                    Constants.device_id = "2";
                    Log.d("选择结果", Constants.device+Constants.device_id);

                }
                if(checkedId == R.id.radioButton5){
                    Constants.device_id = "3";
                    Log.d("选择结果", Constants.device+Constants.device_id);
                }

            }
        });
    }

}
