package com.example.projectx;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class chefPortal extends AppCompatActivity {
    menu_orders menu;
    Button showMenu;
    TextView changePass;
    ListView currentOrders;
    FirebaseFirestore orders;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_portal);

        orders = FirebaseFirestore.getInstance();

        showMenu = findViewById(R.id.showMenu);
        menu = new menu_orders(this);
        changePass = findViewById(R.id.changePassChef);
        currentOrders = findViewById(R.id.currentOrdersChef);

        Intent i = getIntent();
        final String empId = i.getStringExtra("empId");

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i;
                i = new Intent(chefPortal.this, changeEmpPass.class);
                i.putExtra("empId", empId);
                startActivity(i);
            }
        });

        showMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(chefPortal.this, menu.class);
                i.putExtra("empType","1");
                startActivity(i);
            }
        });

        final ArrayList<String> Currorders = new ArrayList<>();

        orders.collection("All Orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                if(e!=null)
                {
                    Toast.makeText(chefPortal.this,"some exception",Toast.LENGTH_SHORT).show();
                }
                for(DocumentChange emp: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(emp.getType() == DocumentChange.Type.ADDED)
                    {
                        if(emp.getDocument().getString("status").equals("placed"))
                            Currorders.add(emp.getDocument().getId()+"\nItem : "+ emp.getDocument().get("Item").toString()+"\nQuantity : "+emp.getDocument().get("Quantity").toString());
                    }
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(chefPortal.this,android.R.layout.simple_list_item_1, Currorders);
                currentOrders.setAdapter(arrayAdapter);
            }
        });

        currentOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(chefPortal.this,Currorders.get(position).charAt(0),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
