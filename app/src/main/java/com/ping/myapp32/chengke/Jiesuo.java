package com.ping.myapp32.chengke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ping.myapp32.R;

/**
 * Created by 2-1Ping on 2016/7/24.
 */
public class Jiesuo extends Activity {
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiesuo);
        btn=(Button)findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jiesuoIntent = new Intent(BluetoothTools.ACTION_STOP);
                Log.d("ACTION_START_DISCOVERY", "OK");
                sendBroadcast(jiesuoIntent);
                IsSure isSure=new IsSure();
                isSure.setIssure("已经解锁");
                Toast.makeText(Jiesuo.this,"已经解锁",Toast.LENGTH_LONG).show();
                Intent failIntent = new Intent(BluetoothTools.ACTION_CONNECT_STOP);
                sendBroadcast(failIntent);
            }
        });

    }
}
