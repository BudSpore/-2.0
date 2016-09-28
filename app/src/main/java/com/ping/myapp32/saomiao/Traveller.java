package com.ping.myapp32.saomiao;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by 2-1Ping on 2016/8/6.
 */
public class Traveller {
    private static ArrayList<String> nam = new ArrayList<String>();//姓名
    private static ArrayList<String> addr = new ArrayList<String>();//目的地
    private static ArrayList<String> coach=new ArrayList<String>();//车厢号
    private static ArrayList<String>seat=new ArrayList<String>();//座位号
    private static int number=0;//租出设备的数量
    public void setNumber(){
        number++;
    }
    public int getNumber(){
        return number;
    }

    public void setnam(String nam1){
        nam.add(nam1);
    }
    public void setaddr(String addr1){
        addr.add(addr1);
    }
    public void setcoach(String coach1){coach.add(coach1);}
    public void setseat(String seat1){seat.add(seat1);}

    public ArrayList<String> getnam(){
        return nam;
    }
    public ArrayList<String> getaddr(){
        return addr;
    }
    public ArrayList<String>getcoach(){return coach;}
    public ArrayList<String>getseat(){return seat;}
    public void deletenam(String nam1) {
        for (int i = 0; i < nam.size(); i++) {
            if (nam.get(i).equals(nam1)) {
                nam.remove(i);
                number--;
                i--;
            }
        }
    }
    public void deleteaddr(String addr1) {
        for (int i = 0; i < addr.size(); i++) {
            if (addr.get(i).equals(addr1)) {
                addr.remove(i);
                i--;
            }
        }
    }
    public void deletecoach(String coach1) {
        for (int i = 0; i < coach.size(); i++) {
            if (coach.get(i).equals(coach1)) {
                coach.remove(i);
                i--;
            }
        }
    }
    public void deleteseat(String seat1) {
        for (int i = 0; i < seat.size(); i++) {
            if (seat.get(i).equals(seat1)) {
                seat.remove(i);
                i--;
            }
        }
    }
}
