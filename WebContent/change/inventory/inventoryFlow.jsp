<%@page import="weaver.general.BaseBean"%>
<%@page import="weaver.general.Util"%>
<%@page import="weaver.conn.RecordSet"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.change.cronjob.utils.DateUtils"%>
<%@page import="com.change.workflow.action.inventory.util.FlowUtil"%>
<%@page import="weaver.file.Prop"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	BaseBean bb = new BaseBean();
	bb.writeLog("创建流程开始");
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
	String title = "盘点资产异动审批流程" + DateUtils.getNowStr("yyyy-MM-dd HH:mm:ss");
	String requestid = flow.createFlow(workflowId, createId, title, "0", paramKey, paramVal, detailKey, detailVal, "1");
	bb.writeLog("创建流程：" + requestid);
	String url = Prop.getPropValue("AssetsInventory", "flow");
	response.sendRedirect(url);

%>