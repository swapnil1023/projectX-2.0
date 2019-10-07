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

public class empAdapter extends ArrayAdapter<classForEmp> {

    private LayoutInflater mInflater;
    private ArrayList<classForEmp> empList;
    private int mResource;

    public empAdapter(@NonNull Context context, int resource, @NonNull List<classForEmp> objects)
    {
        super(context, resource, objects);
        this.mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.empList = (ArrayList<classForEmp>) objects;
        this.mResource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = mInflater.inflate(mResource,null);

        classForEmp emp = empList.get(position);

        TextView name = convertView.findViewById(R.id.adapterEmpName);
        TextView pass = convertView.findViewById(R.id.adapterEmpPass);
        TextView type = convertView.findViewById(R.id.adapterEmpType);

        name.setText(emp.getName());
        pass.setText("Password : "+emp.getPassword());
        if(emp.getType()==0)
            type.setText("Type : Receptionist");
        else if(emp.getType()==1)
            type.setText("Type : Chef");
        else
            type.setText("Type : Delivery Person");

        return convertView;
    }
}
