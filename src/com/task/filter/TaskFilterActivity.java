package com.task.filter;

import java.util.ArrayList;
import java.util.List;

import com.admogo.AdMogoListener;
import com.admogo.AdMogoManager;
import com.feedback.NotificationType;
import com.feedback.UMFeedbackService;
//import com.kuguo.pushads.PushAdsManager;
import com.mobclick.android.MobclickAgent;
import com.task.filter.R;
import com.task.filter.data.DBDataTableApps;
import com.task.filter.data.DBHelper;
import com.task.filter.data.SettingsSP;
import com.task.filter.model.AppListAdapter;
import com.task.filter.service.TaskFilterService;
import com.task.filter.util.Consts;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskFilterActivity extends Activity implements AdMogoListener
{
	private Context context = this;
	private CheckBox bootStartCheckBox;
	private CheckBox startServiceCheckBox;
	private CheckBox includeSysCheckBox;
	private TextView dividerTextView1;
	private TextView dividerTextView2;
	private TextView proTextView;
	private TextView allowTextView;
	private TextView denyTextView;
	private ListView allowAppListView;
	private ListView denyAppListView;
	
	private SettingsSP sSP;
	private AppListAdapter allowAppListAdapter;
	private AppListAdapter denyAppListAdapter;
	private List<DBDataTableApps> allowAppList;
	private List<DBDataTableApps> denyAppList;
	private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initMainView();
        
//        AdMogoLayout adMogoLayout = (AdMogoLayout)findViewById(R.id.admogo_layout);
//		adMogoLayout.setAdMogoListener(this);
//        PushAdsManager.getInstance().receivePushMessage(context);
        MobclickAgent.onError(context);
        UMFeedbackService.enableNewReplyNotification(this, NotificationType.NotificationBar);
    }
    @Override
    protected void onResume()
	{
		refMainView();
		setMainViewListener();
		
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
    private void initMainView()
    {
    	bootStartCheckBox = (CheckBox)findViewById(R.id.tf_settings_bootstart);
    	startServiceCheckBox = (CheckBox)findViewById(R.id.tf_settings_startservice);
    	includeSysCheckBox = (CheckBox)findViewById(R.id.tf_settings_include_sys);
    	
    	dividerTextView1 = (TextView)findViewById(R.id.tf_main_divider1);
    	dividerTextView2 = (TextView)findViewById(R.id.tf_main_divider2);
    	proTextView = (TextView)findViewById(R.id.tf_main_pro);
    	allowTextView = (TextView)findViewById(R.id.tf_settings_allow);
    	denyTextView = (TextView)findViewById(R.id.tf_settings_deny);
    	
    	allowAppListView = (ListView)findViewById(R.id.tf_settings_allow_app);
    	denyAppListView = (ListView)findViewById(R.id.tf_settings_deny_app);
    	
    	sSP = SettingsSP.getInstance(context);
    	sSP.setMyPreferences(Consts.TF_SP_SETTINGS);
    	sSP.setMyEditor();
    	
    	allowAppList = new ArrayList<DBDataTableApps>();
    	denyAppList = new ArrayList<DBDataTableApps>();
    	
    	if (!sSP.getBooleanStatus(Consts.TF_SP_ST_BOOT_START))
    	{
    		bootStartCheckBox.setChecked(false);
    	}
    	if (!sSP.getBooleanStatus(Consts.TF_SP_ST_START))
    	{
    		startServiceCheckBox.setChecked(false);
    	}
    	if (!sSP.getBooleanStatus(Consts.TF_SP_ST_INCLUDE_SYS))
    	{
    		includeSysCheckBox.setChecked(false);
    	}
    	if (startServiceCheckBox.isChecked())
		{
			sSP.saveBoolean(Consts.TF_SP_ST_START, true);
			startService(new Intent(context, TaskFilterService.class));
		}
		else
		{
			sSP.saveBoolean(Consts.TF_SP_ST_START, false);
			stopService(new Intent(context, TaskFilterService.class));
		}
    }
    
    private void setMainViewListener()
    {
    	bootStartCheckBox.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (bootStartCheckBox.isChecked())
				{
					sSP.saveBoolean(Consts.TF_SP_ST_BOOT_START, true);
				}
				else
				{
					sSP.saveBoolean(Consts.TF_SP_ST_BOOT_START, false);
				}
			}
		});
    	startServiceCheckBox.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (startServiceCheckBox.isChecked())
				{
					sSP.saveBoolean(Consts.TF_SP_ST_START, true);
					startService(new Intent(context, TaskFilterService.class));
				}
				else
				{
					sSP.saveBoolean(Consts.TF_SP_ST_START, false);
					stopService(new Intent(context, TaskFilterService.class));
				}
			}
		});
    	includeSysCheckBox.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (includeSysCheckBox.isChecked())
				{
					sSP.saveBoolean(Consts.TF_SP_ST_INCLUDE_SYS, true);
				}
				else
				{
					sSP.saveBoolean(Consts.TF_SP_ST_INCLUDE_SYS, false);
				}
			}
		});
    }
    
    private void refMainView()
    {
    	dividerTextView1.setVisibility(View.GONE);
    	dividerTextView2.setVisibility(View.GONE);
    	proTextView.setVisibility(View.GONE);
    	allowTextView.setVisibility(View.GONE);
    	denyTextView.setVisibility(View.GONE);
    	allowAppListView.setVisibility(View.GONE);
    	denyAppListView.setVisibility(View.GONE);
    	
    	db = DBHelper.getInstance(context);
    	synchronized (db)
		{
			db.cleanup();
			db.establishDb();
			db.getAppInfoByAllow(allowAppList, 1);
			db.establishDb();
			db.getAppInfoByAllow(denyAppList, 0);
			db.cleanup();
			if (!allowAppList.isEmpty())
			{
				dividerTextView1.setVisibility(View.VISIBLE);
				dividerTextView2.setVisibility(View.VISIBLE);
				proTextView.setVisibility(View.VISIBLE);
				allowTextView.setVisibility(View.VISIBLE);
				allowAppListView.setVisibility(View.VISIBLE);
				
				allowAppListAdapter = new AppListAdapter(context, allowAppList);
				
				int totolHeight = 0;
				for (int i = 0; i < allowAppListAdapter.getCount(); i++)
				{
					View listItem = allowAppListAdapter.getView(i, null, allowAppListView);
					listItem.measure(0, 0);
					totolHeight += listItem.getMeasuredHeight();
				}
				ViewGroup.LayoutParams params = allowAppListView.getLayoutParams();
				params.height = totolHeight + (allowAppListView.getDividerHeight() * (allowAppListAdapter.getCount() - 1));
				allowAppListView.setLayoutParams(params);
				
				allowAppListView.setAdapter(allowAppListAdapter);
				allowAppListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
				{
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo)
					{
						menu.setHeaderTitle(getResources().getString(R.string.tf_cm_title));
						menu.add(Menu.FIRST, Menu.FIRST, 0, getResources().getString(R.string.tf_cm_deny));
						menu.add(Menu.FIRST, Menu.FIRST + 1, 0, getResources().getString(R.string.tf_cm_delete));
					}
				});
			}
			if (!denyAppList.isEmpty())
			{
				dividerTextView1.setVisibility(View.VISIBLE);
				dividerTextView2.setVisibility(View.VISIBLE);
				proTextView.setVisibility(View.VISIBLE);
				denyTextView.setVisibility(View.VISIBLE);
				denyAppListView.setVisibility(View.VISIBLE);
				
		    	denyAppListAdapter = new AppListAdapter(context, denyAppList);
		    	
		    	int totolHeight = 0;
				for (int i = 0; i < denyAppListAdapter.getCount(); i++)
				{
					View listItem = denyAppListAdapter.getView(i, null, denyAppListView);
					listItem.measure(0, 0);
					totolHeight += listItem.getMeasuredHeight();
				}
				ViewGroup.LayoutParams params =denyAppListView.getLayoutParams();
				params.height = totolHeight + (denyAppListView.getDividerHeight() * (denyAppListAdapter.getCount() - 1));
				denyAppListView.setLayoutParams(params);
		    	
		    	denyAppListView.setAdapter(denyAppListAdapter);
		    	denyAppListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
				{
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo)
					{
						menu.setHeaderTitle(getResources().getString(R.string.tf_cm_title));
						menu.add(Menu.FIRST + 1, Menu.FIRST, 0, getResources().getString(R.string.tf_cm_allow));
						menu.add(Menu.FIRST + 1, Menu.FIRST + 1, 0, getResources().getString(R.string.tf_cm_delete));
					}
				}) ;
			}
		}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
    	AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

    	if (Menu.FIRST == item.getGroupId())
    	{
    		if (Menu.FIRST == item.getItemId())
    		{
    			cmDeny(menuInfo.position);
    		}
    		else
    		{
    			cmDeleteAllow(menuInfo.position);
    		}
    	}
    	else
    	{
    		if (Menu.FIRST == item.getItemId())
    		{
    			cmAllow(menuInfo.position);
    		}
    		else
    		{
    			cmDeleteDeny(menuInfo.position);
    		}
    	}
    	return super.onContextItemSelected(item);
    }
    
	private void cmDeleteDeny(int position)
	{
		DBDataTableApps location;
		if (null != (location = denyAppList.get(position)))
		{
			db = DBHelper.getInstance(context);
			db.establishDb();
			db.delete(location.id);
			db.cleanup();
		}
		refMainView();
	}
	private void cmDeleteAllow(int position)
	{
		DBDataTableApps location;
		if (null != (location = allowAppList.get(position)))
		{
			db = DBHelper.getInstance(context);
			db.establishDb();
			db.delete(location.id);
			db.cleanup();
		}
		refMainView();
	}
	private void cmAllow(int position)
	{
		DBDataTableApps location;
		if (null != (location = denyAppList.get(position)))
		{
			location.allow = 1;
			db = DBHelper.getInstance(context);
			db.establishDb();
			db.update(location);
			db.cleanup();
		}
		refMainView();
	}
	private void cmDeny(int position)
	{
		DBDataTableApps location;
		if (null != (location = allowAppList.get(position)))
		{
			location.allow = 0;
			db = DBHelper.getInstance(context);
			db.establishDb();
			db.update(location);
			db.cleanup();
		}
		refMainView();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add(0, Consts.MENU_FEEDBACK, Consts.MENU_FEEDBACK, R.string.tf_menu_feedback).setIcon(R.drawable.menu_feedback);
		menu.add(0, Consts.MENU_UPDATE, Consts.MENU_UPDATE, R.string.tf_menu_update).setIcon(R.drawable.menu_update);
		menu.add(0, Consts.MENU_ABOUT, Consts.MENU_ABOUT, R.string.tf_menu_about).setIcon(R.drawable.menu_about);
		menu.add(0, Consts.MENU_SHARE, Consts.MENU_SHARE, R.string.tf_menu_share).setIcon(R.drawable.menu_share);
		menu.add(0, Consts.MENU_QUIT, Consts.MENU_QUIT, R.string.tf_menu_quit).setIcon(R.drawable.menu_quit);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case Consts.MENU_FEEDBACK:
			UMFeedbackService.openUmengFeedbackSDK(this);
			break;
		case Consts.MENU_UPDATE:
			MobclickAgent.update(context);
			break;
		case Consts.MENU_ABOUT:
			showAboutusDialog();
			break;
		case Consts.MENU_QUIT:
			exit();
			break;
		case Consts.MENU_SHARE:
			shareToFriends();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void exit()
	{
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(getString(R.string.tf_menu_exit_content));

		builder.setTitle(getString(R.string.tf_menu_exit_title));
		builder.setPositiveButton(getString(R.string.tf_sys_sure),
			new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				quitAllServices();
				finish();
			}
		});
		builder.setNegativeButton(getString(R.string.tf_sys_cancel),
			new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	private void quitAllServices() 
	{
		if (!sSP.getBooleanStatus(Consts.TF_SP_ST_START))
		{
			Intent intent = new Intent(context, TaskFilterActivity.class);
			stopService(intent);
		}
	}
	
	private void shareToFriends() 
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.tf_menu_share_content));
		startActivity(Intent.createChooser(intent, getResources().getString(R.string.tf_menu_share_title)));
	}
	private void showAboutusDialog() 
	{
		// 从AndroidManifest.xml中读取版本号，并显示在“关于我们”对话框中
		String versionName = null;
		try 
		{
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionName = pinfo.versionName;
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		LinearLayout aboutLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.tf_about_dialog, null);

		TextView versionText = (TextView) aboutLayout.findViewById(R.id.tf_about_version);
		versionText.setText(versionName);
		
//		TextView netAddress = (TextView) aboutLayout.findViewById(R.id.wap_address);
//		String htmlLinkText = "<a href=\"http://app.qq.com/g/s?aid=index&g_f=990424\"><u>" 
//			+ getResources().getString(R.string.menu_about_cooperation) + "</u></a>";  
//		netAddress.setText(Html.fromHtml(htmlLinkText));  
//		netAddress.setMovementMethod(LinkMovementMethod.getInstance()); 
		
		new AlertDialog.Builder(this).setView(aboutLayout).setIcon(R.drawable.ic_launcher)
			.setTitle(R.string.tf_menu_about).setPositiveButton(R.string.tf_sys_sure,
				new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
				}
			}).show();
	}
	private long exitTime=0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (KeyEvent.KEYCODE_BACK == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) 
		{			
			if (!sSP.getBooleanStatus(Consts.TF_SP_ST_START)) 
			{
				exit();
				return true;
			}
			else 
			{
				if((System.currentTimeMillis() - exitTime) > 4000)
				{   
					Toast.makeText(getApplicationContext(), getString(R.string.tf_menu_quit_content), Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();   
				} 
				else 
				{
					TaskFilterActivity.this.finish(); 
				}
				return true;  
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClickAd()
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void onCloseMogoDialog()
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onFailedReceiveAd()
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReceiveAd()
	{
		// TODO Auto-generated method stub
		
	}
}