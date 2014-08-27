package com.ljt.lostfound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/** иафарЁ
  * @ClassName: SplashActivity
  * @Description: TODO
  * @author smile
  * @date 2014-5-21 обнГ2:21:28
  */
@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity {

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_splash);

	}

	@Override
	public void initViews() {

	}

	@Override
	public void initListeners() {

	}

	@Override
	public void initData() {
		mHandler.sendEmptyMessageDelayed(GO_HOME, 4000);
	}

	public void goHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	private static final int GO_HOME = 100;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			}
		}
	};

}
