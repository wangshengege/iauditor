package com.jointeach.iauditor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.jointeach.iauditor.adapter.MainPagerAdapter;
import com.jointeach.iauditor.common.AppConfig;
import com.jointeach.iauditor.common.JKApplication;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.ColumnEntity;
import com.jointeach.iauditor.entity.CoverEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.entity.UpdataBack;
import com.jointeach.iauditor.ui.CreateMouldActivity;
import com.jointeach.iauditor.ui.SelectMouldActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.common.CommonFunction;
import org.mylibrary.utils.Tools;
import org.mylibrary.view.PagerSlidingTabStrip;

import de.greenrobot.event.EventBus;

/**
 * 作者: ws
 * 日期: 2016/5/25.
 * 介绍：主页面
 */
public class MainActivity extends AbstractBaseActivity{
    @ViewInject(R.id.indicator)
    private PagerSlidingTabStrip slidingTabStrip;
    @ViewInject(R.id.pager)
    private ViewPager pager;
    MainPagerAdapter mAdapter;
    //当前页面
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        EventBus.getDefault().register(this,"upData",UpdataBack.class);
    }
    public void upData(UpdataBack back){
            mAdapter.updata(back.isAudit);
    }

    @OnClick(R.id.fab)
    private void addItem(View v){
        if(index==0){//添加模版
            CreateMouldActivity.startAction(self);
        }else{//添加审计
            SelectMouldActivity.actionStart(self);
        }
    }
    //初始化界面
    private void initView(){
        pager.setCurrentItem(2);
        mAdapter=new MainPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);
        slidingTabStrip.setViewPager(pager);
        slidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private long time;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            if(Tools.getTimeStamp()-time<1000){
               finish();
            }else{
                Tools.showToast(self,"再按一次退出");
                time=Tools.getTimeStamp();
            }
            return true;
        }else
        return super.onKeyDown(keyCode, event);
    }
}
