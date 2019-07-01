package com.example.projectx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class changeEmpPass extends AppCompatActivity {

    EmpData pass =new EmpData(this);
    TextView newPass;
    TextView oldPass;
    TextView newPass2;
    Button change;
    Button clear;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_admin_pass);

        Intent i = getIntent();
        final String empId = i.getStringExtra("empId");

        change = findViewById(R.id.empPassChange);
        newPass= findViewById(R.id.empNewPass);
        newPass2= findViewById(R.id.empNewPass2);
        oldPass= findViewById(R.id.empOldPass);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if( newPass.getText().toString().equals("") || newPass2.getText().toString().equals("") || oldPass.getText().toString().equals("") )
                    Toast.makeText(changeEmpPass.this,"Please enter all the fields",Toast.LENGTH_SHORT).show();
                else
                {
                    if(oldPass.getText().toString().equals(pass.getEmpPass(empId)))
                    {
                        if (newPass.getText().toString().equals(newPass2.getText().toString()))
                        {
                            boolean isIns = pass.changeEmpPass(newPass.getText().toString(),empId);
                            if (isIns)
                                Toast.makeText(changeEmpPass.this, "Password Changed to " + pass.getEmpPass(empId), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(changeEmpPass.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(changeEmpPass.this,"The Re-entered Password Doesn't Match The New Password",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(changeEmpPass.this,"Incorrect old password",Toast.LENGTH_SHORT).show();

                }
            }
        });

        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                oldPass.setText("");
                newPass.setText("");
                newPass2.setText("");
            }
        });
    }
}
