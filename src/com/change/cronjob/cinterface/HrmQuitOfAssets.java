package com.change.cronjob.cinterface;

import java.util.ArrayList;
import java.util.List;

import com.change.cronjob.entities.ModeVO;
import com.change.cronjob.entities.UfZctz;
import com.change.cronjob.utils.AssetUtil;
import com.change.cronjob.utils.DateUtils;

import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

public class HrmQuitOfAssets extends BaseCronJob {
	
	private Logger log = LoggerFactory.getLogger(HrmQuitOfAssets.class);
	
	public void execute() {
		log.info("人员离职资产汇总执行开始...");
		String del_sql = "delete from uf_rylzgdzcb";
		RecordSet del_rs = new RecordSet();
		del_rs.execute(del_sql);
		
		String sql = "select id,requestId,glzz,zczt,szkf,sybm,syr,syrgh,zclx,zclxbm,"
				+ "zcmc,zcbm,gg,xh,cfdd,ly,kssyrq,zcczrq,gys,cgdw,zjqs,czl,yzjl,cgjehs,"
				+ "zcyzbhs,yzjje,yzjqsy,zcljzj,zcjz,yzcbm,lyrq,bz,glzzbm,zjsfwc,sydwzcgly,"
				+ "sydwqtry,glzzzggly,glzzjggly,glzzqtry,kfgly,kfqtry from uf_zctz where zczt = 1";
		String rylz_modelid = Prop.getPropValue("AssetsModelId", "rylz_modelid");
		List<UfZctz> assetList = AssetUtil.getAssetList(sql);
		RecordSet inser_rs = new RecordSet();
		RecordSet status_rs = new RecordSet();
		for (UfZctz ufZctz : assetList) {
			if(ufZctz.getSyr() == 0) {
				continue;
			}
			log.info("->->->->->->->->->->->->->->->->->->");
			String status_sql = "select status from hrmresource where id=" +ufZctz.getSyr() ;
			log.info("status_sql -> " + status_sql);
			status_rs.execute(status_sql);
			status_rs.next();
			int status = Util.getIntValue(status_rs.getString("status"));
			if(status == 5){ // 如果人员状态为离职状态
				String insert_sql = "insert into uf_rylzgdzcb(zclx,zcmc,zcbm,syr,sybm,sydwzcgly,"
						+ "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime)" + 
						"values('"+ufZctz.getZclx() + "','" + ufZctz.getId() + "','" + ufZctz.getZcbm()+ "','" + ufZctz.getSyr() + 
						"','" + ufZctz.getSybm() + "','" + ufZctz.getSydwzcgly() + "'," + 
						rylz_modelid+",1,0,'" + DateUtils.getNowStr("yyyy-MM-dd") + "','" + DateUtils.getNowStr("HH:mm:dd") + "')";
				log.info("人员异动资产汇总sql -> " + insert_sql);
				inser_rs.execute(insert_sql);
			}
		}
		
		// 获取新增数据
		log.info("人员离职资产权限重构开始...");
		List<ModeVO> modeList = new ArrayList<ModeVO>();
		RecordSet rebuild_rs = new RecordSet();
		String rebuild_sql = "select id,modedatacreater,formmodeid from uf_rylzgdzcb";
		rebuild_rs.execute(rebuild_sql);
		while (rebuild_rs.next()) {
			ModeVO vo = new ModeVO();
			Integer billid = Util.getIntValue(rebuild_rs.getString("id"));
			Integer modedatacreater = Util.getIntValue(rebuild_rs.getString("modedatacreater"));
			Integer formmodeid = Util.getIntValue(rebuild_rs.getString("formmodeid"));
			vo.setBillid(billid);
			vo.setModeId(formmodeid);
			vo.setCreater(modedatacreater);
			modeList.add(vo);
		}
		ModeRightInfo modeRightInfo = new ModeRightInfo();
		modeRightInfo.setNewRight(true);
		for (ModeVO modeVO : modeList) {
			modeRightInfo.editModeDataShare(modeVO.getCreater(), modeVO.getModeId(), modeVO.getBillid());
		}
		log.info("人员离职资产权限重构结束...");
		log.info("人员离职资产汇总执行结束...");
	}
}
