package com.phj.quickbrowse.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phj.quickbrowse.MyDataAdapter;
import com.phj.quickbrowse.util.Constants;
import com.phj.quickbrowse.util.HttpUtils;
import com.phj.quickbrowse.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Map<String,Object> obj = (Map)msg.obj;
            if(obj==null) return;
            Intent intent = new Intent(MainActivity.this, WebActivity.class);
            Iterator<String> iterator = obj.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = obj.get(key);
                if(value instanceof String){
                    intent.putExtra(key, value.toString());
                }
            }
            startActivity(intent);
        }
    };
    private Button btJwgl;
    private TextView tvJwgl;
    private Button btnBinding;
    private Button btnExtend;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init(){
        btJwgl = findViewById(R.id.bt_jwgl);
        tvJwgl = findViewById(R.id.tv_jwgl);
        btnBinding = findViewById(R.id.btn_binding);
        btnExtend = findViewById(R.id.btn_extend);
        listView = findViewById(R.id.lv);
        JwglClickEvent event = new JwglClickEvent();
        btJwgl.setOnClickListener(event);
        tvJwgl.setOnClickListener(event);
        btnBinding.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, BindingActivity.class);
            startActivity(intent);
        });
        btnExtend.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            startActivity(intent);
        });
        MyDataAdapter adapter = new MyDataAdapter(this, handler);
        listView.setAdapter(adapter);
    }

    private class JwglClickEvent implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            SharedPreferences preferences = MainActivity.this.getSharedPreferences("jwgl", Context.MODE_PRIVATE);
            String account = preferences.getString("account", "");
            String password = preferences.getString("password", "");
            if (account.equals("") || password.equals("")) {
                Toast.makeText(MainActivity.this, "请先绑定账号!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(MainActivity.this, "处理中，请稍等...", Toast.LENGTH_LONG).show();
            new Thread(){
                @Override
                public void run() {
                    Message message = handler.obtainMessage();
                    Map<String, String> param = new HashMap<>();
                    param.put("account", account);
                    param.put("password", password);
                    String json = HttpUtils.postData(Constants.JWGL_SERVER_API, param);
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        Map<String,Object> response = mapper.readValue(json, Map.class);
                        response.put("callback", response.get("callback") + "?xh=" + account);
                        Log.d("PHJ", response.toString());
                        message.obj = response;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    handler.sendMessage(message);
                }
            }.start();
        }
    }
}
