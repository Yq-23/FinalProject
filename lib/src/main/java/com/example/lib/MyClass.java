package com.example.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {

    public static void main(String[] args){

        String str = "sh601006";
        List<String> stockList = new ArrayList<String>();
        stockList.add(null);
        System.out.println(stockList);

        /*String s = "http://hq.sinajs.cn/list=" + str;
        String a = SendGET(s);
        String[] d = a.split(";");
        Pattern p = Pattern.compile("\"(.*?)\"");
        Matcher m = p.matcher(a);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < d.length; i++) {
            //System.out.println(d[i]);
            while (m.find()) {
                list.add(m.group().trim().replace("\"","")+" ");
            }
            if (list.remove(" ")) {
                System.out.println("Error!");
            }else{
                String stock = list.toString();
                String[] d1 = stock.split(",");

                String c0 = d1[0].substring(1);
                System.out.println("Name:" + c0);

                String c1 = str;
                System.out.println("Code:" + c1);

                String c2 = d1[3];
                System.out.println("Price:" + c2);
            }
        }*/
    }

        public static String SendGET(String url) {
            String result = "";//访问返回结果
            BufferedReader read = null;//读取访问结果
            try {
                java.net.URL realurl = new java.net.URL(url);
                URLConnection connection = realurl.openConnection();
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                connection.connect();
                Map<String, List<String>> map = connection.getHeaderFields();
                for (String key : map.keySet()) {
                    //System.out.println(key + "--->" + map.get(key));
                }
                read = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), "GBK"));
                String line;        //循环读取
                while ((line = read.readLine()) != null) {
                    result += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (read != null) {
                    try {
                        read.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }


}