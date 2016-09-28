package com.ping.myapp32.chengke;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.ping.myapp32.CustomListViewAdapter;
import com.ping.myapp32.GuanliActivity;
import com.ping.myapp32.PhoneId;
import com.ping.myapp32.R;
import com.ping.myapp32.saomiao.Capture1;
import com.ping.myapp32.saomiao.CaptureActivity;

import java.util.ArrayList;

/**
 * Created by 2-1Ping on 2016/9/6.
 */
public class MainTravel extends Activity {
    private boolean sure=false;
    AVObject u = new AVObject("KK");
    private String phoneid=null;
    String travellername;
    private final ArrayList<String> sourcesArrayList = new ArrayList<String>();
    private final Boolean isRecyclerview = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example);
        AVOSCloud.initialize(this, "rll1Okai77GFdJsIPPIP35ki-gzGzoHsz",
                "f621DpypKIbFFFrBetKG2jTM");
        PushService.setDefaultPushCallback(this, MainTravel.class);
        PushService.subscribe(this, "public", GuanliActivity.class);
        PushService.subscribe(this, "private", MainTravel.class);
        String s;
        s= AVInstallation.getCurrentInstallation().getInstallationId();
        // 保存 installation 到服务器
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVInstallation.getCurrentInstallation().saveInBackground();
            }
        });
        PhoneId phoneId=new PhoneId();
        phoneId.id=s;
        phoneId.save();
        AVOSCloud.initialize(this, "rll1Okai77GFdJsIPPIP35ki-gzGzoHsz", "f621DpypKIbFFFrBetKG2jTM");
        AVOSCloud.setDebugLogEnabled(true);
        sourcesArrayList.add("注册防盗锁");
        sourcesArrayList.add("绑定防盗锁");
        sourcesArrayList.add("解除防盗锁");
        sourcesArrayList.add("报警");
        final TravelListViewAdapter travelListViewAdapter = new TravelListViewAdapter(this);
        travelListViewAdapter.updateList(sourcesArrayList);
        final ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(travelListViewAdapter);
        final IsSure isSure=new IsSure();
        final TextView textView=(TextView)findViewById(R.id.light2);
        textView.setText(isSure.getIssure());
        final Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent failIntent = new Intent(BluetoothTools.ACTION_CONNECT_STOP);
                sendBroadcast(failIntent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("MainActivity", "ListView tap item : " + position);
                String data = sourcesArrayList.get(position).toString();
                if(data.equals("注册防盗锁"))
                {
                    Intent intent =new Intent(MainTravel.this,CaptureActivity.class);
                    startActivity(intent);
                    sure=true;
                    textView.setText(isSure.getIssure());
                }
                if(data.equals("绑定防盗锁")){
//                    if(sure) {
                        Intent intent = new Intent(MainTravel.this, ClientActivity.class);
                        startActivity(intent);
//                        Toast.makeText(MainTravel.this, "HAHAHA", Toast.LENGTH_LONG).show();

//                    }
//                    else{
//                        Toast.makeText(MainTravel.this, "请先注册设备", Toast.LENGTH_LONG).show();
//                    }
                }
                if(data.equals("解除防盗锁")){
                    Intent intent1=new Intent(MainTravel.this,Jiesuo.class);
                    startActivity(intent1);
                }
                if(data.equals("报警")){
                    Intent intent2=new Intent(MainTravel.this,Notification.class);
                    startActivity(intent2);
                }

                travelListViewAdapter
                        .notifyDataSetChanged();
            }
        });




    }
    @Override
    protected void onResume(){
        super.onResume();
        IsSure isSure=new IsSure();
        final TextView textView=(TextView)findViewById(R.id.light2);
        textView.setText(isSure.getIssure());
    }
}
