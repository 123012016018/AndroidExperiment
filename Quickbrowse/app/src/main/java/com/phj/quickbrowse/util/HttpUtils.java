package com.phj.quickbrowse.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {
    //获取url的页面数据
    public static String getHtml(String url) {
        String result = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    //将数据post到服务器上
    public static String postData(String url, Map<String, String> map) {
        String result = "";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.add(key, map.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getCookieByPost(String url, Map<String, String> map) {
        String result = null;
        String json = postData(url, map);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String,String> value = mapper.readValue(json, Map.class);
            result = value.get("cookie");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
