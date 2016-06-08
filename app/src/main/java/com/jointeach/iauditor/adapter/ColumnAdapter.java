package com.jointeach.iauditor.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.entity.ColumnEntity;
import com.jointeach.iauditor.view.ColumnViewHolder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 作者: ws
 * 日期: 2016/5/27.
 * 介绍：
 */
public class ColumnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ColumnEntity> items;

    public ColumnAdapter(ArrayList<ColumnEntity> items) {
        this.items = items;
    }

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChapterViewHolder) {
            ChapterViewHolder chapterViewHolder = (ChapterViewHolder) holder;
        } else {
            ColumnEntity entity = items.get(position - 1);
            final ColumnViewHolder cHolder = (ColumnViewHolder) holder;
            cHolder.tv_title.setText(entity.getTitle());
            if (entity.getIsGroup() == 1) {
                cHolder.iv_type.setImageResource(R.drawable.icon_editor_category);
            } else {
                cHolder.iv_type.setImageResource(R.drawable.icon_editor_question);
            }
            cHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cHolder.style == ColumnViewHolder.ColumnStyle.BLUE) {
                        cHolder.setColumnStyle(ColumnViewHolder.ColumnStyle.WHITE);
                    } else {
                        cHolder.setColumnStyle(ColumnViewHolder.ColumnStyle.BLUE);
                    }
                }
            });
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
