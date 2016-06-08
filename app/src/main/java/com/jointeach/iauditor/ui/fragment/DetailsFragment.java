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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.base.AbstractBaseFragment;
import org.mylibrary.utils.Tools;

import java.util.ArrayList;

/**
 * 作者: ws
 * 日期: 2016/5/26.
 * 介绍：详细说明
 */
public class DetailsFragment extends AbstractBaseFragment {

    private int mId;
    public static DetailsFragment newInstance(int mId){
        DetailsFragment fragment=new DetailsFragment();
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
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_details,null);
        ViewUtils.inject(this, rootView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
