package com.bmob.lostfound.loginRegistActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bmob.lostfound.BaseActivity;
import com.bmob.lostfound.LostFoundActivity;
import com.bmob.lostfound.R;
import com.bmob.lostfound.bean.User;
import com.bmob.lostfound.config.Constants;
import com.bmob.lostfound.mainactivity.MainActivity;
import com.bmob.lostfound.mywidget.LineEditText;

import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    LineEditText et_account;
    LineEditText et_password;

    Button btn_login;
    TextView tv_regist;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);

    }

    @Override
    public void initViews() {
        et_account = (LineEditText) findViewById(R.id.et_account);
        et_password = (LineEditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_regist = (TextView) findViewById(R.id.tv_regist);
    }

    @Override
    public void initListeners() {
        btn_login.setOnClickListener(this);
        tv_regist.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        if (view == btn_login) {
            login();
        } else if (view == tv_regist) {
            regist();
        }
    }
    private void login() {
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(account)) {

            return;
        }
        if (TextUtils.isEmpty(password)) {

            return;
        }

        User user = new User();
        user.setUsername(account);
        user.setPassword(password);
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivityForResult(intent, Constants.REQUESTCODE_ADD);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUESTCODE_ADD:
                finish();
                break;
        }
    }

    private void regist() {
        startActivity(new Intent(LoginActivity.this,RegistDialog.class));
    }
}
