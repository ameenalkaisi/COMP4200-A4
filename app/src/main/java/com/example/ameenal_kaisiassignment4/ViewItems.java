package com.example.ameenal_kaisiassignment4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ViewItems extends AppCompatActivity {
    FloatingActionButton addItemsButton;
    ListView itemsListView;

    ArrayAdapter<String> itemsAdapter;
    ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        addItemsButton = findViewById(R.id.addItemsButton);
        itemsListView = findViewById(R.id.itemsListView);

        addItemsButton.setOnClickListener(view -> {
            Intent addItemsIntent = new Intent(ViewItems.this, EditAddItems.class);
            //q5
            addItemsIntent.putExtra("mode", "add");
            startActivity(addItemsIntent);
        });

        //q5
        itemsListView.setOnItemLongClickListener( (adapterView, view, i, l) -> {
            Intent editItemsIntent = new Intent(ViewItems.this, EditAddItems.class);
            editItemsIntent.putExtra("mode", "edit");
            editItemsIntent.putExtra("index", i);
            startActivity(editItemsIntent);

            return true;
        });

        itemsListView.setOnItemClickListener( (adapterView, view, i, l) -> {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ViewItems.this);
            alertBuilder.setTitle("Deletion")
                    .setMessage("Do you want to delete?")
                    .setCancelable(false)
                    .setNegativeButton("No", (dialog_interface, k) -> {})
                    .setPositiveButton("Yes", (dialog_interface, k) -> {
                        itemsAdapter.remove(items.remove(i));
                        itemsAdapter.notifyDataSetChanged();
                        FileHandler.writeData(ViewItems.this, items);
                    })
                    .create().show();
        });

        items = FileHandler.readData(this);

        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        itemsListView.setAdapter(itemsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        items = FileHandler.readData(this);
        itemsAdapter.clear();
        itemsAdapter.addAll(items);
        itemsAdapter.notifyDataSetChanged();
    }
}