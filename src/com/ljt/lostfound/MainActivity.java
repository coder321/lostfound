package com.ljt.lostfound;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.ljt.config.Constants;
import com.ljt.lostfound.adapter.FoundAdapter;
import com.ljt.lostfound.adapter.LostAdapter;

import com.ljt.lostfound.base.EditPopupWindow;
import com.ljt.lostfound.bean.Found;
import com.ljt.lostfound.bean.Lost;
import com.ljt.lostfound.i.IPopupItemClick;


public class MainActivity extends BaseActivity implements OnClickListener,OnItemLongClickListener, IPopupItemClick {

	
	RelativeLayout layout_action;// 创建失物和招领的菜单按钮
	LinearLayout layout_all; //连个按钮
	TextView tv_lost;
	ListView listview; //显示数据的信息
	Button btn_add; 

	protected List<Lost> losts;// 失物
	protected List<Found> founds;// 失物
	//protected QuickAdapter<Found> FoundAdapter;// 招领

	private Button layout_found;
	private Button layout_lost;
	PopupWindow morePop;

	RelativeLayout progress;
	LinearLayout layout_no;
	TextView tv_no;

	LostAdapter lostAdapter;
	FoundAdapter foundAdapter;
	
	EditPopupWindow mPopupWindow;
	
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void initViews() {
		progress = (RelativeLayout) findViewById(R.id.progress);
		layout_no = (LinearLayout) findViewById(R.id.layout_no);
		tv_no = (TextView) findViewById(R.id.tv_no);

		layout_action = (RelativeLayout) findViewById(R.id.layout_action);
		layout_all = (LinearLayout) findViewById(R.id.layout_all);
		// 默认是失物界面
		tv_lost = (TextView) findViewById(R.id.tv_lost);
		tv_lost.setTag("Lost");
		listview = (ListView) findViewById(R.id.list_lost);
		btn_add = (Button) findViewById(R.id.btn_add);
		// 初始化长按弹窗
		initEditPop();
	}

