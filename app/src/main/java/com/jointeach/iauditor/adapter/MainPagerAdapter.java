package com.jointeach.iauditor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jointeach.iauditor.ui.base.BaseMainFragment;
import com.jointeach.iauditor.ui.fragment.AuditFragment;
import com.jointeach.iauditor.ui.fragment.MouldFragment;

import org.mylibrary.base.AbstractBaseFragment;
import org.mylibrary.utils.Tools;

/**
 * 作者: ws
 * 日期: 2016/5/25.
 * 介绍：
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    /** Tab标题 */
    private static final String[] TITLE = new String[] { "模版", "审计"};
    private BaseMainFragment[] fragments=new BaseMainFragment[TITLE.length];
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void updata(boolean isAudit){
        if(isAudit){
            fragments[1].updata();
        }else{
            fragments[0].updata();
        }
    }
    @Override
    public Fragment getItem(int position) {
        BaseMainFragment fragment=fragments[position];
        if (Tools.isEmpty(fragment)){
            if(position==0){
                fragment=new MouldFragment();
            }else {
                fragment=new AuditFragment();
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
