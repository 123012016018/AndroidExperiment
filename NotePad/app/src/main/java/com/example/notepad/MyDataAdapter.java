package com.example.notepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.activity.NoteEditor;
import com.example.notepad.dao.NoteDAO;
import com.example.notepad.entity.NoteVO;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.notepad.constant.NoteTable.CONTENT;
import static com.example.notepad.constant.NoteTable.ID;
import static com.example.notepad.constant.NoteTable.LAST_UPDATE;
import static com.example.notepad.constant.NoteTable.TITLE;

public class MyDataAdapter extends BaseAdapter {
    private List<NoteVO> list;
    private Context context;
    private String keyword = null;
    MyDataAdapter(Context context) {
        super();
        this.context = context;
        init();
    }

    public void setKeyword(List<NoteVO> list, String keyword) {
        this.list =  list;
        this.keyword = keyword;
    }

    public void init(){
        NoteDAO dao = new NoteDAO(context);
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
        NoteVO noteVO = list.get(position);
        TextView tvTitle = view.findViewById(R.id.item_tv_title);
        if(keyword==null)
            tvTitle.setText(noteVO.getTitle());
        else
            tvTitle.setText(highlight(noteVO.getTitle(),keyword,"#EA2D2D",0,0));
        TextView tvDate = view.findViewById(R.id.item_tv_date);
        tvDate.setText(noteVO.getLastUpdate());
        view.setOnClickListener(new MyClickListener(noteVO, "query"));
        ImageView ivEdit = view.findViewById(R.id.item_iv_edit);
        ivEdit.setOnClickListener(new MyClickListener(noteVO, "edit"));
        ImageView ivDelete = view.findViewById(R.id.item_iv_delete);
        ivDelete.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("删除笔记")
                    .setMessage("确认删除吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NoteDAO dao = new NoteDAO(context);
                            dao.delete(noteVO.getId());
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
        private NoteVO noteVO;
        private String type;

        public MyClickListener(NoteVO noteVO, String type) {
            this.noteVO = noteVO;
            this.type = type;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, NoteEditor.class);
            intent.putExtra("type", type);
            intent.putExtra(TITLE, noteVO.getTitle());
            intent.putExtra(CONTENT, noteVO.getContent());
            intent.putExtra(ID, noteVO.getId());
            intent.putExtra(LAST_UPDATE, noteVO.getLastUpdate());
            context.startActivity(intent);
        }
    }


    /**
     *
     * @param text 关键字
     * @param target 需要匹配的目标文本
     * @param color 关键字的颜色
     * @return
     */
    private static SpannableString highlight(String text, String target,
                                            String color, int start, int end) {
        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile(target);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
            spannableString.setSpan(span, matcher.start() - start, matcher.end() + end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
