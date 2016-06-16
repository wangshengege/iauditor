package com.jointeach.iauditor.ui;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jointeach.iauditor.MainActivity;
import com.jointeach.iauditor.R;
import com.jointeach.iauditor.common.AppConfig;
import com.jointeach.iauditor.common.ImgLoadUtils;
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
    private String[] groups={"群组1","群组2"};
    private String[][] ques={{"wen1","w2"},{"w3","4"}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView iv=new ImageView(self);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ImgLoadUtils.getImageLoader().displayImage("drawable://"+ R.drawable.load,iv);
        setContentView(iv);
            new Thread() {
                @Override
                public void run() {
                    super.run();

                    if(!CommonFunction.getBoolean(AppConfig.ISADDMOULD, false)){
                    try {
                        initData(AppDao.db,false,2);//模拟一个模版
                        initQus(false);
                        initCover(false);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                /*    try {
                        initData(AppDao.db, true,1);//模拟一个审计
                        initQus(true);
                        initCover(true);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
*/
                    CommonFunction.putBoolean(AppConfig.ISADDMOULD, true);
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Tools.toActivity(self, LoginActivity.class);
                    finish();
                }
            }.start();
        }
    //初始化问题
    private void initQus(boolean isAudit) throws DbException{
        MouldEntity mouldEntity=null;
        mouldEntity= AppDao.db.findFirst(Selector.from(MouldEntity.class)
                .where("type","=",isAudit?"1":"0"));
        for (int i = 0; i < groups.length; i++) {
            String group=groups[i];
            AuditGroupEntity gE=new AuditGroupEntity();
            gE.setTitle(group);
            gE.setMouldId(mouldEntity.getId());
            gE.setIsAudit(isAudit?1:0);
            AppDao.saveGroup(gE);
            List<AuditGroupEntity> gs= AppDao.getGroups(mouldEntity.getId());
            AuditGroupEntity a=gs.get(gs.size()-1);//存储小组完成后，获取该小组
            for (int j = 0; j < ques[i].length; j++) {
                String que=ques[i][j];
                AuditItemEntity iE = new AuditItemEntity();
                iE.setTitle(que);
                iE.setmId(a.getMouldId());
                iE.setgId(a.getId());
                iE.setIsAudit(isAudit?1:0);
                AppDao.saveQus(iE);
            }
        }
    }
    private void initCover(boolean isAudit) throws DbException{
        MouldEntity en=null;
        en= AppDao.db.findFirst(Selector.from(MouldEntity.class)
                .where("type","=",isAudit?"1":"0"));
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
