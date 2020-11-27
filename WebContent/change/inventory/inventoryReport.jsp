<%@page import="weaver.formmode.setup.ModeRightInfo"%>
<%@page import="weaver.general.BaseBean"%>
<%@page import="weaver.file.Prop"%>
<%@page import="com.change.cronjob.utils.DateUtils"%>
<%@page import="weaver.general.Util"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.change.workflow.action.inventory.entities.InventoryDetail"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="weaver.conn.RecordSet"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<%
	BaseBean bb = new BaseBean();
	String retUrl = request.getHeader("Referer");
	Map<String, String> params = getParams(retUrl);
	bb.writeLog("输出报告开始...");
	bb.writeLog("页面来源..."+retUrl);
	
	String jhbh_str = params.get("con"+Prop.getPropValue("AssetsInventory", "jhbh_field_id")+"_value");
	bb.writeLog("计划编号id..."+jhbh_str);
	Map<String, Collection<?>> wp = getInventoryByType(0, jhbh_str); // 未盘
	Map<String, Collection<?>> yp = getInventoryByType(1, jhbh_str); // 已盘
	
	Map<String, Collection<?>> py = getInventoryByType(2, jhbh_str); // 盘盈
	Map<String, Collection<?>> pk = getInventoryByType(3, jhbh_str); // 盘亏
	
	Map<String, Collection<?>> yd = getInventoryByType(4, jhbh_str); // 异动
	
	Set<String> pyIds = (Set<String>)py.get("id");
	Set<String> pkIds = (Set<String>)pk.get("id");
	List<InventoryDetail> wpList = (List<InventoryDetail>) wp.get("inventory");
	List<InventoryDetail> ypList = (List<InventoryDetail>) yp.get("inventory");
	List<InventoryDetail> pyList = (List<InventoryDetail>)py.get("inventory"); // 盘盈的数据
	List<InventoryDetail> pkList = (List<InventoryDetail>)pk.get("inventory"); // 盘亏的数据
	List<InventoryDetail> yd_List = (List<InventoryDetail>)yd.get("inventory");
	
	bb.writeLog("wp = " + wpList.size() + ", yp = " + ypList.size() + ", py = " + pyList.size() + ", pk = " + pkList.size() + ", ydr = " + yd_List.size());
	
	Set<String> idSet = new HashSet<String>();
	idSet.addAll(pkIds);
	idSet.addAll(pyIds);
	
	idSet.removeAll(pyIds);
	pkIds.removeAll(idSet); // pkIds此时为盘盈和盘亏的交集
	
	// 遍历盘盈的数据，剔除既盘亏又盘盈的数据
	List<InventoryDetail> ydList = new ArrayList<InventoryDetail>();
	for (int i = 0; i < pyList.size(); i++) {
		InventoryDetail detail = pyList.get(i);
		if(pkIds.contains(detail.getId())) {
			pyList.remove(i);
			ydList.add(detail);
		}
		/* if("".equals(detail.getZcbm())) {
			pyList.remove(i);
		} */
	}
	
	// 遍历盘亏的数据，剔除既盘亏又盘盈的数据
	for (int i = 0; i < pkList.size(); i++) {
		InventoryDetail detail = pkList.get(i);
		if (pkIds.contains(detail.getId())) {
			pkList.remove(i);
		}
		/* if ("".equals(detail.getZcbm())) {
			pkList.remove(i);
		} */
	}
	String date = DateUtils.getNowStr("yyyy-MM-dd");
	String time = DateUtils.getNowStr("HH:mm:dd");
	ydList.addAll(yd_List); // ydList 是最终为异动的数据
	RecordSet rs = new RecordSet();
	rs.execute("delete from uf_hzpdb");
	bb.writeLog("删除数据");
	bb.writeLog("wp = " + wpList.size() + ", yp = " + ypList.size() + ", py = " + pyList.size() + ", pk = " + pkList.size() + ", ydr = " + ydList.size());
	doWriteIntoReport(wpList, "0", date, time);
	doWriteIntoReport(ypList, "1", date, time);
	doWriteIntoReport(pyList, "2", date, time);
	doWriteIntoReport(pkList, "3", date, time);
	doWriteIntoReport(ydList, "4", date, time);
	
	// 权限重构
	List<Map<String, Integer>> modelList = new ArrayList<Map<String, Integer>>();
	String right_sql = "select id,modedatacreater,formmodeid from uf_hzpdb where modedatacreatedate = '"+date+"' and modedatacreatetime = '"+time+"'";
	RecordSet right_rs = new RecordSet();
	right_rs.execute(right_sql);
	while(right_rs.next()) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		int billid = Util.getIntValue(right_rs.getString("id"));
		int creater = Util.getIntValue(right_rs.getString("modedatacreater"));
		int modeid = Util.getIntValue(right_rs.getString("formmodeid"));
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
	
	String url = Prop.getPropValue("AssetsInventory", "report");
	response.sendRedirect(url);
