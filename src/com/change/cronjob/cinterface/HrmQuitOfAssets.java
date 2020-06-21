package com.change.cronjob.cinterface;

import java.util.List;

import com.change.cronjob.entities.UfZctz;
import com.change.cronjob.utils.AssetUtil;
import com.change.cronjob.utils.DateUtils;

import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.Util;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

public class HrmQuitOfAssets extends BaseCronJob {
	
	private Logger log = LoggerFactory.getLogger(HrmQuitOfAssets.class);
	
	public void execute() {
		log.info("��Ա��ְ�ʲ�����ִ�п�ʼ...");
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
			if(status == 5){ // �����Ա״̬Ϊ��ְ״̬
				String insert_sql = "insert into uf_rylzgdzcb(zclx,zcmc,zcbm,syr,sybm,formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime)" + 
						"values('"+ufZctz.getZclx() + "','" + ufZctz.getId() + "','" + ufZctz.getZcbm()+ "','" + ufZctz.getSyr() + "','" + ufZctz.getSybm() + "',"+rylz_modelid+",1,0,'" + DateUtils.getNowStr("yyyy-MM-dd") + "','" + DateUtils.getNowStr("HH:mm:dd") + "')";
				log.info("��Ա�춯�ʲ�����sql -> " + insert_sql);
				inser_rs.execute(insert_sql);
			}
		}
		log.info("��Ա��ְ�ʲ�����ִ�н���...");
	}
}
