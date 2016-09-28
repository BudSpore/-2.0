package com.ping.myapp32.chengke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ping.myapp32.R;

/**
 * Created by 2-1Ping on 2016/9/11.
 */
public class Notification extends Activity {
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baojing);
        btn=(Button)findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent failIntent = new Intent(BluetoothTools.ACTION_CONNECT_STOP);
                sendBroadcast(failIntent);
            }
        });
    }
}
