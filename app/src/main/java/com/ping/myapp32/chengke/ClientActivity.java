package com.ping.myapp32.chengke;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SendCallback;
import com.ping.myapp32.R;
import com.ping.myapp32.ZujieActivity;
import com.ping.myapp32.fragments.FirstFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by h- on 2016/7/16.
 */
public class ClientActivity extends Activity {
    private TextView serversText;
    private Button startSearchBtn;
    private Button selectDeviceBtn;
    private Button tryAgainBtn;
    private Button secretBtn;
    private Button rumBtn;
    private  String data="121";
    private TextView lightLevel;
    private TextView lightLevel1;
    private double value = 0.4;
    String travellername;
    private String phoneid=null;
    private MediaPlayer mediaplayer=new MediaPlayer();
    private MediaPlayer mediaplayer2=new MediaPlayer();
    private BluetoothCommunThread communThread;
    private static String display;
    private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
//    Camera  camera = Camera.open();
//    Camera.Parameters parameter = camera.getParameters();
    static private Camera camera = null;
    private Camera.Parameters parameters = null;
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 在这里可以进行UI操作
                    lightLevel.setText("箱包与您的距离是 " + value + " m");
                    break;
                default:
                    break;
            }
        }

    };

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
// TODO Auto-generated method stub
            super.handleMessage(msg);
        }

    };
    Runnable startThread = new Runnable(){
        //将要执行的操作写在线程对象的run方法当中
        public void run(){
            System.out.println("updateThread");
            flashopen();
            try {
                Thread.sleep(100);
                flashclose();
            } catch (InterruptedException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
            handler1.post(startThread);
        }

    };

    Runnable closeThread = new Runnable(){
        //将要执行的操作写在线程对象的run方法当中
        public void run(){
            System.out.println("updateThread");
            flashclose();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
            handler1.post(closeThread);
        }

    };
    private void flashopen() {
        if(camera==null){
            camera = Camera.open();
        }
        parameters = camera.getParameters();

        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        camera.setParameters(parameters);
        camera.startPreview();
    }
    private void flashclose() {
        if(camera==null){
            camera = Camera.open();
        }
        parameters = camera.getParameters();

        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        camera.setParameters(parameters);
    }

    //主开启
    public void start(){
        handler1.post(startThread);
        handler1.post(closeThread);
    }
    //关闭
    public void close(View v){
        handler1.removeCallbacks(startThread);
        handler1.removeCallbacks(closeThread);
        flashclose();
        camera.stopPreview();
        camera.release();
        camera=null;
    }


    //广播接收器
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothTools.ACTION_NOT_FOUND_SERVER.equals(action)) {
                //未发现设备
                serversText.append("not found device\r\n");

            } else if (BluetoothTools.ACTION_FOUND_DEVICE.equals(action)) {
                //获取到设备对象
                BluetoothDevice device = (BluetoothDevice)intent.getExtras().get(BluetoothTools.DEVICE);
                Log.d("ACTION_FOUND_DEVICE","搜索到设备,进行显示");
                selectDeviceBtn.setEnabled(true);
                deviceList.add(device);
                serversText.append(device.getName() + "\r\n");

            } else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
                //连接成功
                serversText.append("连接成功" + "\r\n");
                lightLevel.setVisibility(View.INVISIBLE);
                Log.d("ACTION_CONNECT_SUCCESS", "连接成功");
                lightLevel1.setText("箱包状态：安全");
                IsSure isSure=new IsSure();
                isSure.setIssure("成功绑定");
                Intent sendDataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_SERVICE);
                sendDataIntent.putExtra(BluetoothTools.DATA, data);
                sendBroadcast(sendDataIntent);
                final AVPush push = new AVPush();
