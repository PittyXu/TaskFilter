package com.task.filter.data;

import com.task.filter.util.Consts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper
{
	public DBOpenHelper(Context context, String dbName, int version)
	{
		super(context, Consts.DB_NAME, null, Consts.DB_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(Consts.DB_CREATE);
	}
	@Override
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + Consts.DB_TABLE);
		this.onCreate(db);
	}
}
