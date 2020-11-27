package com.change.workflow.action.inventory.model;

import java.util.ArrayList;
import java.util.List;
import com.change.cronjob.utils.DateUtils;
import com.change.workflow.action.inventory.util.FlowUtil;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.Util;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.RequestInfo;

public class InventoryFlow extends BaseAction {

	public String execute(RequestInfo request) {
		
		String workflowId = Prop.getPropValue("AssetsInventory", "workflowid");
		String createId = Prop.getPropValue("AssetsInventory", "creator");
		String workflowTitle = Prop.getPropValue("AssetsInventory", "title");
		String dept = Prop.getPropValue("AssetsInventory", "dept");
		FlowUtil flow = new FlowUtil();
		String[] paramKey = new String[]{"sqr","sqbm","sqrq"}; // 主表字段
		String[] paramVal = new String[]{createId,dept,DateUtils.getNowStr("yyyy-MM-dd")}; // 主表字段值
		
		String[] detailKey = new String[]{"zcmc","zcbm","zclx","szkf","sydw","syr","syrgh",
				"sydd","gg","xh","sjszkf","sjsydw","sjsyr","sjsyrgh","sjsydd","jhbh","pdrwbh",
				"pdrq","pdsj"}; // 明细表字段
		List<String[]> detailVal = new ArrayList<String[]>();
		
		String sql = "select pdr,pdlx,zczt,szkf,sybm,syr,syrgh,sydd,zclx,pdrq,pdsj,zcmc,gg,zcbm,xh,sjsyr,sjsydw,sjsydd,pdzt,sjsyrgh,sjszkf,jhbhwb,pdrwbhwb,jhbh from uf_hzpdb where pdzt = 4";
		RecordSet rs = new RecordSet();
		rs.execute(sql);
		while(rs.next()) {
			String zcmc = Util.null2String(rs.getString("zcmc"));
			String zcbm = Util.null2String(rs.getString("zcbm"));
			String zclx = Util.null2String(rs.getString("zclx"));
			String szkf = Util.null2String(rs.getString("szkf"));
			String sydw = Util.null2String(rs.getString("sybm"));
			String syr = Util.null2String(rs.getString("syr"));
			String syrgh = Util.null2String(rs.getString("syrgh"));
			String sydd = Util.null2String(rs.getString("sydd"));
			String gg = Util.null2String(rs.getString("gg"));
			String xh = Util.null2String(rs.getString("xh"));
			String sjszkf = Util.null2String(rs.getString("sjszkf"));
			String sjsydw = Util.null2String(rs.getString("sjsydw"));
			String sjsyr = Util.null2String(rs.getString("sjsyr"));
			String sjsyrgh = Util.null2String(rs.getString("sjsyrgh"));
			String sjsydd = Util.null2String(rs.getString("sjsydd"));
			String jhbh = Util.null2String(rs.getString("jhbh"));
			String pdrwbh = Util.null2String(rs.getString("pdrwbhwb"));
			String pdrq = Util.null2String(rs.getString("pdrq"));
			String pdsj = Util.null2String(rs.getString("pdsj"));
			String[] vals = new String[]{zcmc,zcbm,zclx,szkf,sydw,syr,syrgh,sydd,gg,
					xh,sjszkf,sjsydw,sjsyr,sjsyrgh,sjsydd,jhbh,pdrwbh,pdrq,pdsj};
			detailVal.add(vals);
		}
		flow.createFlow(workflowId, createId, workflowTitle, "0", paramKey, paramVal, detailKey, detailVal, "1");
		
		return "1";
	}

}
