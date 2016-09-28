package com.ping.myapp32;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ping.myapp32.chengke.MainTravel;

/**
 * Created by 2-1Ping on 2016/9/6.
 */
public class WelcomeActivity extends Activity {
//    private Button btn1;
   private Button btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        btn1=(Button)findViewById(R.id.button1);
        btn2=(Button)findViewById(R.id.button2);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
//                startActivity(i);
//            }
//        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, MainTravel.class);
                startActivity(i);
            }
        });
    }

}
