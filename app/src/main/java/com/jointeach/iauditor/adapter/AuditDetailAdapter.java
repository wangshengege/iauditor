package com.jointeach.iauditor.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.common.ImgLoadUtils;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.AuditQusPicEntity;
import com.jointeach.iauditor.view.SelectPicListener;
import com.jointeach.iauditor.view.SelectPicPop;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.mylibrary.common.FileAccessor;
import org.mylibrary.utils.LogTools;
import org.mylibrary.utils.PxUtil;
import org.mylibrary.utils.Tools;
import org.mylibrary.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
    private Activity ctx;
    private AuditItemEntity picItem;
    private File iconPath;
    private HashSet<Integer> set;
    public AuditDetailAdapter(Activity ctx,ArrayList<AuditGroupEntity> groups, HashMap<Integer, ArrayList<AuditItemEntity>> items,ExpandableListView plv) {
        this.ctx=ctx;
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
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audit_pos, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AuditItemEntity entity = (AuditItemEntity) getChild(groupPosition, childPosition);
        holder.tv_title.setText(entity.getTitle());
        holder.btn_qus_ncan.setTag(entity);
        holder.btn_qus_ncan.setOnClickListener(qus_nocan);
        holder.btn_qus_no.setTag(entity);
        if(entity.getState()==2){
            holder.ll_data.setVisibility(View.VISIBLE);
            initPic(holder,entity);
        }else {
            holder.ll_data.setVisibility(View.GONE);
            initPic(holder,null);
        }
        holder.btn_qus_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuditItemEntity entity= (AuditItemEntity) v.getTag();
                entity.setState(2);
                v.setSelected(true);
                upData(entity);
                holder.ll_data.setVisibility(View.VISIBLE);
            }
        });
        holder.btn_qus_yes.setTag(entity);
        holder.btn_qus_yes.setOnClickListener(qus_yes);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               List<AuditQusPicEntity> itms= getPics(entity);
                if(itms!=null && itms.size()>=4){
                    Tools.showToast(ctx,"照片不能多于4张");
                    return;
                }
                picItem=entity;
                SelectPicPop pop=new SelectPicPop(v.getContext());
                pop.setSelectPicListener(new SelectPicListener() {
                    @Override
                    public void camera(View v) {
                        iconPath = new File(FileAccessor.getImagePathName(), Tools.getTimeStamp() + ".jpg");
                        Utils.photo(ctx, iconPath, 100);
                    }

                    @Override
                    public void gallery(View v) {
                        Utils.album(ctx, 200);
                    }
                });
                pop.getPop().showAsDropDown(v);
            }
        });
        holder.et_describe.setText(entity.getDescribe());
        holder.et_describe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                entity.setDescribe(s.toString());
                try {
                    AppDao.db.saveOrUpdate(entity);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       switch (entity.getState()){
           case 1:
               holder.setNoState();
               holder.btn_qus_yes.setSelected(true);
               break;
           case 2:
               holder.setNoState();
               holder.btn_qus_no.setSelected(true);
               break;
           case 3:
               holder.setNoState();
               holder.btn_qus_ncan.setSelected(true);
               break;
           default:
               holder.setNoState();
               break;
       }
        return convertView;
    }
    //获取图片
    private List<AuditQusPicEntity> getPics(AuditItemEntity entity){
        List<AuditQusPicEntity> pics=null;
        try {
            pics=AppDao.db.findAll(Selector.from(AuditQusPicEntity.class)
                    .where("qusId","=",entity.getId())
            .and("gId","=",entity.getgId()));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return pics;
    }
    private void initPic(ViewHolder holder,AuditItemEntity entity) {
        holder.ll_gallery.removeAllViews();
        if(entity!=null){
        List<AuditQusPicEntity> pics=getPics(entity);
        if(pics!=null && pics.size()>0){
            for (AuditQusPicEntity pic:pics) {
                ImageView iv=new ImageView(ctx);
                iv.setLayoutParams(new ViewGroup.LayoutParams(PxUtil.dip2px(ctx,85),PxUtil.dip2px(ctx,80)));
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setPadding(PxUtil.dip2px(ctx,2.5f),0,PxUtil.dip2px(ctx,2.5f),0);
                ImgLoadUtils.loadImageRes(pic.getImgPath(),iv);
                holder.ll_gallery.addView(iv);
            }
        }}
    }

    private View.OnClickListener qus_yes=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AuditItemEntity entity= (AuditItemEntity) v.getTag();
            entity.setState(1);
            v.setSelected(true);
            upData(entity);
        }
    };
    private View.OnClickListener qus_nocan=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AuditItemEntity entity= (AuditItemEntity) v.getTag();
            entity.setState(3);
            v.setSelected(true);
            upData(entity);
        }
    };
    //更新数据
    private void upData(AuditItemEntity entity){
        try {
            AppDao.db.saveOrUpdate(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
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
        Button btn_qus_yes;
        Button btn_qus_no;
        Button btn_qus_ncan;
        View ll_data;
        Button add;
        LinearLayout ll_gallery;
        EditText et_describe;
        ImageView iv_ok;
        public ViewHolder(View itemView) {
            this.itemView = itemView;
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            btn_qus_yes= (Button) itemView.findViewById(R.id.btn_qus_yes);
            btn_qus_no= (Button) itemView.findViewById(R.id.btn_qus_no);
            btn_qus_ncan= (Button) itemView.findViewById(R.id.btn_qus_ncan);
            ll_data=itemView.findViewById(R.id.ll_data);
            add= (Button) itemView.findViewById(R.id.btn_addpic);
            ll_gallery= (LinearLayout) itemView.findViewById(R.id.ll_gallery);
            et_describe= (EditText) itemView.findViewById(R.id.et_describe);
            iv_ok= (ImageView) itemView.findViewById(R.id.iv_ok);
        }
        public void setNoState(){
            btn_qus_yes.setSelected(false);
            btn_qus_no.setSelected(false);
            btn_qus_ncan.setSelected(false);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==0 || picItem==null){//取消操作
            return;
        }
        String iconP = null;
        if (requestCode == 100) {//拍照
            iconP = iconPath.getAbsolutePath();
        } else if (requestCode == 200) {//相册
            iconP = Utils.resolvePhotoFromIntent(ctx, data, FileAccessor.getImagePathName().getAbsolutePath());
        }
        AuditQusPicEntity pic=new AuditQusPicEntity();
        pic.setImgPath(iconP);
        pic.setQusId(picItem.getId());
        pic.setgId(picItem.getgId());
        pic.setmId(picItem.getmId());
        try {
            AppDao.db.save(pic);
            notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