%>
<%!
void doWriteIntoReport(List<InventoryDetail> inventoryDetail, String status, String date, String time) {
	BaseBean bb = new BaseBean();
	RecordSet rs = new RecordSet();
	String hzpdb_formmodeid = Prop.getPropValue("AssetsInventory", "hzpdb_formmodeid");
	for (InventoryDetail detail : inventoryDetail) {
		String sql = "insert into uf_hzpdb(pdr,pdlx,zczt,szkf,sybm,syr,syrgh,"
				+ "sydd,zclx,pdrq,pdsj,zcmc,gg,zcbm,xh,sjsyr,sjsydw,sjsydd,"
				+ "pdzt,sjsyrgh,sjszkf,jhbhwb,pdrwbhwb,jhbh,jhbhn,"
				+ "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime)"
				+ " values('"+detail.getPdr()+"','"+detail.getPdlx()+"','"+detail.getZczt()+"','"+detail.getSzkf()+"','"+detail.getSybm()+"','"+detail.getSyr()+"',"
				+ "'"+detail.getSyrgh()+"','"+detail.getSydd()+"','"+detail.getZclx()+"','"+detail.getPdrq()+"','"+detail.getPdsj()+"','"+detail.getZcmc()+"','"+detail.getGg()+"','"
				+ detail.getZcbm()+"','"+detail.getXh()+"','"+detail.getSjsyr()+"','"+detail.getSjsydw()+"','"+detail.getSjsydd()+"','"+status+"','"+detail.getSjsyrgh()+"','"
				+ detail.getSjszkf()+"','"+detail.getJhbhwb()+"','"+detail.getPdrwbhwb()+"','"+detail.getJhbh()+"','"+detail.getJhbhn()+"','"
				+ hzpdb_formmodeid+"',1,0,'"+date+"','"+time+"')";
		bb.writeLog("报告sql ：" + sql);
		rs.execute (sql);
	}
}

