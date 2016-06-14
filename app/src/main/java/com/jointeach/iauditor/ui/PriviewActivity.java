package com.jointeach.iauditor.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.entity.QusBaseEntity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: ws
 * 日期: 2016/5/25.
 * 介绍：预览审计内容
 */
public class PriviewActivity extends AbstractBaseActivity {
    private int mId;
    @ViewInject(R.id.tv_priview)
    private TextView tv_priview;
    private MouldEntity entity;
    private ArrayList<QusBaseEntity> columnItems=new ArrayList<>();
    /**
     * 审计内容预览启动方法
     * @param mId 模版Id
     * */
    public static void startAction(Context ctx,int mId){
        Intent intent=new Intent(ctx,PriviewActivity.class);
        intent.putExtra("mId",mId);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_priview);
        Intent intent=getIntent();
        mId=intent.getIntExtra("mId",-1);
        getData();
        initData();
        initView();
    }

    private void initView() {
        tv_priview.setText("\n                                          "+entity.getTitle()+"\n\n\n");
        tv_priview.append("创建日期\n");
        tv_priview.append("\t"+Tools.getFormatTime(entity.getCreateTime(),"yyyy/MM/dd HH:mm")+"\n\n");
        tv_priview.append("作者\n");
        tv_priview.append("\t"+entity.getAuthor()+"\n\n");
        tv_priview.append("地点\n");
        tv_priview.append("\t"+entity.getLocation()+"\n\n");
        tv_priview.append("参与人员\n\t--\n\n\n");
        for (QusBaseEntity en:columnItems) {
            if(en instanceof AuditItemEntity){
                AuditItemEntity aE= (AuditItemEntity) en;
                tv_priview.append("\t"+aE.getTitle()+"\n");
                switch (aE.getState()){//0是未操作，1是是，2是否，3是不适用
                    case 0:
                        tv_priview.append("\t未操作\n");
                        break;
                    case 1:
                        tv_priview.append("\t是\n");
                        break;
                    case 2:
                        tv_priview.append("\t否\n");
                        break;
                    case 3:
                        tv_priview.append("\t不适用\n");
                        break;
                }
                tv_priview.append("\n");
            }else {
                tv_priview.append("\n"+en.getTitle()+"\n\n");
            }
        }
    }

    private void getData(){
        try {
            entity= AppDao.db.findFirst(Selector.from(MouldEntity.class).where("id","=",mId));
        } catch (DbException e) {
            e.printStackTrace();
            Tools.showToast(self,"数据出错");
            finish();
        }
        if(entity==null){
            Tools.showToast(self,"数据出错");
           finish();
        }
    }
    //加载数据
    private void initData() {
        List<AuditGroupEntity> goups= AppDao.getGroups(mId);
        for (AuditGroupEntity gE:goups) {
            columnItems.add(gE);
            List<AuditItemEntity> ies=AppDao.getQus(gE.getId(),gE.getMouldId());
            for (AuditItemEntity ie:ies) {
                columnItems.add(ie);
            }
        }
    }
}
