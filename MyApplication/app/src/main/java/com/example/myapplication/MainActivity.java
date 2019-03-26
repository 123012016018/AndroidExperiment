package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLinearLayoutDemo = findViewById(R.id.btn_linearlayout);
        btnLinearLayoutDemo.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, LinearLayoutDemoActivity.class);
            startActivity(intent);
        });

        Button btnConstraintLayoutDemo = findViewById(R.id.btn_contraintlayout);
        btnConstraintLayoutDemo.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ConstraintLayoutDemoActivity.class);
            startActivity(intent);
        });

        Button btnTableLayoutDemo = findViewById(R.id.btn_tablelayout);
        btnTableLayoutDemo.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, TableLayoutDemoActivity.class);
            startActivity(intent);
        });

    }
}