//                // 设置频道
                push.setChannel("private");

                AVQuery<AVObject> query = new AVQuery<AVObject>("KK");
                query.whereEqualTo("idlogo", "3");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {//服务器成功交互
                            if (list.isEmpty() == true) {
                            } else {
                                travellername = list.get(0).getString("travellername");
                                Cache cache = new Cache();
                                cache.name = travellername;
                            }
                        }
                    }
                });



                query.whereEqualTo("idlogo", "2");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {//服务器成功交互
                            if (list.isEmpty() == true) {
                            } else {
                                phoneid = list.get(0).getString("id");
                                Cache cache = new Cache();
                                // 设置消息
                                push.setMessage(Cache.name + "已经成功绑定了你给他的设备");
                                push.setQuery(AVInstallation.getQuery().whereEqualTo("installationId",
                                        phoneid));
// 推送
                                push.sendInBackground(new SendCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Toast toast = null;
                                        if (e == null) {
                                            toast = Toast.makeText(ClientActivity.this, "Send successfully.", Toast.LENGTH_SHORT);
                                        } else {
                                            toast =
                                                    Toast.makeText(ClientActivity.this, "Send fails with :" + e.getMessage(), Toast.LENGTH_LONG);
                                        }
                                        // 放心大胆地show，我们保证 callback 运行在 UI 线程。
                                        toast.show();
                                    }
                                });
                                AVQuery<AVInstallation> query1 = AVInstallation.getQuery();
                                query1.whereEqualTo("installationId", phoneid);
                                push.setQuery(query1);
                                push.setChannel("private");
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("action", "com.pushdemo.action");
                                jsonObject.put("alert", "已经成功绑定了你给他的设备");
                                push.setData(jsonObject);
                                push.setPushToAndroid(true);
                                push.sendInBackground(new SendCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Toast.makeText(getApplicationContext(), "send successfully", Toast.LENGTH_SHORT);
                                    }
                                });

                            }
                        }
                    }
                });

            }else if(BluetoothTools.ACTION_CONNECT_STOP.equals(action)){

                Log.d("ACTION_CONNECT_STOP", "该有通知了");
                serversText.setText("");
                IsSure isSure=new IsSure();
                isSure.setIssure("绑定失败");
                serversText.append("连接失败!" + "\r\n");
                selectDeviceBtn.setEnabled(false);
                tryAgainBtn.setEnabled(true);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ClientActivity.this);
                mBuilder.setSmallIcon(R.drawable.logo);
                mBuilder.setContentTitle("危险");
                mBuilder.setTicker("箱包与您断开连接！");
                mBuilder.setContentText("箱包被别人偷了");
                start();
                if(display.equals("2")) {
                    if (!mediaplayer.isPlaying()) {
                        mediaplayer.start();
                    }
                }
                Notification notification=mBuilder.build();
                notification.flags=Notification.FLAG_AUTO_CANCEL|Notification.FLAG_SHOW_LIGHTS;
                notification.ledARGB = Color.RED;
                notification.ledOnMS = 5000;
                notification.ledOffMS =5000;
                NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(3600, notification);
                value=0.4;

                lightLevel.setVisibility(View.VISIBLE);
//              notification.ledOnMS = 350;
//              notification.ledOffMS = 300;


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);// 将Message对象发送出去
                            try {
                                value+=0.1;
                                Thread.sleep(8000);
                                //   Thread.sleep(1000);
                                if(value%2==1) {
                                    value += 1;
                                }
                                else{
                                    value+=1.01;
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                Intent sendDataIntent = new Intent(BluetoothTools.ACTION_START_DISCOVERY);
                sendBroadcast(sendDataIntent);
                final AVPush push = new AVPush();
//                // 设置频道
                push.setChannel("private");

                AVQuery<AVObject> query = new AVQuery<AVObject>("KK");
                query.whereEqualTo("idlogo", "3");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {//服务器成功交互
                            if (list.isEmpty() == true) {
                            } else {
                                travellername = list.get(0).getString("travellername");
                                Cache cache = new Cache();
                                cache.name = travellername;
                            }
                        }
                    }
                });



                query.whereEqualTo("idlogo", "2");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {//服务器成功交互
                            if (list.isEmpty() == true) {
                            } else {
                                phoneid = list.get(0).getString("id");
                                Cache cache = new Cache();
                                // 设置消息
                                push.setMessage(Cache.name + "的箱包被偷,请尽快联系乘客！");
                                push.setQuery(AVInstallation.getQuery().whereEqualTo("installationId",
                                        phoneid));
// 推送
                                push.sendInBackground(new SendCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Toast toast = null;
                                        if (e == null) {
                                            toast = Toast.makeText(ClientActivity.this, "Send successfully.", Toast.LENGTH_SHORT);
                                        } else {
                                            toast =
                                                    Toast.makeText(ClientActivity.this, "Send fails with :" + e.getMessage(), Toast.LENGTH_LONG);
                                        }
                                        // 放心大胆地show，我们保证 callback 运行在 UI 线程。
                                        toast.show();
                                    }
                                });
                                AVQuery<AVInstallation> query1 = AVInstallation.getQuery();
                                query1.whereEqualTo("installationId", phoneid);
                                push.setQuery(query1);
                                push.setChannel("private");
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("action", "com.pushdemo.action");
                                jsonObject.put("alert", "请尽快通知乘客");
                                push.setData(jsonObject);
                                push.setPushToAndroid(true);
                                push.sendInBackground(new SendCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Toast.makeText(getApplicationContext(), "send successfully", Toast.LENGTH_SHORT);
                                    }
                                });

                            }
                        }
                    }
                });
                if(display.equals("2")){
                        lightLevel1.setText("箱包状态：箱包发生挪动");
                }

            }
            else if(BluetoothTools.ACTION_CONNECT_AREA_STOP.equals(action)){
                Log.d("ACTION_CONNECT_STOP", "该有通知了");
                serversText.setText("");
                IsSure isSure=new IsSure();
                isSure.setIssure("绑定失败~");
                serversText.append("连接失败~!" + "\r\n");
                selectDeviceBtn.setEnabled(false);
                tryAgainBtn.setEnabled(true);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ClientActivity.this);
                mBuilder.setSmallIcon(R.drawable.logo);
                mBuilder.setContentTitle("危险");
                mBuilder.setTicker("箱包与您断开连接！");
                mBuilder.setContentText("箱包与您距离远了");
                start();


                Notification notification=mBuilder.build();

                notification.flags=Notification.FLAG_AUTO_CANCEL|Notification.FLAG_SHOW_LIGHTS;
                notification.ledARGB = Color.RED;
                notification.ledOnMS = 5000;
                notification.ledOffMS =5000;
                NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(3600, notification);
                value=0.4;
                if(display.equals("1"))
                lightLevel1.setText("箱包状态：箱包与您距离远了");
                lightLevel.setVisibility(View.VISIBLE);
