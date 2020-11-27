package com.change.workflow.action.inventory.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCode;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 开发点5 更新资产盘点明细表的盘点状态
 * @author changxizhao
 */
//public class UpdateStatus extends BaseAction {
public class UpdateStatus extends AbstractModeExpandJavaCode {
	
	private static BaseBean bb = new BaseBean();
	
	@Override
	//public String execute(RequestInfo request) {
	public void doModeExpand(Map<String, Object> param) throws Exception {
		bb.writeLog("更新资产盘点明细表的盘点状态...开始");
		//int userId = request.getRequestManager().getUserId();
		//User user = request.getRequestManager().getUser();
		User user = (User)param.get("user");
		//int uid = user.getUID();
		bb.writeLog("当前登录人" + user.getUID());
		RecordSet rs = new RecordSet();
		String sql = "select id,pdzt,pdrwbhwb from uf_dwkczcmxb where ','||t1.pdr||',' like '%,"+user.getUID()+",%'";
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		rs.execute(sql);
		while(rs.next()) {
			Map<String, String> map = new HashMap<String, String>();
			String id = Util.null2String(rs.getString("id"));// 主键id
			String pdzt = Util.null2String(rs.getString("pdzt"));// 盘点状态
			String pdrwbhwb = Util.null2String(rs.getString("pdrwbhwb"));// 盘点状态
			map.put("id", id);
			map.put("pdzt", pdzt);
			map.put("pdrwbhwb", pdrwbhwb);
			list.add(map);
		}
		RecordSet u_rs = new RecordSet();
		for (Map<String, String> map : list) {
			String id = map.get("id");
			String pdzt = map.get("pdzt");
			String u_sql = "";
			if("0".equals(pdzt)) {
				u_sql = "update uf_dwkczcmxb set pdzt = 3,sdbs = 1 where id = " + id;
			}else {
				u_sql = "update uf_dwkczcmxb set sdbs = 1 where id = " + id;
			}
			String jh_sql = "update uf_pdjh set pdzt = 1 where jhbh = " + map.get("pdrwbhwb");
			bb.writeLog("UpdateStatus jh更新sql : " + jh_sql);
			String rw_sql = "update uf_pdrw_dt1 set pdzt = 1 where pdrwbh = " + map.get("pdrwbhwb");
			bb.writeLog("UpdateStatus rw更新sql : " + rw_sql);
			bb.writeLog("UpdateStatus 更新sql : " + u_sql);
			u_rs.execute(jh_sql);
			u_rs.execute(rw_sql);
			u_rs.execute(u_sql);
		}
		//return "1";
	}

	

}
