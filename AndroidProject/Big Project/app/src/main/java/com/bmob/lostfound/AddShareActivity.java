package com.bmob.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bmob.lostfound.bean.Answer;
import com.bmob.lostfound.bean.Question;
import com.bmob.lostfound.bean.Share;
import com.bmob.lostfound.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class AddShareActivity extends BaseActivity implements View.OnClickListener{



    Button btn_back;
    Button btn_true;

    EditText et_content;

    Share share;

    TextView tv_title;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_add_share);
    }

    @Override
    public void initViews() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_true = (Button) findViewById(R.id.btn_true);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    public void initListeners() {
        btn_true.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tv_title.setText("Share Info");
    }


    @Override
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
        } else if (v == btn_true) {
            addShare();
        }
    }

    private void addShare() {
        String content = et_content.getText().toString();
        if (TextUtils.isEmpty(content)) {

            return;
        }
        User user = BmobUser.getCurrentUser(this, User.class);
        Share share = new Share();
        share.setContent(content);
        share.setAuthor(user);
        share.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
    }

}
