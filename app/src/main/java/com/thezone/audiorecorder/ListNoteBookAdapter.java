package com.thezone.audiorecorder;

import android.app.Activity;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListNoteBookAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private DBHelper database;

    public ListNoteBookAdapter(Activity a, ArrayList<HashMap<String, String>> d, DBHelper mydb) {
        activity = a;
        data = d;
        database = mydb;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ListTaskViewHolder holder = null;
        if (convertView == null) {
            holder = new ListTaskViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.activity_notebook_list_items, parent, false);
            holder.title = convertView.findViewById(R.id.title);
            holder.description = convertView.findViewById(R.id.description);
            holder.date = convertView.findViewById(R.id.date);
            holder.fevourite = convertView.findViewById(R.id.fev);
            convertView.setTag(holder);
        } else {
            holder = (ListTaskViewHolder) convertView.getTag();
        }


        final HashMap<String, String> singleTask = data.get(position);
        final ListTaskViewHolder tmpHolder = holder;

        holder.title.setId(position);
        holder.description.setId(position);
        holder.date.setId(position);
        holder.fevourite.setId(position);

        try {

            holder.title.setText(Html.fromHtml(singleTask.get("title")));
            holder.description.setText(Html.fromHtml(singleTask.get("description")));
            holder.date.setText(Html.fromHtml(singleTask.get("date")));
            if(singleTask.get("fevourite").trim().contentEquals("0")){
                holder.fevourite.setImageResource(R.drawable.fev_off);
            }else{
                holder.fevourite.setImageResource(R.drawable.fev_on);
            }

            holder.fevourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor cursor = database.getSingleNoteBookData(singleTask.get("id"));
                    cursor.moveToFirst();
                    if(cursor.getString(4).toString().contentEquals("0")){
                        database.updateNoteBookFev(singleTask.get("id"),1);
                        tmpHolder.fevourite.setImageResource(R.drawable.fev_on);
                    }else{
                        database.updateNoteBookFev(singleTask.get("id"),0);
                        tmpHolder.fevourite.setImageResource(R.drawable.fev_off);
                    }
                }
            });


        } catch (Exception e) {
        }
        return convertView;
    }
}

class ListTaskViewHolder {
    TextView title;
    TextView description;
    TextView date;
    ImageView fevourite;
}