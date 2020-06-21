package com.change.cronjob.cinterface;

import java.util.List;

import com.change.cronjob.entities.UfZctz;
import com.change.cronjob.utils.AssetUtil;
import com.change.cronjob.utils.DateUtils;
import com.weaver.general.Util;

import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

/**  
 * @Author: changxizhao
 * @Date: 2020年6月20日 上午10:01:12
 */
public class HrmTransferOfAssets extends BaseCronJob {
	
	private Logger log = LoggerFactory.getLogger(HrmTransferOfAssets.class);
	
	public void execute() {
		log.info("人员异动资产汇总执行开始...");
		
		String del_sql = "delete from uf_ryydgdzcb";
		RecordSet del_rs = new RecordSet();
		del_rs.execute(del_sql);
		
		String sql = "select id,requestId,glzz,zczt,szkf,sybm,syr,syrgh,zclx,zclxbm,"
				+ "zcmc,zcbm,gg,xh,cfdd,ly,kssyrq,zcczrq,gys,cgdw,zjqs,czl,yzjl,cgjehs,"
				+ "zcyzbhs,yzjje,yzjqsy,zcljzj,zcjz,yzcbm,lyrq,bz,glzzbm,zjsfwc,sydwzcgly,"
				+ "sydwqtry,glzzzggly,glzzjggly,glzzqtry,kfgly,kfqtry from uf_zctz where zczt = 1";
		String ydzj_modelid = Prop.getPropValue("AssetsModelId", "ryyd_modelid");
		List<UfZctz> assetList = AssetUtil.getAssetList(sql);
		RecordSet user_rs = new RecordSet();
		RecordSet yjzz_rs = new RecordSet();
//		RecordSet sybm_rs = new RecordSet();
		RecordSet inser_rs = new RecordSet();
//		RecordSet sybm_id_rs = new RecordSet();
		for (UfZctz ufZctz : assetList) {
			if(ufZctz.getSyr() == 0 ){
				continue;
			}
			log.info("->->->->->->->->->->->->->->->->->->->->->->->");
			// 获取使用人当前部门
			String user_sql = "select departmentid from HrmResource where id = " + ufZctz.getSyr();
			log.info("user_sql ->" + user_sql);
			user_rs.execute(user_sql);
			user_rs.next();
			Integer deptId = Util.getIntValue(user_rs.getString("departmentid"));
			
			// 获取此部门的一级组织编码
			String yjzz_sql = "select yjzz from HrmDepartmentDefined where deptid = " + deptId;
			log.info("yjzz_sql ->" + yjzz_sql);
			yjzz_rs.execute(yjzz_sql);
			yjzz_rs.next();
			String yjzz = Util.null2String(yjzz_rs.getString("yjzz"));
			
			// 获取使用部门编码
//			String sybm = ufZctz.getSybm();
//			String sybm_sql = "select dwbm from uf_yjzz where id = " + sybm;
//			log.info("sybm_sql ->" + sybm_sql);
//			sybm_rs.execute(sybm_sql);
//			sybm_rs.next();
//			String dwbm = Util.null2String(sybm_rs.getString("yjzz"));
			
			// 如果使用人当前的一级组织和使用单位编码不一致，则写入建模表中
			if(!ufZctz.getSybm().equals(yjzz)){
//				String sybm_id_sql = "select id from uf_yjzz where dwbm = " + yjzz;
//				log.info("sybm_id_sql ->" + sybm_id_sql);
//				sybm_id_rs.execute(sybm_id_sql);
//				sybm_id_rs.next(); 
//				Integer sybm_id = Util.getIntValue(sybm_id_rs.getString("id"));
				String insert_sql = "insert into uf_ryydgdzcb(zclx,zcmc,zcbm,syr,zcsybm,syrbm,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime)" + 
						"values('"+ufZctz.getZclx() + "','" + ufZctz.getId() + "','" + ufZctz.getZcbm()+ "','" + ufZctz.getSyr() + "','" + ufZctz.getSybm() + "','" + yjzz + "',"+ydzj_modelid+",1,0,'" + DateUtils.getNowStr("yyyy-MM-dd") + "','" + DateUtils.getNowStr("HH:mm:dd") + "')";
				log.info("人员异动资产汇总sql -> " + insert_sql);
				inser_rs.execute(insert_sql); 
			}
		}
		log.info("人员异动资产汇总执行结束...");
	}
	
}
