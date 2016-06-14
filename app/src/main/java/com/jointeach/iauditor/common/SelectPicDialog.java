package com.jointeach.iauditor.common;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jointeach.iauditor.R;

/**
 * 作者: ws
 * 日期: 2016/6/12.
 * 介绍：iauditor
 */
public class SelectPicDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private SelectPicListener selectPicListener;
    public SelectPicDialog(Context context) {
        this(context,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
    }

    public SelectPicDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
        setContentView(R.layout.dialog_select_pic);
        findViewById(R.id.tv_camera).setOnClickListener(this);
        findViewById(R.id.tv_gallery).setOnClickListener(this);
    }

    public SelectPicListener getSelectPicListener() {
        return selectPicListener;
    }

    public void setSelectPicListener(SelectPicListener selectPicListener) {
        this.selectPicListener = selectPicListener;
    }

    @Override
    public void onClick(View v) {
        if(selectPicListener==null){
            return;
        }
        switch (v.getId()){
            case R.id.tv_camera:
                selectPicListener.camera(v);
                break;
            case R.id.tv_gallery:
                selectPicListener.gallery(v);
                break;
        }
        dismiss();
    }

    public interface SelectPicListener{
        void camera(View v);
        void gallery(View v);
    }
}
