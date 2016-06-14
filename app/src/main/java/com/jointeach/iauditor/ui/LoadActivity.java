package com.jointeach.iauditor.ui;

import android.os.Bundle;

import com.jointeach.iauditor.MainActivity;
import com.jointeach.iauditor.common.AppConfig;
import com.jointeach.iauditor.common.JKApplication;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.ColumnEntity;
import com.jointeach.iauditor.entity.CoverEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.common.CommonFunction;
import org.mylibrary.utils.LogTools;
import org.mylibrary.utils.Tools;

import java.util.List;

/**
 * 作者: ws
 * 日期: 2016/5/25.
 * 介绍：
 */
public class LoadActivity extends AbstractBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CommonFunction.getBoolean(AppConfig.ISADDMOULD, false)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    DbUtils db = DbUtils.create(JKApplication.getContext());
                    try {
                        initData(db,false,2);//模拟一个模版
                        initQus(false);
                        initCover(false);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    try {
                        initData(db, true,1);//模拟一个审计
                        initQus(true);
                        initCover(true);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                    CommonFunction.putBoolean(AppConfig.ISADDMOULD, true);
                    Tools.toActivity(self, MainActivity.class);
                    finish();
                }
            }.start();
        } else {
            Tools.toActivity(self, MainActivity.class);
            finish();
        }
    }
    private void initQus(boolean isAudit) throws DbException{
        MouldEntity mouldEntity=null;
             mouldEntity= AppDao.db.findFirst(Selector.from(MouldEntity.class)
            .where("type","=",isAudit?"1":"0"));
        AuditGroupEntity gE1 = new AuditGroupEntity();
        gE1.setTitle("第1组"+(isAudit?"审计":"模版")+"问题");
        gE1.setMouldId(mouldEntity.getId());
        gE1.setIsAudit(1);
        AuditGroupEntity gE2 = new AuditGroupEntity();
        gE2.setTitle("第2组"+(isAudit?"审计":"模版")+"问题");
        gE2.setMouldId(mouldEntity.getId());
        gE2.setIsAudit(1);
        AppDao.saveGroup(gE1);
        List<AuditGroupEntity> gs= AppDao.getGroups(mouldEntity.getId());
        AuditGroupEntity a=gs.get(gs.size()-1);
        for (int i = 0; i <3 ; i++) {
            AuditItemEntity iE = new AuditItemEntity();
            iE.setTitle("问题:" + 1);
            iE.setmId(a.getMouldId());
            iE.setgId(a.getId());
            if (isAudit) {
                iE.setIsAudit(1);
            }
            AppDao.saveQus(iE);
        }
        AppDao.saveGroup(gE2);
        List<AuditGroupEntity> gs2= AppDao.getGroups(mouldEntity.getId());
        AuditGroupEntity a2=gs2.get(gs2.size()-1);
        for (int i = 0; i < 3; i++) {
            AuditItemEntity iE1 = new AuditItemEntity();
            iE1.setTitle("问题:" + 2);
            iE1.setmId(a2.getMouldId());
            iE1.setgId(a2.getId());
            if (isAudit) {
                iE1.setIsAudit(1);
            }
            AppDao.saveQus(iE1);
        }

    }
    private void initCover(boolean isAudit) throws DbException{
        MouldEntity en=null;
        en= AppDao.db.findFirst(Selector.from(MouldEntity.class)
                .where("type","=",isAudit?"1":"0"));
        CoverEntity cE1 = new CoverEntity();
        cE1.setTitle("标题");
        cE1.setmId(en.getId());
        CoverEntity cE2 = new CoverEntity();
        cE2.setTitle("创建日期");
        cE2.setmId(en.getId());
        CoverEntity cE3 = new CoverEntity();
        cE3.setTitle("作者");
        cE3.setmId(en.getId());
        CoverEntity cE4 = new CoverEntity();
        cE4.setTitle("地点");
        cE4.setmId(en.getId());
        CoverEntity cE5 = new CoverEntity();
        cE5.setTitle("参与人员");
        cE5.setmId(en.getId());
        if (isAudit) {
            cE1.setIsAudit(1);
            cE2.setIsAudit(1);
            cE3.setIsAudit(1);
            cE4.setIsAudit(1);
            cE5.setIsAudit(1);
        }
        AppDao.db.save(cE1);
        AppDao.db.save(cE2);
        AppDao.db.save(cE3);
        AppDao.db.save(cE4);
        AppDao.db.save(cE5);
    }
    private void initData(DbUtils db, boolean isAudit,int mId) throws DbException {
        MouldEntity en = new MouldEntity();
        en.setIcPath("http://h.hiphotos.baidu.com/image/h%3D200/sign=71cd4229be014a909e3e41bd99763971/472309f7905298221dd4c458d0ca7bcb0b46d442.jpg");
        en.setCreateTime(Tools.getTimeStamp());
        en.setDescribe("这是我的模版");
        en.setTitle("我的模版");
        en.setId(mId);
        en.setAuthor("ws");
        en.setIndustry("建筑");
        en.setcIndustry("装修");
        en.setLastRevise(Tools.getTimeStamp());
        ColumnEntity group = new ColumnEntity();
        group.setTitle("第一个群组");
        group.setmId(en.getId());
        group.setIsGroup(1);
        group.setType(1);
        group.setId(1+mId);
        ColumnEntity cC = new ColumnEntity();
        cC.setmId(en.getId());
        cC.setTitle("第一选项");
        cC.setpId(group.getId());
        cC.setIsGroup(0);
        cC.setType(2);

        if (isAudit) {
            en.setIcPath("http://img4.imgtn.bdimg.com/it/u=3239270334,2003259050&fm=21&gp=0.jpg");
            en.setType(1);
            en.setState(1);
            group.setIsAudit(1);
            cC.setIsAudit(1);

        }
            db.save(en);
            db.save(group);
            db.save(cC);

    }
}
