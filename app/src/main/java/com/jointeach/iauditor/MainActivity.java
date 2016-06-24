package com.jointeach.iauditor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.jointeach.iauditor.ui.MyReportActivity;
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
public class MainActivity extends AbstractBaseActivity implements View.OnClickListener{
    @ViewInject(R.id.indicator)
    private PagerSlidingTabStrip slidingTabStrip;
    @ViewInject(R.id.pager)
    private ViewPager pager;
    MainPagerAdapter mAdapter;
    //当前页面
    private int index;
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer_layout;
    @ViewInject(R.id.nav_view)
    private NavigationView nav_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("首页");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_white_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });
        initView();
        initNav();
        EventBus.getDefault().register(this,"upData",UpdataBack.class);
    }
    //初始化导航栏
    private void initNav() {
        nav_view.findViewById(R.id.iv_acator).setOnClickListener(this);
        nav_view.findViewById(R.id.tv_sent).setOnClickListener(this);
        nav_view.findViewById(R.id.tv_receive).setOnClickListener(this);
        TextView nike= (TextView) nav_view.findViewById(R.id.tv_nike);
        TextView account= (TextView) nav_view.findViewById(R.id.tv_account);
        if(CommonFunction.getBoolean(AppConfig.ISFIRST,true)){
            nike.setText("账户1");
            account.setText("123456");
        }else{
            nike.setText("账户2");
            account.setText("654321");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_acator:
                break;
            case R.id.tv_sent:
                MyReportActivity.startAction(self,0);
                break;
            case R.id.tv_receive:
                MyReportActivity.startAction(self,1);
                break;
        }
        drawer_layout.closeDrawer(GravityCompat.START);
    }

    public void upData(UpdataBack back){
            mAdapter.updata(back.isAudit);
        if(back.isAudit){
            pager.setCurrentItem(1);
        }
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
            if(drawer_layout.isDrawerOpen(GravityCompat.START)){
                drawer_layout.closeDrawer(GravityCompat.START);
                return true;
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
