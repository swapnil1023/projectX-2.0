package com.example.projectx;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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
        Cursor cursor1 = menu.getCurrentData();
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

       /* while (cursor.moveToNext())
        {
            name.add(cursor.getString(1));
            ArrayAdapter Adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, name);
            itemName.setAdapter(Adapter);
        }*/

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
                Cursor cursor = menu.getCurrentData();
                while(cursor.moveToNext())
                {
                    boolean isIns = menu.insertAllOrder(Integer.parseInt(menu.getId(cursor.getString(1))),Integer.parseInt(cursor.getString(2)));
                    if (isIns)
                        Toast.makeText(receptionPortal.this, "Order Placed", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(receptionPortal.this, "Failed", Toast.LENGTH_SHORT).show();

                    menu.clearOrder();
                    itemName.setText("");
                    quantity.setText("");
                    total.setText("0");
                    ArrayList<String> orders = new ArrayList();
                    final ArrayAdapter arrayAdapterList;
                    arrayAdapterList = new ArrayAdapter(receptionPortal.this,android.R.layout.simple_list_item_1, orders);
                    current.setAdapter(arrayAdapterList);
                }
            }
        });
    }
}
