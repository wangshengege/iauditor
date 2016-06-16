package com.jointeach.iauditor.common;

import android.os.Handler;
import android.os.Message;

import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.CoverEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.entity.QusBaseEntity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.mylibrary.common.FileAccessor;
import org.mylibrary.utils.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: ws
 * 日期: 2016/6/16.
 * 介绍：iauditor
 */
public class MouldHelper {
    private Handler handler;
    private ReportListener reportListener;
    public MouldHelper() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(reportListener==null){
                    return;
                }
                if(msg.what==1){
                    reportListener.success((String) msg.obj);
                }else{
                    reportListener.error();
                }
            }
        };
    }
    public void report(int mId,ReportListener reportListener){
        this.reportListener=reportListener;
        handler.post(getMakeData(mId));
    }
    public interface ReportListener{
         void error();
        void success(String filePath);
    }
    private Runnable getMakeData(final int mId){
        Runnable makeData=null;
        makeData=new Runnable() {
            private MouldEntity entity;
            private ArrayList<QusBaseEntity> columnItems=new ArrayList<>();
            private byte[] getBytes(String str){
                return str.getBytes();
            }
            @Override
            public void run() {
                getData();
                initData();
                File file=new File(FileAccessor.getFilePathName(),entity.getTitle()+"-"+entity.getId()+".doc");
                try {
                    FileOutputStream fout=new FileOutputStream(file);
                    fout.write(getBytes("                                  "+entity.getTitle()+"\n\n\n"));
                    fout.flush();
                    List<CoverEntity> cEs= AppDao.getCovers(entity.getId());
                    if(cEs!=null && cEs.size()>3){
                        for (CoverEntity ce:cEs) {
                            if(ce.getType()<1){
                                continue;
                            }
                            fout.write(getBytes(ce.getTitle()+"\n"));
                            fout.write(getBytes("\t"+ CoverTypeHelper.getTitleByType(ce.getType(),entity)+"\n\n"));
                        }
                        fout.write(getBytes("\n\n\n\n\n\n"));
                    }else {
                        fout.write(getBytes("创建日期\n"));
                        fout.flush();
                        fout.write(getBytes(Tools.getFormatTime(entity.getCreateTime(), "yyyy/MM/dd HH:mm") + "\n\n"));
                        fout.write(getBytes("作者\n"));
                        fout.flush();
                        fout.write(getBytes(entity.getAuthor() + "\n\n"));
                        fout.flush();
                        fout.write(getBytes("地点\n"));
                        fout.flush();
                        fout.write(getBytes(entity.getLocation() + "\n\n"));
                        fout.flush();
                        fout.write(getBytes("参与人员\n"));
                        fout.flush();
                        fout.write(getBytes(entity.getParticipants()+"\n\n\n\n\n\n\n\n"));
                        fout.flush();
                    }
                    for (QusBaseEntity en:columnItems) {
                        if(en instanceof AuditItemEntity){
                            AuditItemEntity aE= (AuditItemEntity) en;
                            fout.write(getBytes(aE.getTitle()+"\n"));
                            fout.flush();
                            String str="";
                            switch (aE.getState()){//0是未操作，1是是，2是否，3是不适用
                                case 0:
                                    str="\t未操作\n\n";
                                    break;
                                case 1:
                                    str="\t是\n\n";
                                    break;
                                case 2:
                                    str="\t否\n\n";
                                    break;
                                case 3:
                                    str="\t不适用\n\n";
                                    break;
                            }
                            fout.write(str.getBytes());
                            fout.flush();
                        }else {
                            fout.write(getBytes("\n"+en.getTitle()+"\n\n"));
                            fout.flush();
                        }
                    }
                    fout.close();
                    entity.setReport(file.getAbsolutePath());
                    AppDao.saveMould(entity);
                    Message msg=new Message();
                    msg.what=1;
                    msg.obj=file.getAbsolutePath();
                    handler.sendMessage(msg);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }

            }
            private void getData(){
                try {
                    entity= AppDao.db.findFirst(Selector.from(MouldEntity.class).where("id","=",mId));
                } catch (DbException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
                if(entity==null){
                    handler.sendEmptyMessage(2);
                }
            }
            //加载数据
            private void initData() {
                List<AuditGroupEntity> goups= AppDao.getGroups(mId);
                for (AuditGroupEntity gE:goups) {
                    columnItems.add(gE);
                    List<AuditItemEntity> ies=AppDao.getQus(gE.getId(),gE.getMouldId());
                    for (AuditItemEntity ie:ies) {
                        columnItems.add(ie);
                    }
                }
            }
        };
        return makeData;
    }

}
