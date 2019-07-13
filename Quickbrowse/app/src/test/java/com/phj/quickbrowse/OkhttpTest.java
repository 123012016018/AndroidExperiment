package com.phj.quickbrowse;

import com.phj.quickbrowse.util.HttpUtils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OkhttpTest {
    @Test
    public void testPostData(){
        Map<String, String> map = new HashMap<>();
        map.put("account", "123012016018");
        map.put("password", "jingru123");
        String url = "http://www.penghaojie.cn:2001/api";
        String response = HttpUtils.postData(url, map);
        System.out.println(response);
    }
}
