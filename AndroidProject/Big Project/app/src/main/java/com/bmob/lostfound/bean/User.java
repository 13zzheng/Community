package com.bmob.lostfound.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/9/11.
 */
public class User extends BmobUser{
    private String name;
    private BmobRelation agrees;

    public BmobRelation getAgrees() {
        return agrees;
    }

    public void setAgrees(BmobRelation agrees) {
        this.agrees = agrees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
