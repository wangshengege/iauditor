package com.jointeach.iauditor.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.CoverEntity;
import com.jointeach.iauditor.view.ColumnViewHolder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 作者: ws
 * 日期: 2016/5/27.
 * 介绍：封面的适配器
 */
public class CoverAdapter extends RecyclerView.Adapter<ColumnViewHolder> {
    private ArrayList<CoverEntity> items;
    private HashSet<Integer> set=new HashSet<>();
    public CoverAdapter(ArrayList<CoverEntity> items) {
        this.items = items;
    }

    @Override
    public ColumnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColumnViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.column_item,null));
    }

    @Override
    public void onBindViewHolder(final ColumnViewHolder holder, final int position) {
        final CoverEntity entity=items.get(position);
        holder.iv_type.setImageResource(R.drawable.icon_editor_textsingle);
        holder.tv_title.setText(entity.getTitle());
        holder.iv_delete.setVisibility(View.GONE);
        holder.et_info.setText(entity.getTitle());
        if(set.contains(position)){
            holder.setColumnStyle(ColumnViewHolder.ColumnStyle.BLUE);
            holder.iv_delete.setVisibility(View.VISIBLE);
        }else {
            holder.setColumnStyle(ColumnViewHolder.ColumnStyle.WHITE);
            holder.iv_delete.setVisibility(View.GONE);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.style== ColumnViewHolder.ColumnStyle.WHITE){
                    holder.setColumnStyle(ColumnViewHolder.ColumnStyle.BLUE);
                    set.add(position);
                }else{
                    set.remove(position);
                    holder.setColumnStyle(ColumnViewHolder.ColumnStyle.WHITE);
                    holder.iv_delete.setVisibility(View.GONE);
                }
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                entity.setTitle(holder.et_info.getText().toString());
                try {
                    AppDao.db.saveOrUpdate(entity);
                    notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
