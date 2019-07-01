package com.example.projectx;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class deleteEmployee extends AppCompatActivity {
    EmpData emp;
    TextView id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        emp = new EmpData(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_employee);
        id= findViewById(R.id.remEmpId);

        Button remove = findViewById(R.id.remEmp);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(id.getText().toString().equals(""))
                    Toast.makeText(deleteEmployee.this,"You have not entered any value",Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(deleteEmployee.this);
                    try
                    {
                        builder.setTitle("Remove Employee")
                                .setMessage("Are you sure? you want to remove " + emp.getName(id.getText().toString()).toUpperCase())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        int deletedRows = emp.delete(id.getText().toString());
                                        if (deletedRows > 0)
                                            Toast.makeText(deleteEmployee.this, "Employee Removed", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(deleteEmployee.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        id.setText("");
                                    }
                                })
                                .setNegativeButton("No", null).setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(deleteEmployee.this, "Please enter a valid Employee ID", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        Button clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                id.setText("");
            }
        });
    }
}
