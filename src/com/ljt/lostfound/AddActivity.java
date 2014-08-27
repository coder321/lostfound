package com.ljt.lostfound;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.ljt.lostfound.bean.Found;
import com.ljt.lostfound.bean.Lost;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddActivity extends BaseActivity implements OnClickListener{

	
	private EditText edit_title, edit_phone, edit_describe;
	private Button btn_back, btn_true;

	private TextView tv_add; 
	private String from = "";
	
	String title = "";
	String describe = "";
	String photo="";
	String objectId;
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_add);
	}

	@Override
	public void initViews() {
		tv_add = (TextView) findViewById(R.id.tv_add);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_true = (Button) findViewById(R.id.btn_true);
		edit_phone = (EditText) findViewById(R.id.edit_phone);
		edit_describe = (EditText) findViewById(R.id.edit_describe);
		edit_title = (EditText) findViewById(R.id.edit_title);
	}

	@Override
	public void initListeners() {
		btn_back.setOnClickListener(this);
		btn_true.setOnClickListener(this);
	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		from = intent.getStringExtra("from");
		objectId = intent.getStringExtra("objectId");
		if("Lost".equals(from)){
			//添加lost 信息，将标题部分改掉
			tv_add.setText("添加失物信息");
		}else if("Found".equals(from)){
			tv_add.setText("添加招领信息");
		}else if("edit_lost".equals(from)){
			
			tv_add.setText("修改失物信息");
			edit_title.setText(intent.getStringExtra("title"));
			edit_describe.setText(intent.getStringExtra("describe"));
			edit_phone.setText(intent.getStringExtra("phone"));
			
		}else if("edit_found".equals(from)){
			tv_add.setText("修改招领信息");
			edit_title.setText(intent.getStringExtra("title"));
			edit_describe.setText(intent.getStringExtra("describe"));
			edit_phone.setText(intent.getStringExtra("phone"));
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.btn_back:
			//返回上一个Activity
			finish();
			break;
		case R.id.btn_true:
			if("Lost".equals(from)){
				addLost();
			}else if("Found".equals(from)){
				addFound();
			}else if("edit_found".equals(from)){
				updateFound();
			}else if("edit_lost".equals(from)){
				updateLost();
			}
			
		}
	}
	public void updateFound(){
		Found editFound = new Found();
		editFound.setTitle(edit_title.getText().toString());
		editFound.setDescribe(edit_describe.getText().toString());
		editFound.setPhone(edit_phone.getText().toString());
		editFound.update(this, objectId,new UpdateListener() {
			@Override
			public void onSuccess() {
				ShowToast("招领信息已更新");
				Intent intent = getIntent();
				//intent.putExtra("found", editFound);
				intent.putExtra("op", "update_found");
				setResult(RESULT_OK, intent);
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				
			}
		});
	}
	public void updateLost(){
		Lost editLost = new Lost();
		editLost.setTitle(edit_title.getText().toString());
		editLost.setDescribe(edit_describe.getText().toString());
		editLost.setPhone(edit_phone.getText().toString());
		editLost.update(this, objectId,new UpdateListener() {
			@Override
			public void onSuccess() {
				ShowToast("失物信息已更新");
				Intent intent = getIntent();
				//intent.putExtra("lost", editLost);
				intent.putExtra("op", "update_lost");
				setResult(RESULT_OK, intent);
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				
			}
		});
	}
	public void addFound(){
		title = edit_title.getText().toString();
		describe = edit_describe.getText().toString();
		photo = edit_phone.getText().toString();
		if(TextUtils.isEmpty(title)){
			ShowToast("请填写标题");
			return;
		}
		if(TextUtils.isEmpty(describe)){
			ShowToast("请填写描述");
			return;
		}
		if(TextUtils.isEmpty(photo)){
			ShowToast("请填写手机");
			return;
		}
		Found found = new Found();
		found.setTitle(edit_title.getText().toString());
		found.setDescribe(edit_describe.getText().toString());
		found.setPhone(edit_phone.getText().toString());
		found.save(this, new SaveListener() {

			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast(arg1);
				System.out.println("+++"+arg1+"+++");
			}

			@Override
			public void onSuccess() {
				System.out.println("成功！！！");
				ShowToast("添加招领信息成功！");
				//setResult
				Intent intent = getIntent();
				//intent.putExtra("found", editFound);
				intent.putExtra("op", "add_found");
				setResult(RESULT_OK, intent);
				//setResult(RESULT_OK);
				finish();
			}
			
		});
	}
	public void addLost(){
		
		title = edit_title.getText().toString();
		describe = edit_describe.getText().toString();
		photo = edit_phone.getText().toString();
		if(TextUtils.isEmpty(title)){
			ShowToast("请填写标题");
			return;
		}
		if(TextUtils.isEmpty(describe)){
			ShowToast("请填写描述");
			return;
		}
		if(TextUtils.isEmpty(photo)){
			ShowToast("请填写手机");
			return;
		}
		//添加信息
		System.out.println("开始添加");
		Lost lost = new Lost();
		lost.setTitle(edit_title.getText().toString());
		lost.setDescribe(edit_describe.getText().toString());
		lost.setPhone(edit_phone.getText().toString());
		lost.save(this, new SaveListener() {

			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast(arg1);
				System.out.println("+++"+arg1+"+++");
			}

			@Override
			public void onSuccess() {
				System.out.println("成功！！！");
				ShowToast("添加失物信息成功！");
				//setResult
				Intent intent = getIntent();
				//intent.putExtra("found", editFound);
				intent.putExtra("op", "add_lost");
				setResult(RESULT_OK, intent);
				//setResult(RESULT_OK);
				finish();
			}
			
		});
	}

}
