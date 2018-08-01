package com.example.investigacion.fourth.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LastUserResponse {


    public String result;
    public String ref;
    public ArrayList<Map<String,ArrayList<UserData>>> data;



    @Override
    public String toString(){
        ;
        return "<LastUserResponse>"+result+ " " + ref+" "+ data.toString()+"</LastUserResponse>";
    }

}





