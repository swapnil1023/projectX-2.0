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

public class reqAdapter extends ArrayAdapter<requirementClass> {

    private LayoutInflater mInflater;
    private ArrayList<requirementClass> reqList;
    private int mResource;

    public reqAdapter(@NonNull Context context, int resource, @NonNull List<requirementClass> objects)
    {
        super(context, resource, objects);
        this.mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.reqList = (ArrayList<requirementClass>) objects;
        this.mResource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = mInflater.inflate(mResource,null);

        requirementClass req = reqList.get(position);

        TextView title = convertView.findViewById(R.id.adapterEmpName);
        TextView description = convertView.findViewById(R.id.adapterEmpType);

        title.setText(req.getTitle());
        description.setText(req.getDescription());

        return convertView;
    }

}
