package com.jointeach.iauditor.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者: ws
 * 日期: 2016/6/7.
 * 介绍：审计内容
 */
public class AuditDetailAdapter extends BaseExpandableListAdapter {
    private ArrayList<AuditGroupEntity> groups;
    private HashMap<Integer, ArrayList<AuditItemEntity>> items;
    private HashMap<Integer,Boolean> isOpen;
    private ExpandableListView plv;
    public AuditDetailAdapter(ArrayList<AuditGroupEntity> groups, HashMap<Integer, ArrayList<AuditItemEntity>> items,ExpandableListView plv) {
        this.groups = groups;
        this.items = items;
        this.plv=plv;
        isOpen=new HashMap<>();
        for (int i = 0; i <groups.size() ; i++) {
            isOpen.put(i,false);
        }
    }

    @Override
    public int getGroupCount() {
        return groups == null ? 0 : groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<AuditItemEntity> ai = items.get(groups.get(groupPosition).getId());
        return ai == null ? 0 : ai.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groups.get(groupPosition).getId()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audit_group, null);
            holder=new GroupViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (GroupViewHolder) convertView.getTag();
        }
        AuditGroupEntity entity = (AuditGroupEntity) getGroup(groupPosition);
        holder.tv_title.setText(entity.getTitle());
        if (isOpen.get(groupPosition)) {
            holder.iv_icon.setImageResource(R.drawable.ic_collapse_selector);
        } else
            holder.iv_icon.setImageResource(R.drawable.ic_collapse_nor);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen.get(groupPosition)) {
                    holder.iv_icon.setImageResource(R.drawable.ic_collapse_selector);
                    plv.expandGroup(groupPosition);
                } else{
                    holder.iv_icon.setImageResource(R.drawable.ic_collapse_nor);
                    plv.collapseGroup(groupPosition);
                }
                isOpen.put(groupPosition,!isOpen.get(groupPosition));
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audit_pos, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AuditItemEntity entity = (AuditItemEntity) getChild(groupPosition, childPosition);
        holder.tv_title.setText(entity.getTitle());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        View itemView;
        TextView tv_title;
        ImageView iv_icon;

        public GroupViewHolder(View itemView) {
            this.itemView = itemView;
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

    class ViewHolder {
        View itemView;
        TextView tv_title;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
