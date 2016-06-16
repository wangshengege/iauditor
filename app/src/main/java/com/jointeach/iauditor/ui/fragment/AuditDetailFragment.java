package com.jointeach.iauditor.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.adapter.AuditDetailAdapter;
import com.jointeach.iauditor.common.JKApplication;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.ui.base.BaseAuditFragment;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.utils.LogTools;
import org.mylibrary.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者: ws
 * 日期: 2016/6/7.
 * 介绍：审计详细
 */
public class AuditDetailFragment extends BaseAuditFragment {
    @ViewInject(R.id.elv)
    private ExpandableListView plv;
    private AuditDetailAdapter mApapter;
    private ArrayList<AuditGroupEntity> groups;//群组数据
    private HashMap<Integer, ArrayList<AuditItemEntity>> items;//数据源
    private int mId;
    private TextView tv_point;
    private Button btn_mark;
    private MouldEntity entity;
    public static AuditDetailFragment newInstance(int mId) {
        AuditDetailFragment fragment = new AuditDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mId", mId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mId = bundle.getInt("mId");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int index= (int) msg.obj;
                if (msg.what == 1 && index>=groups.size()-1) {
                    mApapter = new AuditDetailAdapter((Activity) self,groups, items, plv);
                    plv.setAdapter(mApapter);
                }
            }
        };
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    initData();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_audit_detail, container, false);
        ViewUtils.inject(this, rootView);
        View foot=inflater.inflate(R.layout.audit_foot,null);
        initView(foot);
        plv.addFooterView(foot);
        return rootView;
    }
    private void getData() {
        try {
            entity=AppDao.db.findFirst(Selector.from(MouldEntity.class).where("id","=",String.valueOf(mId))
                    .and("type","=","1"));
        } catch (DbException e) {
            e.printStackTrace();
            Tools.showToast(self,"数据出错");
            ((AbstractBaseActivity)self).finish();
        }
        if(entity==null){
            Tools.showToast(self,"数据出错");
            ((AbstractBaseActivity)self).finish();
        }
    }
    private void initView(final View foot){
        tv_point= (TextView) foot.findViewById(R.id.tv_point);
        btn_mark= (Button) foot.findViewById(R.id.btn_mark);
        getData();
        if(entity.getState()==2){
            btn_mark.setSelected(true);
            btn_mark.setText("已完成");
            tv_point.setVisibility(View.GONE);
        }
        btn_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(!v.isSelected());
                if(v.isSelected()){
                    v.setSelected(false);
                    tv_point.setVisibility(View.VISIBLE);
                    btn_mark.setText("标记已完成");
                }else {
                    v.setSelected(true);
                    tv_point.setVisibility(View.GONE);
                    btn_mark.setText("已完成");
                }
            }
        });
    }
    private void setState(boolean state){
        if(entity==null){
            return;
        }
        if(state){
            entity.setState(2);
        }else{
            entity.setState(1);
        }
        AppDao.saveMould(entity);
    }
    private void initData() throws DbException {
        groups = new ArrayList<>();
        items = new HashMap<>();
        List<AuditGroupEntity> gItms =AppDao.getGroups(mId);
        if (gItms != null) {
            groups.addAll(gItms);
        }
        for (int i = 0; i < groups.size(); i++) {
            AuditGroupEntity en=groups.get(i);
            AsyncData da=new AsyncData(items,en.getId(),en.getMouldId(),i);
            da.execute();
        }
    }
    class AsyncData extends AsyncTask<Integer,Integer,Object>{
        private HashMap<Integer, ArrayList<AuditItemEntity>> items;//数据源
        int gId;
        int mId;
        private int index;
        public AsyncData(HashMap<Integer, ArrayList<AuditItemEntity>> items,int gId,int mId,int index) {
            this.items = items;
            this.index=index;
            this.gId=gId;
            this.mId=mId;
        }

        @Override
        protected Object doInBackground(Integer... params) {
            return AppDao.getQus(gId,mId);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            List<AuditItemEntity> iItems= (List<AuditItemEntity>) o;
            if(!items.containsKey(gId)){
                ArrayList<AuditItemEntity> ens=new ArrayList<>();
                items.put(gId,ens);
            }
            if (iItems != null) {
                items.get(gId).addAll(iItems);
            }
            Message msg=new Message();
            msg.what=1;
            msg.obj=index;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mApapter.onActivityResult(requestCode,resultCode,data);
    }
}

