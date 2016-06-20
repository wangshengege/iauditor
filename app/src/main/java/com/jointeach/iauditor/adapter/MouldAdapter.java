package com.jointeach.iauditor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.common.ImgLoadUtils;
import com.jointeach.iauditor.common.JKApplication;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.ui.InfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.utils.Tools;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者: ws
 * 日期: 2016/5/25.
 * 介绍：模型的适配器
 */
public class MouldAdapter  extends RecyclerView.Adapter<MouldAdapter.ViewHolder> implements View.OnClickListener{
    private ArrayList<MouldEntity> mouldItems;
    //0是模版1是审计
    private int type ;
    private Context ctx;
    public MouldAdapter(ArrayList<MouldEntity> mouldItems,Context ctx) {
        this(mouldItems,0,ctx);
    }
    /**
     * @param mouldItems 数据源
     * @param type 0是模版1是审计
     * */
    public MouldAdapter(ArrayList<MouldEntity> mouldItems,int type,Context ctx) {
        this.mouldItems = mouldItems;
        this.type=type;
        this.ctx=ctx;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.mould_item,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MouldEntity entity=mouldItems.get(position);

        holder.tv_describe.setText(Tools.isEmpty(entity.getDescribe())?"暂无描述":entity.getDescribe());
        holder.tv_revise.setText(Tools.getFormatTime(entity.getLastRevise(), "yyyy/MM/dd"));
        holder.tv_title.setText(entity.getTitle());
        ImgLoadUtils.loadImageRes(entity.getIcPath(),holder.iv_icon);
        if(type==0){//模版不显示状态
            holder.tv_state.setVisibility(View.GONE);
        }else{//审计显示状态
            holder.tv_state.setVisibility(View.VISIBLE);
            if(entity.getState()==2){//完成
                holder.tv_state.setText("已完成");
                holder.tv_state.setTextColor(Color.parseColor("#90C634"));
            }else{
                holder.tv_state.setText("未完成");
                holder.tv_state.setTextColor(Color.RED);
            }
        }
        holder.item.setTag(position);
        holder.item.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mouldItems==null?0:mouldItems.size();
    }

    @Override
    public void onClick(View v) {
    int position= (Integer) v.getTag();
        MouldEntity entity=mouldItems.get(position);
        if(type==0){
            InfoActivity.startAction(ctx,0,0,entity.getId());
        }else{
            InfoActivity.startAction(ctx,1,0,entity.getId());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @ViewInject(R.id.tv_title)
        TextView tv_title;
        @ViewInject(R.id.tv_describe)
        TextView tv_describe;
        @ViewInject(R.id.tv_revise)
        TextView tv_revise;
        @ViewInject(R.id.tv_state)
        TextView tv_state;
        View item;
        @ViewInject(R.id.iv_icon)
        CircleImageView iv_icon;
        public ViewHolder(View itemView) {
            super(itemView);
            item=itemView;
            ViewUtils.inject(this,itemView);
        }
    }
}
