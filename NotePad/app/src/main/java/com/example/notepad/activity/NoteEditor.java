package com.example.notepad.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.notepad.MainActivity;
import com.example.notepad.R;
import com.example.notepad.dao.NoteDAO;
import com.example.notepad.entity.NoteVO;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.notepad.constant.NoteTable.CONTENT;
import static com.example.notepad.constant.NoteTable.ID;
import static com.example.notepad.constant.NoteTable.LAST_UPDATE;
import static com.example.notepad.constant.NoteTable.TITLE;


public class NoteEditor extends AppCompatActivity implements View.OnClickListener{
    private EditText etTitle;
    private EditText etContent;
    private NoteVO noteVO = null;
    private String type = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        init();
    }

    private void init(){
        etTitle = findViewById(R.id.et_adt_title);
        etContent = findViewById(R.id.et_adt_content);
        Button btnSave = findViewById(R.id.btn_adt_save);
        btnSave.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras==null) return;
        String type = extras.getString("type");
        String title = extras.getString(TITLE);
        String content = extras.getString(CONTENT);
        String date = extras.getString(LAST_UPDATE);
        int id = extras.getInt(ID);
        if(type.equals("query")){
            etTitle.setText(title);
            etContent.setText(content);
            etTitle.setEnabled(false);
            etContent.setEnabled(false);
            btnSave.setVisibility(View.INVISIBLE);
        } else if ("edit".equals(type)) {
            this.noteVO = new NoteVO();
            etTitle.setText(title);
            etContent.setText(content);
            noteVO.setId(id);
            this.type = "edit";
        }
    }

    @Override
    public void onClick(View v) {
        if(noteVO==null)
            noteVO = new NoteVO();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        noteVO.setLastUpdate(sdf.format(new Date()));
        noteVO.setTitle(etTitle.getText().toString());
        noteVO.setContent(etContent.getText().toString());
        NoteDAO dao = new NoteDAO(this);
        if(!"edit".equals(this.type))
            dao.add(noteVO);
        else
            dao.update(noteVO);
        dao.close();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
