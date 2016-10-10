package com.bmob.lostfound.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bmob.lostfound.AddShareActivity;
import com.bmob.lostfound.R;
import com.bmob.lostfound.bean.Question;
import com.bmob.lostfound.bean.Share;
import com.bmob.lostfound.config.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class ShareFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    List<Share> shares ;

    SimpleAdapter simpleAdapter;

    Button btn_add;

    ListView lv_share;



    // TODO: Rename and change types and number of parameters
    public static ShareFragment newInstance(String param1) {
        ShareFragment fragment = new ShareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ShareFragment() {
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
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        lv_share = (ListView) view.findViewById(R.id.lv_share);
        btn_add = (Button) view.findViewById(R.id.btn_add);

        initListeners();
        initData();
    }

    private void initListeners() {
        btn_add.setOnClickListener(this);
    }

    private void initData() {
        shares = new ArrayList<>();
        queryShare();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUESTCODE_ADD:
                queryShare();
                break;
        }
    }


    private void queryShare() {
        BmobQuery<Share> query = new BmobQuery<Share>();
        query.order("-createdAt");
        query.include("author");
        query.findObjects(getActivity(), new FindListener<Share>() {

            @Override
            public void onSuccess(List<Share> arg0) {
                // TODO Auto-generated method stub
                System.out.println(arg0.size() + "aaaaaaaaaa");
                shares = arg0;
                if (arg0 == null || arg0.size() == 0) {
                    return;
                }
                //queryQuestionAnswerNum(arg0);
                setSimpleAdapter(arg0);
                //list_question.setAdapter(simpleAdapter);
            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void setSimpleAdapter(List<Share> list) {

        List<Map<String,Object>> listItems  = new ArrayList<>();
        for (int i = 0; i < list.size() ; i++) {
            Share share = list.get(i);
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("content",share.getContent());
            listItem.put("username",share.getAuthor().getName());
            listItem.put("time", share.getCreatedAt());

            listItems.add(listItem);
        }
        simpleAdapter = new SimpleAdapter(getActivity(),listItems,R.layout.list_answer,
                new String[] {"username", "content" , "time"},
                new int[] {R.id.tv_name, R.id.tv_content, R.id.tv_time});

        lv_share.setAdapter(simpleAdapter);

    }


    @Override
    public void onClick(View v) {
        if (v == btn_add) {
            System.out.println("11111111111111111111111111111111111111111111111111");
            Intent intent = new Intent(getActivity(), AddShareActivity.class);
            startActivityForResult(intent, Constants.REQUESTCODE_ADD);
        }
    }
}
