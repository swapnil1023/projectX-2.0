package com.example.projectx;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class receptionPortal extends AppCompatActivity {

    AutoCompleteTextView itemName;
    TextView quantity;
    TextView total;
    Button add;
    Button clear;
    menu_orders menu;
    ListView current;
    Button clearOrder;
    TextView changePass;
    Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception_portal);

        menu = new menu_orders(this);

        itemName = findViewById(R.id.nameRecep);
        quantity = findViewById(R.id.quantityRecep);
        total = findViewById(R.id.totalRecep);
        add = findViewById(R.id.addRecep);
        clear = findViewById(R.id.clearRecep);
        current = findViewById(R.id.listCurrent);
        clearOrder = findViewById(R.id.clearOrder);
        changePass= findViewById(R.id.changePassRecep);
        placeOrder = findViewById(R.id.placeOrder);
        final FirebaseFirestore fMenu;

        Intent i = getIntent();
        final String empId = i.getStringExtra("empId");

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i;
                i = new Intent(receptionPortal.this, changeEmpPass.class);
                i.putExtra("empId", empId);
                startActivity(i);
            }
        });

        fMenu = FirebaseFirestore.getInstance();

        ArrayList<String> orders = new ArrayList();
        final Cursor cursor1 = menu.getCurrentData();
        ArrayAdapter arrayAdapterList;
        while(cursor1.moveToNext())
        {
            orders.add("Item: "+cursor1.getString(1) + "\nQuantity: "+cursor1.getString(2));
        }
        arrayAdapterList = new ArrayAdapter(receptionPortal.this,android.R.layout.simple_list_item_1, orders);
        current.setAdapter(arrayAdapterList);


        final ArrayList<String> name = new ArrayList<>();
        fMenu.collection("menu").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                if(e!=null)
                {
                    Toast.makeText(receptionPortal.this,"some exception",Toast.LENGTH_SHORT).show();
                }
                for(DocumentChange menuSnap: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(menuSnap.getType() == DocumentChange.Type.ADDED)
                    {
                        name.add(menuSnap.getDocument().getId());
                    }
                }
                ArrayAdapter Adapter = new ArrayAdapter(receptionPortal.this,android.R.layout.simple_list_item_1, name);
                itemName.setAdapter(Adapter);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(itemName.getText().toString().equals("")  || quantity.getText().toString().equals(""))
                {
                    Toast.makeText(receptionPortal.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    String name = itemName.getText().toString();
                    final int quan = Integer.parseInt(quantity.getText().toString());
                    fMenu.collection("menu").document(name).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
                        {
                            int price;
                             price = Integer.parseInt(documentSnapshot.getString("price"));
                            int orderTotal = Integer.parseInt(total.getText().toString());
                            orderTotal+= price*quan;
                            total.setText(String.valueOf(orderTotal));
                        }
                    });

                    boolean isIns = menu.insertCurrent(name,quan);
                    if (isIns)
                        Toast.makeText(receptionPortal.this, "Item Added", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(receptionPortal.this, "Item Addition Failed", Toast.LENGTH_SHORT).show();


                    itemName.setText("");
                    quantity.setText("");

                    ArrayList<String> orders = new ArrayList();
                    Cursor cursor1 = menu.getCurrentData();
                    ArrayAdapter arrayAdapterList;
                    while(cursor1.moveToNext())
                    {
                        orders.add("Item: "+cursor1.getString(1) + "\nQuantity: "+cursor1.getString(2));
                    }
                    arrayAdapterList = new ArrayAdapter(receptionPortal.this,android.R.layout.simple_list_item_1, orders);
                    current.setAdapter(arrayAdapterList);
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                itemName.setText("");
                quantity.setText("");
            }
        });

        clearOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                menu.clearOrder();
                itemName.setText("");
                quantity.setText("");
                total.setText("0");
                ArrayList<String> orders = new ArrayList();
                final ArrayAdapter arrayAdapterList;
                arrayAdapterList = new ArrayAdapter(receptionPortal.this,android.R.layout.simple_list_item_1, orders);
                current.setAdapter(arrayAdapterList);
                Toast.makeText(receptionPortal.this,"Order cleared",Toast.LENGTH_SHORT).show();
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Cursor cursor = menu.getCurrentData();
                DocumentReference idRef = fMenu.collection("docID").document("Current");
                DocumentReference itemPrice;
                cursor.moveToFirst();
                Task<DocumentSnapshot> task1;
                Task<DocumentSnapshot> task2;
                while(!cursor.isAfterLast()) {

                    itemPrice = fMenu.collection("menu").document(cursor.getString(1));
                    task1 = idRef.get();
                    task2 = itemPrice.get();
                    while(!task1.isComplete() && !task2.isComplete()) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    DocumentSnapshot menuSnap = task2.getResult();
                    DocumentSnapshot ds = task1.getResult();
                    int ID = Integer.parseInt(ds.get("Current").toString());
                    int price = Integer.parseInt(menuSnap.get("price").toString());
                    Map ordMap=  new HashMap<>();
                    ordMap.put("Item",cursor.getString(1));
                    ordMap.put("Quantity",Integer.parseInt(cursor.getString(2)));
                    fMenu.collection("All Orders")
                            .document(String.valueOf(ID))
                            .set(ordMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Toast.makeText(receptionPortal.this,"added to firebase",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Toast.makeText(receptionPortal.this,"failed to add it on firebase",Toast.LENGTH_SHORT).show();
                                }
                            });

                    Map newId = new HashMap<>();
                    newId.put("Current",String.valueOf(ID +1));
                    fMenu.collection("docID")
                            .document("Current")
                            .set(newId)
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o)
                                {
                                    Toast.makeText(receptionPortal.this,"done",Toast.LENGTH_SHORT).show();
                                }
                            });

                    cursor.moveToNext();
                    task1 = null;
                    task2 = null;


                          //  Task<DocumentSnapshot>  ds = idRef.get();

               /* ds.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot)
                                {
                                    int ID = Integer.parseInt(documentSnapshot.get("Current").toString());

                                    Map ordMap= Collections.EMPTY_MAP;
                                    ordMap.put("Item",cursor.getString(1));
                                    ordMap.put("Quantity",Integer.parseInt(cursor.getString(2)));
                                    fMenu.collection("All Orders")
                                            .document(String.valueOf(ID))
                                            .set(ordMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {
                                                    Toast.makeText(receptionPortal.this,"added to firebase",Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e)
                                                {
                                                    Toast.makeText(receptionPortal.this,"failed to add it on firebase",Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                    Map newId = new HashMap<>();
                                    newId.put("Current",String.valueOf(ID +1));
                                    fMenu.collection("docID")
                                            .document("Current")
                                            .set(newId)
                                            .addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o)
                                                {
                                                    Toast.makeText(receptionPortal.this,"done",Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Toast.makeText(receptionPortal.this,"failed",Toast.LENGTH_SHORT).show();
                                }
                            });*/

                            //cursor.moveToNext();

                    /*boolean isIns = menu.insertAllOrder(Integer.parseInt(menu.getId(cursor.getString(1))), Integer.parseInt(cursor.getString(2)));
                    if (isIns)
                        Toast.makeText(receptionPortal.this, "Order Placed", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(receptionPortal.this, "Failed", Toast.LENGTH_SHORT).show();*/
                }
                    menu.clearOrder();
                    itemName.setText("");
                    quantity.setText("");
                    total.setText("0");
                    ArrayList<String> orders = new ArrayList();
                    final ArrayAdapter arrayAdapterList;
                    arrayAdapterList = new ArrayAdapter(receptionPortal.this,android.R.layout.simple_list_item_1, orders);
                    current.setAdapter(arrayAdapterList);
            }
        });
    }
}
