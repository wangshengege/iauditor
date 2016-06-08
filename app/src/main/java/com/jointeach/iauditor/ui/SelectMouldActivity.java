package com.jointeach.iauditor.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.adapter.MouldAdapter;
import com.jointeach.iauditor.entity.MouldEntity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.utils.Tools;

import java.util.ArrayList;

/**
 * 作者: ws
 * 日期: 2016/5/27.
 * 介绍：选择模版
 */
public class SelectMouldActivity extends AbstractBaseActivity{
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.recycler)
    private RecyclerView recycler;
    private MouldAdapter mouldAdapter;
    private ArrayList<MouldEntity> mouldItems;

    public static void actionStart(Context ctx){
        Tools.toActivity(ctx,SelectMouldActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_selectmould);
        initView();
    }
    //初始化界面
    private void initView() {
        toolbar.setTitle("选择一个模板");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(self));
        mouldItems=new ArrayList<>();
        MouldEntity en=new MouldEntity();
        en.setTitle("模版1");
        en.setDescribe("这是第一个测试的模版");
        en.setLastRevise(Tools.getTimeStamp());
        mouldItems.add(en);
        en=new MouldEntity();
        en.setTitle("模版2");
        en.setDescribe("这是第2个测试的模版");
        en.setLastRevise(Tools.getTimeStamp());
        mouldItems.add(en);
        en=new MouldEntity();
        en.setTitle("模版3");
        en.setDescribe("这是第3个测试的模版");
        en.setLastRevise(Tools.getTimeStamp());
        mouldItems.add(en);
        mouldAdapter= new MouldAdapter(mouldItems,self);
        recycler.setAdapter(mouldAdapter);
    }

    @OnClick(R.id.btn_create_mould)//创建模版
    private void createMould(View v){
        CreateMouldActivity.startAction(self);
    }
}
