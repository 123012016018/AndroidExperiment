package com.phj.quickbrowse.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class InfoDAO {
    private static final String TABLE_NAME = "info";
    private static final String ID = "_id";
    private static final String TITLE = "title";
    private static final String URL = "url";
    private static final String PARAMS = "params";
    private DatabaseHelper helper;
    private SQLiteDatabase readDB;
    private SQLiteDatabase writeDB;
    private Context context;

    public InfoDAO(Context context) {
        this.context = context;
        this.helper = new DatabaseHelper(context);
        this.readDB = helper.getReadableDatabase();
        this.writeDB = helper.getWritableDatabase();
    }

    public void add(Info info) {
        String sql = "insert into info (title,url,params) values(?,?,?)";
        writeDB.execSQL(sql,new Object[]{info.getTitle(),info.getUrl(),info.getParams()});
    }

    public void delete(Integer id) {
        writeDB.delete("info", "_id=?", new String[]{"" + id});
    }


    //获取数据库中的所有笔记
    public List<Info> listAll() {
        String sql = "select * from " + TABLE_NAME;
        return listAll(sql,null);
    }

    //获取数据库中满足某些条件的所有笔记
    public List<Info> listAll(String sql,String[] args){
        List<Info> list = new ArrayList<>();
        Cursor cursor = readDB.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            Info info = new Info();
            info.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            info.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            info.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
            info.setParams(cursor.getString(cursor.getColumnIndex(PARAMS)));
            list.add(info);
        }
        return list;
    }

    public void close(){
        readDB.close();
        writeDB.close();
        helper.close();
    }
}
