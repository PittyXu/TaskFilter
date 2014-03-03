package com.task.filter.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//import com.kuguo.pushads.PushAdsManager;
import com.task.filter.TaskFilterDialogActivity;
import com.task.filter.data.DBDataTableApps;
import com.task.filter.data.DBHelper;
import com.task.filter.data.SettingsSP;
import com.task.filter.model.LogFilter;
import com.task.filter.util.AppUtil;
import com.task.filter.util.Consts;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class TaskFilterService extends Service
{
	private Context context = this;
	private Handler handler = null;
	private String appLog[];
	private ApplicationInfo appInfo = null;
	DBHelper db;
	DBDataTableApps dataTable = null;
	SettingsSP settings = null;
	@Override
	public void onCreate()
	{
		settings = SettingsSP.getInstance(context);
		settings.setMyPreferences(Consts.TF_SP_ST_INCLUDE_SYS);
		
		startTFService();
//		startAd();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() 
	{
		if (settings.getBooleanStatus(Consts.TF_SP_ST_START))
		{
			Intent intent = new Intent(context, TaskFilterService.class);
			startService(intent);
		}
		super.onDestroy();
	}
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
//	public void startAd()
//	{
//		new Timer().schedule(new TimerTask()
//		{
//			@Override
//			public void run()
//			{
//				PushAdsManager.getInstance().receivePushMessage(context);
//			}
//		}, 3600000);
//	}
	public void startTFService()
	{
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) 
			{
				appLog = (String[])msg.obj;
				appInfo = AppUtil.getAppInfo(context, appLog[4], settings.getBooleanStatus(Consts.TF_SP_ST_INCLUDE_SYS));
//				System.out.println(appLog[4] + " " + appLog[6] + " " + appLog[9]);
				if (null != appInfo)
				{
					db = DBHelper.getInstance(context);
					synchronized (db)
					{
						db.cleanup();
						db.establishDb();
						dataTable = db.getAppInfo(appLog[4]);
						db.cleanup();
						
						if (null == dataTable)
						{
							Intent startDialog = new Intent(context, TaskFilterDialogActivity.class);
							startDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startDialog.putExtra(Consts.TF_APP_UID, appInfo.uid);
							startDialog.putExtra(Consts.TF_APP_PACKAGENAME, appLog[4]);
							startActivity(startDialog);
						}
						else
						{
							if (0 == dataTable.allow)
							{
								AppUtil.finishTask(context, dataTable.packagename);
							}
						}
					}
				}
				super.handleMessage(msg);
			}
		};
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				String appLogTemp[];
				while(true)
				{
					appLogTemp = LogFilter.getLog(context);
					if (null != appLogTemp)
					{
						Message msg = Message.obtain(handler, 1,appLogTemp);
						handler.sendMessage(msg);
					}
				}
			}
		}).start();
	}
}
