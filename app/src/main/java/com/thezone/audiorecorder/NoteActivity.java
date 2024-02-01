package com.thezone.audiorecorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteActivity extends AppCompatActivity {
    DBHelper mydb;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    ListView simpleList;
    FloatingActionButton add_button;
    TextView empty_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //this.getSupportActionBar().hide();
            getSupportActionBar().show();
        }
        // catch block to handle NullPointerException
        catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_note);

        mydb = new DBHelper(this);
        empty_text = (TextView) findViewById(R.id.empty_text);
        simpleList = (ListView) findViewById(R.id.ListView);
        simpleList.setEmptyView(empty_text);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteActivity.this,AddAndModifyNoteBookData.class);
                i.putExtra("action","add");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.archive:
                startActivity(new Intent(NoteActivity.this, ArchivedActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateData();
    }

    public void populateData() {
        mydb = new DBHelper(this);

        runOnUiThread(new Runnable() {
            public void run() {
                fetchDataFromDB();
            }
        });
    }

    public void fetchDataFromDB() {
        dataList.clear();

        Cursor cursor = mydb.getNoteBookData();
        loadDataList(cursor, dataList);
    }

    public void loadDataList(Cursor cursor, final ArrayList<HashMap<String, String>> List) {
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                HashMap<String, String> mapData = new HashMap<String, String>();
                mapData.put("id", cursor.getString(0).toString());
                mapData.put("title", cursor.getString(1).toString());
                mapData.put("description", cursor.getString(2).toString());
                mapData.put("date", cursor.getString(3).toString());
                mapData.put("fevourite", cursor.getString(4).toString());
                mapData.put("status", cursor.getString(5).toString());
                List.add(mapData);
                cursor.moveToNext();
            }
            ListNoteBookAdapter adapter = new ListNoteBookAdapter(this, List, mydb);
            simpleList.setAdapter(adapter);
            simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                    builder.setTitle(List.get(+position).get("title"));
                    builder.setMessage(List.get(+position).get("description"));
                    builder.setNeutralButton("Заархивировать", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mydb.updateNoteBookStatus(List.get(+position).get("id"), 1);
                            populateData();
                        }
                    });

                    builder.setNegativeButton("ИЗМЕНИТЬ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent i = new Intent(NoteActivity.this,AddAndModifyNoteBookData.class);
                            i.putExtra("id",List.get(+position).get("id"));
                            i.putExtra("title",List.get(+position).get("title"));
                            i.putExtra("description",List.get(+position).get("description"));
                            i.putExtra("action","edit");
                            startActivity(i);
                        }
                    });
                    builder.setPositiveButton("НАЗАД", null);
                    builder.show();
                }
            });
        }
    }
    public void ShowArchive(View view){
        startActivity(new Intent(NoteActivity.this, ArchivedActivity.class));
    }
}