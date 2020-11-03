package com.example.myfinalproject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myfinalproject.Adapter.MyAdapter;
import com.example.myfinalproject.DB.StockItem;
import com.example.myfinalproject.DB.StockManager;
import com.example.myfinalproject.Util.GetURL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener{

    private static final String TAG = "ResultActivity";
    Handler handler;
    ListView list;
    String s, ss = null;
    ListAdapter adapter;
    StockManager stockManager = new  StockManager(ResultActivity.this);
    List<StockItem> stocklist = new ArrayList<StockItem>();

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
                }else if(msg.what == 6){
                    list.setAdapter(adapter);
                    list.setEmptyView(findViewById(R.id.nodata));
                    Toast.makeText(ResultActivity.this, "无股票信息，请添加！", Toast.LENGTH_SHORT).show();
                    Intent return_add = new Intent(ResultActivity.this,AddActivity.class);
                    startActivityForResult(return_add,3);
                }
                super.handleMessage(msg);

            }
        };
    }

    public void ret(View V){
        Intent return_main = new Intent(ResultActivity.this,MainActivity.class);
        startActivityForResult(return_main,1);
    }

    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        Message msg = handler.obtainMessage();
        ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
        try{
            stocklist = stockManager.listAll();
           if(stocklist.size() == 0){
                msg.what = 6;
                Log.i(TAG,"数据库里无信息，请添加股票");
            }else{
                msg.what = 5;
                for(StockItem stockItem :stocklist){
                    s = stockItem.getStockcode();
                    ss = "https://hq.sinajs.cn/list=" + s;
                    String a = GetURL.SendGET(ss);
                    String[] d = a.split(";");
                    for (int i = 0; i < d.length; i++) {
                        // 获取当前价格
                        String c3 = d[i].split(",")[3];
                        String c = stockItem.getStockname();
                        String cp = stockItem.getStockprice();
                        //Log.i("网络上",c + " ==> " + c3);
                        //Log.i("数据库",c + " ==> " + cp);
                        if(stockItem.getStockprice().equals(c3)){
                            Log.i(TAG,c + ": 价格未发生变化，不更新。。。。。。");
                        }else{
                            StockItem si = new StockItem(c, s, c3);
                            stockManager.updatePrice(si);
                            Log.i(TAG,c + ": 价格已发生变化，已更新。。。。。。");
                        }

                    }
                }

               for(StockItem stockItem1 :stocklist){
                   HashMap<String, String> map = new HashMap<String, String>();
                   map.put("StockName", stockItem1.getStockname());
                   map.put("StockPrice", stockItem1.getStockprice());
                   list1.add(map);
               }
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

        Log.i(TAG, "onItemClick: " + nameStr + "  ==>  " + priceStr);

        Intent image = new Intent(ResultActivity.this,ImageActivity.class);
        //传递参数
        Bundle bdl = new Bundle();
        bdl.putString("StockName",nameStr);
        bdl.putFloat("StockPrice",price);
        image.putExtras(bdl);
        //打开新页面
        startActivity(image);
    }
}