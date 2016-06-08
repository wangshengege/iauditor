package com.jointeach.iauditor.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.adapter.AuditDetailAdapter;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.base.AbstractBaseFragment;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者: ws
 * 日期: 2016/6/7.
 * 介绍：审计详细
 */
public class AuditDetailFragment extends AbstractBaseFragment {
    @ViewInject(R.id.elv)
    private ExpandableListView plv;
    private AuditDetailAdapter mApapter;
    private ArrayList<AuditGroupEntity> groups;//群组数据
    private HashMap<Integer, ArrayList<AuditItemEntity>> items;//数据源

    public static AuditDetailFragment newInstance(int mId) {
        AuditDetailFragment fragment = new AuditDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_audit_detail, container, false);
        ViewUtils.inject(this, rootView);
        initData();
        initView();
        return rootView;
    }

    private void initView() {

        mApapter=new AuditDetailAdapter(groups,items,plv);
        plv.setAdapter(mApapter);
    }

    private void initData() {
        groups = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AuditGroupEntity en = new AuditGroupEntity();
            en.setId(i);
            en.setTitle("标题" + i);
            groups.add(en);
        }
        items = new HashMap<>();
        for (AuditGroupEntity en : groups) {
            if (!items.containsKey(en.getId())) {
                ArrayList<AuditItemEntity> item = new ArrayList<>();
                items.put(en.getId(), item);
            }
            for (int i = 0; i < en.getId(); i++) {
                AuditItemEntity iEn=new AuditItemEntity();
                iEn.setTitle(en.getId()+"组，"+"标题1"+i);
                items.get(en.getId()).add(iEn);
            }
        }

    }

}
