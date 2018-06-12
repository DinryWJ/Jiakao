package com.example.a82712.jiakao;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;


/**
 *驾照题库调用示例代码 － 聚合数据
 *在线接口文档：http://www.juhe.cn/docs/183
 **/

public class JuheDemo {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY ="a8a7b6238ae297c64377eb30258c16f7";

    //1.题库接口
    public static Object getRequest1(){
        String result =null;
        String url ="http://api2.juheapi.com/jztk/query";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//您申请的appKey
        params.put("subject","1");//选择考试科目类型，1：科目1；4：科目4
        params.put("model","c1");//驾照类型，可选择参数为：c1,c2,a1,a2,b1,b2；当subject=4时可省略
        params.put("testType","rand");//测试类型，rand：随机测试（随机100个题目），order：顺序测试（所选科目全部题目）

        try {
            result =net(url, params, "GET");
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
                System.out.println(object.get("result"));
                return object.get("result");
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //2.answer字段对应答案
    public static void getRequest2(){
        String result =null;
        String url ="http://api2.juheapi.com/jztk/answers";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//您申请的appk

        try {
            result =net(url, params, "GET");
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
                System.out.println(object.get("result"));
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        //parseJsonWithGson(String.valueOf(JuheDemo.getRequest1()));
    }
//    private static void parseJsonWithGson(String jsonData){
//        Gson gson = new Gson();
//        List<QuestionHolder> list = gson.fromJson(jsonData,new TypeToken<List<QuestionHolder>>(){}.getType());
//        for (QuestionHolder holder : list){
//            System.out.print("MainActivity"+"id is"+holder.getId());
//            System.out.print("MainActivity"+"question is"+holder.getQuestion());
//            System.out.print("MainActivity"+"answer is"+holder.getAnswer());
//            System.out.print("MainActivity"+"item1 is"+holder.getItem1());
//            System.out.print("MainActivity"+"item2 is"+holder.getItem2());
//            System.out.print("MainActivity"+"item3 is"+holder.getItem3());
//            System.out.print("MainActivity"+"item4 is"+holder.getItem4());
//            System.out.print("MainActivity"+"explains is"+holder.getExplains());
//            System.out.print("MainActivity"+"url is"+holder.getUrl());
//            System.out.println();
//        }
//    }
    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}