package com.example.uicomponentexp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnSimpleAdapter;
    Button btnAlertDialog;
    Button btnDefineMenu;
    Button btnContextualAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSimpleAdapter = findViewById(R.id.btn_simple_adapter);
        btnSimpleAdapter.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, SimpleAdapterActivity.class);
            startActivity(intent);
        });

        btnAlertDialog = findViewById(R.id.btn_alert_dialog);
        btnAlertDialog.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, AlertDialogActivity.class);
            startActivity(intent);
        });

        btnDefineMenu = findViewById(R.id.btn_define_menu);
        btnDefineMenu.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, XmlDefineMenuActivity.class);
            startActivity(intent);
        });

        btnContextualAction = findViewById(R.id.btn_contextual_action);
        btnContextualAction.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ContextualActionBarActivity.class);
            startActivity(intent);
        });
    }
}
