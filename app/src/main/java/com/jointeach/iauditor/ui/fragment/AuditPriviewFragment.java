package com.jointeach.iauditor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.common.ImgLoadUtils;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.ColumnEntity;
import com.jointeach.iauditor.entity.InfoEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.ui.EditAuditActivity;
import com.jointeach.iauditor.ui.PriviewActivity;
import com.jointeach.iauditor.ui.base.BaseMainFragment;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者: ws
 * 日期: 2016/5/30.
 * 介绍：审计预览
 */
public class AuditPriviewFragment extends BaseMainFragment {
    @ViewInject(R.id.btn_audit)
    private Button btn_audit;
    @ViewInject(R.id.btn_priview)
    private Button btn_priview;
    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.tv_sub_title)
    private TextView tv_sub_title;
    private ArrayList<InfoEntity> infoItems;
    @ViewInject(R.id.iv_acator)
    private CircleImageView iv_acator;
    private int mId;
    private MouldEntity entity;
    public static AuditPriviewFragment newInstance(int mId) {
        AuditPriviewFragment fragment = new AuditPriviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mId", mId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mId = bundle.getInt("mId");
        getData();
    }
    private void getData(){
        try {
            entity= AppDao.db.findFirst(Selector.from(MouldEntity.class).where("id","=",mId));
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
        rootView = inflater.inflate(R.layout.fragment_info, null);
        ViewUtils.inject(this, rootView);
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void updata() {
        super.updata();
        getData();
        ll_content.removeAllViews();
        initView();
    }

    private void initView() {
        btn_audit.setText("发送报告");
        btn_priview.setVisibility(View.VISIBLE);
        btn_priview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PriviewActivity.startAction(self,mId);
            }
        });
        ImgLoadUtils.loadImageRes(entity.getIcPath(),iv_acator);
        tv_title.setText(entity.getTitle());
        tv_sub_title.setText(entity.getDescribe());
        infoItems = new ArrayList<>();
        infoItems.add(new InfoEntity("状态", entity.getState()==2?"完成":"未完成"));
        infoItems.add(new InfoEntity("得分", ""+entity.getScore()));
        infoItems.add(new InfoEntity("开始于", Tools.getCurrentDateStr(entity.getCreateTime(),"yyyy/MM/dd HH:mm")));
        infoItems.add(new InfoEntity("完成时间", "-"));
        infoItems.add(new InfoEntity("修改时间",  Tools.getCurrentDateStr(entity.getLastRevise(),"yyyy/MM/dd HH:mm")));
        infoItems.add(new InfoEntity("位置",entity.getLocation()));
        infoItems.add(new InfoEntity("最后编辑", "-"));
        infoItems.add(new InfoEntity("拥有者", entity.getAuthor()));
        LayoutInflater inflater = LayoutInflater.from(self);
        for (InfoEntity info : infoItems) {
            View v = inflater.inflate(R.layout.info_item, null);
            TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
            TextView tv_sub = (TextView) v.findViewById(R.id.tv_subtitle);
            tv_title.setText(info.getTitle());
            tv_sub.setText(info.getSubTitle());
            ll_content.addView(v);
        }
    }

    @OnClick(R.id.tv_edit)
    private void toAudit(View v) {
        EditAuditActivity.startAction(self,mId);
    }

}
