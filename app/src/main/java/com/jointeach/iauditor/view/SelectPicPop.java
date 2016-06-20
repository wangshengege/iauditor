package com.jointeach.iauditor.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.jointeach.iauditor.R;

/**
 * 作者: ws
 * 日期: 2016/6/15.
 * 介绍：iauditor
 */
public class SelectPicPop  implements View.OnClickListener{
    private Context context;
    private PopupWindow pop;
    private SelectPicListener selectPicListener;
    public SelectPicPop(Context context) {
        this.context = context;
        View v= LayoutInflater.from(context).inflate(R.layout.dialog_select_pic,null);
       v.findViewById(R.id.tv_camera).setOnClickListener(this);
        v.findViewById(R.id.tv_gallery).setOnClickListener(this);
        pop=new PopupWindow(context);
        pop.setContentView(v);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.abc_popup_background_mtrl_mult));
        pop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
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
        pop.dismiss();
    }
    public PopupWindow getPop() {
        return pop;
    }
}
