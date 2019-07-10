package com.example.projectx;

import android.database.Cursor;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class newEmployee extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EmpData empdata= new EmpData(this);
    TextView name;
    Button create;
    FirebaseFirestore employee;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_employee);

        employee =FirebaseFirestore.getInstance();

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

                Map empMap= new HashMap<>();
                empMap.put("type",spin.getSelectedItemPosition());
                empMap.put("password","0000");
                employee.collection("employees")
                        .document(name.getText().toString())
                        .set(empMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(newEmployee.this,"added to firebase",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(newEmployee.this,"failed to add it on firebase",Toast.LENGTH_SHORT).show();
                            }
                        });

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
