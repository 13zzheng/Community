package com.bmob.lostfound.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bmob.lostfound.AddQAActivity;
import com.bmob.lostfound.QuestionItemActivity;
import com.bmob.lostfound.R;
import com.bmob.lostfound.bean.Question;
import com.bmob.lostfound.config.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class QAFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    ListView list_question;

    LinearLayout layout_no;

    SimpleAdapter simpleAdapter;

    Button btn_add;

    private List<Question> questions;

    // TODO: Rename and change types and number of parameters
    public static QAFragment newInstance(String param1) {
        QAFragment fragment = new QAFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public QAFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qa, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        list_question = (ListView) view.findViewById(R.id.list_question);
        layout_no = (LinearLayout) view.findViewById(R.id.layout_no);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        initListeners();
        initData();
    }

    private void initListeners() {
        btn_add.setOnClickListener(this);
        list_question.setOnItemClickListener(this);
    }

    private void initData() {
        questions = new ArrayList<>();
        queryQuestion();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_add) {

            Intent intent = new Intent(getActivity(),AddQAActivity.class);
            startActivityForResult(intent, Constants.REQUESTCODE_ADD);
        }
    }

    private List<Integer> answerNums;
    private void queryQuestion() {

        showView();
        BmobQuery<Question> query = new BmobQuery<Question>();
        query.order("-createdAt");
        query.include("author");
        query.findObjects(getActivity(), new FindListener<Question>() {

            @Override
            public void onSuccess(List<Question> arg0) {
                // TODO Auto-generated method stub
                questions = arg0;
                if (arg0 == null || arg0.size() == 0) {
                    showErrorView();
                    return;
                }
                //queryQuestionAnswerNum(arg0);
                setSimpleAdapter(arg0);
                //list_question.setAdapter(simpleAdapter);
            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub
                showErrorView();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUESTCODE_ADD:
                queryQuestion();
                break;
        }
    }


    private void setSimpleAdapter(List<Question> list) {
        List<Map<String,Object>> listItems  = new ArrayList<>();
        for (int i = 0; i < list.size() ; i++) {
            Question question = list.get(i);
            System.out.println(question.getTitle());
            System.out.println(question.getAuthor().getName());
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("title", question.getTitle());
            listItem.put("content",question.getContent());
            listItem.put("username",question.getAuthor().getName());
            listItem.put("answer_num",question.getAnswerNum() + " 回答");


            listItems.add(listItem);
        }
        simpleAdapter = new SimpleAdapter(getActivity(),listItems,R.layout.list_question,
                new String[] {"title", "content" ,"answer_num", "username"},
                new int[] {R.id.question_title, R.id.question_content, R.id.answer_num, R.id.tv_username});

        list_question.setAdapter(simpleAdapter);

    }

    private void showView() {
        list_question.setVisibility(View.VISIBLE);
        layout_no.setVisibility(View.GONE);
    }

    private void showErrorView() {
        list_question.setVisibility(View.GONE);
        layout_no.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Question question = questions.get(position);
        Intent intent = new Intent(getActivity(),QuestionItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("questionID",question.getObjectId());
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.REQUESTCODE_ADD);
    }
}
