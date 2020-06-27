<%@page import="java.text.ParseException"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="weaver.general.BaseBean"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="weaver.general.Util"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="weaver.conn.RecordSet"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%-- <%@ include file="/systeminfo/init_wev8.jsp"%> --%>
<%
	Integer limit = Integer.parseInt(request.getParameter("limit"));
	Integer offset = Integer.parseInt(request.getParameter("offset"));
	String pageSize = request.getParameter("page");
	String cznyp = request.getParameter("czny");
	String sybm = request.getParameter("sybm");
	
	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); 
	// 如果没有操作年月，则获取当前的年份
	String sql_where = "";
	if(cznyp != null && !"".equals(cznyp) && !"null".equals(cznyp)){
		//int year = getYear(calendar,sdf,cznyp);
		sql_where += " and czny like '"+cznyp+"%'";
	}else {
		int year = getNowYear(calendar,sdf);
		sql_where += " and czny like '"+year+"%'";
	}
	
	new BaseBean().writeLog("参数 limit= " + limit + ", offset = " + offset + ", page = " + pageSize);
	new BaseBean().writeLog("参数 czny= " + cznyp + ", sybm = " + sybm);
	
	
	
	List<Map<String,Object>> yjzzList = new ArrayList<Map<String,Object>>();
	RecordSet yjzz_rs = new RecordSet();
	// 如果使用部门存在值
	// sql server
	//String yjzz_sql = "select distinct convert(int,convert(varchar(20),sybm)) as sybm,yjzz from uf_ydzczjb zj,uf_yjzz zz where convert(int,convert(varchar(20),sybm)) = zz.id " + sql_where ;
	// oracle
	String yjzz_sql = "select distinct cast(sybm as int) as sybm,yjzz from uf_ydzczjb zj,uf_yjzz zz where cast(sybm as int) = zz.id " + sql_where ;
	if(sybm != null && !"".equals(sybm) && !"null".equals(sybm)){
		yjzz_sql += " and zz.id in(" + sybm + ")";
	}
	new BaseBean().writeLog("yjzz_sql = " + yjzz_sql);
	yjzz_rs.execute(yjzz_sql);
	while(yjzz_rs.next()){
		Map<String,Object> m = new HashMap<String,Object>();
		int id = Util.getIntValue(yjzz_rs.getString("sybm"));
		String yjzz = Util.null2String(yjzz_rs.getString("yjzz"));
		m.put("id", id);
		m.put("yjzz", yjzz);
		yjzzList.add(m);
	}
	new BaseBean().writeLog("yjzz_list = " + JSONObject.toJSONString(yjzzList));
	RecordSet rs = new RecordSet();
	for(Map map : yjzzList){
		// sql server
		//String sql = "select convert(char(7),czny) as czny,sum(yzjje) as zje from uf_ydzczjb where convert(int,convert(varchar(20),sybm)) = "+map.get("id") + sql_where + " group by convert(char(7),czny)";
		// oracle
		String sql = "select substr(czny, 0, 7) as czny,sum(yzjje) as zje from uf_ydzczjb where cast(sybm as int) = "+map.get("id") + sql_where + " group by substr(czny, 0, 7)";
		
		new BaseBean().writeLog("sql = " + sql);
		rs.execute(sql);
		while(rs.next()){
			String czny = Util.null2String(rs.getString("czny"));
			float zje = Util.getFloatValue(rs.getString("zje"));
			String month = getMonthName(calendar, sdf, czny);
			map.put(month, zje);
		}
	}
	new BaseBean().writeLog("yjzz_list = " + JSONObject.toJSONString(yjzzList));
	
	JSONObject result = new JSONObject();

	result.put("total",yjzzList.size());//返回列表总条数
	int end = offset+limit;
	if(end - yjzzList.size() > 0){
		end = yjzzList.size();
	}
	result.put("rows",yjzzList.subList(offset, end));//返回列表数据
	
	response.setCharacterEncoding("utf-8");
	response.setContentType("application/json; charset=utf-8");
	PrintWriter writer = response.getWriter();
	writer.write(result.toJSONString());

%>
<%!
public String getMonthName(Calendar calendar,SimpleDateFormat sdf,String ny){
	Date parse;
	try{
		parse = sdf.parse(ny);
	}catch(ParseException e) {
		parse = new Date();
	}
	calendar.setTime(parse);
	int month = calendar.get(Calendar.MONTH) + 1;
	return "month"+month;
}
%>

<%!
public int getYear(Calendar calendar,SimpleDateFormat sdf,String ny){
	Date parse;
	try{
		parse = sdf.parse(ny);
	}catch(ParseException e) {
		parse = new Date();
	}
	calendar.setTime(parse);
	int year = calendar.get(Calendar.YEAR);
	return year;
}
%>

<%!
public int getNowYear(Calendar calendar,SimpleDateFormat sdf){
	Date parse = new Date();
	calendar.setTime(parse);
	int year = calendar.get(Calendar.YEAR);
	return year;
}
%>