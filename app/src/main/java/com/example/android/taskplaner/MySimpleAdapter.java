package com.example.android.taskplaner;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MySimpleAdapter extends ArrayAdapter<String>{

    private Context context;
    private List<String> actions;
    private int[] done;

    public MySimpleAdapter(@NonNull Context context, List<String> actions, int[] done) {
        super(context, R.layout.row_layout, actions);
        this.context = context;
        this.actions = actions;
        this.done = done;
    }

    static class ViewHolder{
        TextView text;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView)convertView.findViewById(R.id.row_text);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if (done[position] == 1){
            convertView.setBackgroundResource(R.color.checked);
        }else {
            convertView.setBackgroundResource(android.R.color.transparent);
        }
        viewHolder.text.setText(actions.get(position));
        return convertView;
    }
}
