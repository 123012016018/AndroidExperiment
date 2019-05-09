package com.example.notepad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notepad.activity.NoteEditor;
import com.example.notepad.dao.NoteDAO;
import com.example.notepad.entity.NoteVO;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            adapter.init();
            adapter.notifyDataSetChanged();
        }
    };
    private Button btnAdd;
    private Button btnBackup;
    private Button btnSynchronization;
    private EditText etSearch;
    private MyDataAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new MyDataAdapter(this);
        ListView listView = findViewById(R.id.lv);
        listView.setAdapter(adapter);
        initBtnAdd();
        initBtnBackup();
        initBtnSynchronization();
        initEtSearch();
    }

    private void initBtnAdd(){
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, NoteEditor.class);
            startActivity(intent);
        });
    }

    private void initEtSearch(){
        etSearch = findViewById(R.id.et_search);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //判断回车键是否被按下
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String keyword = etSearch.getText().toString();
                    NoteDAO dao = new NoteDAO(MainActivity.this);
                    List<NoteVO> list = dao.search(keyword);
                    adapter.setKeyword(list, keyword);
                    adapter.notifyDataSetChanged();
                    dao.close();
                    return true;
                }
                return false;
            }
        });
    }

    private void initBtnBackup(){
        btnBackup = findViewById(R.id.btn_backup);
        btnBackup.setOnClickListener(v->{
            NoteDAO dao = new NoteDAO(MainActivity.this);
            dao.backup();
            dao.close();
            Toast.makeText(MainActivity.this,"云备份成功!",Toast.LENGTH_SHORT).show();
        });
    }

    private void initBtnSynchronization(){
        btnSynchronization = findViewById(R.id.btn_synchronization);
        btnSynchronization.setOnClickListener(v->{

            synchronization(handler);
            Toast.makeText(MainActivity.this,"正在同步!",Toast.LENGTH_SHORT).show();
        });
    }

    public void synchronization(Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NoteDAO dao = new NoteDAO(MainActivity.this);
                dao.synchronization();
                dao.close();
                handler.sendMessage(new Message());
            }
        }).start();
    }

}
