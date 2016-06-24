package com.jointeach.iauditor.ui;

import android.os.Bundle;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jointeach.iauditor.R;
import com.jointeach.iauditor.common.AppConfig;
import com.jointeach.iauditor.common.ImgLoadUtils;
import com.jointeach.iauditor.dao.AppDao;
import com.jointeach.iauditor.entity.AuditGroupEntity;
import com.jointeach.iauditor.entity.AuditItemEntity;
import com.jointeach.iauditor.entity.CoverEntity;
import com.jointeach.iauditor.entity.MouldEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.mylibrary.base.AbstractBaseActivity;
import org.mylibrary.common.CommonFunction;
import org.mylibrary.utils.Tools;

import java.util.List;

/**
 * 作者: ws
 * 日期: 2016/5/25.
 * 介绍：加载界面
 */
public class LoadActivity extends AbstractBaseActivity {
    private String[] groups = {"1.0-已完成的检查", "2.0-火灾预防", "3.0-一般照明", "4.0-建筑安全", "5.0-工作平台"
            , "6.0-垃圾清理", "7.0-储存规划和使用", "8.0-机械设备", "9.0-电气安全", "10.0-化学安全", "11.0-梯子"
            , "12.0-急救设施", "13.0-办公危险", "14.0-交通状况"};

