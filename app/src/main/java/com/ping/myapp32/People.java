package com.ping.myapp32;

/**
 * Created by 2-1Ping on 2016/8/14.
 */
public class People {
    public static String name;
    public static String area;
    public static String coach;
    public static String seat;
    public String getName(){return this.name;}
    public String getArea(){
        return this.area;
    }
    public String getCoach(){
        return this.coach;
    }
    public String getSeat(){
        return this.seat;
    }
    public void setName(String n){
        this.name=n;
    }
    public void setArea(String a){
        this.area=a;
    }
    public void setCoach(String c){this.coach=c;}
    public void setSeat(String s){this.seat=s;}
}
