package com.task.filter.util;

import com.task.filter.data.DBDataTableApps;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppUtil
{
	/**
	 * 获得应用程序信息函数
	 * @param context
	 * @param packagename
	 * @param isIncludeSys 
	 * @return
	 */
	public static ApplicationInfo getAppInfo(Context context, String packagename, boolean isIncludeSys)
	{
		PackageManager packageManager = context.getPackageManager();
		try
		{
			ApplicationInfo appInfo = packageManager.getApplicationInfo(packagename, PackageManager.GET_ACTIVITIES);
			//判断是否为系统应用
			if (isIncludeSys)
			{
				return appInfo;
			}
			else
			{
				if (0 == (appInfo.flags & ApplicationInfo.FLAG_SYSTEM))
					return appInfo;
			}
		}
		catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @param context
	 * @param packagename
	 * @return
	 */
	public static DBDataTableApps getAppTable(Context context, String packagename)
	{
		DBDataTableApps dataTable = new DBDataTableApps();
		PackageManager packageManager = context.getPackageManager();
		try
		{
			ApplicationInfo appInfo  = packageManager.getApplicationInfo(packagename, PackageManager.GET_ACTIVITIES);
			PackageInfo pkgInfo = packageManager.getPackageInfo(packagename, PackageManager.GET_UNINSTALLED_PACKAGES
				| PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES | PackageManager.GET_PERMISSIONS);
//			dataTable.icon = packageManager.getApplicationIcon(dataTable.packagename);
			dataTable.name = appInfo.loadLabel(packageManager).toString();
			dataTable.icon = appInfo.loadIcon(packageManager);
			dataTable.uid = appInfo.uid;
			dataTable.packagename = packagename;
			dataTable.version = pkgInfo.versionCode;
			
			return dataTable;
		}
		catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void finishTask(Context context, String packagename)
	{
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.restartPackage(packagename);
		activityManager.killBackgroundProcesses(packagename);
//		PackageManager packageManager = context.getPackageManager();
//		Process.killProcess(pid);root后
	}
}
