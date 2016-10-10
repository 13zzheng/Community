package com.bmob.lostfound.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bmob.lostfound.R;
import com.bmob.lostfound.bean.User;
import com.bmob.lostfound.loginRegistActivity.LoginActivity;
import com.bmob.lostfound.personal.MyLostFoundActivity;

import cn.bmob.v3.BmobUser;


public class PersonalFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    LinearLayout ll_name;
    LinearLayout ll_lost;
    LinearLayout ll_found;
    LinearLayout ll_question;
    LinearLayout ll_answer;

    TextView tv_logout;
    TextView tv_name;

    private User mUser;




    // TODO: Rename and change types and number of parameters
    public static PersonalFragment newInstance(String param1) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public PersonalFragment() {
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
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ll_name = (LinearLayout) view.findViewById(R.id.ll_name);
        ll_lost = (LinearLayout) view.findViewById(R.id.ll_lost);
        ll_found = (LinearLayout) view.findViewById(R.id.ll_found);
        ll_question = (LinearLayout) view.findViewById(R.id.ll_question);
        ll_answer = (LinearLayout) view.findViewById(R.id.ll_answer);
        tv_logout = (TextView) view.findViewById(R.id.tv_logout);
        tv_name = (TextView) view.findViewById(R.id.tv_name);

        initListeners();
        initData();
    }

    private void initListeners() {
        ll_lost.setOnClickListener(this);
        ll_found.setOnClickListener(this);
        ll_question.setOnClickListener(this);
        ll_answer.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
    }

    private void initData() {
        mUser = BmobUser.getCurrentUser(getActivity(), User.class);
        tv_name.setText(mUser.getName());
    }


    @Override
    public void onClick(View v) {
        if (v == tv_logout) {
            BmobUser.logOut(getActivity());
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        } else if (v == ll_lost) {
            Intent intent = new Intent(getActivity(), MyLostFoundActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("tag","lost");
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v ==ll_found) {
            Intent intent = new Intent(getActivity(), MyLostFoundActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("tag","found");
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v ==ll_question) {
            Intent intent = new Intent(getActivity(), MyLostFoundActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("tag","question");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
