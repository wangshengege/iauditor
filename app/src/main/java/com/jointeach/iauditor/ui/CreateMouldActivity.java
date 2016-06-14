package com.jointeach.iauditor.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.adapter.CreateMouldAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.utils.Tools;
import org.mylibrary.view.PagerSlidingTabStrip;

/**
 * 作者: ws
 * 日期: 2016/5/26.
 * 介绍：创建模版界面
 */
public class CreateMouldActivity  extends AbstractBaseActivity{
    @ViewInject(R.id.pager)
    private ViewPager pager;
    @ViewInject(R.id.indicator)
    private PagerSlidingTabStrip indicator;
    @ViewInject(R.id.toolbar)
    private  Toolbar toolbar;
    @ViewInject(R.id.fab)
    private FloatingActionButton fab;
    private CreateMouldAdapter mouldAdapter;
    //页面位置
    private int index;
    private int mId;
    //0是创建模版1是编辑模版
    private int type;
    public static void startAction(Context ctx,int mId){
        Intent intent=new Intent(ctx,CreateMouldActivity.class);
        intent.putExtra("type",1);
        intent.putExtra("mId",mId);
        ctx.startActivity(intent);
    }
    public static void startAction(Context ctx){
        Intent intent=new Intent(ctx,CreateMouldActivity.class);
        intent.putExtra("type",1);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_createmould);
        Intent intent=getIntent();
        type=intent.getIntExtra("type",0);
        if(type==0) {//创建模版
            toolbar.setTitle(getResources().getString(R.string.creat_mould));
        }else{//编辑模版
            toolbar.setTitle(getResources().getString(R.string.edit_mould));
            mId=intent.getIntExtra("mId",-1);
        }
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        pager.setCurrentItem(3);
        mouldAdapter=new CreateMouldAdapter(getSupportFragmentManager(),mId);
        pager.setAdapter(mouldAdapter);
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            ObjectAnimator inAn;//出现动画
            ObjectAnimator outAn;//退出动画
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                if(inAn!=null && inAn.isRunning()){
                    inAn.end();
                }
                if(outAn!=null && outAn.isRunning()){
                    outAn.end();
                }
                if(position!=2 ){
                    if(fab.getVisibility()==View.GONE) {
                        fab.setVisibility(View.VISIBLE);
                        if(inAn==null){
                            inAn=ObjectAnimator.ofFloat(fab, "translationY",fab.getHeight()+fab.getHeight()/2, 0);
                            inAn.setDuration(500);
                        }
                        inAn.start();
                    }

                }else {
                    if(fab.getVisibility()==View.VISIBLE){
                        if(outAn==null){
                            outAn= ObjectAnimator.ofFloat(fab, "translationY",0, fab.getHeight()+fab.getHeight()/2);
                            outAn.setDuration(500);
                            outAn.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    fab.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                        }
                    }
                    outAn .start();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.fab)
    private void addItem(View v){
        switch (index){
            case 0://字段
                break;
            case 1://封面
                break;
            case 2://详细说明
                break;
        }
    }
    private Toolbar.OnMenuItemClickListener onMenuItemClick=new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_more:
                    Tools.showToast(self,"更多");
                    break;
                case R.id.action_preview:
                    Tools.showToast(self,"预览");
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.preview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mouldAdapter!=null && mouldAdapter.getItem(2)!=null){
            mouldAdapter.getItem(2).onActivityResult(requestCode,resultCode,data);
        }
    }
}
