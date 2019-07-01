package com.example.projectx;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class newEmployee extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EmpData empdata= new EmpData(this);
    TextView name;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_employee);

        final Spinner spin = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.empType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(this);
        name = findViewById(R.id.empName);
        create = findViewById((R.id.create));
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               boolean isIns = empdata.insertData(name.getText().toString(),  spin.getSelectedItemPosition());
               if(isIns)
                   Toast.makeText(newEmployee.this,"Employee Added",Toast.LENGTH_SHORT).show();
               else
                   Toast.makeText(newEmployee.this,"Employee Addition Failed",Toast.LENGTH_SHORT).show();
               name.setText("");
            }
        });





        Button clear = findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TextView name = findViewById(R.id.empName);
                name.setText("");
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