//              notification.ledOnMS = 350;
//              notification.ledOffMS = 300;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);// 将Message对象发送出去
                            try {
                                value+=0.1;
                                Thread.sleep(8000);
                                //   Thread.sleep(1000);
                                if(value%2==1) {
                                    value += 1;
                                }
                                else{
                                    value+=1.01;
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                Intent sendDataIntent = new Intent(BluetoothTools.ACTION_START_DISCOVERY);
                sendBroadcast(sendDataIntent);
                final AVPush push = new AVPush();
//                // 设置频道
                push.setChannel("private");

                AVQuery<AVObject> query = new AVQuery<AVObject>("KK");
                query.whereEqualTo("idlogo", "3");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {//服务器成功交互
                            if (list.isEmpty() == true) {
                            } else {
                                travellername = list.get(0).getString("travellername");
                                Cache cache = new Cache();
                                cache.name = travellername;
                            }
                        }
                    }
                });



                query.whereEqualTo("idlogo", "2");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {//服务器成功交互
                            if (list.isEmpty() == true) {
                            } else {
                                phoneid = list.get(0).getString("id");
                                Cache cache = new Cache();
                                // 设置消息
                                push.setMessage(Cache.name + "的箱包丢失,请尽快联系乘客！");
                                push.setQuery(AVInstallation.getQuery().whereEqualTo("installationId",
                                        phoneid));
// 推送
                                push.sendInBackground(new SendCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Toast toast = null;
                                        if (e == null) {
                                            toast = Toast.makeText(ClientActivity.this, "Send successfully.", Toast.LENGTH_SHORT);
                                        } else {
                                            toast =
                                                    Toast.makeText(ClientActivity.this, "Send fails with :" + e.getMessage(), Toast.LENGTH_LONG);
                                        }
                                        // 放心大胆地show，我们保证 callback 运行在 UI 线程。
                                        toast.show();
                                    }
                                });
                                AVQuery<AVInstallation> query1 = AVInstallation.getQuery();
                                query1.whereEqualTo("installationId", phoneid);
                                push.setQuery(query1);
                                push.setChannel("private");
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("action", "com.pushdemo.action");
                                jsonObject.put("alert", "请尽快通知乘客");
                                push.setData(jsonObject);
                                push.setPushToAndroid(true);
                                push.sendInBackground(new SendCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Toast.makeText(getApplicationContext(), "send successfully", Toast.LENGTH_SHORT);
                                    }
                                });

                            }
                        }
                    }
                });
                if(!mediaplayer2.isPlaying()){
                    mediaplayer2.start();
                }


            }
        }

    };
    @Override
    public void onDestroy() {
      //  Intent intent = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
        Log.d("ACTION_STOP_SERVICE", "OK");
    //    sendBroadcast(intent);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        //清空设备列表
        deviceList.clear();

        //开启后台service
        Intent startService = new Intent(ClientActivity.this, BluetoothClientService.class);
        startService(startService);

        //注册BoradcasrReceiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothTools.ACTION_NOT_FOUND_SERVER);
        intentFilter.addAction(BluetoothTools.ACTION_FOUND_DEVICE);
    //    intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_STOP);
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_AREA_STOP);
        registerReceiver(broadcastReceiver, intentFilter);

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.client);
        AVOSCloud.initialize(this, "rll1Okai77GFdJsIPPIP35ki-gzGzoHsz", "f621DpypKIbFFFrBetKG2jTM");
        AVOSCloud.setDebugLogEnabled(true);
        AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
        AVOSCloud.setLastModifyEnabled(true);
        PushService.subscribe(this, "public", ClientActivity.class);
        PushService.subscribe(this, "private", MainTravel.class);
        serversText = (TextView)findViewById(R.id.clientServersText);
        startSearchBtn = (Button)findViewById(R.id.startSearchBtn);
        selectDeviceBtn = (Button)findViewById(R.id.selectDeviceBtn);
        secretBtn=(Button)findViewById(R.id.button4);//可以提示箱包被偷了
        rumBtn=(Button)findViewById(R.id.button3);//可以提示距离远了
        tryAgainBtn=(Button)findViewById(R.id.tryAgain);
        tryAgainBtn.setEnabled(false);
        selectDeviceBtn.setEnabled(false);
        lightLevel=(TextView)findViewById(R.id.light_level);
        lightLevel1=(TextView)findViewById(R.id.light_leve2);
        initMediaPlayer();
        initMediaPlayer2();

        startSearchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //开始搜索
                Log.d("search", "开始搜索");
                Intent startSearchIntent = new Intent(BluetoothTools.ACTION_START_DISCOVERY);
                Log.d("ACTION_START_DISCOVERY", "OK");
                sendBroadcast(startSearchIntent);
            }
        });

        selectDeviceBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //选设备
                int i = 0;
                Intent selectDeviceIntent = new Intent(BluetoothTools.ACTION_SELECTED_DEVICE);
                for (i = 0; i < deviceList.size(); i++) {
                    if (deviceList.get(i).getName() != null) {
                        if (deviceList.get(i).getName().equals("HC-05")) {

                            selectDeviceIntent.putExtra(BluetoothTools.DEVICE, deviceList.get(i));
                            Log.d("ACTION_SELECTED_DEVICE", "连接设备");
                            sendBroadcast(selectDeviceIntent);
                            break;
                        }
                    }
                }
                if (i == deviceList.size()) {
                    Toast.makeText(ClientActivity.this, "未发现要绑定的设备", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选设备
                int i = 0;
                Intent selectDeviceIntent = new Intent(BluetoothTools.ACTION_SELECTED_DEVICE);

                for (i = 0; i < deviceList.size(); i++) {
                    if (deviceList.get(i).getName() != null) {
                        if (deviceList.get(i).getName().equals("HC-05")) {
                            selectDeviceIntent.putExtra(BluetoothTools.DEVICE, deviceList.get(i));
                            Log.d("ACTION_SELECTED_DEVICE", "连接设备");
                            lightLevel.setVisibility(View.INVISIBLE);
                            sendBroadcast(selectDeviceIntent);
                            break;
                        }
                    }
                }
                if (i == deviceList.size()) {
                    Toast.makeText(ClientActivity.this, "未发现要绑定的设备", Toast.LENGTH_SHORT).show();
                }
                close(v);
            }
        });
        secretBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display = "2";
                Intent failIntent = new Intent(BluetoothTools.ACTION_CONNECT_STOP);
                sendBroadcast(failIntent);

            }
        });
        rumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display = "1";
                Intent failIntent1 = new Intent(BluetoothTools.ACTION_CONNECT_AREA_STOP);
                sendBroadcast(failIntent1);
            }
        });
    }

    @Override
    protected void onStop() {
        //关闭后台Service
        //Intent stopService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
       // sendBroadcast(stopService);

      //  unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private void initMediaPlayer(){
        File file=new File(Environment.getExternalStorageDirectory(),"baojing.mp3");
        try {
            mediaplayer.setDataSource(file.getPath());
            mediaplayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMediaPlayer2(){
        File file=new File(Environment.getExternalStorageDirectory(),"yuanli.mp3");
        try {
            mediaplayer2.setDataSource(file.getPath());
            mediaplayer2.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
