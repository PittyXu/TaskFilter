package com.task.filter.model;

import java.util.List;

import com.task.filter.R;
import com.task.filter.data.DBDataTableApps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppListAdapter extends BaseAdapter
{
	private Context context;
	private List<DBDataTableApps> arrayList;
	
	public AppListAdapter(Context context, List<DBDataTableApps> arrayList)
	{
		this.context = context;
		this.arrayList = arrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		if (null == convertView)
		{
			holder = new ViewHolder();
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(inflater);
			// LinearLayout linearLayout = null;

			convertView = (LinearLayout) layoutInflater.inflate(R.layout.tf_allow_app_list, null);

			holder.appIcon = (ImageView) convertView.findViewById(R.id.tf_list_app_icon);
			holder.appName = (TextView) convertView.findViewById(R.id.tf_list_app_name);
			holder.appAllow = (ImageView) convertView.findViewById(R.id.tf_list_app_allow);
			holder.appDeny = (ImageView) convertView.findViewById(R.id.tf_list_app_deny);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.appIcon.setBackgroundDrawable(arrayList.get(position).icon);
		holder.appName.setText(arrayList.get(position).name);
		holder.appAllow.setVisibility(View.GONE);
		holder.appDeny.setVisibility(View.GONE);
		
		if (0 == arrayList.get(position).allow)
		{
			holder.appDeny.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.appAllow.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	@Override
	public int getCount()
	{
		try
		{
			return arrayList.size();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}
	
	static class ViewHolder
	{
		ImageView appIcon;
		TextView appName;
		ImageView appDeny;
		ImageView appAllow;
	}
}
