package com.example.projectx;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class changeAdminPass extends AppCompatActivity {

    EmpData pass =new EmpData(this);
    TextView newPass;
    TextView oldPass;
    TextView newPass2;
    Button change;
    Button clear;
    ProgressBar prog;
    FirebaseFirestore adminPass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_admin_pass);

        adminPass = FirebaseFirestore.getInstance();
        change = findViewById(R.id.empPassChange);
        newPass= findViewById(R.id.empNewPass);
        newPass2= findViewById(R.id.empNewPass2);
        oldPass= findViewById(R.id.empOldPass);
        prog = findViewById(R.id.prog6);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if( newPass.getText().toString().equals("") || newPass2.getText().toString().equals("") || oldPass.getText().toString().equals("") )
                    Toast.makeText(changeAdminPass.this,"Please enter all the fields",Toast.LENGTH_SHORT).show();
                else
                    {
                        prog.setVisibility(View.VISIBLE);
                        adminPass.collection("admin password").document("password").get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot)
                                    {
                                        if(oldPass.getText().toString().equals(documentSnapshot.getString("password")))
                                        {
                                            if (newPass.getText().toString().equals(newPass2.getText().toString()))
                                            {
                                                Map passMap = new HashMap();
                                                passMap.put("password",newPass.getText().toString());

                                                adminPass.collection("admin password").document("password").update(passMap)
                                                        .addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o)
                                                            {
                                                                Toast.makeText(changeAdminPass.this, "Password Changed in fireStore", Toast.LENGTH_SHORT).show();
                                                                prog.setVisibility(View.INVISIBLE);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e)
                                                            {
                                                                Toast.makeText(changeAdminPass.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            }
                                            else
                                            {
                                                Toast.makeText(changeAdminPass.this,"The Re-entered Password Doesn't Match The New Password",Toast.LENGTH_SHORT).show();
                                                prog.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(changeAdminPass.this,"Incorrect old password",Toast.LENGTH_SHORT).show();
                                            prog.setVisibility(View.INVISIBLE);
                                        }

                                    }
                                });

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
