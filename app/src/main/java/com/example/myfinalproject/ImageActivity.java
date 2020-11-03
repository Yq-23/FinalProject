package com.example.myfinalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.myfinalproject.DB.StockManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageActivity extends AppCompatActivity implements Runnable{

    private static final String TAG = "ImageActivity";
    ImageView image;
    TextView txtImage;
    Handler handler;
    String uri;
    String s, sn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        image = (ImageView)findViewById(R.id.image_view);

        Intent intent = getIntent();
        //使用Bundle传递参数时获取数据
        Bundle bdl_image = intent.getExtras();
        String stockname = bdl_image.getString("StockName","");
        float stockprice = bdl_image.getFloat("StockPrice",0.1f);
        sn = stockname;

        Log.i(TAG,"StockName = " + stockname + "  StockPrice = " + stockprice);

        txtImage = (TextView) findViewById(R.id.TitleImage);
        txtImage.setText(stockname + "——日K线图");

        Thread t = new Thread(this);
        t.start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 7) {
                    Bitmap bitmap = (Bitmap)msg.obj;
                    image.setImageBitmap(bitmap);
                }
                super.handleMessage(msg);

            }
        };
    }

    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        StockManager stockManager = new  StockManager(ImageActivity.this);
        s = stockManager.findByName(sn).stockcode;
        Log.i(TAG,sn + " 的日K线图");
        uri = "https://image.sinajs.cn/newchart/daily/n/"+ s +".gif";
        Bitmap bitmap = getBitmap(uri);
        if (bitmap != null) {
        Message msg = new Message();
        msg.what = 7;
        msg.obj = bitmap;
        handler.sendMessage(msg);
        }
    }

    private Bitmap getBitmap(String uri) {
        HttpURLConnection connection = null;
        try {
            // 1、获得图片的url
            URL url = new URL(uri);
            // 2、获得网络连接
            connection = (HttpURLConnection) url.openConnection();
            // 3、设置请求的一些常用参数
            connection.setReadTimeout(3000);          // 设置连接去读取数据的最长时间
            connection.setConnectTimeout(3000);       // 设置超时
            connection.setDoInput(true);              // 设置请求可以让服务器写入数据
            // 4、请求图片，然后把从服务器上请求的二进制流保存到inputStream里面
            connection.connect();
            InputStream in = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 5、关闭网络连接
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public void re(View V){
        Intent return_result = new Intent(ImageActivity.this,ResultActivity.class);
        startActivityForResult(return_result,2);
    }


}