package com.jointeach.iauditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.adapter.MouldAdapter;
import com.jointeach.iauditor.entity.MouldEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.base.AbstractBaseFragment;
import org.mylibrary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: ws
 * 日期: 2016/5/25.
 * 介绍：审计界面
 */
public class AuditFragment extends AbstractBaseFragment {
    @ViewInject(R.id.recycler)
    private RecyclerView recycler;
    private MouldAdapter mouldAdapter;
    private ArrayList<MouldEntity> mouldItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        mouldItems=new ArrayList<>();
        DbUtils db = DbUtils.create(self);
        try {
            List<MouldEntity> items=db.findAll(Selector.from(MouldEntity.class).where("type","=","1"));
            mouldItems.addAll(items);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_audit,null);
        ViewUtils.inject(this, rootView);
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    private void initView(){
        recycler.setLayoutManager(new LinearLayoutManager(self));
        mouldAdapter= new MouldAdapter(mouldItems,1,self);
        recycler.setAdapter(mouldAdapter);
    }
}
