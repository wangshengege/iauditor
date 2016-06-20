package com.jointeach.iauditor.view;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.common.JKApplication;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 作者: ws
 * 日期: 2016/6/8.
 * 介绍：封面和字段条目
 */
public class ColumnViewHolder extends RecyclerView.ViewHolder {
    public ColumnStyle style=ColumnStyle.WHITE;
    @ViewInject(R.id.tv_title)
    public TextView tv_title;
    @ViewInject(R.id.iv_type)
    public ImageView iv_type;
    @ViewInject(R.id.iv_delete)
    public ImageView iv_delete;
    public View view;
    @ViewInject(R.id.ll_content)
    public View ll_content;
    @ViewInject(R.id.et_info)
    public EditText et_info;
    @ViewInject(R.id.view_dot)
    public View dot;
    public ColumnViewHolder(View itemView) {
        super(itemView);
        view=itemView;
        ViewUtils.inject(this, itemView);
    }
    public void setColumnStyle(ColumnStyle style){
        this.style=style;
    if(style==ColumnStyle.BLUE){
        ll_content.setBackgroundColor(JKApplication.getContext().getResources().getColor(R.color.colorPrimary));
        et_info.setVisibility(View.VISIBLE);
        iv_delete.setVisibility(View.VISIBLE);
        iv_delete.setImageResource(R.drawable.icon_selected);
    }else{
        ll_content.setBackgroundColor(Color.WHITE);
        et_info.setVisibility(View.GONE);
        iv_delete.setImageResource(R.drawable.icon_actionbar_no_fill);
    }
    }
    public enum ColumnStyle{
        BLUE,WHITE;
    }
}
