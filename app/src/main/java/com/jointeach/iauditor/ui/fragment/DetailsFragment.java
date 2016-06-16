package com.jointeach.iauditor.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.common.ImgLoadUtils;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.entity.UpdataBack;
import com.jointeach.iauditor.ui.base.BaseAuditFragment;
import com.jointeach.iauditor.view.SelectPicListener;
import com.jointeach.iauditor.view.SelectPicPop;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.common.FileAccessor;
import org.mylibrary.utils.LogTools;
import org.mylibrary.utils.Tools;
import org.mylibrary.utils.Utils;

import java.io.File;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者: ws
 * 日期: 2016/5/26.
 * 介绍：详细说明
 */
public class DetailsFragment extends BaseAuditFragment implements View.OnClickListener {
    @ViewInject(R.id.et_title)
    private EditText et_title;
    @ViewInject(R.id.et_subtitle)
    private EditText et_subtitle;
    @ViewInject(R.id.tv_point)
    private TextView tv_point;
    @ViewInject(R.id.iv_acator)
    private CircleImageView iv;
    private MouldEntity mould;
    private int mId;
    private File iconPath;

    public static DetailsFragment newInstance(int mId) {
        DetailsFragment fragment = new DetailsFragment();
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
        if (mId >= 0) {//是编辑模版
            initData();
        }
    }

    //加载数据
    private void initData() {
        mould = AppDao.getMould(mId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_details, null);
        ViewUtils.inject(this, rootView);
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView() {
        if (mould != null) {
            et_title.setText(mould.getTitle());
            et_subtitle.setText(mould.getDescribe());
            ImgLoadUtils.loadImageRes(mould.getIcPath(), iv);
        }
        iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SelectPicPop pop=new SelectPicPop(self);
        pop.setSelectPicListener(new SelectPicListener() {
            @Override
            public void camera(View v) {
                iconPath = new File(FileAccessor.getImagePathName(), Tools.getTimeStamp() + ".jpg");
                Utils.photo((Activity) self, iconPath, 100);
            }

            @Override
            public void gallery(View v) {
                Utils.album((Activity) self, 200);
            }
        });
        pop.getPop().showAsDropDown(v);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mould != null) {
            mould.setTitle(et_title.getText().toString());
            mould.setDescribe(et_subtitle.getText().toString());
            AppDao.saveMould(mould);
            EventBus.getDefault().post(new UpdataBack());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){//取消操作
            return;
        }
        String iconP = mould.getIcPath();
        if (requestCode == 100) {//拍照
            iconP = iconPath.getAbsolutePath();
        } else if (requestCode == 200) {//相册
            iconP = Utils.resolvePhotoFromIntent(self, data, FileAccessor.getImagePathName().getAbsolutePath());
        }
        LogTools.logi(this, iconP);
        if (!Tools.isEmpty(iconP)) {
            mould.setIcPath(iconP);
            ImgLoadUtils.loadImageRes(iconP, iv);
        }
    }
}
