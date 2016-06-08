package com.jointeach.iauditor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jointeach.iauditor.ui.fragment.AuditDetailFragment;
import com.jointeach.iauditor.ui.fragment.AuditInfoFragment;

import org.mylibrary.base.AbstractBaseFragment;

/**
 * 作者: ws
 * 日期: 2016/6/7.
 * 介绍：审计页面的适配器
 */
public class EditAuditPagAdapter extends FragmentPagerAdapter {
    AbstractBaseFragment[] fragments=new AbstractBaseFragment[2];
    private int mId;
    public EditAuditPagAdapter(FragmentManager fm,int mId) {
        super(fm);
        this.mId=mId;
    }

    @Override
    public Fragment getItem(int position) {
        AbstractBaseFragment fragment=fragments[position];
        if(fragment==null){
            if(position==0){
                fragment=AuditInfoFragment.newInstance(mId);
            }else{
                fragment= AuditDetailFragment.newInstance(mId);
            }
            fragments[position]=fragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
