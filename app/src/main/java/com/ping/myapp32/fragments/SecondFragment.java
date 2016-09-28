package com.ping.myapp32.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.ping.myapp32.GuanliActivity;
import com.ping.myapp32.MyApplication;
import com.ping.myapp32.NamePeople;
import com.ping.myapp32.PeopleAdapter;
import com.ping.myapp32.Phoneid1;
import com.ping.myapp32.R;
import com.ping.myapp32.chengke.MainTravel;
import com.ping.myapp32.saomiao.Device;
import com.ping.myapp32.saomiao.Traveller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SecondFragment extends Fragment {
    // 控件
    private ListView listview;
    private List<NamePeople> namePeopleList=new ArrayList<NamePeople>();
    // 数组
    private SimpleAdapter listItemAdapter;
    private ArrayList<HashMap<String, Object>> listItem = null;
    private int number=0;
    private String s;
    Device device;
    String m;
//    private MediaPlayer mediaplayer=new MediaPlayer();

    AVObject u = new AVObject("KK");
    private String phoneid=null;
    public static SecondFragment instance() {
        SecondFragment view = new SecondFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.second_fragment, null);
//        View view = inflater.inflate(R.layout.items, null);
//        return view;
        PeopleAdapter adapter=new PeopleAdapter(MyApplication.context,R.layout.items,namePeopleList);
        View view=inflater.inflate(R.layout.second_fragment,container,false);
        ListView listView=(ListView)view.findViewById(R.id.listView);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NamePeople namePeople = namePeopleList.get(position);
                Toast.makeText(getActivity(),namePeople.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Context context = getActivity();
        AVOSCloud.initialize(getActivity(), "rll1Okai77GFdJsIPPIP35ki-gzGzoHsz", "f621DpypKIbFFFrBetKG2jTM");
        AVAnalytics.enableCrashReport(getActivity().getApplicationContext(), true);
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(true);
        PushService.subscribe(getActivity(), "public", GuanliActivity.class);
        PushService.subscribe(getActivity(), "private", MainTravel.class);
        String s1;
        s1= AVInstallation.getCurrentInstallation().getInstallationId();
        // 保存 installation 到服务器
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVInstallation.getCurrentInstallation().saveInBackground();
            }
        });
        Phoneid1 phoneId=new Phoneid1();
        phoneId.id=s1;
        phoneId.save();



        // 初始化数据
        init();

        PeopleAdapter adapter=new PeopleAdapter(getActivity(),R.layout.items,namePeopleList);
        // 获取控件
        listview = (ListView)getActivity().findViewById(R.id.listView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// 加载输入框的布局文件
                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout layout = (LinearLayout) inflater.inflate(
                        R.layout.input_add, null);

                // 弹出的对话框

                new AlertDialog.Builder(getActivity())
					/* 弹出窗口的最上头文字 */
                        .setTitle("乘客到站提醒")
					/* 设置弹出窗口的图式 */
                        .setIcon(android.R.drawable.ic_dialog_info)
					/* 设置弹出窗口的信息 */
                        .setMessage("选择发送信息")
                        .setView(layout)
                        .setPositiveButton("到站提醒",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        final AVPush push = new AVPush();
//                // 设置频道
                                        push.setChannel("private");
                                        // 设置消息
                                        push.setMessage("乘客你快到站了!");
                                        AVQuery<AVObject> query = new AVQuery<AVObject>("KK");
                                        query.whereEqualTo("idlogo", "1");
                                        query.findInBackground(new FindCallback<AVObject>() {
                                            @Override
                                            public void done(List<AVObject> list, AVException e) {
                                                if (e == null) {//服务器成功交互
                                                    if (list.isEmpty() == true) {

                                                    } else {
                                                        u.put("sing",true);
                                                        u.saveInBackground();
                                                        phoneid = list.get(0).getString("id");
                                                        push.setQuery(AVInstallation.getQuery().whereEqualTo("installationId",
                                                                phoneid));
// 推送
                                                        push.sendInBackground(new SendCallback() {
                                                            @Override
                                                            public void done(AVException e) {
                                                                Toast toast = null;
                                                                if (e == null) {
                                                                    toast = Toast.makeText(context, "Send successfully.", Toast.LENGTH_SHORT);
                                                                } else {
                                                                    toast =
                                                                            Toast.makeText(context, "Send fails with :" + e.getMessage(), Toast.LENGTH_LONG);
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
                                                        jsonObject.put("alert", "请解锁设备，并下车，并把设备归还给乘务员");
//                                                        if(!mediaplayer.isPlaying()){
//                                                            mediaplayer.start();
//                                                        }
                                                        push.setData(jsonObject);
                                                        push.setPushToAndroid(true);
                                                        push.sendInBackground(new SendCallback() {
                                                            @Override
                                                            public void done(AVException e) {
                                                                Toast.makeText(getActivity(), "send successfully", Toast.LENGTH_SHORT);
                                                            }
                                                        });

                                                    }
                                                }
                                            }
                                        });
                                        // 设置查询条件，只推送给自己，不要打扰别人啦，这是 demo


                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() { /* 设置跳出窗口的返回事件 */
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
//                                        final AVPush push = new AVPush();
////                // 设置频道
//                                        push.setChannel("private");
//                                        // 设置消息
//                                        push.setMessage("乘客你的行李遗忘在座位上了!");
//                                        AVQuery<AVObject> query = new AVQuery<AVObject>("KK");
//                                        query.whereEqualTo("idlogo", "1");
//                                        query.findInBackground(new FindCallback<AVObject>() {
//                                            @Override
//                                            public void done(List<AVObject> list, AVException e) {
//                                                if (e == null) {//服务器成功交互
//                                                    if (list.isEmpty() == true) {
//                                                    } else {
//                                                        phoneid = list.get(0).getString("id");
//                                                        push.setQuery(AVInstallation.getQuery().whereEqualTo("installationId",
//                                                                phoneid));
//                                                        // 推送
//                                                        push.sendInBackground(new SendCallback() {
//                                                            @Override
//                                                            public void done(AVException e) {
//                                                                Toast toast = null;
//                                                                if (e == null) {
//                                                                    toast = Toast.makeText(context, "Send successfully.", Toast.LENGTH_SHORT);
//                                                                } else {
//                                                                    toast =
//                                                                            Toast.makeText(context, "Send fails with :" + e.getMessage(), Toast.LENGTH_LONG);
//                                                                }
//                                                                // 放心大胆地show，我们保证 callback 运行在 UI 线程。
//                                                                toast.show();
//                                                            }
//                                                        });
//                                                        AVQuery<AVInstallation> query1 = AVInstallation.getQuery();
//                                                        query1.whereEqualTo("installationId", phoneid);
//                                                        push.setQuery(query1);
//                                                        push.setChannel("private");
//                                                        JSONObject jsonObject = new JSONObject();
//                                                        jsonObject.put("action", "com.pushdemo.action");
//                                                        jsonObject.put("alert", "请回去领取您的行李");
//                                                        push.setData(jsonObject);
//                                                        push.setPushToAndroid(true);
//                                                        push.sendInBackground(new SendCallback() {
//                                                            @Override
//                                                            public void done(AVException e) {
//                                                                Toast.makeText(getActivity(), "send successfully", Toast.LENGTH_SHORT);
//                                                            }
//                                                        });
//
//                                                    }
//                                                }
//                                            }
//                                        });
//                                        // 设置查询条件，只推送给自己，不要打扰别人啦，这是 demo


                                    }


                                }).show();

            }
        });
    }
    // 初始化数据
    private void init() {

        listItem = new ArrayList<HashMap<String, Object>>();
        Traveller traveller=new Traveller();
        number=traveller.getNumber();
        for(int i = 0; i<traveller.getnam().size(); i++){
            m ="姓名:  "+traveller.getnam().get(i).toString()+"     目的地:  "+ traveller.getaddr().get(i).toString()+"    车厢号："+traveller.getcoach().get(i).toString()+
            "   座位号："+traveller.getseat().get(i).toString();
            NamePeople namePeople=new NamePeople(m,R.drawable.right);
            namePeopleList.add(namePeople);
        }
        NamePeople namePeople=new NamePeople("姓名：韩文凯 目的地：周口 车厢号：2 座位号：29F",R.drawable.right);
        namePeopleList.add(namePeople);
        NamePeople namePeople1=new NamePeople("姓名：刘国平 目的地：张家口 车厢号：5 座位号：32D",R.drawable.right);
        namePeopleList.add(namePeople1);

        //initMediaPlayer();



//        for (int i = 0; i < number; i++) {
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("viewspot", m[i]);
//            map.put("add", R.drawable.right);
//            listItem.add(map);
//        }
//
//        listItemAdapter = new SimpleAdapter(getActivity(), listItem,// 数据源
//                R.layout.items, new String[] { "viewspot", "add" }, new int[] {
//                R.id.viewspot, R.id.add });
//        listview.setAdapter(listItemAdapter);

    }
//    private void initMediaPlayer(){
//        File file=new File(Environment.getExternalStorageDirectory(),"daozhan.mp3");
//        try {
//            mediaplayer.setDataSource(file.getPath());
//            mediaplayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void delete(int number) {

        listItem.remove(number);
        listItemAdapter
                .notifyDataSetChanged();


    }

}