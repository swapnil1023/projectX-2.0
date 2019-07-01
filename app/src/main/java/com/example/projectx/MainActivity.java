package com.example.projectx;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button b1;
    Button b2;
    EmpData password =new EmpData(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1= findViewById(R.id.admin);
        b2=findViewById(R.id.emp);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final View vi = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_admin_pass,null);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Admin Login")
                        .setView(vi)
                        .setPositiveButton("Login", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                EditText admin_pass = vi.findViewById(R.id.admin_pass);

                                String pass = admin_pass.getText().toString();
                                if(pass.equals(password.getPass()))
                                {
                                    Intent in;
                                    in = new Intent(MainActivity.this,adminLogin.class);
                                    startActivity(in);
                                    Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"Wrong Password Try Again",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                EditText admin_pass = vi.findViewById(R.id.admin_pass);
                                admin_pass.setText("");
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent in = new Intent(MainActivity.this,empLogin.class);
                startActivity(in);
                Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit?")
                .setMessage("You sure, you wanna exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        finish();
                        System.exit(1);
                    }
                })
                .setNegativeButton("Nope",null).setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

    }
}
