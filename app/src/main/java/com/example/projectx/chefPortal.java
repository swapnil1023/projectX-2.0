package com.example.projectx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class chefPortal extends AppCompatActivity {
    menu_orders menu;
    Button add;
    TextView name;
    TextView price;
    Button showMenu;
    TextView changePass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_portal);



        add = findViewById(R.id.addChef);
        name = findViewById(R.id.itemNameChef);
        price = findViewById(R.id.priceChef);
        showMenu = findViewById(R.id.showMenu);
        menu = new menu_orders(this);
        changePass = findViewById(R.id.changePassChef);

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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean isIns = menu.insertData(name.getText().toString(),price.getText().toString());
                if(isIns)
                    Toast.makeText(chefPortal.this,"Item Added",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(chefPortal.this,"Item Addition Failed",Toast.LENGTH_SHORT).show();
                name.setText("");
                price.setText("");
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
    }
}
