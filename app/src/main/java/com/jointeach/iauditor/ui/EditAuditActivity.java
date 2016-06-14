package com.jointeach.iauditor.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.adapter.EditAuditPagAdapter;
import com.jointeach.iauditor.entity.UpdataBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.EventBase;

import org.mylibrary.base.AbstractBaseActivity;

import de.greenrobot.event.EventBus;

/**
 * 作者: ws
 * 日期: 2016/6/7.
 * 介绍：编辑审计
 */
public class EditAuditActivity extends AbstractBaseActivity implements View.OnClickListener {
    @ViewInject(R.id.fab)
    private Toolbar fab;
    @ViewInject(R.id.ll_audit_title)//导航栏的上面的标题栏
    private LinearLayout ll_audit_title;
    @ViewInject(R.id.ll_title_content)//导航栏的根布局
    private LinearLayout ll_audit_content;
    @ViewInject(R.id.vp)
    private ViewPager vp;
    private EditAuditPagAdapter mAdapter;
    @ViewInject(R.id.iv_icon)//上下的倒三角
    private ImageView iv_icon;
    @ViewInject(R.id.tv_page)//标题栏标题
    private TextView tv_page;
    @ViewInject(R.id.tv_page_title)//标题栏副标题
    private TextView tv_page_title;
    private int mId;
    private Handler handler;
    public static void startAction(Context ctx,int mId){
        Intent intent=new Intent(ctx,EditAuditActivity.class);
        intent.putExtra("mId",mId);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_edit_audit);
        fab.setTitle(getResources().getString(R.string.audit));
        fab.setTitleTextColor(Color.WHITE);
        setSupportActionBar(fab);
        Intent intent=getIntent();
        mId=intent.getIntExtra("mId",-1);
        initView();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    if(mAdapter!=null){
                        if(mAdapter.fragments!=null){
                            for (int i = 0; i < mAdapter.fragments.length; i++) {
                                mAdapter.fragments[i].audit();
                            }
                        }
                    }
                    EventBus.getDefault().post(new UpdataBack(true) );
                    finish();
                }
            }
        };
    }


    //初始化界面
    private void initView() {
        fab.setNavigationIcon(R.drawable.icon_actionbar_done);
        fab.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            handler.sendEmptyMessage(1);
            }
        });
        ll_audit_title.setOnClickListener(this);
        mAdapter=new EditAuditPagAdapter(getSupportFragmentManager(),mId);
        vp.setAdapter(mAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    tv_page.setText("第1节 共2节");
                    tv_page_title.setText("信息");
                }else {
                    tv_page.setText("第2节 共2节");
                    tv_page_title.setText("审计");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.ll_info).setOnClickListener(this);
        findViewById(R.id.ll_audit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_audit_title:
                visView();
            break;
            case R.id.ll_info:
                visView();
                vp.setCurrentItem(0);
                break;
            case R.id.ll_audit:
                visView();
                vp.setCurrentItem(1);
                break;
        }
    }
    //显示还是隐藏导航栏
    private void visView(){
        if(ll_audit_content.getVisibility()== View.VISIBLE){
            ll_audit_content.setVisibility(View.GONE);
            iv_icon.setImageResource(R.drawable.ic_collapse_selector);
        }else{
            ll_audit_content.setVisibility(View.VISIBLE);
            iv_icon.setImageResource(R.drawable.ic_collapse_nor);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter!=null){
        if(mAdapter.fragments!=null){
            for (int i = 0; i < mAdapter.fragments.length; i++) {
                mAdapter.fragments[i].quit();
            }
        }
        }
    }
}
