package com.example.myfinalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void OnClick(View btn){
        switch (btn.getId()){
            case R.id.btn_search:
                Intent result = new Intent(this,ResultActivity.class);
                startActivityForResult(result,2);
                break;
            case R.id.btn_add:
                Intent add = new Intent(this,AddActivity.class);
                startActivityForResult(add,3);
                break;
            case R.id.btn_delete:
                Intent delete = new Intent(this,DeleteActivity.class);
                startActivityForResult(delete,4);
        }
    }

    /*@Override
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
    }*/
}