package com.task.filter.receiver;

import com.task.filter.data.SettingsSP;
import com.task.filter.service.TaskFilterService;
import com.task.filter.util.Consts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver
{
	SettingsSP sSP;
	@Override
	public void onReceive(Context context, Intent arg1)
	{
		sSP = SettingsSP.getInstance(context);
		sSP.setMyPreferences(Consts.TF_SP_SETTINGS);
		if (sSP.getBooleanStatus(Consts.TF_SP_ST_BOOT_START))
		{
			context.startService(new Intent(context, TaskFilterService.class));
		}
	}
}
