package com.example.projectx;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class businessReport extends AppCompatActivity {

    menu_orders orders;
    TextView totalSell;
    FirebaseFirestore menu;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_report);

        orders = new menu_orders(this);
        totalSell = findViewById(R.id.totalSell);

        menu = FirebaseFirestore.getInstance();

        menu.collection("All Orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                int total = 0;

                if(e!=null)
                {
                    Toast.makeText(businessReport.this,"some exception",Toast.LENGTH_SHORT).show();
                }
                for(DocumentChange emp: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(emp.getType() == DocumentChange.Type.ADDED)
                    {
                           total = total +Integer.parseInt(emp.getDocument().get("Total Price").toString());

                    }
                }
                totalSell.setText(String.valueOf(total));

            }
        });


    }
}