private Map<String, Collection<?>> getInventoryByType(int type, String jhbhStr) {
	RecordSet rs = new RecordSet();
	String sql = "select id,requestId,pdlx,sydd,pdrq,pdsj,jhbh,pdr,zczt,szkf,sybm,syr,syrgh,zclx,zcbm,zcmc,gg,xh,sjsyr," +
			"sjsydw,sjsydd,pdzt,sjsyrgh,jhbhn,sjszkf,bz,jhbhwb,pdrwbhwb,sdbs,pdksrq,pdjsrq from uf_dwkczcmxb where pdzt = " + type + " and jhbhn = '" + jhbhStr + "'";
	BaseBean bb = new BaseBean();
	bb.writeLog("获取数据sql = " + sql);
	rs.execute(sql);
	List<InventoryDetail> list = new ArrayList<InventoryDetail>();
	Set<String> idSet = new HashSet<String>();
	while(rs.next()) {
		InventoryDetail detail = new InventoryDetail();
		String id = Util.null2String(rs.getString("id"));
		String pdlx = Util.null2String(rs.getString("pdlx"));
		String sydd = Util.null2String(rs.getString("sydd"));
		String pdrq = Util.null2String(rs.getString("pdrq"));
		String pdsj = Util.null2String(rs.getString("pdsj"));
		String jhbh = Util.null2String(rs.getString("jhbh"));
		String pdr = Util.null2String(rs.getString("pdr"));
		String zczt = Util.null2String(rs.getString("zczt"));
		String szkf = Util.null2String(rs.getString("szkf"));
		String sybm = Util.null2String(rs.getString("sybm"));
		String syr = Util.null2String(rs.getString("syr"));
		String syrgh = Util.null2String(rs.getString("syrgh"));
		String zclx = Util.null2String(rs.getString("zclx"));
		String zcbm = Util.null2String(rs.getString("zcbm"));
		String zcmc = Util.null2String(rs.getString("zcmc"));
		String gg = Util.null2String(rs.getString("gg"));
		String xh = Util.null2String(rs.getString("xh"));
		String sjsyr = Util.null2String(rs.getString("sjsyr"));//
		if(isEmpty(sjsyr)) {
			sjsyr = syr;
		}
		String sjsydw = Util.null2String(rs.getString("sjsydw"));//
		if(isEmpty(sjsydw)) {
			sjsydw = sybm;
		}
		String sjsydd = Util.null2String(rs.getString("sjsydd"));//
		if(isEmpty(sjsydd)) {
			sjsydd = sydd;
		}
		String pdzt = Util.null2String(rs.getString("pdzt"));
		String sjsyrgh = Util.null2String(rs.getString("sjsyrgh"));//
		if(isEmpty(sjsyrgh)) {
			sjsyrgh = syrgh;
		}
		String jhbhn = Util.null2String(rs.getString("jhbhn"));
		String sjszkf = Util.null2String(rs.getString("sjszkf"));//
		if(isEmpty(sjszkf)) {
			sjszkf = szkf;
		}
		String bz = Util.null2String(rs.getString("bz"));
		String jhbhwb = Util.null2String(rs.getString("jhbhwb"));
		String pdrwbhwb = Util.null2String(rs.getString("pdrwbhwb"));
		String sdbs = Util.null2String(rs.getString("sdbs"));
		String pdksrq = Util.null2String(rs.getString("pdksrq"));
		String pdjsrq = Util.null2String(rs.getString("pdjsrq"));
		detail.setZczt(zczt);
		detail.setZcmc(zcmc);
		detail.setZclx(zclx);
		detail.setZcbm(zcbm);
		detail.setXh(xh);
		detail.setSzkf(sjszkf);
		detail.setSyrgh(sjsyrgh);
		detail.setSyr(syr);
		detail.setSydd(sydd);
		detail.setSybm(sybm);
		detail.setSjszkf(sjszkf);
		detail.setSjsyrgh(sjsyrgh);
		detail.setSjsyr(sjsyr);
		detail.setBz(bz);
		detail.setGg(gg);
		detail.setId(id);
		detail.setJhbh(jhbh);
		detail.setJhbhn(jhbhn);
		detail.setJhbhwb(jhbhwb);
		detail.setPdjsrq(pdjsrq);
		detail.setPdksrq(pdksrq);
		detail.setPdlx(pdlx);
		detail.setPdr(pdr);
		detail.setPdrq(pdrq);
		detail.setPdrwbhwb(pdrwbhwb);
		detail.setPdsj(pdsj);
		detail.setPdzt(pdzt);
		detail.setSdbs(sdbs);
		detail.setSjsydd(sjsydd);
		detail.setSzkf(szkf);
		detail.setSyrgh(syrgh);
		detail.setSjsydw(sjsydw);
		list.add(detail);
		idSet.add(id);
	}
	Map<String, Collection<?>> map = new HashMap<String, Collection<?>>();
	map.put("id", idSet);
	map.put("inventory", list);
	return map;
}

public static boolean isEmpty(String str) {
	if(str == null || "".equals(str)) {
		return true;
	}
	return false;
}

public static Map<String, String> getParams(String url) {
	Map<String, String> map = new HashMap<String, String>();
	String[] urlParts = url.split("\\?");
	//没有参数
	if (urlParts.length == 1) {
		return map;
	}
	//有参数
	String[] params = urlParts[1].split("&");
	for (String param : params) {
		String[] keyValue = param.split("=");
		if(keyValue.length > 1) {
			map.put(keyValue[0], keyValue[1]);
		}
	}
	return map;
}
%>