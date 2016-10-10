package com.bmob.lostfound.personal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bmob.lostfound.BaseActivity;
import com.bmob.lostfound.R;
import com.bmob.lostfound.bean.Found;
import com.bmob.lostfound.bean.Lost;
import com.bmob.lostfound.bean.Question;
import com.bmob.lostfound.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class MyLostFoundActivity extends BaseActivity {



    SimpleAdapter simpleAdapter;
    ListView lv_lost;
    TextView tv_title;
    String tag;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_my_lost);
    }

    @Override
    public void initViews() {
        lv_lost = (ListView) findViewById(R.id.lv_lost);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        tag = intent.getExtras().getString("tag");
        if (tag.equals("lost")) {
            initLost();
        } else if (tag.equals("found")) {
            initFound();
        } else if (tag.equals("question")) {
            initQuestion();
        }
    }

    private void initLost() {
        tv_title.setText("My Lost");
        queryLost();
    }
    private void initFound() {
        tv_title.setText("My Found");
        queryFounds();
    }
    private void initQuestion() {
        tv_title.setText("My Question");
        queryQuestion();
    }


    private void queryLost() {
        BmobQuery<Lost> query = new BmobQuery<Lost>();
        User user = BmobUser.getCurrentUser(this, User.class);
        query.addWhereEqualTo("user",user);
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Lost>() {

            @Override
            public void onSuccess(List<Lost> list) {
                setLostAdapter(list);
            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void setLostAdapter(List<Lost> list) {
        List<Map<String,Object>> listItems  = new ArrayList<>();
        for (int i = 0; i < list.size() ; i++) {
            Lost lost = list.get(i);
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("title", lost.getTitle());
            listItem.put("content",lost.getDescribe());
            listItem.put("time",lost.getCreatedAt());
            listItem.put("phone",lost.getPhone());
            listItems.add(listItem);
        }


        simpleAdapter = new SimpleAdapter(this,listItems,R.layout.item_list,
                new String[] {"title", "content" ,"time","phone"},
                new int[] {R.id.tv_title, R.id.tv_describe, R.id.tv_time, R.id.tv_photo});

        lv_lost.setAdapter(simpleAdapter);
    }

    public void queryFounds() {
        BmobQuery<Found> query = new BmobQuery<Found>();
        User user = BmobUser.getCurrentUser(this, User.class);
        query.addWhereEqualTo("user",user);
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Found>() {

            @Override
            public void onSuccess(List<Found> arg0) {
                // TODO Auto-generated method stub
                setFoundAdapter(arg0);
            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void setFoundAdapter(List<Found> list) {
        List<Map<String,Object>> listItems  = new ArrayList<>();
        for (int i = 0; i < list.size() ; i++) {
            Found found = list.get(i);
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("title", found.getTitle());
            listItem.put("content",found.getDescribe());
            listItem.put("time",found.getCreatedAt());
            listItem.put("phone",found.getPhone());
            listItems.add(listItem);
        }


        simpleAdapter = new SimpleAdapter(this,listItems,R.layout.item_list,
                new String[] {"title", "content" ,"time","phone"},
                new int[] {R.id.tv_title, R.id.tv_describe, R.id.tv_time, R.id.tv_photo});

        lv_lost.setAdapter(simpleAdapter);
    }

    private void queryQuestion() {
        BmobQuery<Question> query = new BmobQuery<Question>();
        User user = BmobUser.getCurrentUser(this, User.class);
        query.addWhereEqualTo("author",user);
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Question>() {

            @Override
            public void onSuccess(List<Question> arg0) {
                // TODO Auto-generated method stub
                setQuestionAdapter(arg0);
            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void setQuestionAdapter(List<Question> list) {
        List<Map<String,Object>> listItems  = new ArrayList<>();
        for (int i = 0; i < list.size() ; i++) {
            Question question = list.get(i);
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("title", question.getTitle());
            listItem.put("content",question.getContent());
            listItem.put("time",question.getCreatedAt());
            listItem.put("agreeNum",question.getAnswerNum() +" 回答");
            listItems.add(listItem);
        }

        simpleAdapter = new SimpleAdapter(this,listItems,R.layout.list_my_question,
                new String[] {"title", "content" ,"time","agreeNum"},
                new int[] {R.id.question_title, R.id.question_content, R.id.question_time, R.id.answer_num});

        lv_lost.setAdapter(simpleAdapter);
    }
}
