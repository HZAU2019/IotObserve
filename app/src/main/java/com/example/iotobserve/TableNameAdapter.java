package com.example.iotobserve;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotobserve.okhttp.TableName;

import java.util.List;

public class TableNameAdapter extends RecyclerView.Adapter<TableNameAdapter.ViewHolder> {
    private List<TableName> table_name_list;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView table_name_history;
        View get_name_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            get_name_view = itemView;
            table_name_history = itemView.findViewById(R.id.textView30);
        }
    }

    public TableNameAdapter(List<TableName> table_name_list){
        this.table_name_list = table_name_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.get_name_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.table_name_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.select_table_name = (String) viewHolder.table_name_history.getText();
                Log.d("选择的表名为", Constants.select_table_name);
                NavController navController = Navigation.findNavController(v);
                if(Constants.history_isColumnChart){
                    navController.navigate(R.id.action_getTableName_to_historyChartMap);
                }else {
                    navController.navigate(R.id.action_getTableName_to_historyChart);
                }

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("位置是：", String.valueOf(position));
        TableName tableName = table_name_list.get(position);
       // String table_name_string = (String) table_name_list.get(position);
        holder.table_name_history.setText(tableName.getTableName());
    }


    @Override
    public int getItemCount() {
        return table_name_list.size();
    }
}
