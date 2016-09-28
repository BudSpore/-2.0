package com.ping.myapp32;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.ping.myapp32.saomiao.Capture1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by 2-1Ping on 2016/7/22.
 */
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *租出设备列表Activity
 */
public class KeyList extends Activity
{
    private final Boolean isRecyclerview = false;
    private final ArrayList<String> sourcesArrayList = new ArrayList<String>();
    CustomAdapter customAdapter = new CustomAdapter();
    RecyclerView recyclerView;
    private static int num=0;
    private String name;
    private String address;
    private String coach;
    private String seat;
    private People p=new People();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AVOSCloud.initialize(this, "rll1Okai77GFdJsIPPIP35ki-gzGzoHsz", "f621DpypKIbFFFrBetKG2jTM");
        AVOSCloud.setDebugLogEnabled(true);
        if (isRecyclerview)
            setContentView(R.layout.activity_main_recycler);
        else
            setContentView(R.layout.activity_main_list);

        final RippleView rippleView = (RippleView) findViewById(R.id.rect);
        final TextView textView = (TextView) findViewById(R.id.rect_child);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        rippleView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("Sample", "Click Rect !");

            }
        });
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Log.d("Sample", "Ripple completed");

            }
        });
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Log.e("Sample", "Click rect child !");

            }
        });

        sourcesArrayList.add("HC-05");
        sourcesArrayList.add("XM");
        sourcesArrayList.add("CHN-16");
        sourcesArrayList.add("Han");


        if (isRecyclerview)
        {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

//            CustomAdapter customAdapter = new CustomAdapter();
            customAdapter.updateList(sourcesArrayList);

            customAdapter.setOnTapListener(new OnTapListener()
            {
                @Override
                public void onTapView(int position)
                {
                    Log.e("MainActivity", "Tap item : " + position);

                }
            });
            recyclerView.setAdapter(customAdapter);
        }
        else
        {
            final CustomListViewAdapter customListViewAdapter = new CustomListViewAdapter(this);
            customListViewAdapter.updateList(sourcesArrayList);
            final ListView listView = (ListView) findViewById(R.id.listview);

            listView.setAdapter(customListViewAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    num++;
                    Log.e("MainActivity", "ListView tap item : " + position);
                    String data=sourcesArrayList.get(position).toString();
//                    Toast.makeText(LiecheyuanActivity.this,data,Toast.LENGTH_LONG).show();
//                    name=p.getName();
//                    address=p.getArea();
                    Intent intent=new Intent(KeyList.this,Capture1.class);
                    Bundle b = new Bundle();
                    b.putString("device", data);
//                    b.putString("nam",name);
//                    b.putString("addr",address);
                    intent.putExtras(b);

                    startActivity(intent);
                    customListViewAdapter
                            .notifyDataSetChanged();
                }
            });
        }
    }

}
