package com.jointeach.iauditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.entity.InfoEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.ui.CreateMouldActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.base.AbstractBaseFragment;
import org.mylibrary.utils.Tools;

import java.util.ArrayList;

/**
 * 作者: ws
 * 日期: 2016/5/30.
 * 介绍：模版预览
 */
public class MouldPriviewFragment extends AbstractBaseFragment {
    @ViewInject(R.id.btn_audit)
    private Button btn_audit;
    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;
    private ArrayList<InfoEntity> infoItems;
    //模版id
    private int mId;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.tv_sub_title)
    private TextView tv_subtitle;
    private MouldEntity entity;
    /**
     * @param mId 模版id
     * */
    public static MouldPriviewFragment newInstance(int mId){
        MouldPriviewFragment fragment=new MouldPriviewFragment();
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
        DbUtils db = DbUtils.create(self);
        try {
            entity=db.findFirst(Selector.from(MouldEntity.class).where("id","=",String.valueOf(mId)));
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_info,null);
        ViewUtils.inject(this,rootView);
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @OnClick(R.id.tv_edit)//编辑
    private void toEdit(View v){
        CreateMouldActivity.startAction(self,mId);
    }
    private void initView() {
        btn_audit.setText("开始审核");
        tv_title.setText(entity.getTitle());
        tv_subtitle.setText(entity.getDescribe());


        infoItems=new ArrayList<>();
        infoItems.add(new InfoEntity("行业",entity.getIndustry()));
        infoItems.add(new InfoEntity("子行业",entity.getcIndustry()));
        infoItems.add(new InfoEntity("时间",Tools.getCurrentDateStr(entity.getCreateTime(),"yyyy/MM/dd")));
        infoItems.add(new InfoEntity("拥有者",entity.getAuthor()));
        LayoutInflater inflater=LayoutInflater.from(self);
        for (InfoEntity info:infoItems) {
            View v=inflater.inflate(R.layout.info_item,null);
            TextView tv_title= (TextView) v.findViewById(R.id.tv_title);
            TextView tv_sub=(TextView)v.findViewById(R.id.tv_subtitle);
            tv_title.setText(info.getTitle());
            tv_sub.setText(info.getSubTitle());
            ll_content.addView(v);
        }
    }
}
