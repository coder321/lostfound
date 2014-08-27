package com.ljt.lostfound.adapter;

import java.util.List;

import com.ljt.lostfound.R;
import com.ljt.lostfound.bean.Found;
import com.ljt.lostfound.bean.Lost;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FoundAdapter extends BaseAdapter {

	private List<Found> founds;
	LayoutInflater layoutInflater;
	
	
	
	public FoundAdapter( LayoutInflater layoutInflater,List<Found> founds) {
		super();
		this.founds = founds;
		this.layoutInflater = layoutInflater;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return founds.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return founds.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.item_list,null);
			vh = new ViewHolder();
			vh.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			vh.tv_describe = (TextView) convertView
					.findViewById(R.id.tv_describe);
			vh.tv_phone = (TextView) convertView
					.findViewById(R.id.tv_phone);
			vh.tv_createdAt = (TextView) convertView
					.findViewById(R.id.tv_time);
			vh.tv_objectId = (TextView) convertView
					.findViewById(R.id.tv_objectId);
			convertView.setTag(vh);
			
			vh = (ViewHolder) convertView.getTag();
			Found found = founds.get(position);
			vh.tv_title.setText(found.getTitle());
			vh.tv_describe.setText(found.getDescribe());
			vh.tv_phone.setText(found.getPhone());
			vh.tv_createdAt.setText(found.getCreatedAt());
			vh.tv_objectId.setText(found.getObjectId());
			return convertView;
		}
		return convertView;
	}

}
