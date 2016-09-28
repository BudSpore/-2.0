package com.ping.myapp32;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SendCallback;
import com.ping.myapp32.chengke.MainTravel;

import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CustomReceiver extends BroadcastReceiver {
    private MediaPlayer mediaplayer=new MediaPlayer();
    AVObject u = new AVObject("KK");

    @Override
  public void onReceive(Context context, Intent intent) {

    try {
      if (intent.getAction().equals("com.pushdemo.action")) {
          initMediaPlayer();
        JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
        final String message = json.getString("alert");
        Intent resultIntent = new Intent(AVOSCloud.applicationContext, MainTravel.class);
        PendingIntent pendingIntent =
            PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(AVOSCloud.applicationContext)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(
                    AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
                .setContentText(message)
                .setTicker(message);

          final AVQuery<AVObject> query = new AVQuery<AVObject>("KK");
          query.whereEqualTo("sing", true);
          query.findInBackground(new FindCallback<AVObject>() {
              @Override
              public void done(List<AVObject> list, AVException e) {
                  if (e == null) {//服务器成功交互
                      if (list.isEmpty() == true) {

                      } else {
                          if (!mediaplayer.isPlaying()) {
                              mediaplayer.start();
                          }
                          query.deleteAllInBackground(new DeleteCallback() {
                              @Override
                              public void done(AVException e) {
                                  if(e==null){

                                  }
                              }
                          });

                      }
                  }
              }
          });




          AVQuery<AVObject> query1 = new AVQuery<AVObject>("KK");
          query1.whereEqualTo("idlogo", "1");
          query1.findInBackground(new FindCallback<AVObject>() {
              @Override
              public void done(List<AVObject> list, AVException e) {
                  if (e == null) {//服务器成功交互
                      if (list.isEmpty() == true) {

                      } else {

                      }
                  }
              }
          });





        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        int mNotificationId = 10086;
        NotificationManager mNotifyMgr =
            (NotificationManager) AVOSCloud.applicationContext
                .getSystemService(
                    Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
      }
    } catch (Exception e) {

    }
  }
    private void initMediaPlayer(){
        File file=new File(Environment.getExternalStorageDirectory(),"daozhan.mp3");
        try {
            mediaplayer.setDataSource(file.getPath());
            mediaplayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
