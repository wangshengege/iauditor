package com.jointeach.iauditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.adapter.CoverAdapter;
import com.jointeach.iauditor.adapter.MouldAdapter;
import com.jointeach.iauditor.entity.CoverEntity;
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
 * 日期: 2016/5/26.
 * 介绍：封面
 */
public class CoverFragment extends AbstractBaseFragment {
    @ViewInject(R.id.recycler)
    private RecyclerView recycler;
    private CoverAdapter coverAdapter;
    private ArrayList<CoverEntity> coverItems=new ArrayList<>();
    private int mId;
    public static CoverFragment newInstance(int mId){
        CoverFragment fragment=new CoverFragment();
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
            initData();
        }
    }
    //加载数据
    private void initData() {
        DbUtils db=DbUtils.create(self);
        try {
            List<CoverEntity> list=db.findAll(Selector.from(CoverEntity.class).where("mId","=",String.valueOf(mId)));
            if(list!=null) {
                coverItems.addAll(list);
            }
        } catch (DbException e) {
            e.printStackTrace();
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
        coverAdapter= new CoverAdapter(coverItems);
        recycler.setAdapter(coverAdapter);
    }
}
