package com.bmob.lostfound.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bmob.lostfound.AddLostFoundActivity;
import com.bmob.lostfound.R;
import com.bmob.lostfound.adapter.BaseAdapterHelper;
import com.bmob.lostfound.adapter.QuickAdapter;
import com.bmob.lostfound.base.EditPopupWindow;
import com.bmob.lostfound.bean.Found;
import com.bmob.lostfound.bean.Lost;
import com.bmob.lostfound.config.Constants;
import com.bmob.lostfound.i.IPopupItemClick;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import static com.bmob.lostfound.R.id.tv_describe;
import static com.bmob.lostfound.R.id.tv_photo;
import static com.bmob.lostfound.R.id.tv_time;
import static com.bmob.lostfound.R.id.tv_title;


public class LostFoundFragment extends Fragment implements View.OnClickListener,IPopupItemClick,
        AdapterView.OnItemLongClickListener, IRefresh {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    RelativeLayout layout_action;//
    LinearLayout layout_all;
    TextView tv_lost;
    ListView listview;
    Button btn_add;

    protected QuickAdapter<Lost> LostAdapter;// ʧ��

    protected QuickAdapter<Found> FoundAdapter;// ����

    private Button layout_found;
    private Button layout_lost;
    PopupWindow morePop;

    RelativeLayout progress;
    LinearLayout layout_no;
    TextView tv_no;

    private int mScreenWidth;
    private int mScreenHeight;


    // TODO: Rename and change types and number of parameters
    public static LostFoundFragment newInstance(String param1) {
        LostFoundFragment fragment = new LostFoundFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_lostfound, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        progress = (RelativeLayout) view.findViewById(R.id.progress);
        layout_no = (LinearLayout) view.findViewById(R.id.layout_no);
        tv_no = (TextView) view.findViewById(R.id.tv_no);

        layout_action = (RelativeLayout) view.findViewById(R.id.layout_action);
        layout_all = (LinearLayout) view.findViewById(R.id.layout_all);

        tv_lost = (TextView) view.findViewById(R.id.tv_lost);
        tv_lost.setTag("Lost");
        listview = (ListView) view.findViewById(R.id.list_lost);
        btn_add = (Button) view.findViewById(R.id.btn_add);

        initEditPop();

        initListeners();
        initData();

    }

    private void initListeners() {
        listview.setOnItemLongClickListener(this);
        btn_add.setOnClickListener(this);
        layout_all.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == layout_all) {
            showListPop();
        } else if (v == btn_add) {
            Intent intent = new Intent(getActivity(), AddLostFoundActivity.class);
            intent.putExtra("from", tv_lost.getTag().toString());
            startActivityForResult(intent, Constants.REQUESTCODE_ADD);
        } else if (v == layout_found) {
            changeTextView(v);
            morePop.dismiss();
            queryFounds();
        } else if (v == layout_lost) {
            changeTextView(v);
            morePop.dismiss();
            queryLosts();
        }
    }

    public void initData() {
        // TODO Auto-generated method stub
        if (LostAdapter == null) {
            LostAdapter = new QuickAdapter<Lost>(getActivity(), R.layout.item_list) {
                @Override
                protected void convert(BaseAdapterHelper helper, Lost lost) {
                    helper.setText(tv_title, lost.getTitle())
                            .setText(tv_describe, lost.getDescribe())
                            .setText(tv_time, lost.getCreatedAt())
                            .setText(tv_photo, lost.getPhone());
                }
            };
        }

        if (FoundAdapter == null) {
            FoundAdapter = new QuickAdapter<Found>(getActivity(), R.layout.item_list) {
                @Override
                protected void convert(BaseAdapterHelper helper, Found found) {
                    helper.setText(tv_title, found.getTitle())
                            .setText(tv_describe, found.getDescribe())
                            .setText(tv_time, found.getCreatedAt())
                            .setText(tv_photo, found.getPhone());
                }
            };
        }
        listview.setAdapter(LostAdapter);

        queryLosts();
    }

    private void changeTextView(View v) {
        if (v == layout_found) {
            tv_lost.setTag("Found");
            tv_lost.setText("Found");
        } else {
            tv_lost.setTag("Lost");
            tv_lost.setText("Lost");
        }
    }

    private void showListPop() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_lost, null);
        // ע��
        layout_found = (Button) view.findViewById(R.id.layout_found);
        layout_lost = (Button) view.findViewById(R.id.layout_lost);
        layout_found.setOnClickListener(this);
        layout_lost.setOnClickListener(this);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        morePop = new PopupWindow(view, mScreenWidth, 600);

        morePop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    morePop.dismiss();
                    return true;
                }
                return false;
            }
        });

        morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        morePop.setTouchable(true);
        morePop.setFocusable(true);
        morePop.setOutsideTouchable(true);
        morePop.setBackgroundDrawable(new BitmapDrawable());
        // ����Ч�� �Ӷ�������
        morePop.setAnimationStyle(R.style.MenuPop);
        morePop.showAsDropDown(layout_action, 0, -dip2px(getActivity(), 2.0F));
    }

    private void initEditPop() {
        mPopupWindow = new EditPopupWindow(getActivity(), 200, 48);
        mPopupWindow.setOnPopupItemClickListner(this);
    }

    EditPopupWindow mPopupWindow;
    int position;


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP,
                location[0], getStateBar() + location[1]);
        return false;
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
                refresh();
                break;
        }
    }

    public void refresh() {
        String tag = tv_lost.getTag().toString();
        if (tag.equals("Lost")) {
            queryLosts();
        } else {
            queryFounds();
        }
    }

    private void queryLosts() {
        showView();
        BmobQuery<Lost> query = new BmobQuery<Lost>();
        query.order("-createdAt");//
        query.findObjects(getActivity(), new FindListener<Lost>() {

            @Override
            public void onSuccess(List<Lost> losts) {
                // TODO Auto-generated method stub
                LostAdapter.clear();
                FoundAdapter.clear();
                if (losts == null || losts.size() == 0) {
                    showErrorView(0);
                    LostAdapter.notifyDataSetChanged();
                    return;
                }
                progress.setVisibility(View.GONE);
                LostAdapter.addAll(losts);
                listview.setAdapter(LostAdapter);
            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub
                showErrorView(0);
            }
        });
    }

    public void queryFounds() {
        showView();
        BmobQuery<Found> query = new BmobQuery<Found>();
        query.order("-createdAt");// ����ʱ�併��
        query.findObjects(getActivity(), new FindListener<Found>() {

            @Override
            public void onSuccess(List<Found> arg0) {
                // TODO Auto-generated method stub
                LostAdapter.clear();
                FoundAdapter.clear();
                if (arg0 == null || arg0.size() == 0) {
                    showErrorView(1);
                    FoundAdapter.notifyDataSetChanged();
                    return;
                }
                FoundAdapter.addAll(arg0);
                listview.setAdapter(FoundAdapter);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub
                showErrorView(1);
            }
        });
    }

    private void showErrorView(int tag) {
        progress.setVisibility(View.GONE);
        listview.setVisibility(View.GONE);
        layout_no.setVisibility(View.VISIBLE);
        if (tag == 0) {
            tv_no.setText(getResources().getText(R.string.list_no_data_lost));
        } else {
            tv_no.setText(getResources().getText(R.string.list_no_data_found));
        }
    }

    private void showView() {
        listview.setVisibility(View.VISIBLE);
        layout_no.setVisibility(View.GONE);
    }

    @Override
    public void onEdit(View v) {
        String tag = tv_lost.getTag().toString();
        Intent intent = new Intent(getActivity(), AddLostFoundActivity.class);
        String title = "";
        String describe = "";
        String phone = "";
        if (tag.equals("Lost")) {
            title = LostAdapter.getItem(position).getTitle();
            describe = LostAdapter.getItem(position).getDescribe();
            phone = LostAdapter.getItem(position).getPhone();
        } else {
            title = FoundAdapter.getItem(position).getTitle();
            describe = FoundAdapter.getItem(position).getDescribe();
            phone = FoundAdapter.getItem(position).getPhone();
        }
        intent.putExtra("describe", describe);
        intent.putExtra("phone", phone);
        intent.putExtra("title", title);
        intent.putExtra("from", tag);
        startActivityForResult(intent, Constants.REQUESTCODE_ADD);
    }

    @Override
    public void onDelete(View v) {
        String tag = tv_lost.getTag().toString();
        if (tag.equals("Lost")) {
            deleteLost();
        } else {
            deleteFound();
        }
    }

    private void deleteLost() {
        Lost lost = new Lost();
        lost.setObjectId(LostAdapter.getItem(position).getObjectId());
        lost.delete(getActivity(), new DeleteListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                LostAdapter.remove(position);
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void deleteFound() {
        Found found = new Found();
        found.setObjectId(FoundAdapter.getItem(position).getObjectId());
        found.delete(getActivity(), new DeleteListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                FoundAdapter.remove(position);
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public  int getStateBar(){
        Rect frame = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    public static int dip2px(Context context,float dipValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int) (scale*dipValue+0.5f);
    }
}
