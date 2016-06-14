package com.jointeach.iauditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.adapter.ColumnAdapter;
import com.jointeach.iauditor.adapter.MouldAdapter;
import com.jointeach.iauditor.common.JKApplication;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.ColumnEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.entity.QusBaseEntity;
import com.jointeach.iauditor.ui.base.BaseAuditFragment;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.base.AbstractBaseFragment;
import org.mylibrary.biz.DbBaseEntity;
import org.mylibrary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: ws
 * 日期: 2016/5/26.
 * 介绍：字段
 */
public class ColumnFragment extends BaseAuditFragment {
    @ViewInject(R.id.recycler)
    private RecyclerView recycler;
    private ColumnAdapter columnAdapter;
    private ArrayList<QusBaseEntity> columnItems=new ArrayList<>();
    private int mId;
    public static ColumnFragment newInstance(int mId){
        ColumnFragment fragment=new ColumnFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("mId",mId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        mId= bundle.getInt("mId");
        if(mId>=0){//是编辑模版
            initData(mId);
        }
    }
    //加载数据
    private void initData(int mId) {
        List<AuditGroupEntity> goups= AppDao.getGroups(mId);
        for (AuditGroupEntity gE:goups) {
            columnItems.add(gE);
            List<AuditItemEntity> ies=AppDao.getQus(gE.getId(),gE.getMouldId());
            for (AuditItemEntity ie:ies) {
                columnItems.add(ie);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_mould,null);
        ViewUtils.inject(this, rootView);
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    private void initView(){
        recycler.setLayoutManager(new LinearLayoutManager(self));
        columnAdapter= new ColumnAdapter(columnItems);
        recycler.setAdapter(columnAdapter);
    }
}
