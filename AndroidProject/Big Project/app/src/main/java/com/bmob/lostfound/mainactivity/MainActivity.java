package com.bmob.lostfound.mainactivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bmob.lostfound.BaseActivity;
import com.bmob.lostfound.fragment.IRefresh;
import com.bmob.lostfound.fragment.PersonalFragment;
import com.bmob.lostfound.fragment.QAFragment;
import com.bmob.lostfound.R;
import com.bmob.lostfound.fragment.LostFoundFragment;
import com.bmob.lostfound.fragment.ShareFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener{

    BottomNavigationBar bottom_navigation_bar;

    private FragmentManager fm;
    private FragmentTransaction transaction;

    private List<Fragment> fragments;

    private int currentPosition ;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initViews() {
        bottom_navigation_bar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
        bottom_navigation_bar.setMode(BottomNavigationBar.MODE_FIXED);
        bottom_navigation_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottom_navigation_bar
                .setActiveColor(R.color.blue)
                .setInActiveColor(R.color.tab_normal_color)
                .setBarBackgroundColor(R.color.white);
        bottom_navigation_bar
                .addItem(new BottomNavigationItem(R.drawable.icon_lostfound,"Lost & Found"))
                .addItem(new BottomNavigationItem(R.drawable.icon_qa,"Q & A"))
                .addItem(new BottomNavigationItem(R.drawable.icon_share, "Share"))
                .addItem(new BottomNavigationItem(R.drawable.icon_account, "Personal"))
                .initialise();
        fragments = getFragments();
        setDefultFragment();
        bottom_navigation_bar.setTabSelectedListener(this);
    }

    private void setDefultFragment() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.add(R.id.page_fragment, fragments.get(0));
        currentPosition = 0;
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(LostFoundFragment.newInstance("Lost & Found"));
        fragments.add(QAFragment.newInstance("Q & A"));
        fragments.add(ShareFragment.newInstance("Share"));
        fragments.add(PersonalFragment.newInstance("Personal"));

        return fragments;
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = fragments.get(currentPosition);
        f.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onTabSelected(int i) {
        System.out.println("3333333");
        hideFragment();
        if (fragments != null) {
            if (i < fragments.size()) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(i);
                if (fragment.isAdded()) {
                    //ft.replace(R.id.page_fragment, fragment);
                    ft.show(fragment);
                } else {
                    System.out.println("11111111111222222");

                    ft.add(R.id.page_fragment, fragment);
//                    ft.show(fragment);
                }
                currentPosition = i;
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int i) {
//        if (fragments != null) {
//            if (i < fragments.size()) {
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                Fragment fragment = fragments.get(i);
//                ft.remove(fragment);
//                ft.commitAllowingStateLoss();
//            }
//        }

    }

    @Override
    public void onTabReselected(int i) {
        System.out.println(i);
        Fragment fragment = fragments.get(i);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment.isAdded()) {
            ft.remove(fragment);
        } else {
            ft.replace(R.id.page_fragment, fragment);
        }
        ft.commit();


    }


    private void hideFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment.isAdded()) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }
}
