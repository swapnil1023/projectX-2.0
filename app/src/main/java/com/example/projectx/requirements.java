package com.example.projectx;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class requirements extends AppCompatActivity {

    FloatingActionButton add;
    FirebaseFirestore requirements ;
    ListView list;
    requirementClass reqClass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements);
        add = findViewById(R.id.addReq);
        list = findViewById(R.id.reqList);
        final View vi = LayoutInflater.from(requirements.this).inflate(R.layout.add_req,null);
        requirements = FirebaseFirestore.getInstance();


        final ArrayList<requirementClass> reqList = new ArrayList();
        requirements.collection("requirements").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                if(e!=null)
                {
                    Toast.makeText(requirements.this,"some exception",Toast.LENGTH_SHORT).show();
                }
                for(DocumentChange Snap: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(Snap.getType() == DocumentChange.Type.ADDED)
                    {
                        reqClass = new requirementClass(Snap.getDocument().getString("Title"),Snap.getDocument().getString("Description"));
                        reqList.add(reqClass);
                    }
                }
                reqAdapter adapter = new reqAdapter(requirements.this,R.layout.req_card,reqList);
                list.setAdapter(adapter);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(requirements.this);
                builder.setTitle("Add New Requirement")
                        .setView(vi)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                final EditText title = vi.findViewById(R.id.reqTitle);
                                final EditText desc = vi.findViewById(R.id.reqDesc);
                                Map map = new HashMap<>();
                                map.put("Title",title.getText().toString());
                                map.put("Description",desc.getText().toString());

                                requirements.collection("requirements").document()
                                        .set(map)
                                        .addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o)
                                            {
                                                Toast.makeText(requirements.this,"Requirement recorded",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e)
                                            {
                                                Toast.makeText(requirements.this,"Something went wrong",Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                final EditText title = vi.findViewById(R.id.reqTitle);
                                final EditText description = vi.findViewById(R.id.reqDesc);

                                title.setText("");
                                description.setText("");
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }
}
