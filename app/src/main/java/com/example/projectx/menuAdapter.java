package com.example.projectx;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class menuAdapter extends ArrayAdapter<classForMenu> {

    private LayoutInflater mInflater;
    private ArrayList<classForMenu> menuList;
    private static final String TAG = "menuAdapter";
    private int mResource;

    public menuAdapter(Context context, int resource, ArrayList<classForMenu> objects)
    {
        super(context, resource, objects);
        this.menuList = objects;
        mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent)
    {
        convertView = mInflater.inflate(mResource,null);

        classForMenu item = menuList.get(position);

        TextView name = convertView.findViewById(R.id.itemName);
        TextView price = convertView.findViewById(R.id.itemPrice);

        name.setText(item.getName());
        price.setText(item.getPrice());

        return convertView;
    }
}
