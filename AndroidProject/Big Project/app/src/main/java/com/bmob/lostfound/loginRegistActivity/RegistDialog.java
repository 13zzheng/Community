package com.bmob.lostfound.loginRegistActivity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.bmob.lostfound.BaseActivity;
import com.bmob.lostfound.LostFoundActivity;
import com.bmob.lostfound.R;
import com.bmob.lostfound.bean.User;
import com.bmob.lostfound.mainactivity.MainActivity;
import com.bmob.lostfound.mywidget.LineEditText;

import cn.bmob.v3.listener.SaveListener;

public class RegistDialog extends BaseActivity implements View.OnClickListener,TextWatcher {

    LineEditText et_account;
    LineEditText et_password;
    LineEditText et_password_confirmation;
    LineEditText et_username;

    TextInputLayout wrapper_account;
    TextInputLayout wrapper_password;
    TextInputLayout wrapper_password_confirmation;
    TextInputLayout wrapper_username;
    TextView tv_regist;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_regist_dialog);
    }

    @Override
    public void initViews() {
        et_account = (LineEditText) findViewById(R.id.et_account);
        et_password = (LineEditText) findViewById(R.id.et_password);
        et_password_confirmation = (LineEditText) findViewById(R.id.et_password_confirmation);
        et_username = (LineEditText) findViewById(R.id.et_username);
        wrapper_account = (TextInputLayout) findViewById(R.id.wrapper_account);
        wrapper_password = (TextInputLayout) findViewById(R.id.wrapper_password);
        wrapper_password_confirmation = (TextInputLayout) findViewById(R.id.wrapper_password_confirmation);
        wrapper_username = (TextInputLayout) findViewById(R.id.wrapper_username);
        tv_regist = (TextView) findViewById(R.id.tv_regist);
    }

    @Override
    public void initListeners() {
        tv_regist.setOnClickListener(this);
        et_account.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_password_confirmation.addTextChangedListener(this);
        et_username.addTextChangedListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {

        if (v == tv_regist) {
            regist();
        }
    }

    private void regist() {
        String account = et_account.getText().toString();
        String rePassword = et_password_confirmation.getText().toString();
        String username = et_username.getText().toString();
        User user = new User();
        user.setUsername(account);
        user.setPassword(rePassword);
        user.setName(username);
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(RegistDialog.this, MainActivity.class));
                setResult(RESULT_OK);
                finish();

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        String rePassword = et_password_confirmation.getText().toString();
        String username = et_username.getText().toString();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(rePassword) && !TextUtils.isEmpty(username) &&
                !isTextError) {
            tv_regist.setTextColor(getResources().getColor(R.color.blue));
            tv_regist.setClickable(true);
        } else {
            tv_regist.setTextColor(getResources().getColor(R.color.blue_trans));
            tv_regist.setClickable(false);
        }
    }

    private boolean temp1 = true;
    private boolean temp2 = true;
    private boolean temp3 = true;
    private boolean temp4 = true;
    private boolean isTextError = false;
    @Override
    public void afterTextChanged(Editable s) {
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        String rePassword = et_password_confirmation.getText().toString();
        String username = et_username.getText().toString();

        if (account.contains(" ")) {
            if (temp1) {
                wrapper_account.setError("Can not contain Space !");
            }
            temp1 = false;
        } else {
            wrapper_account.setErrorEnabled(false);
            temp1 = true;
        }

        if (password.length() < 6 && !TextUtils.isEmpty(password)) {
            if (temp2) {
                wrapper_password.setError("At least 6 !");
            }
            temp2 = false;
        } else {
            wrapper_password.setErrorEnabled(false);
            temp2 = true;
        }

        if (!rePassword.equals(password) && !TextUtils.isEmpty(rePassword)) {
            if (temp3) {
                wrapper_password_confirmation.setError("Confirm your password !");
            }
            temp3 = false;
        } else {
            wrapper_password_confirmation.setErrorEnabled(false);
            temp3 = true;
        }

        if (username.contains(" ")) {
            if (temp4) {
                wrapper_username.setError("Can not contain Space !");
            }
            temp4 =false;
        } else {
            wrapper_username.setErrorEnabled(false);
            temp4 = true;
        }

        if (temp1 && temp2 && temp3 && temp4) {
            isTextError = false;
        } else {
            isTextError = true;
        }
    }

}
