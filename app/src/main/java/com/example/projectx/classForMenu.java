package com.example.projectx;

public class classForMenu {

    private String id;
    private String name;
    private String price;

    public classForMenu(String id1, String name1, String price1)
    {
        id = id1;
        name = name1;
        price = price1;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getPrice()
    {
        return price;
    }
}
