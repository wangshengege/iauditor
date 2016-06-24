package com.jointeach.iauditor.common;

import android.os.Handler;
import android.os.Message;

import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.AuditQusPicEntity;
import com.jointeach.iauditor.entity.CoverEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.jointeach.iauditor.entity.QusBaseEntity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
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
    private CreateMouldListener createMouldListener;
    private DeleteMouldListener deleteMouldListener;

    public MouldHelper() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    if (reportListener != null) {
                        reportListener.success((String) msg.obj);
                    }
                } else if (msg.what == 2) {
                    if (reportListener != null) {
                        reportListener.error();
                    }
                    if (createMouldListener != null) {
                        createMouldListener.error();
                    }
                    if (deleteMouldListener != null) {
                        deleteMouldListener.error();
                    }
                } else if (msg.what == 3) {
                    if (createMouldListener != null) {
                        createMouldListener.success((Integer) msg.obj);
                    }
                } else if (msg.what == 5) {
                    if (deleteMouldListener != null) {
                        deleteMouldListener.success();
                    }
                }
            }
        };
    }

    /**
     * 导出报告
     *
     * @param mId 模版id
     */
    public void report(int mId, ReportListener reportListener) {
        this.reportListener = reportListener;
        handler.post(getMakeData(mId));
    }

    public void createMould(CreateMouldListener createMouldListener) {
        this.createMouldListener = createMouldListener;
        handler.post(creatMould());
    }

    public interface ReportListener {
        void error();

        void success(String filePath);
    }

    public interface CreateMouldListener {
        void error();

        void success(int mId);
    }

    public interface DeleteMouldListener {
        void error();

        void success();
    }

    private Runnable getMakeData(final int mId) {
        Runnable makeData = null;
        makeData = new Runnable() {
            private MouldEntity entity;
            private ArrayList<QusBaseEntity> columnItems = new ArrayList<>();

            private byte[] getBytes(String str) {
                return str.getBytes();
            }

            @Override
            public void run() {
                getData();
                initData();
                File file = new File(FileAccessor.getFilePathName(), entity.getTitle() + "-" + entity.getId() + ".doc");
                try {
                    FileOutputStream fout = new FileOutputStream(file);
                    fout.write(getBytes("                                  " + entity.getTitle() + "\n\n\n"));
                    fout.flush();
                    List<CoverEntity> cEs = AppDao.getCovers(entity.getId());
                    if (cEs != null && cEs.size() > 3) {
                        for (CoverEntity ce : cEs) {
                            if (ce.getType() < 1) {
                                continue;
                            }
                            fout.write(getBytes(ce.getTitle() + "\n"));
                            fout.write(getBytes("\t" + CoverTypeHelper.getTitleByType(ce.getType(), entity) + "\n\n"));
                        }
                        fout.write(getBytes("\n\n\n\n\n\n"));
                    } else {
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
                        fout.write(getBytes(entity.getParticipants() + "\n\n\n\n\n\n\n\n"));
                        fout.flush();
                    }
                    for (QusBaseEntity en : columnItems) {
                        if (en instanceof AuditItemEntity) {
                            AuditItemEntity aE = (AuditItemEntity) en;
                            fout.write(getBytes(aE.getTitle() + "\n"));
                            fout.flush();
                            String str = "";
                            switch (aE.getState()) {//0是未操作，1是是，2是否，3是不适用
                                case 0:
                                    str = "\t未操作\n";
                                    break;
                                case 1:
                                    str = "\t是\n";
                                    break;
                                case 2:
                                    str = "\t否\n";
                                    break;
                                case 3:
                                    str = "\t不适用\n";
                                    break;
                            }
                            fout.write(str.getBytes());
                            fout.flush();
                            fout.write(getBytes("备注："+((AuditItemEntity) en).getDescribe()+"\n\n"));
                            fout.flush();
                        } else {
                            fout.write(getBytes("\n" + en.getTitle() + "\n\n"));
                            fout.flush();
                        }
                    }
                    fout.close();
                    entity.setReport(file.getAbsolutePath());
                    AppDao.saveMould(entity);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = file.getAbsolutePath();
                    handler.sendMessage(msg);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }

            }

            private void getData() {
                try {
                    entity = AppDao.db.findFirst(Selector.from(MouldEntity.class).where("id", "=", mId));
                } catch (DbException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
                if (entity == null) {
                    handler.sendEmptyMessage(2);
                }
            }

            //加载数据
            private void initData() {
                List<AuditGroupEntity> goups = AppDao.getGroups(mId);
                for (AuditGroupEntity gE : goups) {
                    columnItems.add(gE);
                    List<AuditItemEntity> ies = AppDao.getQus(gE.getId(), gE.getMouldId());
                    for (AuditItemEntity ie : ies) {
                        columnItems.add(ie);
                    }
                }
            }
        };
        return makeData;
    }

    public Runnable creatMould() {
        Runnable creatRun = null;
        creatRun = new Runnable() {
            @Override
            public void run() {
                MouldEntity mouldEntity = new MouldEntity();
                mouldEntity.setCreateTime(Tools.getTimeStamp());
                mouldEntity.setLastRevise(Tools.getTimeStamp());
                mouldEntity.setType(0);
                try {
                    AppDao.db.save(mouldEntity);
                    List<MouldEntity> mList = AppDao.getMoulds(false);
                    if (mList == null || mList.size() < 1) {
                        handler.sendEmptyMessage(2);
                        return;
                    }
                    MouldEntity cM = mList.get(mList.size() - 1);
                    initCover(cM);
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = cM.getId();
                    handler.sendMessage(msg);
                } catch (DbException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
            }

            private void initCover(MouldEntity en) throws DbException {
                CoverEntity cE1 = new CoverEntity();
                cE1.setTitle("标题");
                cE1.setmId(en.getId());
                //1是创建日期，2是作者，3是地点，4是参与者
                CoverEntity cE2 = new CoverEntity();
                cE2.setTitle("创建日期");
                cE2.setmId(en.getId());
                cE2.setType(1);
                CoverEntity cE3 = new CoverEntity();
                cE3.setTitle("作者");
                cE3.setmId(en.getId());
                cE3.setType(2);
                CoverEntity cE4 = new CoverEntity();
                cE4.setTitle("地点");
                cE4.setmId(en.getId());
                cE4.setType(3);
                CoverEntity cE5 = new CoverEntity();
                cE5.setTitle("参与人员");
                cE5.setmId(en.getId());
                cE5.setType(4);
                AppDao.db.save(cE1);
                AppDao.db.save(cE2);
                AppDao.db.save(cE3);
                AppDao.db.save(cE4);
                AppDao.db.save(cE5);
            }
        };
        return creatRun;
    }

    /**
     * 删除模版
     */
    public void deleteMould(int mId, boolean isAudit, DeleteMouldListener deleteMouldListener) {
        this.deleteMouldListener = deleteMouldListener;
        handler.post(getdeleteMould(mId, isAudit));
    }

    private Runnable getdeleteMould(final int mId, final boolean isAudit) {
        Runnable deRun = null;
        deRun = new Runnable() {
            @Override
            public void run() {
                try {
                    WhereBuilder mwb = WhereBuilder.b("id", "=", mId);
                    mwb.and("type", "=", isAudit ? 1 : 0);
                    AppDao.db.delete(MouldEntity.class, mwb);
                    WhereBuilder cwb = WhereBuilder.b("mId", "=", mId);
                    AppDao.db.delete(CoverEntity.class, cwb);
                    WhereBuilder gwb = WhereBuilder.b("mouldId", "=", mId);
                    AppDao.db.delete(AuditGroupEntity.class, gwb);
                    AppDao.db.delete(AuditItemEntity.class, cwb);
                    AppDao.db.delete(AuditQusPicEntity.class, cwb);
                    handler.sendEmptyMessage(5);
                } catch (DbException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
            }
        };
        return deRun;
    }
}
