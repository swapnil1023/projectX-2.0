package com.example.projectx;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminLogin extends AppCompatActivity {
    Button logout;
    Button allOrders;
    Button report;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        logout= findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(adminLogin.this);
                builder.setMessage("Are You Sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent i = new Intent(adminLogin.this, MainActivity.class);
                                finish();
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Nahh",null).setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Button addemp = findViewById(R.id.add_emp);
        addemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(adminLogin.this,newEmployee.class);
                startActivity(i);
            }
        });

        Button employees = findViewById(R.id.emp);
        employees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(adminLogin.this,allEmployees.class);
                startActivity(i);
            }
        });

        Button remove = findViewById(R.id.remov_emp);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(adminLogin.this,deleteEmployee.class);
                startActivity(i);
            }
        });

        Button changePass = findViewById(R.id.changeAdminPass);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(adminLogin.this,changeAdminPass.class);
                startActivity(i);
            }
        });

        allOrders = findViewById(R.id.all_ord);
        allOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(adminLogin.this,allOrders.class);
                startActivity(i);
            }
        });

        report = findViewById(R.id.bus_repo);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(adminLogin.this,businessReport.class);
                startActivity(i);
            }
        });
    }
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(adminLogin.this);
        builder.setTitle("Exit?")
                .setMessage("Are You Sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        adminLogin.super.onBackPressed();
                       /* Intent i = new Intent(empLogin.this, MainActivity.class);
                        startActivity(i);*/
                    }
                })
                .setNegativeButton("Nahh",null).setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
