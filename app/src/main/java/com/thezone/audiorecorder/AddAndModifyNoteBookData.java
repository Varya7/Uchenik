package com.thezone.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddAndModifyNoteBookData extends AppCompatActivity {
    DBHelper mydb;
    String title_data, des_data,i_action,i_id;
    TextView title,description;
    Button save_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        i_action = i.getStringExtra("action");
        if(i_action.trim().contentEquals("edit")){
           // getSupportActionBar().setTitle("Изменение");
        }else{
            //getSupportActionBar().setTitle("Добавить");
        }

        setContentView(R.layout.activity_add_and_modify_note_book_data);

        mydb = new DBHelper(this);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        save_btn = findViewById(R.id.save_btn);


        if(i_action.trim().contentEquals("edit")){
            i_id = i.getStringExtra("id");
            String i_title = i.getStringExtra("title");
            String i_des = i.getStringExtra("description");

            title.setText(i_title);
            description.setText(i_des);
            save_btn.setText("СОХРАНИТЬ");
        }


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_data = title.getText().toString();
                des_data = description.getText().toString();

                if(i_action.trim().contentEquals("edit") && title_data.length() > 3){
                    if(title_data.trim().length() > 0){
                        mydb.updateNoteBookData(i_id, title_data, des_data);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"Название пустое.",Toast.LENGTH_SHORT).show();
                    }

                }
                if(i_action.trim().contentEquals("add")){
                    String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    if(title_data.trim().length() > 0){
                        mydb.insertNoteBookData(title_data, des_data, date.toString());
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"Название пустое",Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });


        //mydb.insertNoteBookData(title_data, des_data, date.toString());
    }
}
