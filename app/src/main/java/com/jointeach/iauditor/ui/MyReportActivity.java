package com.jointeach.iauditor.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.adapter.MouldAdapter;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.MouldEntity;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的报告
 * */
public class MyReportActivity extends AbstractBaseActivity {
    //0是发送的，1是接收的
    private int type;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    private ArrayList<MouldEntity> moulds;
    @ViewInject(R.id.recycler)
    private RecyclerView recycler;
    /**
     * 我的发送或者接收的报告列表
     * @param type 0是发送，1是接收
     * */
    public static void startAction(Context ctx,int type){
        Intent intent=new Intent(ctx,MyReportActivity.class);
        intent.putExtra("type",type);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_my_report);
        Intent intent=getIntent();
        type=intent.getIntExtra("type",-1);
        if(type!=0 && type!=1){
            Tools.showToast(self,"参数出错");
            finish();
        }
        toolbar.setTitleTextColor(Color.WHITE);
        if(type==0) {
            toolbar.setTitle(getResources().getString(R.string.report_sent));
        }else{
            toolbar.setTitle(getResources().getString(R.string.report_receive));
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
        initView();
    }

    private void initView() {
        recycler.setLayoutManager(new LinearLayoutManager(self));
        recycler.setAdapter(new MouldAdapter(moulds,1,self));
    }

    private void initData() {
        moulds=new ArrayList<>();
        List<MouldEntity> items= AppDao.getMoulds(true);
        if(items==null || items.size()<1){
            Tools.showToast(self,"没有数据");
        }else{
            moulds.addAll(items);
        }
    }
}
