package com.jointeach.iauditor.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.ColumnEntity;
import com.jointeach.iauditor.entity.QusBaseEntity;
import com.jointeach.iauditor.view.ColumnViewHolder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.mylibrary.biz.DbBaseEntity;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 作者: ws
 * 日期: 2016/5/27.
 * 介绍：
 */
public class ColumnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<QusBaseEntity> items;

    public ColumnAdapter(ArrayList<QusBaseEntity> items) {
        this.items = items;
    }
    private HashSet<Integer> set;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_chapter_item, null);
            holder = new ChapterViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_item, null);
            holder = new ColumnViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof ChapterViewHolder) {
            ChapterViewHolder chapterViewHolder = (ChapterViewHolder) holder;
        } else {
            final QusBaseEntity entity= (QusBaseEntity) items.get(position - 1);
            final ColumnViewHolder cHolder = (ColumnViewHolder) holder;
            cHolder.tv_title.setText(entity.getTitle());
            if (entity instanceof  AuditGroupEntity) {
                cHolder.iv_type.setImageResource(R.drawable.icon_editor_category);
            } else {
                cHolder.iv_type.setImageResource(R.drawable.icon_editor_question);
            }
            if(set!=null){
                if(set.contains(position)){
                    cHolder.setColumnStyle(ColumnViewHolder.ColumnStyle.BLUE);
                }else {
                    cHolder.setColumnStyle(ColumnViewHolder.ColumnStyle.WHITE);
                }
            }
            cHolder.et_info.setText(entity.getTitle());
            cHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(set==null){
                        set=new HashSet<Integer>();
                    }
                    if (cHolder.style == ColumnViewHolder.ColumnStyle.BLUE) {
                        cHolder.setColumnStyle(ColumnViewHolder.ColumnStyle.WHITE);
                        if(set.contains(position)){
                            set.remove(position);
                        }
                    } else {
                        cHolder.setColumnStyle(ColumnViewHolder.ColumnStyle.BLUE);
                        set.add(position);
                    }
                }
            });
            cHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dealInfo(cHolder,entity, position);
                }
            });
        }
    }
    //处理信息
    private void dealInfo(ColumnViewHolder cHolder, QusBaseEntity entity,int position){
        if(cHolder.style== ColumnViewHolder.ColumnStyle.BLUE){//确定
            try {
                chageInfo(cHolder,entity);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }else{//删除
            try {
                delete(entity,position-1);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    private void delete( QusBaseEntity entity,int position) throws DbException{
        if(entity instanceof AuditItemEntity){
            AuditItemEntity iE= (AuditItemEntity) entity;
            AppDao.db.delete(iE);
            items.remove(position);
        }else if(entity instanceof AuditGroupEntity){
            AuditGroupEntity gE= (AuditGroupEntity) entity;
            AppDao.db.delete(gE);
            WhereBuilder wb= WhereBuilder.b("gId","=",gE.getId());
            wb.and("mId","=",gE.getMouldId());
          AppDao.db.delete(AuditItemEntity.class,wb);
            for (int i = position; i < items.size(); i++) {//删除数据
            if((i!=position && items.get(i) instanceof AuditGroupEntity) || i==items.size()-1){
                if(i==items.size()-1){//最后一个小组的时候要先删除最后一条
                    items.remove(i);
                }
                for (int j = 0; j < i-position; j++) {//删除当前小组的问题
                    items.remove(position);
                }
                return;
            }
            }

        }

    }

    private void chageInfo(ColumnViewHolder cHolder, QusBaseEntity entity) throws DbException{
        if(entity instanceof AuditGroupEntity){
            AuditGroupEntity gE= (AuditGroupEntity) entity;
            gE.setTitle(cHolder.et_info.getText().toString());
            AppDao.db.saveOrUpdate(gE);
        }else if(entity instanceof AuditItemEntity){
            AuditItemEntity iE= (AuditItemEntity) entity;
            iE.setTitle(cHolder.et_info.getText().toString());
            AppDao.db.saveOrUpdate(iE);
        }
    }
    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        }
        return 1;
    }

    class ChapterViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.tv_title)
        TextView tv_title;
        @ViewInject(R.id.tv_subtitle)
        TextView tv_subtitle;
        View view;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ViewUtils.inject(this, itemView);
        }
    }
}
