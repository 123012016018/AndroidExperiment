package com.example.uicomponentexp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

public class AlertDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);
        Button button = findViewById(R.id.custom_dialog_btn);
        button.setOnClickListener(v->{
            createDialog();
        });
    }


    private void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater flater = getLayoutInflater();

        builder.setView(flater.inflate(R.layout.custom_dialog, null))
                .setPositiveButton("sign in", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("sign in the user");
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("cancel ...");
                    }
                });
        builder.create();
        builder.show();
    }
}
