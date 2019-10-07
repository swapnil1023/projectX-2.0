package com.example.projectx;

public class classForEmp
{
    String name;
    int type;
    String password;

    public classForEmp(String name, int type, String password)
    {
        this.name = name;
        this.type = type;
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public int getType()
    {
        return type;
    }

    public String getPassword()
    {
        return password;
    }
}
