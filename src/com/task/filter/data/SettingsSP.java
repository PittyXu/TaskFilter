package com.task.filter.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Administrator
 *存储模式
 */
public class SettingsSP 
{
	private static SettingsSP _instance = null;
	private SharedPreferences myPreferences;
	private SharedPreferences.Editor myEditor;
	private Context context;

	/**
	 * 初始化
	 * @param context
	 */
	public SettingsSP(Context context) 
	{
		this.context = context;
	}
	
	/**
	 * 初始化单例对象
	 * @param context
	 * @return
	 */
	public static SettingsSP getInstance(Context context)
	{
		if (null == _instance)
		{
			return _instance = new SettingsSP(context);
		}
		else
		{
			return _instance;
		}
	}

	/**
	 * 初始化Preference
	 * @param name(用于确定数据存储在哪个XML中)
	 */
	public void setMyPreferences(String name)
	{
		this.myPreferences = context.getSharedPreferences(name,
				Context.MODE_WORLD_WRITEABLE);
	}

	/**
	 * 初始化Editor,需要写操作时初始化此参数(必须先调用setMyPreferences函数,初始化Preference)
	 */
	public void setMyEditor()
	{
		this.myEditor = myPreferences.edit();
	}
	
	/**
	 * 获得boolean类型的数据项
	 * @param name
	 * @return
	 */
	public boolean getBooleanStatus(String name)
	{
		return myPreferences.getBoolean(name, true);
	}

	/**
	 * 获得Int类型的数据项
	 * @param name
	 * @return
	 */
	public int getIntStatus(String name)
	{
		return myPreferences.getInt(name, 0);
	}

	/**
	 * 保存boolean类型的数据项
	 * @param name
	 * @param status
	 */
	public void saveBoolean(String name, boolean status)
	{
		myEditor.putBoolean(name, status);
		myEditor.commit();
	}
	/**
	 * 保存Int类型的数据项
	 * @param name
	 * @param status
	 */
	public void saveInt(String name, int status)
	{
		myEditor.putInt(name, status);
		myEditor.commit();
	}
	public void saveIntStatus(String name, int status)
	{
		myEditor.putInt(name, status);
		myEditor.commit();
	}
}
