package com.task.filter.data;

import java.util.List;

import com.task.filter.util.BitPicUtil;
import com.task.filter.util.Consts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.DatabaseObjectNotClosedException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class DBHelper
{
	private SQLiteDatabase db;
	private DBOpenHelper dbOpenHelper;
	private static DBHelper _instance = null;
	
	/**
	 * 构造函数
	 * @param context
	 */
	public DBHelper(Context context)
	{
		dbOpenHelper = new DBOpenHelper(context, "WR_DATA", 1);
		this.establishDb();
	}
	/**
	 * 单例构造函数
	 * @param context
	 * @return
	 */
	public static DBHelper getInstance(Context context)
	{
		if (null == _instance)
		{
			return _instance = new DBHelper(context);
		}
		else
		{
			return _instance;
		}
	}
	/**
	 * 创建资源
	 */
	public void establishDb()
	{
		if (null == this.db)
		{
			try
			{
				this.db = this.dbOpenHelper.getWritableDatabase();
			}catch(DatabaseObjectNotClosedException e)
			{
				this.db.close();
				this.db = this.dbOpenHelper.getWritableDatabase();
			}
		}
	}
	/**
	 * 释放资源
	 */
	public void cleanup()
	{
		if (this.db != null)
		{
			this.db.close();
			this.db = null;
		}
	}
	/**
	 * @param packagename
	 * @return
	 */
	public synchronized  DBDataTableApps getAppInfo(String packagename)
	{
		Cursor c = null;
		DBDataTableApps location = new DBDataTableApps();
		try
		{
			c = this.db.query(true, Consts.DB_TABLE, Consts.COLS, "packagename= '" + packagename + "'", null, null, 
					null, null, null);
			if (c.getCount() > 0)
			{
				c.moveToFirst();

				location.id = c.getLong(0);
				location.uid = c.getInt(1);
				location.packagename = c.getString(2);
				location.name = c.getString(3);
				location.version = c.getInt(4);
				location.icon = new BitmapDrawable(BitPicUtil.Bytes2Bimap(c.getBlob(5)));
				location.allow = c.getInt(6);
				
				cleanup();
				return location;
			}
		}
		catch(SQLException e)
		{
			Log.e(Consts.CLASSNAME, e.toString());
		}
		return null;
	}
	public synchronized  void getAppInfoByAllow(List<DBDataTableApps> appList, int allow)
	{
		Cursor c = null;
		appList.clear();
		try
		{
			c = this.db.query(true, Consts.DB_TABLE, Consts.COLS, "allow= '" + allow + "'", null, null, 
					null, null, null);
			if (c.getCount() > 0)
			{
				c.moveToFirst();
				do
				{
					DBDataTableApps location = new DBDataTableApps();
					
					location.id = c.getLong(0);
					location.uid = c.getInt(1);
					location.packagename = c.getString(2);
					location.name = c.getString(3);
					location.version = c.getInt(4);
					location.icon = new BitmapDrawable(BitPicUtil.Bytes2Bimap(c.getBlob(5)));
					location.allow = c.getInt(6);
					
					appList.add(location);
				}while(c.moveToNext());
				
				cleanup();
			}
		}
		catch(SQLException e)
		{
			Log.e(Consts.CLASSNAME, e.toString());
		}
	}
	public void insert(DBDataTableApps location)
	{
		ContentValues values = new ContentValues();
		Bitmap bitmapOrig;
        bitmapOrig = ((BitmapDrawable) location.icon).getBitmap();
        
        values.put("uid", location.uid);
		values.put("packagename", location.packagename);
		values.put("name", location.name);
		values.put("version", location.version);
		values.put("icon", BitPicUtil.Bitmap2Bytes(bitmapOrig));
		values.put("allow", location.allow);
		this.db.insert(Consts.DB_TABLE, null, values);
		cleanup();
	}
	
	public void update(DBDataTableApps location)
	{
		ContentValues values = new ContentValues();
		Bitmap bitmapOrig;
        bitmapOrig = ((BitmapDrawable) location.icon).getBitmap();
        
        values.put("_id", location.id);
        values.put("uid", location.uid);
		values.put("packagename", location.packagename);
		values.put("name", location.name);
		values.put("version", location.version);
		values.put("icon", BitPicUtil.Bitmap2Bytes(bitmapOrig));
		values.put("allow", location.allow);
		String[] args = {location.packagename.toString()};
		db.update(Consts.DB_TABLE, values, "packagename=?", args);
		cleanup();
	}
	
	public synchronized  void delete(long id)
	{
		String[] args = {String.valueOf(id)};
		this.db.delete(Consts.DB_TABLE, "_id=?", args);
		cleanup();
	}
	
	public synchronized void delete(String packagename)
	{
		String[] args = {packagename};
		this.db.delete(Consts.DB_TABLE, "packagename=?", args);
		cleanup();
	}
	
}
