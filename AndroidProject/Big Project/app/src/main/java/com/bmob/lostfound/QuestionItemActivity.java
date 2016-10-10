package com.bmob.lostfound;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bmob.lostfound.R;
import com.bmob.lostfound.bean.Answer;
import com.bmob.lostfound.bean.Question;
import com.bmob.lostfound.bean.User;
import com.bmob.lostfound.config.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

public class QuestionItemActivity extends BaseActivity implements View.OnClickListener{


    Question question;
    TextView question_title;
    TextView question_content;
    TextView question_time;
    TextView question_answerNum;
    TextView question_username;

    ListView list_answers;
    List<Answer> list2;

    Button btn_add;

    MyAdapterAnswer myAdapterAnswer;

    private List<User> users;
    private List<String> contents;
    private List<String> times;
    private List<Integer> agreeNum;
    private List<Boolean> ischecks;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_question_item);
    }

    @Override
    public void initViews() {
        question_title = (TextView) findViewById(R.id.question_title);
        question_content = (TextView) findViewById(R.id.question_content);
        question_time = (TextView) findViewById(R.id.question_time);
        question_answerNum = (TextView) findViewById(R.id.question_answerNum);
        question_username = (TextView) findViewById(R.id.question_username);
        btn_add = (Button) findViewById(R.id.btn_add);
        list_answers = (ListView) findViewById(R.id.list_answers);
    }

    @Override
    public void initListeners() {
        btn_add.setOnClickListener(this);
    }

    @Override
    public void initData() {
        list2 = new ArrayList<>();
        myAdapterAnswer = new MyAdapterAnswer(this);
        getQuestion();
    }

    private void showAnswer() {

        users = new ArrayList<>();
        contents = new ArrayList<>();
        times = new ArrayList<>();
        agreeNum = new ArrayList<>();
        ischecks = new ArrayList<>();

        BmobQuery<Answer> query = new BmobQuery<Answer>();
        query.addWhereEqualTo("question", question);
        query.include("author");
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Answer>() {
            @Override
            public void onSuccess(List<Answer> list) {
                list2 = list;
                for (int i = 0; i < list.size(); i++) {
                    Answer answer = list.get(i);
                    users.add(answer.getAuthor());
                    contents.add(answer.getContent());
                    times.add(answer.getCreatedAt());
                    agreeNum.add(answer.getAgreeNum());
                    ischecks.add(false);
                }

                getAgreeState(list);

            }

            @Override
            public void onError(int i, String s) {
                System.out.println("66666666666");
            }
        });

    }

    private void getAgreeState(final List<Answer> list) {
        BmobQuery<Answer> query = new BmobQuery<Answer>();
        User user = BmobUser.getCurrentUser(this, User.class);
        query.addWhereRelatedTo("agrees", new BmobPointer(user));
        query.findObjects(this, new FindListener<Answer>() {
            @Override
            public void onSuccess(List<Answer> l) {
                System.out.println(l.size());
                for (int i = 0; i < l.size(); i++) {
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j).getObjectId().equals(l.get(i).getObjectId())) {
                            System.out.println("1113333111");
                            ischecks.set(j, true);
                        }
                    }
                }
                list_answers.setAdapter(myAdapterAnswer);
                myAdapterAnswer.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

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
                showAnswer();
                setResult(RESULT_OK);
                break;
        }
    }


    private void getQuestion() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String questionID = bundle.getString("questionID");

        BmobQuery<Question> query = new BmobQuery<Question>();
        query.addWhereEqualTo("objectId",questionID);
        query.include("author");
        query.findObjects(this, new FindListener<Question>() {
            @Override
            public void onSuccess(List<Question> list) {
                question = list.get(0);
                initQuestion();
                showAnswer();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void initQuestion() {
        if (question != null) {
            question_title.setText(question.getTitle());
            question_content.setText(question.getContent());
            question_time.setText(question.getCreatedAt());
            question_answerNum.setText(question.getAnswerNum() + "回答");
            question_username.setText(question.getAuthor().getName());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_add) {
            Intent intent = new Intent(QuestionItemActivity.this, AddAnswerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("questionId", question.getObjectId());
            intent.putExtras(bundle);
            startActivityForResult(intent, Constants.REQUESTCODE_ADD);
        }
    }

    //退出时更新点赞数据
    public void onDestroy(){
        super.onDestroy();
        for (int i = 0; i < list2.size(); i++) {
            Answer answer = list2.get(i);
            User user = BmobUser.getCurrentUser(this, User.class);
            BmobRelation relation = new BmobRelation();
            if (ischecks.get(i)) {
                relation.add(answer);
            } else {
                relation.remove(answer);
            }
            user.setAgrees(relation);
            user.update(this, new UpdateListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int i, String s) {
                }
            });

            answer.setAgreeNum(agreeNum.get(i));
            answer.update(this, new UpdateListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
    }






    public class MyAdapterAnswer extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;


        public MyAdapterAnswer(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(this.context);
        }


        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_answer,parent,false);
                TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                final TextView tv_agreeNum = (TextView) convertView.findViewById(R.id.tv_agreeNum);
                final ImageView iv_agree = (ImageView) convertView.findViewById(R.id.iv_agree);

                tv_name.setText(users.get(position).getName());
                tv_content.setText(contents.get(position));
                tv_time.setText(times.get(position));
                tv_agreeNum.setText(agreeNum.get(position) + "");

                if (ischecks.get(position)) {
                    iv_agree.setImageResource(R.drawable.icon_agree_true);
                } else {
                    iv_agree.setImageResource(R.drawable.icon_agree_false);
                }
                iv_agree.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (ischecks.get(position)) {
                            iv_agree.setImageResource(R.drawable.icon_agree_false);
                            ischecks.set(position, false);
                            agreeNum.set(position, agreeNum.get(position) - 1);
                            tv_agreeNum.setText(agreeNum.get(position) + "");
                        } else {
                            iv_agree.setImageResource(R.drawable.icon_agree_true);
                            ischecks.set(position,true);
                            agreeNum.set(position, agreeNum.get(position) + 1);
                            tv_agreeNum.setText(agreeNum.get(position) + "");
                        }
                    }
                });
            }
            return convertView;
        }
    }

}
