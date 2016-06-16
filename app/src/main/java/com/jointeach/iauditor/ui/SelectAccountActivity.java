package com.jointeach.iauditor.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.adapter.AccountAdapter;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.entity.SelectAccontBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.mylibrary.base.AbstractBaseActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import de.greenrobot.event.EventBus;

public class SelectAccountActivity extends AbstractBaseActivity {
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    private ArrayList<String> accs;
    @ViewInject(R.id.recycler)
    private RecyclerView recycler;
    private AccountAdapter mAdapter;
    private int type;
    public static void startAction(Context ctx,int type){
        Intent intent=new Intent(ctx,SelectAccountActivity.class);
        intent.putExtra("type",type);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_select_acc);
        type=getIntent().getIntExtra("type",0);
        toolbar.setTitleTextColor(Color.WHITE);
       toolbar.setTitle("选择联系人");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        accs=new ArrayList<>();
        accs.add("用户1");
        accs.add("用户2");
        recycler.setLayoutManager(new LinearLayoutManager(self));
        mAdapter=new AccountAdapter(accs);
        recycler.setAdapter(mAdapter);
    }
    @OnClick(R.id.btn_sure)
    private void submit(View v){
        if(type==1){
            EventBus.getDefault().post(new SelectAccontBack(true));
            finish();
        }else{
        HashSet<Integer> set=mAdapter.getCheckedIndex();
        Iterator<Integer> iterator=set.iterator();
        StringBuilder sb=new StringBuilder();
        while(iterator.hasNext()){
            sb.append(accs.get(iterator.next()));
            sb.append(",");
        }
        String str=sb.toString().substring(0,sb.toString().length()-1);
        EventBus.getDefault().post(new SelectAccontBack(str));
        finish();}
    }
}
