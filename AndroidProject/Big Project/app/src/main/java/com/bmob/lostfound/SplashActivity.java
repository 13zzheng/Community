package com.bmob.lostfound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bmob.lostfound.bean.User;
import com.bmob.lostfound.loginRegistActivity.LoginActivity;
import com.bmob.lostfound.mainactivity.MainActivity;

import cn.bmob.v3.BmobUser;

/** ����ҳ
  * @ClassName: SplashActivity
  * @Description: TODO
  * @author smile
  * @date 2014-5-21 ����2:21:28
  */
@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity {

	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_splash);

	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessageDelayed(GO_HOME, 3000);
	}

	public void goHome() {
		//BmobUser.logOut(this);
		User user = BmobUser.getCurrentUser(this,User.class);
		if (user != null) {
			System.out.println(user.getName());
			startActivity(new Intent(SplashActivity.this,MainActivity.class));
		} else {
			startActivity(new Intent(SplashActivity.this, LoginActivity.class));
		}
		finish();

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
