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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.myfinalproject.DB.StockItem;
import com.example.myfinalproject.DB.StockManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeleteActivity extends AppCompatActivity implements Runnable,AdapterView.OnItemLongClickListener{

    private static final String TAG = "DeleteActivity";
    ListView listdelete;
    Handler handler;
    SimpleAdapter listItemAdapter;
    ListAdapter adapter;
    AlertDialog.Builder builder;
    ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
    String sn;
    StockManager stockManager = new  StockManager(DeleteActivity.this);
    List<StockItem> stocklist = new ArrayList<StockItem>();

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
                if (msg.what == 7) {
                    listItems = (ArrayList<HashMap<String, String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(DeleteActivity.this, listItems,  R.layout.list_item,
                            new String[] { "StockName", "StockPrice", "StockCode" },new int[] { R.id.StockName, R.id.StockPrice, R.id.StockCode} );
                    listdelete.setAdapter(listItemAdapter);
                    listdelete.setOnItemLongClickListener(DeleteActivity.this);//添加长按事件监听
                }else if(msg.what == 8){
                    listdelete.setAdapter(adapter);
                    listdelete.setEmptyView(findViewById(R.id.nodata1));
                    Toast.makeText(DeleteActivity.this, "无股票信息，请添加！", Toast.LENGTH_SHORT).show();
                    Intent return_add = new Intent(DeleteActivity.this,AddActivity.class);
                    startActivityForResult(return_add,3);
                    finish();
                }
                super.handleMessage(msg);

            }
        };
    }

    public void Delete(View btn){
        if(btn.getId() == R.id.btn_return3){
            Intent return_main = new Intent(DeleteActivity.this,MainActivity.class);
            startActivityForResult(return_main,1);
            finish();
        }else if(btn.getId() == R.id.btn_deleteAll){
            stocklist = stockManager.listAll();
            if(stocklist.size() == 0){
                Toast.makeText(DeleteActivity.this, "没有数据可删除！", Toast.LENGTH_SHORT).show();
            }else{
                stockManager.deleteAll();
                Log.i(TAG, "已成功所有删除数据");
                listdelete.setAdapter(adapter);
                listdelete.setEmptyView(findViewById(R.id.nodata1));
                Toast.makeText(DeleteActivity.this, "已成功所有删除数据", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        Message msg = handler.obtainMessage();
        ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
        try{
            stocklist = stockManager.listAll();
            if(stocklist.size() == 0){
                msg.what = 8;
                Log.i(TAG,"数据库里无信息，请添加股票");
            }else{
                msg.what = 7;
                for(StockItem stockItem :stocklist){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("StockName", stockItem.getStockname());
                    map.put("StockPrice", stockItem.getStockprice());
                    map.put("StockCode",stockItem.getStockcode());
                    list1.add(map);
                }
            }
            msg.obj = list1;
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
        Object ip = listdelete.getItemAtPosition(position);
        final HashMap<String, String> map = (HashMap<String, String>)ip;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onItemLongClick: 删除股票");
                        // 删除数据项
                        listItems.remove(position);
                        // 更新适配器
                        listItemAdapter.notifyDataSetChanged();

                        //长按点击之后删除数据库里的数据
                        sn = map.get("StockName");
                        stockManager.deleteName(sn);
                        Log.i(TAG, "已成功删除数据：" + sn);
                        Toast.makeText(DeleteActivity.this, "已成功删除数据：" + sn, Toast.LENGTH_SHORT).show();

                        stocklist = stockManager.listAll();
                        if(stocklist.size() == 0){
                            listdelete.setAdapter(adapter);
                            listdelete.setEmptyView(findViewById(R.id.nodata1));
                        }
                    }
                }).setNegativeButton("否", null);
        builder.create().show();
        return false;
    }

}