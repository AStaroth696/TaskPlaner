package com.example.android.taskplaner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<String>{

    private Context context;
    private List<String> actions;

    public MyListAdapter(@NonNull Context context, List<String> actions){
        super(context, R.layout.list_item, actions);
        this.actions = actions;
        this.context = context;
    }

    static class ViewHolder{
        TextView text;
        Button button;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        final int pos = position;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView)convertView.findViewById(R.id.action_edit);
            viewHolder.button = (Button)convertView.findViewById(R.id.delete_action);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actions.remove(pos);
                    notifyDataSetInvalidated();
                }
            });
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.text.setText(actions.get(position));
        return convertView;
    }
}