	@Override
	public void initListeners() {
		listview.setOnItemLongClickListener(this);
		btn_add.setOnClickListener(this);
		layout_all.setOnClickListener(this);
		
	}

	
	@Override
	public void initData() {
		findLostAll();

	}
	
	
	private void showListPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_lost, null);
		// 注入
		layout_found = (Button) view.findViewById(R.id.layout_found);
		layout_lost = (Button) view.findViewById(R.id.layout_lost);
		layout_found.setOnClickListener(this);
		layout_lost.setOnClickListener(this);
		morePop = new PopupWindow(view, mScreenWidth, 600);

		morePop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					morePop.dismiss();
					return true;
				}
				return false;
			}

		
		});

		morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		morePop.setTouchable(true);
		morePop.setFocusable(true);
		morePop.setOutsideTouchable(true);
		morePop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从顶部弹下
		morePop.setAnimationStyle(R.style.MenuPop);
		morePop.showAsDropDown(layout_action, 0, -dip2px(this, 2.0F));
	}
	
	
	
	
	public void findLostAll(){
		progress.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);
		layout_no.setVisibility(View.GONE);
		BmobQuery<Lost> query = new BmobQuery<Lost>();
		query.order("-createdAt");// 按照时间降序
		query.setLimit(10);   
		query.findObjects(this, new FindListener<Lost>() {
			@Override
			public void onError(int arg0, String msg) {
				//查询错误
				//ShowToast("count failure："+msg);
				System.out.println("---"+msg+"---");
				showErrorView(arg0);
			}
			@Override
			public void onSuccess(List<Lost> losts) {
				//查询成功
				//System.out.println(losts.get(0).getObjectId()+"----------------------");
				MainActivity.this.losts = losts;
				lostAdapter = new LostAdapter(getLayoutInflater(),losts);
				listview.setAdapter(lostAdapter);
				//lostAdapter.notifyDataSetChanged();
				showView();
			}
		});
		
	}
	
	//查询前十条招领信息
		public void findFoundAll(){
			progress.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
			layout_no.setVisibility(View.GONE);
			BmobQuery<Found> query = new BmobQuery<Found>();
			query.order("-createdAt");// 按照时间降序
			query.setLimit(10);   
			query.findObjects(this, new FindListener<Found>() {
				@Override
				public void onError(int arg0, String msg) {
					//查询错误
					//ShowToast("count failure："+msg);
					System.out.println("---"+msg+"---");
					showErrorView(arg0);
				}
				@Override
				public void onSuccess(List<Found> founds) {
					//查询成功
					MainActivity.this.founds = founds;
					foundAdapter = new FoundAdapter(getLayoutInflater(),MainActivity.this.founds);
					listview.setAdapter(foundAdapter);
					foundAdapter.notifyDataSetChanged();
					showView();
				}
			});
			
		}
	//将暂无数据页面以及滚动也隐藏将listView 显示
	private void showView() {
		listview.setVisibility(View.VISIBLE);
		layout_no.setVisibility(View.GONE);
		progress.setVisibility(View.GONE);
	}
	
	

	

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btn_add:
			//添加招领和失物的按钮+
			Intent intent = new Intent(this, AddActivity.class);
			intent.putExtra("from", tv_lost.getTag().toString());
			startActivityForResult(intent, Constants.REQUESTCODE_ADD);
			break;
		case R.id.layout_all:
			//切换数据 失物 === 招领
			showListPop();
			break;
		case R.id.layout_found:
			//招领数据
			tv_lost.setTag("Found");
			tv_lost.setText("招领");
			morePop.dismiss();
			findFoundAll();
			break;
		case R.id.layout_lost:
			//失物数据
			tv_lost.setTag("Lost");
			tv_lost.setText("失物");
			morePop.dismiss();
			findLostAll();
			break;
		default:
			System.out.println(":ERROR:");
			break;
		} 
		
		
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.REQUESTCODE_ADD && resultCode == RESULT_OK) {
			String op = data.getStringExtra("op");
			
			if("add_lost".equals(op)){
				findLostAll();
			}else if ("add_found".equals(op)){
				findFoundAll();
			}else if("update_lost".equals(op)){
				findLostAll();
			}else if ("update_found".equals(op)){
				findFoundAll();
			}
			
			
			
			/*String tag = tv_lost.getTag().toString();
			if (tag.equals("Lost")) {
				findLostAll();
			} else {
				//queryFounds();
				findFoundAll(); 
			}*/
		}
	
	
	}
	
	int position;
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		position = arg2;
		System.out.println("position:==="+arg2);
		int[] location = new int[2];
		arg1.getLocationOnScreen(location);
		mPopupWindow.showAtLocation(arg1, Gravity.RIGHT | Gravity.TOP,
				location[0], getStateBar() + location[1]);
		return false;
		
	}
	private void initEditPop() {
		mPopupWindow = new EditPopupWindow(this, 200, 48);
		mPopupWindow.setOnPopupItemClickListner(this);
	}
	/**
	 * 请求出错或者无数据时候显示的界面 showErrorView
	 * 
	 * @return void
	 * @throws
	 */
	private void showErrorView(int tag) {
		progress.setVisibility(View.GONE);
		listview.setVisibility(View.GONE);
		layout_no.setVisibility(View.VISIBLE);
		if (tag == 0) {
			tv_no.setText(getResources().getText(R.string.list_no_data_lost));
		} else {
			tv_no.setText(getResources().getText(R.string.list_no_data_found));
		}
	}

	@Override
	public void onEdit(View v) {
		// TODO Auto-generated method stub
		if("Lost".equals(tv_lost.getTag().toString())){
			Lost lost = (Lost)lostAdapter.getItem(position);
			Intent intent = new Intent(this,AddActivity.class);
			String title = lost.getTitle();
			String describe = lost.getDescribe();
			String phone = lost.getPhone();
			String objectId = lost.getObjectId();
			intent.putExtra("describe", describe);
			intent.putExtra("phone", phone);
			intent.putExtra("title", title);
			intent.putExtra("objectId", objectId);
			//intent.putExtra("from", tag);
			//intent.putExtra("lost", lost);
			intent.putExtra("from", "edit_lost");
			startActivityForResult(intent, Constants.REQUESTCODE_ADD);
		}else if("Found".equals(tv_lost.getTag().toString())){
			Found f = (Found)foundAdapter.getItem(position);
			Intent intent = new Intent(this,AddActivity.class);
			String title = f.getTitle();
			String describe = f.getDescribe();
			String phone = f.getPhone();
			String objectId = f.getObjectId();
			intent.putExtra("describe", describe);
			intent.putExtra("phone", phone);
			intent.putExtra("title", title);
			intent.putExtra("objectId", objectId);
			//intent.putExtra("found", f);
			intent.putExtra("from", "edit_found");
			startActivityForResult(intent, Constants.REQUESTCODE_ADD);
		}
	}

	@Override
	public void onDelete(View v) {
		// TODO Auto-generated method stub
		if("Lost".equals(tv_lost.getTag().toString())){
			Lost lost = (Lost)lostAdapter.getItem(position);
			lost.delete(this, lost.getObjectId(), new DeleteListener() {
				@Override
				public void onSuccess() {
					ShowToast("失物信息已删除");
					//findLostAll();
					losts.remove(position);
					
					lostAdapter.notifyDataSetChanged();
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					
				}
			});
		}else if("Found".equals(tv_lost.getTag().toString())){
			Found found = (Found)foundAdapter.getItem(position);
			found.delete(this, found.getObjectId(), new DeleteListener() {
				@Override
				public void onSuccess() {
					ShowToast("招领信息已删除");
					//findFoundAll();
					founds.remove(position);
					foundAdapter.notifyDataSetChanged();
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					
				}
			});
		}
	}
	
	
}
