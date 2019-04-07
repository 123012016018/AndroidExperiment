package com.example.uicomponentexp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAdapterActivity extends Activity {
    String[] animalNames = new String[]{"Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant"};
    int[] animalImages = new int[]{
            R.drawable.lion,
            R.drawable.tiger,
            R.drawable.monkey,
            R.drawable.dog,
            R.drawable.cat,
            R.drawable.elephant
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_adpater);
        ListView simpleListView = findViewById(R.id.listView);

        List<Map<String, Object>> listItems = new ArrayList<>();
        for(int i=0;i<animalNames.length;i++){
            Map<String, Object> map = new HashMap<>();
            map.put("animalName", animalNames[i]);
            map.put("animalImage", animalImages[i]);
            listItems.add(map);
        }
        String[] from = new String[]{"animalName", "animalImage"};
        int[] to = new int[]{R.id.textView, R.id.imageView};
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.simple_adapter_listview_item, from, to);
        simpleListView.setAdapter(adapter);

        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), animalNames[position], Toast.LENGTH_SHORT).show();
            }
        });

    }
}
