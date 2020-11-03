package com.example.myfinalproject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.myfinalproject.Util.GetURL;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Runnable{

    private static final String TAG = "MainActivity";
    TextView SHpoint, SZpoint, CYpoint;
    Handler handler;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SHpoint = (TextView)findViewById(R.id.SHpoint);
        SZpoint = (TextView)findViewById(R.id.SZpoint);
        CYpoint = (TextView)findViewById(R.id.CYpoint);

        Thread t = new Thread(this);
        t.start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    List<String> list = (List<String>) msg.obj;
                    SHpoint.setText(list.get(0));
                    SZpoint.setText(list.get(1));
                    CYpoint.setText(list.get(2));
                }
                super.handleMessage(msg);

            }
        };
    }

    public void OnClick(View btn){
        switch (btn.getId()){
            case R.id.btn_search:
                Intent result = new Intent(this,ResultActivity.class);
                startActivityForResult(result,2);
                finish();
                break;
            case R.id.btn_add:
                Intent add = new Intent(this,AddActivity.class);
                startActivityForResult(add,3);
                finish();
                break;
            case R.id.btn_delete:
                Intent delete = new Intent(this,DeleteActivity.class);
                startActivityForResult(delete,4);
                finish();
                break;
        }
    }

    public void run() {
        Log.i(TAG,"run:run()......");
        Message msg = handler.obtainMessage(1);
        List<String> list1 = new ArrayList<String>();
        s = "https://hq.sinajs.cn/list=s_sh000001,s_sz399001,s_sz399006";   //上证指数，深证成指，创业板指
        try{
            String a = GetURL.SendGET(s);
            String[] d = a.split(";");
            for (int i = 0; i < d.length; i++) {
                // 获取当前点数
                String c1 = d[i].split(",")[1];
                list1.add(c1);
            }
            msg.obj = list1;
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//启用菜单项
        getMenuInflater().inflate(R.menu.first_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//处理菜单事件，设置菜单项中每一个item的功能
        if(item.getItemId()==R.id.menu_exit){
            //设置功能，与设置按钮的事件一样
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }
}