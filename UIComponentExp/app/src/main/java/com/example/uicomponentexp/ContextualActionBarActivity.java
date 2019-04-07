package com.example.uicomponentexp;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Set;

public class ContextualActionBarActivity extends ListActivity {
    private String[] data = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine","Ten"};
    private SelectionAdapter selectionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contextual_action_bar);

        selectionAdapter = new SelectionAdapter(this, R.layout.raw_list_item, R.id.textView1, data);
        setListAdapter(selectionAdapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private int nr = 0;
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    nr++;
                    selectionAdapter.setNewSelection(position,checked);
                }else{
                    nr--;
                    selectionAdapter.removeSelection(position);
                }
                mode.setTitle(nr+" selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                nr = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.contextual_menu,menu);
                Log.d("mydebug", "onCreateActionMode");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.item_delete:
                        nr = 0;
                        selectionAdapter.clearSelection();
                        mode.finish();
                }
                Log.d("mydebug", "onActionItemClcked");
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selectionAdapter.clearSelection();
            }

        });

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                // TODO Auto-generated method stub

                getListView().setItemChecked(position, !selectionAdapter.isPositionChecked(position));
                return false;
            }
        });
    }

    private class SelectionAdapter extends ArrayAdapter<String>{
        private HashMap<Integer, Boolean> map = new HashMap<>();
        public SelectionAdapter(Context context, int resource, int textViewResourceId,String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public void setNewSelection(int position, boolean value) {
            map.put(position, value);
            notifyDataSetChanged();
        }

        public boolean isPositionChecked(int position) {
            Boolean result = map.get(position);
            return result==null?false:result;
        }

        public Set<Integer> getCurrentCheckedPosition(){
            return map.keySet();
        }

        public void removeSelection(int position) {
            map.remove(position);
            notifyDataSetChanged();
        }

        public void clearSelection(){
            map = new HashMap<>();
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            if (map.get(position) != null) {
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            }
            return view;
        }
    }
}
