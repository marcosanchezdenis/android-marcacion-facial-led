package com.example.investigacion.fourth.model;

import java.util.HashMap;
import java.util.Map;


public class FaceResponse{
    public Face face;
    @Override
    public String toString(){
        return "<Face> " + face.username+" "+face.distance + " " + face.hash + " " + face.name +"  "+ face.id +"</Face>";
    }
}












