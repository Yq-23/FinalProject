package com.example.myfinalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
}