package com.example.projectx;

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
    FirebaseFirestore orders;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);
        orders = FirebaseFirestore.getInstance();
        lv = findViewById(R.id.allOrderList);

        final ArrayList<String> orderList = new ArrayList<>();

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


    }
}
