package com.example.amna.fyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NewShalimarComputer on 9/29/2016.
 */
public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    private static class ViewHolder {
        TextView tvExp;
        Button btnDelExp;

    }

    public MyCustomAdapter(ArrayList<String> list, Context context) {
        //super(context,R.layout.list_item,list);
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;//list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        String exp = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.tvExp = (TextView) convertView.findViewById(R.id.itemName);
            viewHolder.btnDelExp = (Button) convertView.findViewById(R.id.btnDelExp);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.tvExp.setText(exp);

        // Return the completed view to render on screen
        return convertView;

    }
}