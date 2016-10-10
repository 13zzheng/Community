package com.bmob.lostfound;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bmob.lostfound.bean.Answer;
import com.bmob.lostfound.bean.Question;
import com.bmob.lostfound.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class AddAnswerActivity extends BaseActivity implements View.OnClickListener{



    Button btn_back;
    Button btn_true;

    EditText et_content;

    Question question;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_add_answer);
    }

    @Override
    public void initViews() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_true = (Button) findViewById(R.id.btn_true);
        et_content = (EditText) findViewById(R.id.et_content);
    }

    @Override
    public void initListeners() {
        btn_true.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    private void addAnswertoQuestion() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String objectId = bundle.getString("questionId");

        BmobQuery<Question> query = new BmobQuery<Question>();
        query.getObject(this, objectId, new GetListener<Question>() {
            @Override
            public void onSuccess(Question question1) {
                System.out.println("1111111111111");
                question = question1;
                addAnswer();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
        } else if (v == btn_true) {
            addAnswertoQuestion();
        }
    }

    private void addAnswer() {
        String content = et_content.getText().toString();
        if (TextUtils.isEmpty(content)) {

            return;
        }
        User user = BmobUser.getCurrentUser(this, User.class);
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setQuestion(question);
        answer.setAuthor(user);
        answer.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                update();
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
    }

    private void update() {
        question.setAnswerNum(question.getAnswerNum() + 1);
        question.update(this, new UpdateListener() {
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