    private String[][] ques = {{"1.1-有没有对最近的检查进行确认？", "1.2-未完成项？"}
            , {"2.1-是否公布了应急疏散计划，并使所有员工都理解计划内容？", "2.2-应急疏散程序是否定期回顾？（每年3-4次）"
            , "2.3-是否标识灭火器地点和灭火器种类？", "2.4-近期是否有维护灭火器？（每六个月检查一次）"
            , "2.5-灭火器没被阻挡？", "2.6-灭火器放置不超过1200毫米且不低于100毫米？"
            , "2.7-指示标志是否在地面2.1米以上？", "2.8-是否有明显的消防指示标示？", "2.9-安全出口门是否可以从内容轻易开启？"
            , "2.10-安全出口没被堵塞？", "2.11-报警系统是否正常运行？"}
            , {"3.1-良好的自然采光?", "3.2-墙壁和天花板反射光不会影响员工视觉?", "3.3-灯具干净且状态良好?", "3.4-安全出口灯正常?"}
            , {"4.1-地面平坦、清洁?", "4.2-进出通道保持清洁?", "4.3-通道是否有足够、合适且清楚的标识？"
            , "4.4-通道交叉口没有放置杂物？", "4.5-楼梯和升降装置是否保持清洁?", "4.6-液体泄漏物是否被很快处置？"
            , "4.7-围栏、栏杆状态良好？", "4.8-在有缝隙、缺口等地方是否做好安全防护？", "4.9-人行道是否处于良好状态？"
            , "4.10-家具状态是否良好？", "4.11-装卸区域是否干净整洁？"}
            , {"5.1-垃圾是否清理？", "5.2-不使用的工具是否放置正确？", "5.3-没有使用已损坏的手持工具？"
            , "5.4-没有使用已损坏的电动工具？", "5.5-工作高度是否与工作类型和员工相匹配？", "5.6-没有锋利边缘？"}
            , {"6.1-垃圾箱是否放置在合适的位置？", "6.2-垃圾箱是否定期清理？", "6.3-未使用金属容器盛装含有抹布与易燃废物？"}
            , {"7.1-原料是否存在合适的货架或箱子中？", "7.2-储存设计是否解决了最大程度降低最小起重重量的问题？"
            , "7.3-货架周围没有垃圾？", "7.4-货架和托盘状态是否良好？"}
            , {"8.1-是否保持干挣？", "8.2-设备周围地面是否保持干净？", "8.3-防护装置是否处于良好状态？"
            , "8.4-启动、停止装置是处在操作人员容易操作的范围内？", "8.5-废弃的切割物品是否隔离且安全存放？"
            , "8.6-是否有防泄漏装置放置泄漏物扩散？", "8.7-是否有适当的工作空间？", "8.8-照明是否充足？"
            , "8.9-噪声是否处于合适范围？", "8.10-是否需要弯腰或身体弯曲进行工作？", "8.11-垫板是否处于良好状态？"
            , "8.12-员工是否获得良好的操作培训和指导？", "8.13-是否有培训记录？", "8.14-员工是否遵循培训的要求进行操作？"
            , "8.15-是否执行挂锁上牌程序？", "8.16-气瓶是否有防护？"}
            , {"9.1-是否安装安全开关？", "9.2-安全开关是否每6个月测试一次，并保存记录？", "9.3-是否使用双层保护适配器？"
            , "9.4-是否配备便携式测试设备并做标示？", "9.5-是否有损坏的插头、插座、开关在使用？", "9.6-电源线是否穿越人行道？"
            , "9.7-是否有磨损、损坏的管线？", "9.8-是否有过于绷紧管线？", "9.9-便携式电源是否处于良好状态？", "9.10-紧急停车／停止生产程序是否放置在合适位置？"}
            , {"10.1-危险物质登记是否完整和有效？", "10.2-是否所有的化学品都有安全物质说明书？", "10.3-危险物质危险评估是否完成？"
            , "10.4-是否所有盛装容器都正确张贴标签？", "10.5-未使用化学品是否被正确处置？", "10.6-是否有提供专门的储存条件或场所？"
            , "10.7-是否适用于特殊的存储条件？", "10.8-员工是否进行过危险物质使用培训？", "10.9-是否配置合适的劳动防护用品？"
            , "10.10-是否有充分的通风？", "10.11-员工是否能容易的使用洗眼器、淋浴器？"}
            , {"11.1-所有的梯子都是工业强度吗？（非家用级，检查标签。）", "11.2-是否梯子都处于良好状态？"
            , "11.3-如果用于电气工作，是否使用不导电的梯子？（木头或玻璃纤维）", "11.4-是否依据说明说使用梯子？", "11.5-延伸梯子的绳索、滑轮和踏板是否处于良好的维护状态？"}
            , {"12.1-急救箱和急救用品是否清洁、有序？", "12.2-是否所有的急救用品都定期检查？"
            , "12.3-是否有过期的急救用品？", "12.4-急救箱是否清楚标识？", "12.5-人员是否可以很容易使用到急救用品？"
            , "12.6-员工清楚急救箱位置吗？", "12.7-急救箱管理人员可以使用吗？", "12.8-是否公布了急救电话？"}
            , {"13.1-文件柜是否合适？", "13.2-坐骑是否合适？", "13.3-桌子状态是否良好？"
            , "13.4-屏幕是否受到外部光源影响？", "13.5-复印机是否放在避免员工呼吸到其粉尘的地方？", "13.6-工作任务是否会考虑到员工工作疲劳？"
            , "13.7-通风系统是否定期维护？", "13.8-防滑地板是否良好？"}
            , {"14.1-是否按计划进行维护?", "14.2-轮胎状况是否完好？", "14.3-刹车系统是否完好？"
            , "14.4-车灯是否良好？", "14.5-司机坐椅是否良好？", "14.6-乘客坐椅是否良好？", "14.7-急救用品是否充足？"
            , "14.8-存款规定?"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView iv = new ImageView(self);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        ImgLoadUtils.getImageLoader().displayImage("drawable://" + R.drawable.load, iv);
        setContentView(iv);
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (!CommonFunction.getBoolean(AppConfig.ISADDMOULD, false)) {
                    try {
                        initData(AppDao.db, false, 2);//模拟一个模版
                        initQus(false);
                        initCover(false);
                        CommonFunction.putBoolean(AppConfig.ISADDMOULD, true);
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
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
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
    private void initQus(boolean isAudit) throws DbException {
        MouldEntity mouldEntity = null;
        mouldEntity = AppDao.db.findFirst(Selector.from(MouldEntity.class)
                .where("type", "=", isAudit ? "1" : "0"));
        for (int i = 0; i < groups.length; i++) {
            String group = groups[i];
            AuditGroupEntity gE = new AuditGroupEntity();
            gE.setTitle(group);
            gE.setMouldId(mouldEntity.getId());
            gE.setIsAudit(isAudit ? 1 : 0);
            AppDao.saveGroup(gE);
            List<AuditGroupEntity> gs = AppDao.getGroups(mouldEntity.getId());
            AuditGroupEntity a = gs.get(gs.size() - 1);//存储小组完成后，获取该小组
            for (int j = 0; j < ques[i].length; j++) {
                String que = ques[i][j];
                AuditItemEntity iE = new AuditItemEntity();
                iE.setTitle(que);
                iE.setmId(a.getMouldId());
                iE.setgId(a.getId());
                iE.setIsAudit(isAudit ? 1 : 0);
                AppDao.saveQus(iE);
            }
        }
    }

    private void initCover(boolean isAudit) throws DbException {
        MouldEntity en = null;
        en = AppDao.db.findFirst(Selector.from(MouldEntity.class)
                .where("type", "=", isAudit ? "1" : "0"));
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

    private void initData(DbUtils db, boolean isAudit, int mId) throws DbException {
        MouldEntity en = new MouldEntity();
        en.setCreateTime(Tools.getTimeStamp());
        en.setTitle("工作空间检查");
        en.setId(mId);
        en.setLastRevise(Tools.getTimeStamp());
        if (isAudit) {
            en.setType(1);
            en.setState(1);
        }
        db.save(en);
    }
}
