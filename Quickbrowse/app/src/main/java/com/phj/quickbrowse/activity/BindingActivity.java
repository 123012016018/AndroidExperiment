package com.phj.quickbrowse.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.phj.quickbrowse.R;

public class BindingActivity extends AppCompatActivity {
    private Button btnJwglBind;
    private Button btnXgcBind;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        btnJwglBind = findViewById(R.id.btn_jwgl_bind);
        btnXgcBind = findViewById(R.id.btn_xgc_bind);
        btnBack = findViewById(R.id.btn_back);
        btnJwglBind.setOnClickListener(v->{
            createDialog("jwgl");
        });
        btnXgcBind.setOnClickListener(v->{
            createDialog("xgc");
        });
        btnBack.setOnClickListener(v->{
            Intent intent = new Intent(BindingActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }


    private void createDialog(String tag){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater flater = getLayoutInflater();
        View view = flater.inflate(R.layout.custom_dialog, null);
        builder.setView(view)
                .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText etAccount = view.findViewById(R.id.username);
                        EditText etPassword = view.findViewById(R.id.password);
                        String account = etAccount.getText().toString();
                        String password = etPassword.getText().toString();
                        SharedPreferences preferences = BindingActivity.this.getSharedPreferences(tag, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("account", account);
                        editor.putString("password", password);
                        editor.commit();
                        System.out.printf("%s:%s,%s\n",tag,account,password);
                        Toast.makeText(BindingActivity.this,"绑定成功!",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("cancel ...");
                    }
                });
        builder.create();
        builder.show();
    }
}
