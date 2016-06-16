package com.jointeach.iauditor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.jointeach.iauditor.ui.base.BaseAuditFragment;
import com.jointeach.iauditor.ui.fragment.ColumnFragment;
import com.jointeach.iauditor.ui.fragment.CoverFragment;
import com.jointeach.iauditor.ui.fragment.DetailsFragment;

import org.mylibrary.base.AbstractBaseFragment;

/**
 * 作者: ws
 * 日期: 2016/5/26.
 * 介绍：创建和编辑模版适配器
 */
public class CreateMouldAdapter extends FragmentPagerAdapter {
    private static final String[] TITLE = new String[] { "字段","封面", "详细说明"};
    public BaseAuditFragment[] fragments=new BaseAuditFragment[TITLE.length];
    private int mid;
    public CreateMouldAdapter(android.support.v4.app.FragmentManager fm,int mid) {
        super(fm);
        this.mid=mid;
    }
    @Override
    public Fragment getItem(int position) {
        BaseAuditFragment fragment=fragments[position];
        if(fragment==null){
            switch (position){
                case 0:
                    fragment=ColumnFragment.newInstance(mid);
                    break;
                case 1:
                    fragment=CoverFragment.newInstance(mid);
                    break;
                case 2:
                    fragment=DetailsFragment.newInstance(mid);
                    break;
            }
            fragments[position]=fragment;
        }
        return fragment;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position % TITLE.length];
    }
    @Override
    public int getCount() {
        return TITLE.length;
    }
}
