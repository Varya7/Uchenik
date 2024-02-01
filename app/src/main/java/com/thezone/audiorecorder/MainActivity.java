package com.thezone.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton note, tim, rec, todo, phot;
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
        setContentView(R.layout.activity_main1);

        note = (ImageButton) findViewById(R.id.notee);
        note.setOnClickListener(this);
        tim = (ImageButton) findViewById(R.id.timm);
        tim.setOnClickListener(this);
        rec = (ImageButton) findViewById(R.id.recc);
        rec.setOnClickListener(this);
        todo = (ImageButton) findViewById(R.id.todoo);
        todo.setOnClickListener(this);
        phot = (ImageButton) findViewById(R.id.phott);
        phot.setOnClickListener(this);
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
            case R.id.phott:
                Intent intent4 = new Intent(this, PhotoActivity.class);
                startActivity(intent4);
            default: break;
        }
    }
}