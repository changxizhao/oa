package com.change.workflow.action.inventory;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 开发点1 资产盘点拆分 第一节点接口
 * @author changxizhao
 */
public class AssetsInventorySplitting extends BaseAction {
	
	private BaseBean bb = new BaseBean();

	@Override
	public String execute(RequestInfo request) {
		
		String tableName = request.getRequestManager().getBillTableName();
		
		String sql = "select id,pdksrqn,pdjsrqn,pddwn,pdkfn,jhbh from " + tableName + " where requestid = " + request.getRequestid();
		RecordSet rs = new RecordSet();
		rs.execute(sql);
		String id = "";// 盘点开始时间
		String kssj = "";// 盘点开始时间
		String jssj = "";// 盘点结束时间
		String pddw = "";// 盘点单位
		String pdkf = "";
		String jhbh = "";
		while (rs.next()) {
			id = Util.null2String(rs.getString("id"));// 主表id
			kssj = Util.null2String(rs.getString("pdksrqn"));// 盘点开始时间
			jssj = Util.null2String(rs.getString("pdjsrqn"));// 盘点结束时间
			pddw = Util.null2String(rs.getString("pddwn"));// 盘点单位
			pdkf = Util.null2String(rs.getString("pdkfn"));// 盘点库房
			jhbh = Util.null2String(rs.getString("jhbh"));// 盘点库房
			
		}
		RecordSet rs_dw = new RecordSet();
		RecordSet rs_kf = new RecordSet();
		RecordSet rs_mx = new RecordSet();
		if(pddw != null && !"".equals(pddw)) {
			String[] dws = pddw.split(",");
			for (String dw : dws) {
				String sql_dw = "select dx from uf_yjzz where id = " + dw; // 获取单位管理员
				rs_dw.execute(sql_dw);
				rs_dw.next();
				String dx = Util.null2String(rs_dw.getString("dx"));
				String sql_dw_mx = "insert into " + tableName + "_dt1(mainid,pddw,pdksrq,pdjsrq,pdzt,pdlxn,pdrn) values("+
				id + ",'" + dw + "','" + kssj + "','" + jssj + "',0,0,'" + dx +"')";
				bb.writeLog("盘点单位sql = " + sql_dw_mx);
				rs_mx.execute(sql_dw_mx);
			}
		}
		
		if(pdkf != null && !"".equals(pdkf)) {
			String[] kfs = pdkf.split(",");
			for (String kf : kfs) {
				String sql_kf = "select kfgly from uf_kfgl where id = " + kf; // 获取单位管理员
				rs_kf.execute(sql_kf);
				rs_kf.next();
				String kfgly = Util.null2String(rs_kf.getString("kfgly"));
				
				String sql_kf_mx = "insert into " + tableName + "_dt1(mainid,pdkf,pdksrq,pdjsrq,pdzt,pdlxn,pdrn,pdrwbh) values("+
				id + ",'" + kf + "','" + kssj + "','" + jssj + "',0,1,'" + kfgly + "','" + jhbh + "')";
				bb.writeLog("盘点库房sql = " + sql_kf_mx);
				rs_mx.execute(sql_kf_mx);
			}
		}
		
		return Action.SUCCESS;
	}

}
