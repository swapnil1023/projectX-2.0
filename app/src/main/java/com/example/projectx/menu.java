package com.example.projectx;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class menu extends AppCompatActivity {

    ListView list;
    menu_orders menu;
    Button delete;
    TextView itemId;
    AutoCompleteTextView drop;
    classForMenu menuClass;
    TextView name;
    TextView price;
    Button add;
    FirebaseFirestore fMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menu = new menu_orders(this);
        list = findViewById(R.id.lvMenu);
        delete = findViewById(R.id.deleteItemMenu);
        add = findViewById(R.id.addItemMenu);

        fMenu = FirebaseFirestore.getInstance();

        final Intent i = getIntent();
        final String type = i.getStringExtra("empType");

        if(type.equals("1"))
        {
            add.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);

        }

        final View viDel = LayoutInflater.from(menu.this).inflate(R.layout.menu_delete,null);
        itemId = viDel.findViewById(R.id.itemIdDelete);
        drop = viDel.findViewById(R.id.autoCompMenu);

        final View viAdd = LayoutInflater.from(menu.this).inflate(R.layout.menu_add,null);
        name = viAdd.findViewById(R.id.nameAdd);
        price = viAdd.findViewById(R.id.priceAdd);

        Cursor cursor = menu.getData();
        ArrayList<classForMenu> menuList = new ArrayList();
        while(cursor.moveToNext())
        {
            menuClass = new classForMenu(cursor.getString(0),cursor.getString(1),cursor.getString(2));
            menuList.add(menuClass);
        }

        menuAdapter adapter = new menuAdapter(this,R.layout.adapter_view_menu,menuList);
        list.setAdapter(adapter);

        Cursor cursor2 = menu.getData();
        ArrayList<String> itemName = new ArrayList<>();
        while (cursor2.moveToNext())
        {
            itemName.add(cursor2.getString(1));
            ArrayAdapter Adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, itemName);
            drop.setAdapter(Adapter);
        }

        drop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                itemId.setText(menu.getId(drop.getText().toString()));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(com.example.projectx.menu.this);
                builder.setView(viDel)
                        .setTitle("Item Details")
                        .setCancelable(false)
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(itemId.getText().toString().equals(""))
                                {
                                    Toast.makeText(menu.this, " I am not going to read your mind, you'll have to enter the details!!", Toast.LENGTH_LONG).show();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                                else
                                    {

                                        try
                                        {
                                            String id = itemId.getText().toString();
                                            String str = menu.getName(id);
                                            int deletedRows = menu.delete(itemId.getText().toString());
                                            if (deletedRows > 0)
                                                Toast.makeText(menu.this, str + " Removed", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(menu.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            itemId.setText("");
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(menu.this,"Please enter a valid Item ID or use the search bar",Toast.LENGTH_LONG).show();
                                            itemId.setText("");
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                        }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(menu.this);
                builder.setView(viAdd)
                        .setTitle("New Item")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                Map<String,String> menuMap = new HashMap();
                                menuMap.put("name",name.getText().toString());
                                menuMap.put("price",price.getText().toString());
                                if(name.getText().toString().equals("")  || price.getText().toString().equals(""))
                                {
                                    Toast.makeText(menu.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }

                                else
                                {
                                    fMenu.collection("menu")
                                            .document(name.getText().toString())
                                            .set(menuMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {
                                                    Toast.makeText(menu.this, "Item Added to firestore", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e)
                                                {
                                                    Toast.makeText(menu.this, "Item Addition failed to firestore", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    boolean isIns = menu.insertData(name.getText().toString(), price.getText().toString());
                                    if (isIns)
                                        Toast.makeText(menu.this, "Item Added", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(menu.this, "Item Addition Failed", Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }

                            }
                        })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }
}
