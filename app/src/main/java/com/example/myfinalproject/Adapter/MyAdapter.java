package com.example.myfinalproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myfinalproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends ArrayAdapter {
    private static final String TAG = "MyAdapter";
    public MyAdapter(Context context,
                     int resource,
                     ArrayList<HashMap<String, String>> list){
        super(context, resource,list);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Map<String, String> map = (Map<String, String>) getItem(position);
        TextView name = (TextView)itemView.findViewById(R.id.StockName);
        TextView price = (TextView)itemView.findViewById(R.id.StockPrice);
        TextView code = (TextView)itemView.findViewById(R.id.StockCode);

        name.setText(map.get("StockName"));
        price.setText(map.get("StockPrice"));
        code.setText(map.get("StockCode"));

        return itemView;
    }
}
