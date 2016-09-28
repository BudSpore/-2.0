package com.ping.myapp32.chengke;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙模块客户端主控制Service
 */
public class BluetoothClientService extends Service {

	//搜索到的远程设备集合
	private List<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();

	//蓝牙适配器
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	//蓝牙通讯线程
	private BluetoothCommunThread communThread;

	private  String data="121";


	//控制信息广播的接收器
	private BroadcastReceiver controlReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothTools.ACTION_START_DISCOVERY.equals(action)) {
				//开始搜索
				discoveredDevices.clear();	//清空存放设备的集合
				bluetoothAdapter.enable();	//打开蓝牙
				bluetoothAdapter.startDiscovery();	//开始搜索
				Log.d("startDiscovery", "OK");

			} else if (BluetoothTools.ACTION_SELECTED_DEVICE.equals(action)) {

				//bluetoothAdapter.cancelDiscovery();//关闭搜索释放资源
				//选择了连接的服务器设备
				BluetoothDevice device = (BluetoothDevice)intent.getExtras().get(BluetoothTools.DEVICE);
				Log.d("ACTION_SELECTED_DEVICE", "接受ACTION_SELECTED_DEVICE广播后，选中第一个设备,进入Client_Service");
				//开启设备连接线程
				new BluetoothClientConnThread(handler, device).start();

			}
			else if (BluetoothTools.ACTION_CONNECT_STOP.equals(action)) {
				//bluetoothAdapter.cancelDiscovery();
				bluetoothAdapter.disable();	//关闭蓝牙
			}
			else if(BluetoothTools.ACTION_CONNECT_AREA_STOP.equals(action)){
				bluetoothAdapter.disable();	//关闭蓝牙
			}
			else if (BluetoothTools.ACTION_STOP_SERVICE.equals(action)) {
				//停止后台服务
				if (communThread != null) {
					communThread.isRun = false;
				}

				//stopSelf();

			} else if (BluetoothTools.ACTION_DATA_TO_SERVICE.equals(action)) {
				//获取数据
				String data = intent.getStringExtra(BluetoothTools.DATA);
				if (communThread != null) {
					communThread.sendMessageHandle(data);
				}

			}
			else if(BluetoothTools.ACTION_STOP.equals(action)){
				if (communThread != null) {
					communThread.sendtheMessage("a");
				}
			}
			else if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
				//接收数据
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						while(true) {
//							try {
//								Thread.sleep(100);
//								Intent sendDataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_SERVICE);
//								sendDataIntent.putExtra(BluetoothTools.DATA, data);
//								sendBroadcast(sendDataIntent);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}).start();
				Intent sendDataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_SERVICE);
				sendDataIntent.putExtra(BluetoothTools.DATA, data);
				sendBroadcast(sendDataIntent);
			}

		}
	};

	//蓝牙搜索广播的接收器
	private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			//获取广播的Action
			String action = intent.getAction();

			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				Log.d("ACTIONDISCOVERYSTARTED","开始搜索");//开始搜索
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				//发现远程蓝牙设备
				Log.d("ACTION_FOUND","搜索到设备");
				//获取设备
				BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				discoveredDevices.add(bluetoothDevice);

				//发送发现设备广播
				Intent deviceListIntent = new Intent(BluetoothTools.ACTION_FOUND_DEVICE);
				Log.d("ACTION_FOUND_DEVICE","搜索到设备3");
				deviceListIntent.putExtra(BluetoothTools.DEVICE, bluetoothDevice);
				sendBroadcast(deviceListIntent);

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				//搜索结束
				if (discoveredDevices.isEmpty()) {
					//若未找到设备，则发动未发现设备广播
					Intent foundIntent = new Intent(BluetoothTools.ACTION_NOT_FOUND_SERVER);
					Log.d("ACTION_NOT_FOUND_SERVER","未搜索/到设备");
					sendBroadcast(foundIntent);
				}
			}
		}
	};

	//接收其他线程消息的Handler

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			//处理消息
			switch (msg.what) {
				case BluetoothTools.MESSAGE_CONNECT_ERROR:
					Log.d("MESSAGE_CONNECT_ERROR", "ERROR");
					//连接错误
					//发送连接错误广播
					Intent errorIntent = new Intent(BluetoothTools.ACTION_CONNECT_ERROR);
					sendBroadcast(errorIntent);
					break;
				case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
					Log.d("MESSAGE_CONNECT_SUCCESS", "BluetoothCommunThread is OK");
					//连接成功
					//发送连接成功广播
					Intent succIntent = new Intent(BluetoothTools.ACTION_CONNECT_SUCCESS);
					sendBroadcast(succIntent);

					//开启通讯线程
					communThread = new BluetoothCommunThread(handler, (BluetoothSocket)msg.obj);
					Log.d("MESSAGE_CONNECT_SUCCESS", "进入通信线程");
					communThread.start();
					break;
				case BluetoothTools.MESSAGE_READ_OBJECT:
					//读取到对象
					//发送数据广播（包含数据对象）
					Intent dataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_GAME);
					Bundle bundle = msg.getData();
					String data = bundle.getString("Id");
//					byte[] readBuf = (byte[]) msg.obj;//这是把缓冲区给了readBuf吗？
//					String data = new String(readBuf, 0,msg.arg1);
					Log.d("receiver", data);
					dataIntent.putExtra(BluetoothTools.DATA, data);
					sendBroadcast(dataIntent);
					break;
				case BluetoothTools.MESSAGE_CONNECT_STOP:
					Log.d("MESSAGE_CONNECT_STOP", "主动终止");
					Intent failIntent = new Intent(BluetoothTools.ACTION_CONNECT_STOP);
					sendBroadcast(failIntent);
					break;
			}
			super.handleMessage(msg);
		}

	};

	/**
	 * 获取通讯线程
	 * @return
	 */
	public BluetoothCommunThread getBluetoothCommunThread() {
		return communThread;
	}



	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Service创建时的回调函数
	 */
	@Override
	public void onCreate() {
		//discoveryReceiver的IntentFilter
		IntentFilter discoveryFilter = new IntentFilter();
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);

		//controlReceiver的IntentFilter
		IntentFilter controlFilter = new IntentFilter();
		controlFilter.addAction(BluetoothTools.ACTION_START_DISCOVERY);
		controlFilter.addAction(BluetoothTools.ACTION_SELECTED_DEVICE);
		controlFilter.addAction(BluetoothTools.ACTION_STOP_SERVICE);
		controlFilter.addAction(BluetoothTools.ACTION_CONNECT_STOP);
		controlFilter.addAction(BluetoothTools.ACTION_CONNECT_AREA_STOP);

		controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_SERVICE);
		controlFilter.addAction(BluetoothTools.ACTION_STOP);
		controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);


		//注册BroadcastReceiver
		registerReceiver(discoveryReceiver, discoveryFilter);
		registerReceiver(controlReceiver, controlFilter);
		super.onCreate();
	}

	/**
	 * Service销毁时的回调函数
	 */
	@Override
	public void onDestroy() {
		if (communThread != null) {
			communThread.isRun = false;
		}
		//解除绑定
		unregisterReceiver(controlReceiver);
		unregisterReceiver(discoveryReceiver);
		super.onDestroy();
	}

}
