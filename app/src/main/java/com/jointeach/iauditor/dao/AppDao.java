package com.jointeach.iauditor.dao;

import com.jointeach.iauditor.common.JKApplication;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.CoverEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.mylibrary.utils.LogTools;

import java.util.List;

/**
 * 作者: ws
 * 日期: 2016/6/12.
 * 介绍：获取数据
 */
public class AppDao {
    private static final String TAG="AppDao";
    public static DbUtils db=DbUtils.create(JKApplication.getContext());
        /**获取模版或者审计*/
        public static MouldEntity getMould(int mId){
            MouldEntity mouldEntity = null;
            try {
                mouldEntity=db.findFirst(Selector.from(MouldEntity.class)
                        .where("id","=",mId));
            } catch (DbException e) {
                e.printStackTrace();
                LogTools.e(TAG,e.getMessage());
            }
            return mouldEntity;
    }
    /**保存模版*/
    public static void saveMould(MouldEntity mould){
        try {
            db.saveOrUpdate(mould);
        } catch (DbException e) {
            e.printStackTrace();
            LogTools.e(TAG,e.getMessage());
        }
    }
    /**获取问题群组*/
    public static List<AuditGroupEntity> getGroups(int mouldId){
        List<AuditGroupEntity> gItms=null;
        try {
            gItms = db.findAll(Selector.from(AuditGroupEntity.class)
                    .where("mouldId","=",mouldId));
            return gItms;
        } catch (DbException e) {
            e.printStackTrace();
            LogTools.e(TAG,e.getMessage());
        }
        return gItms;
    }
    /**获取问题列表*/
    public static  List<AuditItemEntity> getQus(int gId,int mId){
        List<AuditItemEntity> items=null;
        try {
            items = db.findAll(Selector.from(AuditItemEntity.class)
                    .where("gId","=",gId)
                    .and("mId","=",mId));
            return items;
        } catch (DbException e) {
            e.printStackTrace();
            LogTools.e(TAG,e.getMessage());
        }
        return items;
    }
    /**获取所有的模版*/
    public static List<MouldEntity> getMoulds(boolean isAudit){
        List<MouldEntity> moulds=null;
        try {
            moulds=db.findAll(Selector.from(MouldEntity.class)
            .where("type","=",isAudit?1:0));
        } catch (DbException e) {
            e.printStackTrace();
            LogTools.e(TAG,e.getMessage());
        }
        return moulds;
    }
    /**保存群组*/
    public static void saveGroup(AuditGroupEntity en){
        try {
            db.save(en);
        } catch (DbException e) {
            e.printStackTrace();
            LogTools.e(TAG,e.getMessage());
        }
    }
    /**保存问题*/
    public static void saveQus(AuditItemEntity en){
        try {
            db.save(en);
        } catch (DbException e) {
            e.printStackTrace();
            LogTools.e(TAG,e.getMessage());
        }
    }
    /**根据模版id获取所有的封面条目*/
    public static  List<CoverEntity> getCovers(int mId){
        List<CoverEntity> list=null;
        try {
            list = AppDao.db.findAll(Selector.from(CoverEntity.class).where("mId","=",String.valueOf(mId)));
        } catch (DbException e) {
            e.printStackTrace();
            LogTools.e(TAG,"getCovers:"+e.getMessage());
        }
        return list;
    }
}
