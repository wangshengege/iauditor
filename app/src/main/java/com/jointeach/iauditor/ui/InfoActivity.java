package com.jointeach.iauditor.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.entity.UpdataBack;
import com.jointeach.iauditor.ui.base.BaseMainFragment;
import com.jointeach.iauditor.ui.fragment.AuditPriviewFragment;
import com.jointeach.iauditor.ui.fragment.MouldFragment;
import com.jointeach.iauditor.ui.fragment.MouldPriviewFragment;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.base.AbstractBaseFragment;
import org.mylibrary.utils.Tools;

import de.greenrobot.event.EventBus;

/**
 * 作者: ws
 * 日期: 2016/5/27.
 * 介绍：模板和审计的信息预览
 */
public class InfoActivity extends AbstractBaseActivity {
    private int mId;
    private BaseMainFragment fragment;
    /**
     * 模板和审计的信息预览的启动方法
     * @param type 0是模版 1是审计
     * @param itemId 附件信息
     * */
    public static void startAction(Context ctx,int type,int itemId,int mId){
        Intent intent=new Intent(ctx,InfoActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("itemId",itemId);
        intent.putExtra("mId",mId);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_info);
        Intent intent=getIntent();
        int type=intent.getIntExtra("type",0);
        int itemId=intent.getIntExtra("itemId",-1);
        mId=intent.getIntExtra("mId",-1);
        if(itemId==-1){
            Tools.showToast(self,getResources().getString(R.string.paramError));
            finish();
        }
        initToolbar(type);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,getFragment(type)).commit();
        EventBus.getDefault().register(this,"upData",UpdataBack.class);
    }
    public void upData(UpdataBack back){
        if(back.isAudit && fragment!=null){
        fragment.updata();
        }
    }
    //初始化toolbar
    private void initToolbar(int type) {
        Toolbar toolbar= (Toolbar) findViewById(R.id.fab);
        toolbar.setTitleTextColor(Color.WHITE);
        if(type==0){
            toolbar.setTitle(R.string.mould);
        }else{
            toolbar.setTitle(R.string.audit);
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(menuListener);
    }

    //获取相应的界面
    private BaseMainFragment getFragment(int type){
        if(type==0){
            fragment=MouldPriviewFragment.newInstance(mId);
        }else{
            fragment =AuditPriviewFragment.newInstance(mId);
        }
        return fragment;
    }
    private Toolbar.OnMenuItemClickListener menuListener=new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(item.getItemId()==R.id.action_output){
                Tools.showToast(self,item.getTitle().toString());
            }
            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
