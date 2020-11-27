<%@page import="weaver.file.Prop"%>
<%@page import="weaver.general.Util"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="weaver.conn.RecordSet"%>
<%@page import="weaver.hrm.User"%>
<%@page import="weaver.hrm.HrmUserVarify"%>
<%@page import="weaver.general.BaseBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	BaseBean bb = new BaseBean();

	bb.writeLog("更新资产盘点明细表的盘点状态...开始");
	User user = HrmUserVarify.getUser(request, response);
	bb.writeLog("当前登录人" + user.getUID());
	RecordSet rs = new RecordSet();
	String sql = "select id,pdzt,jhbh from uf_dwkczcmxb where ','||pdr||',' like '%,"+user.getUID()+",%'";
	bb.writeLog("UpdateStatus 明细sql : " + sql);
	List<Map<String, String>> list = new ArrayList<Map<String,String>>();
	rs.execute(sql);
	while(rs.next()) {
		Map<String, String> map = new HashMap<String, String>();
		String id = Util.null2String(rs.getString("id"));// 主键id
		String pdzt = Util.null2String(rs.getString("pdzt"));// 盘点状态
		String jhbh = Util.null2String(rs.getString("jhbh"));// 盘点状态
		map.put("id", id);
		map.put("pdzt", pdzt);
		map.put("jhbh", jhbh);
		bb.writeLog("UpdateStatus jhbhn : " + jhbh);
		list.add(map);
	}
	bb.writeLog("UpdateStatus size : " + list.size());
	RecordSet u_rs = new RecordSet();
	RecordSet u_jh = new RecordSet();
	RecordSet u_rw = new RecordSet();
	RecordSet u_jhid = new RecordSet();
	for (Map<String, String> map : list) {
		String id = map.get("id");
		String pdzt = map.get("pdzt");
		String u_sql = "";
		if("0".equals(pdzt)) {
			u_sql = "update uf_dwkczcmxb set pdzt = 3,sdbs = 1 where id = '" + id + "'";
		}else {
			u_sql = "update uf_dwkczcmxb set sdbs = 1 where id = '" + id + "'";
		}
		String jh_sql = "update uf_pdjh set pdzt = 1 where id = '" + map.get("jhbh") + "'";
		bb.writeLog("UpdateStatus jh更新sql : " + jh_sql);
		
		String jhid_sql = "select pdrwbh from  uf_pdjh where id = '" + map.get("jhbh") + "'";
		u_jhid.executeQuery(jhid_sql);
		u_jhid.next();
		String pdrwbh = Util.null2String(u_jhid.getString("pdrwbh"));
		
		String rw_sql = "update uf_pdrw_dt1 set pdzt = 1 where pdrwbh = '" + pdrwbh + "'";
		bb.writeLog("UpdateStatus rw更新sql : " + rw_sql);
		bb.writeLog("UpdateStatus 更新sql : " + u_sql);
		u_jh.executeUpdate(jh_sql);
		u_rw.executeUpdate(rw_sql);
		u_rs.executeUpdate(u_sql);
	}
	String url = Prop.getPropValue("AssetsInventory", "assets_updatestatus");
	response.sendRedirect(url);
%>