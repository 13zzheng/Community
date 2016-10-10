package com.bmob.lostfound;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bmob.lostfound.bean.Question;
import com.bmob.lostfound.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


public class AddQAActivity extends BaseActivity implements View.OnClickListener{

    Button btn_back;
    Button btn_true;

    EditText et_title;
    EditText et_content;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_add_qa);
    }

    @Override
    public void initViews() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_true = (Button) findViewById(R.id.btn_true);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
    }

    @Override
    public void initListeners() {
        btn_back.setOnClickListener(this);
        btn_true.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
        } else if (v ==btn_true) {
            addQuestion();
        }
    }

    private void addQuestion() {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();

        if (TextUtils.isEmpty(title)) {

            return;
        }
        if (TextUtils.isEmpty(content)) {

            return;
        }

        Question question = new Question();
        User user = BmobUser.getCurrentUser(this, User.class);
        question.setAuthor(user);
        question.setTitle(title);
        question.setContent(content);
        question.setAnswerNum(0);

        question.save(this, new SaveListener() {
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
