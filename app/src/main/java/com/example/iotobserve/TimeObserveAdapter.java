package com.example.iotobserve;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotobserve.okhttp.GetSeeds;

import java.util.List;

public class TimeObserveAdapter extends RecyclerView.Adapter<TimeObserveAdapter.ViewHolder> {

    public GetSeeds getseeds;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tongdao,frequency,number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tongdao = itemView.findViewById(R.id.textView39);
            frequency = itemView.findViewById(R.id.textView37);
            number = itemView.findViewById(R.id.textView38);
        }


    }
    public TimeObserveAdapter(GetSeeds getseeds){
        this.getseeds = getseeds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_seed_item,parent,false);
       ViewHolder viewHolder = new ViewHolder(view);
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // if(position<Integer.valueOf(getseeds.getMax_tongdao())) {
            switch (position + 1) {
                case 1:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency1()));
                        if(getseeds.getRow_frequency1() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount1()));
                    break;
                case 2:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency2()));
                        if(getseeds.getRow_frequency2() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount2()));
                    break;
                case 3:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency3()));
                        if(getseeds.getRow_frequency3() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount3()));
                    break;
                case 4:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency4()));
                        if(getseeds.getRow_frequency4() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount4()));
                    break;
                case 5:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency5()));
                        if(getseeds.getRow_frequency5() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount5()));
                    break;
                case 6:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency6()));
                        if(getseeds.getRow_frequency6() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }

                        Log.d("框中是否报警", String.valueOf(getseeds.getRow_frequency6()));
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount6()));
                    break;
                case 7:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency7()));
                        if(getseeds.getRow_frequency7() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount7()));
                    break;
                case 8:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency8()));
                        if(getseeds.getRow_frequency8() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount8()));
                    break;
                case 10:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency10()));
                        if(getseeds.getRow_frequency10() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount10()));
                    break;
                case 11:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency11()));
                        if(getseeds.getRow_frequency11() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount11()));
                    break;
                case 9:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        if(getseeds.getRow_frequency9() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency9()));
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount9()));
                    break;
                case 12:
                    holder.tongdao.setText("通道" + String.valueOf(position + 1));
                    if (true) {
                        if(getseeds.getRow_frequency12() < Constants.setStandardNow){
                            holder.frequency.setTextColor(Color.parseColor("#FF0000"));
                            Constants.didi = true;
                        }
                        holder.frequency.setText(String.valueOf(getseeds.getRow_frequency12()));
                    } else {
                        holder.frequency.setText(String.valueOf(0.0));
                    }

                    holder.number.setText(String.valueOf(getseeds.getRow_amount12()));
                    break;


            }

       // }

    }

    @Override
    public int getItemCount() {
        return Integer.valueOf(getseeds.getMax_tongdao());
    }
}
