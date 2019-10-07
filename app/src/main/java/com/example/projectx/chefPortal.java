package com.example.projectx;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class chefPortal extends AppCompatActivity {
    menu_orders menu;
    Button showMenu;
    TextView changePass;
    ListView currentOrders;
    FirebaseFirestore orders;
    ProgressBar prog;
    Button req;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_portal);

        orders = FirebaseFirestore.getInstance();
        prog = findViewById(R.id.progressBar4);
        showMenu = findViewById(R.id.showMenu);
        menu = new menu_orders(this);
        req = findViewById(R.id.req);
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

        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(chefPortal.this,requirements.class);
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
                            Currorders.add("Order no: "+emp.getDocument().getId()+"\nItem : "+ emp.getDocument().get("Item").toString()+"\nQuantity : "+emp.getDocument().get("Quantity").toString());
                    }
                }

            }
        });

        final ArrayAdapter arrayAdapter = new ArrayAdapter(chefPortal.this,android.R.layout.simple_list_item_1, Currorders);
        currentOrders.setAdapter(arrayAdapter);

        currentOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                prog.setVisibility(View.VISIBLE);
                final int orderID = orderID(Currorders.get(position).toCharArray());
                Task<DocumentSnapshot> task1;
                task1 = orders.collection("All Orders").document(String.valueOf(orderID)).get();
                while(!task1.isComplete());
                DocumentSnapshot ds = task1.getResult();

                String itemName = ds.getString("Item");

                final AlertDialog.Builder builder = new AlertDialog.Builder(chefPortal.this);
                builder.setTitle("Order Prepared?")
                        .setCancelable(false)
                        .setMessage(ds.get("Quantity").toString()+" "+ itemName+" prepared?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                orders.collection("All Orders").document(String.valueOf(orderID))
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot)
                                            {
                                                Map statusUpdate = new HashMap<>();
                                                statusUpdate.put("status","prepared");
                                                statusUpdate.put("Prepared By",empId);
                                                orders.collection("All Orders").document(String.valueOf(orderID)).update(statusUpdate);
                                                prog.setVisibility(View.INVISIBLE);
                                                Toast.makeText(chefPortal.this,"Updated",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                Currorders.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                prog.setVisibility(View.INVISIBLE);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    int orderID(char a[])
    {
        String id ="";
        id=id+a[10];
        for(int i=11;i<15;i++)
        {
            if(a[i]>='0' && a[i]<='9')
                id=id+a[i];
            else
                break;
        }

        return Integer.parseInt(id);
    }
}
