package com.jointeach.iauditor.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jointeach.iauditor.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 作者: ws
 * 日期: 2016/6/16.
 * 介绍：iauditor
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private ArrayList<String> accs;
    private HashSet<Integer> set;
    public AccountAdapter(ArrayList<String> accs) {
        this.accs = accs;
    }

    public HashSet<Integer> getCheckedIndex() {
        return set;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(accs.get(position));
        if(set==null){
            set=new HashSet<>();
        }
        if(set.contains(position)){
            holder.cb.setChecked(true);
        }else{
            holder.cb.setChecked(false);
        }
        holder.cb.setTag(position);
        holder.cb.setOnCheckedChangeListener(checkedListener);
    }
    private CompoundButton.OnCheckedChangeListener checkedListener=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int index= (int) buttonView.getTag();
            if(isChecked){
            set.add(index);
            }else {
                set.remove(index);
            }
        }
    };
    @Override
    public int getItemCount() {
        return accs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        CheckBox cb;
        public ViewHolder(View itemView) {
            super(itemView);
            tv= (TextView) itemView.findViewById(R.id.tv_title);
            cb= (CheckBox) itemView.findViewById(R.id.cb);
        }
    }
}
