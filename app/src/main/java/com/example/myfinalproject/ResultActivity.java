package com.example.myfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.myfinalproject.Adapter.MyAdapter;
import com.example.myfinalproject.DB.StockItem;
import com.example.myfinalproject.DB.StockManager;
import com.example.myfinalproject.Util.GetURL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener{

    private static final String TAG = "ResultActivity";
    Handler handler;
    ListView list;
    Button btn_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        list = (ListView)findViewById(R.id.list);

        Thread t = new Thread(this);
        t.start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 5) {
                    ArrayList<HashMap<String, String>> listItems = (ArrayList<HashMap<String, String>>) msg.obj;
                    MyAdapter myAdapter = new MyAdapter(ResultActivity.this, R.layout.list_item,listItems);
                    list.setAdapter(myAdapter);
                    list.setOnItemClickListener(ResultActivity.this);
                }
                super.handleMessage(msg);

            }
        };
    }

    public void ret(View V){
        Intent return_main = new Intent(this,MainActivity.class);
        startActivityForResult(return_main,1);
    }

    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        Message msg = handler.obtainMessage(5);
        try{
            List<StockItem> stocklist = new ArrayList<StockItem>();
            StockManager stockManager = new  StockManager(ResultActivity.this);
            /*if(stockManager.listAll() == null){

            }else{

            }*/
            String s = "https://hq.sinajs.cn/list=sh601006,sh601001,sz300601";
            String a = GetURL.SendGET(s);
            String[] d = a.split(";");
            ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < d.length; i++) {
                //Log.i(TAG,"信息：" + d[i]);
                // 获取股票名字
                String c = d[i].split(",")[0].split("\"")[1];
                c = c.replace(" ", "");
                // 获取股票代码
                String c0 = d[i].split(",")[0].split("\"")[0];
                String c1 = c0.split("=")[0].split("_")[2];
                // 获取当前价格
                String c3 = d[i].split(",")[3];
                Log.i(TAG,"股票名字：" + c + ", 股票代码：" + c1 + ", 当前价格：" + c3);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("StockName", c);
                map.put("StockPrice", c3);
                list1.add(map);
            }
            msg.obj = list1;
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object ip = list.getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>)ip;
        String nameStr = map.get("StockName");
        String priceStr = map.get("StockPrice");
        Float price = Float.parseFloat(priceStr);

        Log.i(TAG, "onItemClick: name=" + nameStr);
        Log.i(TAG, "onItemClick: price=" + priceStr);

        Intent image = new Intent(this,ImageActivity.class);
        //传递参数
        Bundle bdl = new Bundle();
        bdl.putString("StockName",nameStr);
        bdl.putFloat("StockPrice",price);
        image.putExtras(bdl);
        //打开新页面
        startActivity(image);
    }
}