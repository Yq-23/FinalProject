package com.example.myfinalproject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfinalproject.Adapter.MyAdapter;
import com.example.myfinalproject.DB.StockItem;
import com.example.myfinalproject.DB.StockManager;
import com.example.myfinalproject.Util.GetURL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddActivity extends AppCompatActivity implements Runnable{

    private static final String TAG = "AddActivity";
    EditText textCode;
    Button btn_return;
    Handler handler;
    String str;
    String ss = "sh600606";
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        textCode = (EditText)findViewById(R.id.EditCode);
        btn_return = (Button)findViewById(R.id.btn_return);

    }

    public void Add(View btn){
        if(btn.getId() == R.id.btn_submit){
            str = textCode.getText().toString();
            if(str==null || str.equals("")){
                //no input
                Toast.makeText(AddActivity.this, "请输入股票代码！", Toast.LENGTH_SHORT).show();
            }else{
                Thread t = new Thread(this);
                t.start();
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 9) {
                            switch(count){
                                case 0:
                                    Toast.makeText(AddActivity.this, "股票代码输入有误！", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(AddActivity.this, "股票代码已存在，不可重复添加！", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    Toast.makeText(AddActivity.this, "已成功添加数据！", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                        super.handleMessage(msg);
                    }
                };
            }
        }else if(btn.getId() == R.id.btn_return2){
            Intent return_main = new Intent(this,MainActivity.class);
            startActivityForResult(return_main,1);
        }
    }

    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        Message msg = handler.obtainMessage(9);
        try{
            List<StockItem> stocklist = new ArrayList<StockItem>();
            StockManager stockManager = new  StockManager(AddActivity.this);
            String s = "https://hq.sinajs.cn/list=" + str;
            String a = GetURL.SendGET(s);
            Pattern p = Pattern.compile("\"(.*?)\"");
            Matcher m = p.matcher(a);
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
            while (m.find()) {
                list.add(m.group().trim().replace("\"","")+" ");
            }
            if (list.remove(" ")) {
                Log.i(TAG, "run:股票代码输入有误！");
                textCode.setText("");
                count = 0;
            }else{
                String stock = list.toString();
                String[] d1 = stock.split(",");
                // 获取股票代码
                String c1 = str;
                if(c1.equals(ss)){
                    Log.i(TAG, "run:股票代码已存在！");
                    textCode.setText("");
                    count = 1;
                }else{
                    // 获取股票名字
                    String c = d1[0].substring(1);
                    // 获取当前价格
                    String c3 = d1[3];

                    StockItem stockItem = new StockItem(c, c1, c3);
                    //stocklist.add(stockItem);
                    Log.i(TAG, "run: 成功添加数据: " + c + " ==> " + c1 + " ==> " + c3);

                    count = 2;
                    textCode.setText("");
                }
            }
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}