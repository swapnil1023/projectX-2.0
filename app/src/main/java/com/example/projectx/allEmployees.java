package com.example.projectx;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class allEmployees extends AppCompatActivity {

    ListView lv;
    EmpData empDB;
    private FirebaseFirestore employee;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_employees);

        empDB = new EmpData(this);
        employee = FirebaseFirestore.getInstance();

        lv = findViewById(R.id.lvMenu);
        //Cursor cursor = empDB.getData();

        final ArrayList<String> name = new ArrayList();

        employee.collection("employees").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                if(e!=null)
                {
                    Toast.makeText(allEmployees.this,"some exception",Toast.LENGTH_SHORT).show();
                }
                for(DocumentChange emp: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(emp.getType() == DocumentChange.Type.ADDED)
                    {
                        name.add("Name : "+emp.getDocument().getId()+"\nType : "+ emp.getDocument().get("type").toString()+"\nPassword : "+emp.getDocument().getString("password"));
                    }
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(allEmployees.this,android.R.layout.simple_list_item_1, name);
                lv.setAdapter(arrayAdapter);
            }
        });

        /*while(cursor.moveToNext())
        {
            name.add("ID:  "+cursor.getString(0)+"\nname:  "+cursor.getString(1)+ "\nType:  "+ cursor.getString(2)+ "\nPassword:  "+ cursor.getString(3));
            ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, name);
            lv.setAdapter(arrayAdapter);
        }*/
    }
}
