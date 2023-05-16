package com.thezone.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button note, tim, rec, todo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // try block to hide Action bar
        try {
            this.getSupportActionBar().hide();
        }
        // catch block to handle NullPointerException
        catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_main);
        note = (Button) findViewById(R.id.notee);
        note.setOnClickListener(this);
        tim = (Button) findViewById(R.id.timm);
        tim.setOnClickListener(this);
        rec = (Button) findViewById(R.id.recc);
        rec.setOnClickListener(this);
        todo = (Button) findViewById(R.id.todoo);
        todo.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notee:
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                break;
            case R.id.timm:
                Intent intent1 = new Intent(this, TimerActivity.class);
                startActivity(intent1);
                break;
            case R.id.recc:
                Intent intent2 = new Intent(this, RecordingActivity.class);
                startActivity(intent2);
                break;
            case R.id.todoo:
                Intent intent3 = new Intent(this, TodoActivity.class);
                startActivity(intent3);
                break;
            default: break;
        }
    }
}