package com.example.notepad.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.BaseAdapter;

import com.example.notepad.entity.NoteVO;
import com.example.notepad.http.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.notepad.constant.NoteTable.*;


public class NoteDAO {
    private static final String TAG = "NOTEPAD";
    public static final String BACKUP_URL = "http://120.24.246.59:8888/backup";
    public static final String SYNCHRONIZATION_URL = "http://120.24.246.59:8888/query";
    private DatabaseHelper helper;
    private SQLiteDatabase readDB;
    private SQLiteDatabase writeDB;
    private Context context;

    public NoteDAO(Context context) {
        this.context = context;
        this.helper = new DatabaseHelper(context);
        readDB = helper.getReadableDatabase();
        writeDB = helper.getWritableDatabase();
    }

    //添加笔记到数据库中
    public void add(NoteVO noteVO) {
        String sql = "insert into "+TABLE_NAME+" ("+ID+","+TITLE+","+CONTENT+","+LAST_UPDATE+") values(?,?,?,?)";
        writeDB.execSQL(sql, new Object[]{noteVO.getId(),
                noteVO.getTitle(), noteVO.getContent(), noteVO.getLastUpdate()});
    }

    //根据笔记ID从数据库中删除
    public void delete(int id) {
        writeDB.delete(TABLE_NAME, ID+"=?", new String[]{id + ""});
    }


    //更新笔记
    public void update(NoteVO noteVO) {
        ContentValues values = new ContentValues();
        values.put(TITLE, noteVO.getTitle());
        values.put(CONTENT, noteVO.getContent());
        values.put(LAST_UPDATE,noteVO.getLastUpdate());
        writeDB.update(TABLE_NAME, values, ID+"=?", new String[]{noteVO.getId() + ""});
    }

    //获取数据库中的所有笔记
    public List<NoteVO> listAll() {
        String sql = "select * from " + TABLE_NAME;
        return listAll(sql,null);
    }

    //获取数据库中满足某些条件的所有笔记
    public List<NoteVO> listAll(String sql,String[] args){
        List<NoteVO> list = new ArrayList<>();
        Cursor cursor = readDB.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            NoteVO noteVO = new NoteVO();
            noteVO.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            noteVO.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            noteVO.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
            noteVO.setLastUpdate(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)));
            list.add(noteVO);
        }
        return list;
    }

    //根据标题进行模糊搜索
    public List<NoteVO> search(String keyword){
        String sql = "select * from "+TABLE_NAME+" where title like '%"+keyword+"%'";
        return listAll(sql,null);
    }

    //云备份，将数据库中所有的笔记同步到我的阿里云
    public void backup() {
        List<NoteVO> list = this.listAll();//从数据库中获取所有笔记
        ObjectMapper mapper = new ObjectMapper();
        try {
            String value = mapper.writeValueAsString(list);//序列化成json字符串
            Log.d(TAG, value);//日志
            Map<String, String> map = new HashMap<>();
            map.put("data", value);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //将数据提交到服务器上
                    HttpUtils.postData(BACKUP_URL, map);
                }
            });
            t.start();//开启一个后台线程进行数据的提交
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    //云同步,将我的阿里云上数据库中的笔记同步到本地
    public void synchronization() {
        //清空本地数据
        writeDB.delete(TABLE_NAME, null, null);
        String html = HttpUtils.getHtml(SYNCHRONIZATION_URL);
        ObjectMapper mapper = new ObjectMapper();
        //反序列化
        try {
            JsonNode readTree = mapper.readTree(html);
            Iterator<JsonNode> elements = readTree.elements();
            NoteDAO dao = new NoteDAO(NoteDAO.this.context);
            while (elements.hasNext()) {
                JsonNode node = elements.next();
                NoteVO noteVO = mapper.readValue(node.toString(), NoteVO.class);
                dao.add(noteVO);//将服务器上的数据保存到本地
            }
            dao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void close(){
        readDB.close();
        writeDB.close();
        helper.close();
    }




}
