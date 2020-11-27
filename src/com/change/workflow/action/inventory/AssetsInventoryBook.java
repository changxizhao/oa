package com.change.workflow.action.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.change.cronjob.utils.DateUtils;
import com.change.workflow.action.inventory.entities.AssetInfo;

import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 开发点2 盘点资产明细表 第二节点接口
 * @author changxizhao
 */
public class AssetsInventoryBook extends BaseAction {
	
	private static BaseBean bb = new BaseBean();

	@Override
	public String execute(RequestInfo request) {
		
		String tableName = request.getRequestManager().getBillTableName();
		String requestid = request.getRequestid();
		
		String sql = "select id,pdksrqn,pdjsrqn,pddwn,pdkfn,jhbh from " + tableName + " where requestid = " + request.getRequestid();
		RecordSet rs = new RecordSet();
		rs.execute(sql);
		rs.next();
		String jhbh = Util.null2String(rs.getString("jhbh"));// 盘点库房
		AssetThread assetThread = new AssetThread(tableName, requestid, jhbh);
		assetThread.start();
		
		return Action.SUCCESS;
	}
	
	public static void doRun(String tableName, String requestid, String jhbh) {
		RecordSet id_rs = new RecordSet();
		String jhbhId_sql = "select id from uf_pdrw where jhbh = '" + jhbh + "'";
		id_rs.execute(jhbhId_sql);
		id_rs.next();
		String jhbhId = Util.null2String(id_rs.getString("id"));
		Map<String, List<AssetInfo>> assets = getassets(tableName, requestid);
		List<AssetInfo> dwAssets = assets.get("dw");
		List<AssetInfo> kfAssets = assets.get("kf");
		RecordSet rs_dw = new RecordSet();
		RecordSet rs_kf = new RecordSet();
		RecordSet rs_dw_i = new RecordSet();
		RecordSet rs_kf_i = new RecordSet();
		RecordSet pdrwbhId_rs = new RecordSet();
		String dwkczcmxb_formmodeid = Prop.getPropValue("AssetsInventory", "dwkczcmxb_formmodeid");
		String pdjhzcmx_formmodeid = Prop.getPropValue("AssetsInventory", "pdjhzcmx_formmodeid");
		String date = DateUtils.getNowStr("yyyy-MM-dd");
		String time = DateUtils.getNowStr("HH:mm:dd");
		if(dwAssets != null && dwAssets.size() > 0) {
			for (AssetInfo dw : dwAssets) {
				//资产状态、使用人、使用人工号、使用地点、资产类型、资产名称、资产编码、规格、型号、备注
				// zczt syr syrgh cfdd zclx zcmc 	zcbm gg xh bz
				String sql = "select zczt,syr,syrgh,cfdd,zclx,zcmc,zcbm,gg,xh,bz from uf_zctz where sybm = '" + dw.getPddw() + "'";
				rs_dw.execute(sql);
				while(rs_dw.next()) {
					String zczt = Util.null2String(rs_dw.getString("zczt"));
					String syr = Util.null2String(rs_dw.getString("syr"));
					String syrgh = Util.null2String(rs_dw.getString("syrgh"));
					String cfdd = Util.null2String(rs_dw.getString("cfdd"));
					String zclx = Util.null2String(rs_dw.getString("zclx"));
					String zcmc = Util.null2String(rs_dw.getString("zcmc"));
					String zcbm = Util.null2String(rs_dw.getString("zcbm"));
					String gg = Util.null2String(rs_dw.getString("gg"));
					String xh = Util.null2String(rs_dw.getString("xh"));
					String bz = Util.null2String(rs_dw.getString("bz"));
					
					String pdrwbhId_sql = "select id from uf_pdjh where pdrwbh = '" + dw.getPdrwbh() + "'";
					bb.writeLog("盘点单位: 查询盘点任务编号 sql = " + pdrwbhId_sql);
					pdrwbhId_rs.execute(pdrwbhId_sql);
					pdrwbhId_rs.next();
					String pdrwbhId = Util.null2String(pdrwbhId_rs.getString("id"));
					
					// 盘点单位、盘点库房、盘点人、盘点开始、结束日期、盘点类型、盘点任务编号（文本）、盘点任务编号浏览自定义、盘点状态默认未盘
					String m_sql = "insert into uf_dwkczcmxb(zczt,syr,syrgh,sydd,zclx,zcmc,zcbm,gg,xh,bz,sybm,pdr,pdrq,pdlx,jhbh,pdzt,pdksrq,pdjsrq,sdbs,jhbhn,"
							+ "sjsyr,sjsyrgh,sjsydw,sjsydd,"
							+ "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('" + 
							zczt + "','" + syr + "','"+ syrgh + "','"+ cfdd + "','"+ zclx + "','"+ zcmc + "','"+ zcbm
							+ "','"+ gg + "','" + xh + "','"+ bz + "','"+ dw.getPddw() + "','"+ dw.getPdrn() + "','"+ 
							dw.getKssj() + "','"+ dw.getPdlxn() + "','" + pdrwbhId + "','" + dw.getPdzt() + "','" + dw.getKssj() + "','" + dw.getJssj() +"',0,'"+jhbhId+"','"
							+ syr + "','"+syrgh+"','"+dw.getPddw()+"','"+cfdd+"','"
							+ dwkczcmxb_formmodeid + "',1,0,'"+date+"','"+time+"')";
					String x_sql = "insert into uf_pdjhzcmx(zczt,syr,syrgh,sydd,zclx,zcmc,zcbm,sybm,pdr,pdrq,pdlx,jhbh,pdksrq,pdjsrq,jhbhn,"
							+ "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('" + 
							zczt + "','" + syr + "','"+ syrgh + "','"+ cfdd + "','"+ zclx + "','"+ zcmc + "','"+ zcbm
							+ "','"+ dw.getPddw() + "','"+ dw.getPdrn() + "','"+ 
							dw.getKssj() + "','"+ dw.getPdlxn() + "','" + pdrwbhId + "','" + dw.getKssj() + "','" + dw.getJssj() + "','"+jhbhId+"','"
							+ pdjhzcmx_formmodeid + "',1,0,'"+date+"','"+time+"')";
					bb.writeLog("盘点单位m sql = " + m_sql);
					bb.writeLog("盘点单位x sql = " + x_sql);
					rs_dw_i.execute(m_sql);
					rs_dw_i.execute(x_sql);
				}
				
			}
			
		}
		
		if(kfAssets != null && kfAssets.size() > 0) {
			for (AssetInfo kf : kfAssets) {
				String sql = "select zczt,syr,syrgh,cfdd,zclx,zcmc,zcbm,gg,xh,bz from uf_zctz where szkf = '" + kf.getPdkf() + "'";
				rs_kf.execute(sql);
				while(rs_kf.next()) {
					String zczt = Util.null2String(rs_kf.getString("zczt"));
					String syr = Util.null2String(rs_kf.getString("syr"));
					String syrgh = Util.null2String(rs_kf.getString("syrgh"));
					String cfdd = Util.null2String(rs_kf.getString("cfdd"));
					String zclx = Util.null2String(rs_kf.getString("zclx"));
					String zcmc = Util.null2String(rs_kf.getString("zcmc"));
					String zcbm = Util.null2String(rs_kf.getString("zcbm"));
					String gg = Util.null2String(rs_kf.getString("gg"));
					String xh = Util.null2String(rs_kf.getString("xh"));
					String bz = Util.null2String(rs_kf.getString("bz"));
					
					String pdrwbhId_sql = "select id from uf_pdjh where pdrwbh = '" + kf.getPdrwbh() + "'";
					bb.writeLog("盘点库房: 查询盘点任务编号 sql = " + pdrwbhId_sql);
					pdrwbhId_rs.execute(pdrwbhId_sql);
					pdrwbhId_rs.next();
					String pdrwbhId = Util.null2String(pdrwbhId_rs.getString("id"));
					
					String m_sql = "insert into uf_dwkczcmxb(zczt,syr,syrgh,sydd,zclx,zcmc,zcbm,gg,xh,bz,szkf,pdr,pdrq,pdlx,jhbh,pdzt,pdksrq,pdjsrq,sdbs,jhbhn,"
							+ "sjsyr,sjsyrgh,sjsydd,sjszkf,"
							+ "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('" + 
							zczt + "','" + syr + "','"+ syrgh + "','"+ cfdd + "','"+ zclx + "','"+ zcmc + "','"+ zcbm
							+ "','"+ gg + "','" + xh + "','"+ bz + "','"+ kf.getPdkf() + "','"+ kf.getPdrn() + "','"+ 
							kf.getKssj() + "','"+ kf.getPdlxn() + "','" + pdrwbhId + "','" + kf.getPdzt() + "','" + kf.getKssj() + "','" + kf.getJssj() + "',0,'"+jhbhId+"','"
							+ syr + "','"+syrgh+"','"+cfdd+"','"+kf.getPdkf()+"','"
							+dwkczcmxb_formmodeid+ "',1,0,'"+date+"','"+time+"')";
					String x_sql = "insert into uf_pdjhzcmx(zczt,syr,syrgh,sydd,zclx,zcmc,zcbm,szkf,pdr,pdrq,pdlx,jhbh,pdksrq,pdjsrq,jhbhn,"
							+ "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime) values('" + 
							zczt + "','" + syr + "','"+ syrgh + "','"+ cfdd + "','"+ zclx + "','"+ zcmc + "','"+ zcbm
							+ "','"+ kf.getPdkf() + "','"+ kf.getPdrn() + "','"+ 
							kf.getKssj() + "','"+ kf.getPdlxn() + "','" + pdrwbhId + "','" + kf.getKssj() + "','" + kf.getJssj() +"','"+jhbhId+"','"
							+pdjhzcmx_formmodeid+ "',1,0,'"+date+"','"+time+"')";
					bb.writeLog("盘点库房m sql = " + m_sql);
					bb.writeLog("盘点库房x sql = " + m_sql);
					rs_kf_i.execute(m_sql);
					rs_kf_i.execute(x_sql);
				}
			}
		}
		
		List<Map<String, Integer>> modelList = new ArrayList<Map<String, Integer>>();
		String model_sql = "select id,modedatacreater,formmodeid from uf_dwkczcmxb where modedatacreatedate = '"+date+"' and modedatacreatetime = '"+time+"'";
		RecordSet model_rs = new RecordSet();
		model_rs.execute(model_sql);
		while(model_rs.next()) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			int billid = Util.getIntValue(model_rs.getString("id"));
			int creater = Util.getIntValue(model_rs.getString("modedatacreater"));
			int modeid = Util.getIntValue(model_rs.getString("formmodeid"));
			map.put("billid", billid);
			map.put("creater", creater);
			map.put("modeid", modeid);
			modelList.add(map);
		}
		ModeRightInfo ModeRightInfo = new ModeRightInfo();
		ModeRightInfo.setNewRight(true);
		for (Map<String, Integer> map : modelList) {
			ModeRightInfo.rebuildModeDataShareByEdit(map.get("creater"), map.get("modeid"), map.get("billid"));
		}
	}
	
	public static Map<String, List<AssetInfo>> getassets(String tableName, String requestid) {
		
		String sql = "select mainid,pddw,pdkf,pdksrq,pdjsrq,pdzt,pdlxn,pdrn,pdrwbh from " + tableName + 
				"_dt1 where mainid = (select id from " + tableName + " where requestid = " + requestid + ")";
		RecordSet rs = new RecordSet();
		Map<String, List<AssetInfo>> map = new HashMap<String, List<AssetInfo>>();
		List<AssetInfo> dwList = new ArrayList<AssetInfo>();
		List<AssetInfo> kfList = new ArrayList<AssetInfo>();
		rs.execute(sql);
		while(rs.next()) {
			AssetInfo info = new AssetInfo();
			String pddw = Util.null2String(rs.getString("pddw"));// 盘点单位
			String pdkf = Util.null2String(rs.getString("pdkf"));// 盘点库房
			String kssj = Util.null2String(rs.getString("pdksrq"));// 盘点开始时间
			String jssj = Util.null2String(rs.getString("pdjsrq"));// 盘点结束时间
			String pdzt = Util.null2String(rs.getString("pdzt"));// 盘点状态
			String pdlxn = Util.null2String(rs.getString("pdlxn"));// 盘点类型
			String pdrn = Util.null2String(rs.getString("pdrn"));// 盘点人
			String pdrwbh = Util.null2String(rs.getString("pdrwbh"));// 计划编号
			info.setPddw(pddw);
			info.setPdkf(pdkf);
			info.setKssj(kssj);
			info.setJssj(jssj);
			info.setPdzt(pdzt);
			info.setPdlxn(pdlxn);
			info.setPdrn(pdrn);
			info.setPdrwbh(pdrwbh);
			if("0".equals(pdlxn)) {
				dwList.add(info);
			}else {
				kfList.add(info);
			}
		}
		map.put("dw", dwList);
		map.put("kf", kfList);
		return map;
	}
	
	public class AssetThread extends Thread {
		
		private String tableName;
		
		private String requestid;
		
		private String jhbh;
		
        @Override
        public void run() {
        	doRun(this.tableName, this.requestid, this.jhbh);
        }

        public AssetThread(String tableName, String requestid, String jhbh) {
        	this.tableName = tableName;
        	this.requestid = requestid;
        	this.jhbh = jhbh;
        }

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public String getRequestid() {
			return requestid;
		}

		public void setRequestid(String requestid) {
			this.requestid = requestid;
		}

		public String getJhbh() {
			return jhbh;
		}

		public void setJhbh(String jhbh) {
			this.jhbh = jhbh;
		}
		
		
    }

}
