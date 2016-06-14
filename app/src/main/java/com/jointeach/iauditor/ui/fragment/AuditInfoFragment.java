package com.jointeach.iauditor.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.common.JKApplication;
import com.jointeach.iauditor.entity.InfoEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.ui.base.BaseAuditFragment;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.utils.Tools;

import java.util.ArrayList;

/**
 * 作者: ws
 * 日期: 2016/6/7.
 * 介绍：审计信息
 */
public class AuditInfoFragment extends BaseAuditFragment {
    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    private MouldEntity entity;
    private int mId;
    private EditText[] eds;
    DbUtils db;
    public static AuditInfoFragment newInstance(int mId) {
        AuditInfoFragment fragment = new AuditInfoFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("mId",mId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        mId=bundle.getInt("mId");
        db = DbUtils.create(JKApplication.getContext());
        initData();
    }

    private void initData() {
        try {
            entity=db.findFirst(Selector.from(MouldEntity.class).where("id","=",String.valueOf(mId))
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_audit_info, container, false);
        ViewUtils.inject(this,rootView);
        initView();
        return rootView;
    }

    private void initView() {
        tvTitle.setText(entity.getTitle());
        LayoutInflater inflater=LayoutInflater.from(self);
        ArrayList<InfoEntity> infos=new ArrayList<>();
        infos.add(new InfoEntity("标题",entity.getTitle()));
        infos.add(new InfoEntity("作者",entity.getAuthor()));
        infos.add(new InfoEntity("位置",entity.getLocation()));
        if(eds==null){
            eds=new EditText[infos.size()];
        }
        for (int i = 0; i < infos.size(); i++) {
            View v=inflater.inflate(R.layout.item_audit_info,null);
            InfoEntity in=infos.get(i);
            TextView tv= (TextView) v.findViewById(R.id.tv_title);
            EditText et= (EditText) v.findViewById(R.id.et_title);
            eds[i]=et;
            tv.setText(in.getTitle());
            et.setText(in.getSubTitle());
            ll_content.addView(v);
        }
    }

    @Override
    public void audit() {
        super.audit();
        saveData();
    }

    @Override
    public void quit() {
        super.quit();
        saveData();
    }
    private void saveData(){
        initData();
        entity.setLastRevise(Tools.getTimeStamp());
        entity.setTitle(eds[0].getText().toString());
        entity.setAuthor(eds[1].getText().toString());
        entity.setLocation(eds[2].getText().toString());
        try {
            db.saveOrUpdate(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
