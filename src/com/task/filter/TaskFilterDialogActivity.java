package com.task.filter;

import com.admogo.AdMogoManager;
//import com.kuguo.pushads.PushAdsManager;
import com.mobclick.android.MobclickAgent;
import com.task.filter.R;
import com.task.filter.data.DBDataTableApps;
import com.task.filter.data.DBHelper;
import com.task.filter.util.AppUtil;
import com.task.filter.util.Consts;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskFilterDialogActivity extends Activity
{
	ImageView tfDialogAppIcon;
	TextView tfDialogAppName;
	CheckBox tfDialogRemember;
	TextView tfDialogClocker;
	Button tfDialogAllow;
	Button tfDialogDeny;
	
	Context context = this;
	DBHelper db;
	DBDataTableApps dataTable = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.task_filter_dialog);
		
		init();
		setListeners();
		
		MobclickAgent.onError(context);
//		PushAdsManager.getInstance().receivePushMessage(context);
	}
	@Override
    protected void onResume()
	{
		super.onResume();
		MobclickAgent.onResume(context);
	}
    @Override
    protected void onPause()
    {
    	super.onPause();
    	MobclickAgent.onPause(context);
    }
    @Override
    protected void onDestroy()
	{
    	AdMogoManager.clear();
		super.onDestroy();
	}
	private void init()
	{
		tfDialogAppIcon = (ImageView)findViewById(R.id.tf_dialog_app_icon);
		tfDialogAppName = (TextView)findViewById(R.id.tf_dialog_appname);
		tfDialogRemember = (CheckBox)findViewById(R.id.tf_dialog_remember);
		tfDialogClocker = (TextView)findViewById(R.id.tf_dialog_clocker);
		tfDialogAllow = (Button)findViewById(R.id.bt_tf_dialog_allow);
		tfDialogDeny = (Button)findViewById(R.id.bt_tf_dialog_deny);
		
		Bundle bundle = this.getIntent().getExtras();
		dataTable = AppUtil.getAppTable(context, bundle.getString(Consts.TF_APP_PACKAGENAME));
		
		tfDialogAppIcon.setBackgroundDrawable(dataTable.icon);
		tfDialogAppName.setText(dataTable.name);
		
		new CountDownTimer(30000, 1000)
		{
			
			@Override
			public void onTick(long millisUntilFinished)
			{
				tfDialogClocker.setText("" + millisUntilFinished / 1000);
			}
			
			@Override
			public void onFinish()
			{
				TaskFilterDialogActivity.this.finish();
			}
		}.start();
	}
	
	private void setListeners()
	{
		tfDialogAllow.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (tfDialogRemember.isChecked())
				{
					dataTable.allow = 1;
					db = DBHelper.getInstance(context);
					synchronized (db)
					{
						db.cleanup();
						db.establishDb();
						db.insert(dataTable);
						db.cleanup();
					}
					MobclickAgent.onEvent(context, "allow");
				}
				TaskFilterDialogActivity.this.finish();
			}
		});
		
		tfDialogDeny.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (tfDialogRemember.isChecked())
				{
					dataTable.allow = 0;
					db = DBHelper.getInstance(context);
					synchronized (db)
					{
						db.cleanup();
						db.establishDb();
						db.insert(dataTable);
						db.cleanup();
					}
					MobclickAgent.onEvent(context, "deny");
				}
				AppUtil.finishTask(context, dataTable.packagename);
				TaskFilterDialogActivity.this.finish();
			}
		});
	}
}
