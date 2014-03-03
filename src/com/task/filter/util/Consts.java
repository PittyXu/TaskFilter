package com.task.filter.util;

import android.view.Menu;

import com.task.filter.data.DBHelper;

public interface Consts
{
	public static final int MENU_FEEDBACK = Menu.FIRST;
	public static final int MENU_UPDATE = Menu.FIRST + 1;
	public static final int MENU_ABOUT = Menu.FIRST + 2;
	public static final int MENU_SHARE = Menu.FIRST + 3;
	public static final int MENU_QUIT = Menu.FIRST + 4;
	
	//数据库操作
	public static final String DEVICE_ALERT_ENABLED_ZIP = "DAEZ99";
	public static final String DB_NAME = "db_TaskFilterInfo";
	public static final String DB_TABLE = "t_TaskFilterInfo_apps";
	public static final int DB_VERSION = 3;
	public static final String CLASSNAME = DBHelper.class.getSimpleName();
	public static final String[] COLS = new String[]{"_id", "uid", "packagename", "name", "version", "icon", "allow"};
	public static final String DB_CREATE = "CREATE TABLE " + DB_TABLE
		+ " ("
		+ " _id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT," 
		+ " uid INTEGER,"
		+ " packagename TEXT UNIQUE NOT NULL,"
		+ " name TEXT UNIQUE NOT NULL,"
		+ " version INTEGER,"
		+ " icon BYTE,"
		+ " allow INTEGER"
		+ " );";
	//SharedPreferences操作
	/* 表名 */
	/* 存储模式数据 */
	public static String TF_SP_SETTINGS = "TaskTilterSettings";
	/* 数据项名 */
	public static final String TF_SP_ST_START = "isStart";
	public static final String TF_SP_ST_BOOT_START = "isBootStart";
	public static final String TF_SP_ST_INCLUDE_SYS = "isIncludeSys";
	
	
	//数据项
	public static final String TF_APP_UID = "uid";
	public static final String TF_APP_PACKAGENAME = "packagename";
	public static final String TF_APP_NAME = "name";
	public static final String TF_APP_VERSION = "version";
}
