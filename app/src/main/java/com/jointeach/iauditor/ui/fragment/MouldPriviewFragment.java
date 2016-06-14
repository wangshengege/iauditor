package com.jointeach.iauditor.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.common.ImgLoadUtils;
import com.jointeach.iauditor.common.JKApplication;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.InfoEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.entity.UpdataBack;
import com.jointeach.iauditor.ui.CreateMouldActivity;
import com.jointeach.iauditor.ui.base.BaseMainFragment;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.base.AbstractBaseFragment;
import org.mylibrary.utils.LogTools;
import org.mylibrary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者: ws
 * 日期: 2016/5/30.
 * 介绍：模版预览
 */
public class MouldPriviewFragment extends BaseMainFragment {
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
    @ViewInject(R.id.iv_acator)
    private CircleImageView iv_acator;
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

        ImgLoadUtils.loadImageRes(entity.getIcPath(),iv_acator);
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
    @OnClick(R.id.btn_audit)
    private void toAudit(View v){
          mould2audit(entity);
        //AppDao.saveMould(mouldEntity);
       // EventBus.getDefault().post(new UpdataBack());
    }
    /**将模版转成审计*/
    private  void mould2audit(final MouldEntity mould){
        MouldEntity mouldEntity= (MouldEntity) mould.clone();
        mouldEntity.setState(1);
        mouldEntity.setType(1);
        try {
            AppDao.db.save(mouldEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Tools.showToast(self,"完成");
                EventBus.getDefault().post(new UpdataBack(true));
            }
        };
        new Thread(){
            @Override
            public void run() {
                super.run();
                List<MouldEntity> moulds=AppDao.getMoulds(true);//查找所有的审计
                if(moulds==null || moulds.size()<1) {
                    return;
                }
                MouldEntity mo = moulds.get(moulds.size() - 1);//新增加的数据

                List<AuditGroupEntity> groups=AppDao.getGroups(mould.getId());//查找当前群组
                for (AuditGroupEntity en:groups) {
                    AuditGroupEntity nen= (AuditGroupEntity) en.clone();
                    nen.setIsAudit(1);
                    nen.setTitle(nen.getTitle());
                    nen.setMouldId(mo.getId());
                    AppDao.saveGroup(nen);//保存群组数据

                    List<AuditGroupEntity> gs=AppDao.getGroups(mo.getId());//再次查到当前数据
                    AuditGroupEntity e=gs.get(gs.size()-1);
                    List<AuditItemEntity> items= AppDao.getQus(en.getId(),en.getMouldId());//获取这群租的问题
                    for (AuditItemEntity item:items) {
                        AuditItemEntity i= (AuditItemEntity) item.clone();
                        i.setgId(e.getId());
                        i.setmId(e.getMouldId());
                        AppDao.saveQus(i);
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }.start();
    }
}
