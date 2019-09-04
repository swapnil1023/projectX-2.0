package com.example.projectx;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class allOrders extends AppCompatActivity {

    ListView lv;
   // menu_orders orders;
    FirebaseFirestore orders;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);
        orders = FirebaseFirestore.getInstance();
        //orders = new menu_orders(this);
        lv = findViewById(R.id.allOrderList);

        final ArrayList<String> orderList = new ArrayList<>();
       // Cursor cursor = orders.getAllOrderData();
       // ArrayAdapter adapter;

        orders.collection("All Orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                if(e!=null)
                {
                    Toast.makeText(allOrders.this,"some exception",Toast.LENGTH_SHORT).show();
                }
                for(DocumentChange emp: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(emp.getType() == DocumentChange.Type.ADDED)
                    {
                        orderList.add("Order No : "+emp.getDocument().getId()+"\nItem : "+ emp.getDocument().get("Item").toString()+"\nQuantity : "+emp.getDocument().get("Quantity").toString()+"\nTotal : "+emp.getDocument().get("Total Price").toString());
                    }
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(allOrders.this,android.R.layout.simple_list_item_1, orderList);
                lv.setAdapter(arrayAdapter);
            }
        });

       /* while(cursor.moveToNext())
        {
            orderList.add("order ID : "+cursor.getString(0)+"\n Item : "+cursor.getString(1)+"\nQuantity : "+cursor.getString(2)+"\nStatus : "+cursor.getString(3));
        }
        adapter = new ArrayAdapter(allOrders.this,android.R.layout.simple_list_item_1, orderList);
        lv.setAdapter(adapter);*/

    }
}
