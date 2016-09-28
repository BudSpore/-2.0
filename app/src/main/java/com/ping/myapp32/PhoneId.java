package com.ping.myapp32;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

/**
 * Created by 2-1Ping on 2016/8/6.
 * 乘客的手机ID
 */
public class PhoneId {
    public static String id;
    public void save(){
        AVQuery<AVObject> query = new AVQuery<AVObject>("KK");
        AVObject u = new AVObject("KK");
        u.put("id", id);
        u.put("idlogo", "1");
        u.saveInBackground();
    }
}
