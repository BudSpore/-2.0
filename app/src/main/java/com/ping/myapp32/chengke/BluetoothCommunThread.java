package com.ping.myapp32.chengke;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 蓝牙通讯线程
 */
public class BluetoothCommunThread extends Thread {

	private Handler serviceHandler;		//与Service通信的Handler
	private BluetoothSocket socket;
	public volatile boolean isRun = true;	//运行标志
	/**
	 * 构造函数
	 * @param handler 用于接收消息
	 * @param socket
	 */
	public BluetoothCommunThread(Handler handler, BluetoothSocket socket) {
		this.serviceHandler = handler;
		this.socket = socket;
		Log.d("hlep", "OK");
	}


	//发送数据
	public void sendMessageHandle(String msg)
	{
		if (socket == null)
		{
			Log.d("socket","没有连接");
			return;
		}
		try {
			OutputStream os = socket.getOutputStream();
			os.write(msg.getBytes());
			Log.d("success", "send data success");
		} catch (IOException e) {
			Log.d("Error", "send data fail");

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void sendtheMessage(String msg){
		if (socket == null)
		{
			Log.d("socket","没有连接");
			return;
		}
		try {
			OutputStream os = socket.getOutputStream();
			os.write(msg.getBytes());
			Log.d("success", "send data success");
		} catch (IOException e) {
			Log.d("Error", "send data fail");

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//读取数据
	//@Override
	public void run1() {

		Log.i("OK","BEGIN mConnectedThread");
		InputStream mmInStream = null;
		byte[] buffer = new byte[1024];


// int bytes;
		int len = 0;
		int bytes=-1;
		int i = 0;

// Keep listening to the InputStream while connected
		while (true) {
			try {
				mmInStream = socket.getInputStream();
// Read from the InputStream
				Log.i("OK", "Read from the InputStream...");
// bytes = mmInStream.read(buffer);
				if ((bytes = mmInStream.read(buffer)) > -1) {
					Log.d("Tread","NO 4");
					byte[] buf_data = new byte[1024];
					for (int a = 0; a < bytes; a++) {
						buf_data[a] = buffer[a];
						i = a;
					}

					//buffer[i++] = (byte) mmInStream.read();
					if (i == 3) {
						len = buffer[2] + 10;
					}
					Log.i("OK", "Read from the InputStream, data is" + buf_data[i]);
					if (i == len) {
// Send the obtained bytes to the UI Activity
						serviceHandler.obtainMessage(BluetoothTools.MESSAGE_READ_OBJECT, len, -1, buffer).sendToTarget();
						len = 0;
						i = 0;
					}
				}
			} catch (IOException e) {

				Log.d("OK","disconnected", e);
				try {
					mmInStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			}
		}
	}
	@Override
	public void run() {
		Log.d("Tread","NO 1");
		while (true) {
			if (!isRun) {
				Log.d("Tread","NO 2");
				break;
			}
			Log.d("Tread","NO 3");
			byte[] buffer = new byte[1024];
			int bytes;
			InputStream mmInStream = null;

			try {
				mmInStream = socket.getInputStream();
				Log.d("Tread","NO 4");
				// Read from the InputStream
				if ((bytes = mmInStream.read(buffer)) > 0) {
					byte[] buf_data = new byte[bytes];
					Log.d("Tread","NO 5");
					for (int i = 0; i < bytes; i++) {
						buf_data[i] = buffer[i];
					}
					String s = new String(buf_data);
					Log.d("Tread",s);
					Message msg = serviceHandler.obtainMessage();
					Bundle bundle = new Bundle();
					bundle.putString("Id", s);
					Log.d("Tread",s);
					msg.setData(bundle);
					msg.what = BluetoothTools.MESSAGE_READ_OBJECT;//再一次发送数据communThread.sendMessageHandle(data);

					msg.sendToTarget();



				}
			} catch (IOException e) {
				try {
					Log.d("Tread","NO ok");

					mmInStream.close();
					//这个时候发一个广播，发给一个活动，让这个活动进行震动提醒
					String s = "321534";
					Message msg1 = serviceHandler.obtainMessage();
					//Bundle bundle = new Bundle();
					//	bundle.putString("foodId", s);
					Log.d("Tread",s);
					//msg1.setData(bundle);
					msg1.what = BluetoothTools.MESSAGE_CONNECT_STOP;
					msg1.sendToTarget();



				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			}

		}

	}

}
