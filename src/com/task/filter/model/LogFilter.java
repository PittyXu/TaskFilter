package com.task.filter.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;

public class LogFilter
{
	public static String[] getLog(Context context)
	{
		try
		{
			ArrayList<String> cmdLine=new ArrayList<String>();   //设置命令   logcat -d 读取日志                
			cmdLine.add("logcat");                
			cmdLine.add("-d"); 
			
			ArrayList<String> clearLog=new ArrayList<String>();  //设置命令  logcat -c 清除日志               
			clearLog.add("logcat");                
			clearLog.add("-c");
			Runtime.getRuntime().exec(clearLog.toArray(new String[clearLog.size()]));  //清理日志
			Process process = Runtime.getRuntime().exec(cmdLine.toArray(new String[cmdLine.size()]));   //捕获日志                
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));    //将捕获内容转换为BufferedReader                //                
			Runtime.runFinalizersOnExit(true);                
			String str = null;                
			while((str = bufferedReader.readLine()) != null)    //开始读取日志，每次读取一行                
			{                    
				Runtime.getRuntime().exec(clearLog.toArray(new String[clearLog.size()]));  //清理日志....这里至关重要，不清理的话，任何操作都将产生新的日志，代码进入死循环，直到bufferreader满                    
				String a[] = str.split(" ");
				if (a.length > 4)
				{
					if (a[2].equals("Start"))
					{
//							System.out.println(a[4] + " " + a[6]);
							return a;
					}
				}
//				System.out.println(str);    //输出，在logcat中查看效果，也可以是其他操作，比如发送给服务器.. 
			}   
			return null;         
		}            
		catch(Exception e)            
		{                
			e.printStackTrace();    
			return null;
		}  
	}
}
