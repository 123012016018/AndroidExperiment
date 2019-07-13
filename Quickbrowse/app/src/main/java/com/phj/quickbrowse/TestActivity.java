package com.phj.quickbrowse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.phj.quickbrowse.activity.WebActivity;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("url", "http://jwgl.fjnu.edu.cn/xs_main.aspx?xh=123012016018");
        intent.putExtra("cookie", "ASP.NET_SessionId=ykjshre3l1gwzirploumyxav; path=/");
        startActivity(intent);
    }
}
