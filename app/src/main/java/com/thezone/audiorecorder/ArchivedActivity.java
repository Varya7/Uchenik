package com.thezone.audiorecorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ArchivedActivity extends AppCompatActivity {
    DBHelper mydb;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    ListView simpleList;
    TextView empty_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        }
        // catch block to handle NullPointerException
        catch (NullPointerException e) {
        }

        getSupportActionBar().setTitle("Архив");
        setContentView(R.layout.activity_archived);

        mydb = new DBHelper(this);
        empty_text = (TextView) findViewById(R.id.empty_text);
        simpleList = (ListView) findViewById(R.id.ListView);
        simpleList.setEmptyView(empty_text);
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

        Cursor cursor = mydb.getArchivedData();
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

                    final AlertDialog.Builder builder = new AlertDialog.Builder(ArchivedActivity.this);
                    builder.setTitle(List.get(+position).get("title"));
                    //builder.setMessage(List.get(+position).get("description"));
                    builder.setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mydb.deleteNoteBookData(List.get(+position).get("id"));
                            populateData();
                        }
                    });
                    builder.setNegativeButton("Перенести в список", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            mydb.updateNoteBookStatus(List.get(+position).get("id"), 0);
                            populateData();

                        }
                    });
                    builder.setPositiveButton("НАЗАД", null);
                    builder.show();
                }
            });
        }
    }
}