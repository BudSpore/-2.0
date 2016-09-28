package com.ping.myapp32.saomiao;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 2-1Ping on 2016/8/4.
 */
public class Device {
    private static int number=0;//租出设备的数量
    private static ArrayList<String> deviceId = new ArrayList<String>();//设备名称
    public void setDeviceId(String data){
        deviceId.add(data);
    }
    public void setNumber(){
        number++;
    }
    public ArrayList<String> getDeviceId(){
        return deviceId;
    }
    public int getNumber(){
        return number;
    }
    public void deleteDeviceId(String data) {
        for (int i = 0; i < deviceId.size(); i++) {

            if (deviceId.get(i).equals(data)) {

                deviceId.remove(i);

                number--;
                Log.d("number", String.valueOf(number));
i--;
            }
        }
    }


}
