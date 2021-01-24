package com.example.simpletodo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT = 20;
    List<String> items = new ArrayList<>();

    Button cItems;
    Button btnAdd;
    RecyclerView rvItems;
    EditText etItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cItems = findViewById(R.id.cItem);
        btnAdd = findViewById(R.id.btnAdd);
        rvItems = findViewById(R.id.rvItems);
        etItems = findViewById(R.id.etItems);

        loadItems();
        cItems.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // remove all items from list
                items.clear();
                itemsAdapter.notifyItemRangeRemoved(0, items.size() - 1);
                Toast.makeText(getApplicationContext(), "All items removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
        ItemsAdapter.OnClickedListener clickedListener = new ItemsAdapter.OnClickedListener() {
            @Override
            public void onItemClicked(int Position) {
                Log.e("MainActivity", "Single click at position" + Position);

                // creating the new activity when button is click
                Intent i = new Intent(MainActivity.this, EditActivity.class );
                i.putExtra(KEY_ITEM_TEXT, items.get(Position));
                i.putExtra(KEY_ITEM_POSITION, Position);
                // passing the data
                startActivityForResult(i, EDIT_TEXT);
            }
        };

        ItemsAdapter.OnLongClickedListener savingData = new ItemsAdapter.OnLongClickedListener() {
            @Override
            public void onItemLongClicked(int position, TextView tvItem) {
                tvItem.setPaintFlags(tvItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };

        // passing items to our ItemsAdapter
        itemsAdapter = new ItemsAdapter(items, savingData, clickedListener);
        //set adapter to class we made
        rvItems.setAdapter(itemsAdapter);
        // set how its display
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItems.getText().toString();
                //Add item to model
                items.add(todoItem);

                //Notify it has been added to model
                itemsAdapter.notifyItemInserted(items.size() -1);
                etItems.setText("");
                // popup to user confirming add
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }

    // handle the result of edit activity
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == EDIT_TEXT && resultCode == RESULT_OK) {

            // Retrieve the edited made by EditActivity
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // Retrieve the position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //updating the database
            items.set(position, itemText);
            // notify the adapter of changes made
            itemsAdapter.notifyItemChanged(position);
            // saving the data
            saveItems();
            //tells user that
            Toast.makeText(getApplicationContext(), "Edited Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Log.w("MainActivity", " Unknown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // this function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading message", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing message", e);
        }
    }

}