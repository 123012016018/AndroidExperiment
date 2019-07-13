package com.phj.quickbrowse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phj.quickbrowse.activity.MainActivity;
import com.phj.quickbrowse.dao.Info;
import com.phj.quickbrowse.dao.InfoDAO;
import com.phj.quickbrowse.util.Constants;
import com.phj.quickbrowse.util.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDataAdapter extends BaseAdapter {
    private List<Info> list;
    private Context context;
    private Handler handler;
    public MyDataAdapter(Context context,Handler handler) {
        super();
        this.context = context;
        this.handler = handler;
        init();
    }


    public void init(){
        InfoDAO dao = new InfoDAO(context);
        this.list = dao.listAll();
        dao.close();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.list_item, null);
        Info info = list.get(position);
        TextView tvTitle = view.findViewById(R.id.item_tv_title);
        tvTitle.setText(info.getTitle());

        view.setOnClickListener(new MyClickListener(info));
        ImageView ivDelete = view.findViewById(R.id.item_iv_delete);
        ivDelete.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("删除")
                    .setMessage("确认删除吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            InfoDAO dao = new InfoDAO(context);
                            dao.delete(info.getId());
                            list = dao.listAll();
                            dao.close();
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"取消删除",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create()
                    .show();
        });


        return view;
    }

     private class  MyClickListener implements View.OnClickListener{
        private Info info;

        public MyClickListener(Info info) {
            this.info = info;
        }
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "处理中，请稍等...", Toast.LENGTH_LONG).show();
            new Thread(){
                @Override
                public void run() {
                    Message message = handler.obtainMessage();
                    Map<String, String> param = resolveParams(info.getParams());
                    String json = HttpUtils.postData(info.getUrl(), param);
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        Map<String,Object> response = mapper.readValue(json, Map.class);
                        message.obj = response;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    handler.sendMessage(message);
                }
            }.start();
        }
    }


    private Map<String, String> resolveParams(String text) {
        text = text.replaceAll("\\s", "");
        Map<String, String> result = new HashMap<>();
        String[] strings = text.split("&");
        for (String s : strings) {
            String[] split = s.split("=");
            if (split.length == 2) {
                result.put(split[0], split[1]);
            }
        }
        return result;
    }
}
