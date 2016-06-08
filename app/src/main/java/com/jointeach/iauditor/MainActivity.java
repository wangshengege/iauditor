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
import com.jointeach.iauditor.entity.ColumnEntity;
import com.jointeach.iauditor.entity.CoverEntity;
import com.jointeach.iauditor.entity.MouldEntity;
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
    //当前页面
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(!CommonFunction.getBoolean(AppConfig.ISADDMOULD,false)) {
            DbUtils db = DbUtils.create(this);
            initData(db,true);//模拟一个模版
            initData(db,false);//模拟一个审计
        }
        initView();
    }
    private void initData(DbUtils db,boolean isAudit) {
        MouldEntity en=new MouldEntity();
        en.setCreateTime(Tools.getTimeStamp());
        en.setDescribe("这是我的模版");
        en.setTitle("我的模版");
        en.setId(1);
        en.setAuthor("ws");
        en.setIndustry("建筑");
        en.setcIndustry("装修");
        en.setLastRevise(Tools.getTimeStamp());
        if(isAudit){
            en.setType(1);
        }
        ColumnEntity group=new ColumnEntity();
        group.setTitle("第一个群组");
        group.setmId(en.getId());
        group.setIsGroup(1);
        group.setType(1);
        group.setId(1);
        ColumnEntity cC=new ColumnEntity();
        cC.setmId(en.getId());
        cC.setTitle("第一选项");
        cC.setpId(group.getId());
        cC.setIsGroup(0);
        cC.setType(2);
        CoverEntity cE1=new CoverEntity();
        cE1.setTitle("标题");
        cE1.setmId(en.getId());
        CoverEntity cE2=new CoverEntity();
        cE2.setTitle("创建日期");
        cE2.setmId(en.getId());
        CoverEntity cE3=new CoverEntity();
        cE3.setTitle("作者");
        cE3.setmId(en.getId());
        CoverEntity cE4=new CoverEntity();
        cE4.setTitle("地点");
        cE4.setmId(en.getId());
        CoverEntity cE5=new CoverEntity();
        cE5.setTitle("参与人员");
        cE5.setmId(en.getId());

        try {
            db.save(en);
            db.save(group);
            db.save(cC);
            db.save(cE1);
            db.save(cE2);
            db.save(cE3);
            db.save(cE4);
            db.save(cE5);
            CommonFunction.putBoolean(AppConfig.ISADDMOULD,true);
        } catch (DbException e) {
            e.printStackTrace();
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
        pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
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
                JKApplication.getInstance().exit();
            }else{
                Tools.showToast(self,"再按一次退出");
                time=Tools.getTimeStamp();
            }
            return true;
        }else
        return super.onKeyDown(keyCode, event);
    }
}
