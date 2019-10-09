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

public class deliveryPortal extends AppCompatActivity {
    FirebaseFirestore orders;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_portal);

        listView = findViewById(R.id.preparedOrdersList);
        orders = FirebaseFirestore.getInstance();

        final ArrayList<String> placedOrders = new ArrayList<>();

        orders.collection("All Orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                if(e!=null)
                {
                    Toast.makeText(deliveryPortal.this,"some exception",Toast.LENGTH_SHORT).show();
                }
                for(DocumentChange emp: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(emp.getType() == DocumentChange.Type.ADDED)
                    {
                        if(emp.getDocument().getString("status").equals("prepared"))
                            placedOrders.add("Item : "+ emp.getDocument().get("Item").toString()+"\nQuantity : "+emp.getDocument().get("Quantity").toString()+"\nFrom : "+emp.getDocument().get("Prepared By").toString());
                    }
                }

            }
        });

        final ArrayAdapter arrayAdapter = new ArrayAdapter(deliveryPortal.this,android.R.layout.simple_list_item_1, placedOrders);
        listView.setAdapter(arrayAdapter);



    }
}
