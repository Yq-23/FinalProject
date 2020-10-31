package com.example.myfinalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.myfinalproject.Adapter.MyAdapter;
import com.example.myfinalproject.DB.StockItem;
import com.example.myfinalproject.DB.StockManager;
import com.example.myfinalproject.Util.GetURL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeleteActivity extends AppCompatActivity implements Runnable,AdapterView.OnItemLongClickListener{

    private static final String TAG = "DeleteActivity";
    ListView listdelete;
    Handler handler;
    SimpleAdapter listItemAdapter;
    AlertDialog.Builder builder;
    ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        listdelete = (ListView)findViewById(R.id.listDelete);

        Thread t = new Thread(this);
        t.start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 11) {
                    listItems = (ArrayList<HashMap<String, String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(DeleteActivity.this, listItems,  R.layout.list_item,
                            new String[] { "StockName", "StockPrice" },new int[] { R.id.StockName, R.id.StockPrice } );
                    listdelete.setAdapter(listItemAdapter);
                    listdelete.setOnItemLongClickListener(DeleteActivity.this);//添加长按事件监听
                }
                super.handleMessage(msg);

            }
        };
    }

    public void Delete(View V){
        Intent return_main = new Intent(this,MainActivity.class);
        startActivityForResult(return_main,1);
    }

    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        Message msg = handler.obtainMessage(11);
        try{
            List<StockItem> stocklist = new ArrayList<StockItem>();
            StockManager stockManager = new  StockManager(DeleteActivity.this);
            String s = "https://hq.sinajs.cn/list=sh601006,sh601001,sz300601,sh600601,sh600602,sh600603";
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


    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onItemLongClick: 对话框事件处理");
                        // 删除数据项
                        listItems.remove(position);
                        // 更新适配器
                        listItemAdapter.notifyDataSetChanged();

                    }
                }).setNegativeButton("否", null);
        builder.create().show();
        return false;
    }

}