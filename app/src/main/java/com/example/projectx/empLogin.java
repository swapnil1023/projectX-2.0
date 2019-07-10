package com.example.projectx;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class empLogin extends AppCompatActivity {
    EmpData emp = new EmpData(this);
    Button can;
    TextView empId;
    TextView pass;
    Button login;
    private FirebaseFirestore employee;
    DocumentReference empRef;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_login);

        employee = FirebaseFirestore.getInstance();

        final Spinner spin = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.empType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        can = findViewById(R.id.can);
        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(empLogin.this);
                builder.setMessage("Are You Sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent i = new Intent(empLogin.this, MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Nahh",null).setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        empId = findViewById(R.id.empIdLogin);
        pass = findViewById(R.id.empPassLogin);

        login = findViewById(R.id.empLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if(!empId.getText().toString().isEmpty())
                    {
                        empRef = employee.collection("employees").document(empId.getText().toString());

                        empRef.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot)
                                    {
                                        if(documentSnapshot.getString("password").equals(pass.getText().toString()) && documentSnapshot.get("type").toString().equals(String.valueOf(spin.getSelectedItemId())))
                                        {
                                            Toast.makeText(empLogin.this, "login successful", Toast.LENGTH_SHORT).show();
                                            Intent i;
                                            if(spin.getSelectedItemPosition() == 1)
                                                i = new Intent(empLogin.this, chefPortal.class);
                                            else if(spin.getSelectedItemPosition() == 0)
                                                i = new Intent(empLogin.this, receptionPortal.class);
                                            else
                                                i = new Intent(empLogin.this, changeEmpPass.class);
                                            i.putExtra("empId", empId.getText().toString());
                                            startActivity(i);
                                        }
                                        else
                                            Toast.makeText(empLogin.this, "Incorrect User Details", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Toast.makeText(empLogin.this, "login failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else
                        Toast.makeText(empLogin.this, "empty fields", Toast.LENGTH_SHORT).show();



                    /*if (pass.getText().toString().equals(emp.getEmpPass(empId.getText().toString())) && emp.getType(empId.getText().toString()).equals(String.valueOf(spin.getSelectedItemPosition()))) {

                        Toast.makeText(empLogin.this, "login successful", Toast.LENGTH_SHORT).show();
                        Intent i;
                        if(spin.getSelectedItemPosition() == 1)
                             i = new Intent(empLogin.this, chefPortal.class);
                        else if(spin.getSelectedItemPosition() == 0)
                            i = new Intent(empLogin.this, receptionPortal.class);
                        else
                             i = new Intent(empLogin.this, changeEmpPass.class);
                        i.putExtra("empId", empId.getText().toString());
                        startActivity(i);
                    } else
                        Toast.makeText(empLogin.this, "Incorrect User Details", Toast.LENGTH_SHORT).show();*/
                }
                catch(Exception e)
                {
                    Toast.makeText(empLogin.this, "Enter a valid Employee Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(empLogin.this);
        builder.setTitle("Exit?")
                .setMessage("Are You Sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        empLogin.super.onBackPressed();
                    }
                })
                .setNegativeButton("Nahh",null).setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

}
