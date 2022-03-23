package com.example.ameenal_kaisiassignment4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AddItems extends AppCompatActivity {
    Button add_item_btn;
    EditText add_item_text;
    ListView items_listview;

    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> items_adapter;

    View.OnClickListener regular_btn_listener, listview_activated_btn_listener;
    AdapterView.OnItemClickListener listview_onItemClickListener;

    int longclicked_item_index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        add_item_btn = findViewById(R.id.add_item_btn);
        add_item_text = findViewById(R.id.add_item_text);
        items_listview = findViewById(R.id.items_listview);

        items = FileHandler.readData(this);

        items_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        items_listview.setAdapter(items_adapter);

        regular_btn_listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_item = add_item_text.getText().toString();

                add_item_text.setText("");

                // Q1
                if(new_item.isEmpty()) {
                    Snackbar.make(items_listview, "Cannot add empty item!",
                            Snackbar.LENGTH_LONG)
                            .setAction("X", another_view -> {

                            }).setBackgroundTint(Color.BLACK).setTextColor(Color.WHITE)
                            .show();
                }
                else {
                    items.add(new_item);
                    FileHandler.writeData(getApplicationContext(), items);

                    items_adapter.notifyDataSetChanged();
                }
            }
        };
        add_item_btn.setOnClickListener(regular_btn_listener);

        listview_onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddItems.this);
                alertBuilder.setTitle("Deletion")
                        .setMessage("Do you want to delete?")
                        .setCancelable(false)
                        .setNegativeButton("No", (dialog_interface, k) -> {})
                        .setPositiveButton("Yes", (dialog_interface, k) -> {
                            items.remove(i);
                            items_adapter.notifyDataSetChanged();
                            FileHandler.writeData(AddItems.this, items);
                        })
                        .create().show();
            }
        };
        items_listview.setOnItemClickListener(listview_onItemClickListener);

        // Q2
        // note: can replace getString(...) to "savelist.dat"
        Log.d("file-location",
                getFileStreamPath(getString(R.string.filename)).getAbsolutePath());

        // Q3 item long click listener
        items_listview.setOnItemLongClickListener( (adapter_view, view, i, l) -> {
            longclicked_item_index = i;
            add_item_btn.setOnClickListener(listview_activated_btn_listener);
            add_item_text.setText(items.get(i));

            // remove ability to delete items, as
            // if something is deleted while another item is being edited,
            // there could be index reference issues
            items_listview.setOnItemClickListener(null);
            return true;
        });

        // Q3 button functionality on long click
        listview_activated_btn_listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edited_item_text = add_item_text.getText().toString();

                add_item_text.setText("");

                if(edited_item_text.isEmpty()) {
                    Snackbar.make(items_listview, "Cannot change to an empty item!",
                            Snackbar.LENGTH_LONG)
                            .setAction("X", another_view -> {

                            }).setBackgroundTint(Color.BLACK).setTextColor(Color.WHITE)
                            .show();
                }
                else {
                    items.set(longclicked_item_index, edited_item_text);
                    FileHandler.writeData(getApplicationContext(), items);

                    items_adapter.notifyDataSetChanged();

                    // give previous behaviour back to the button and list view
                    add_item_btn.setOnClickListener(regular_btn_listener);
                    items_listview.setOnItemClickListener(listview_onItemClickListener);
                }
            }
        };
    }
}