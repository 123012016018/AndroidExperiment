package com.phj.quickbrowse.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.phj.quickbrowse.R;
import com.phj.quickbrowse.dao.Info;
import com.phj.quickbrowse.dao.InfoDAO;

public class EditorActivity extends AppCompatActivity {
    private EditText etTitle;
    private EditText etUrl;
    private EditText etParams;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        init();
    }

    private void init(){
        etTitle = findViewById(R.id.et_title);
        etUrl = findViewById(R.id.et_url);
        etParams = findViewById(R.id.et_params);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(v->{
            InfoDAO dao = new InfoDAO(EditorActivity.this);
            Info info = new Info();
            info.setTitle(etTitle.getText().toString());
            info.setUrl(etUrl.getText().toString());
            info.setParams(etParams.getText().toString());
            dao.add(info);
            dao.close();
            Toast.makeText(EditorActivity.this,"添加成功!",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditorActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
