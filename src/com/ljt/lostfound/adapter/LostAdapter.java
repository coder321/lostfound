package com.ljt.lostfound.adapter;

import java.util.List;

import com.ljt.lostfound.R;
import com.ljt.lostfound.bean.Lost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LostAdapter extends BaseAdapter{

	private List<Lost> losts;
	LayoutInflater layoutInflater;
	public LostAdapter(LayoutInflater layoutInflater,List<Lost> losts) {
		super();
		this.losts = losts;
		this.layoutInflater = layoutInflater;
	}

	@Override
	public int getCount() {
		return losts.size();
	}

	@Override
	public Object getItem(int position) {
		return losts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
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
			Lost lost = losts.get(position);
			
			vh.tv_title.setText(lost.getTitle());
			vh.tv_describe.setText(lost.getDescribe());
			vh.tv_phone.setText(lost.getPhone());
			vh.tv_createdAt.setText(lost.getCreatedAt());
			vh.tv_objectId.setText(lost.getObjectId());
			
			return convertView;
		}
		return convertView;
		//return arg2;
	}

}
