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
	String sskf = request.getParameter("sskf");
	
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
	
	new BaseBean().writeLog("库房：参数 limit= " + limit + ", offset = " + offset + ", page = " + pageSize);
	new BaseBean().writeLog("库房：参数 czny= " + cznyp + ", sskf = " + sskf);
	
	
	
	List<Map<String,Object>> sskfList = new ArrayList<Map<String,Object>>();
	RecordSet kf_rs = new RecordSet();
	// 如果使用部门存在值
	String kf_sql = "select distinct sskf,kfmc from uf_ydzczjb zj,uf_kfgl kf where zj.sskf = kf.id " + sql_where ;
	if(sskf != null && !"".equals(sskf) && !"null".equals(sskf)){
		kf_sql += " and kf.id in(" + sskf + ")";
	}
	new BaseBean().writeLog("yjzz_sql = " + kf_sql);
	kf_rs.execute(kf_sql);
	while(kf_rs.next()){
		Map<String,Object> m = new HashMap<String,Object>();
		int id = Util.getIntValue(kf_rs.getString("sskf"));
		String kfmc = Util.null2String(kf_rs.getString("kfmc"));
		m.put("id", id);
		m.put("sskf", kfmc);
		sskfList.add(m);
	}
	new BaseBean().writeLog("库房：kf_list = " + JSONObject.toJSONString(sskfList));
	RecordSet rs = new RecordSet();
	for(Map map : sskfList){
		// sql server
		//String sql = "select convert(char(7),czny) as czny,sum(yzjje) as zje from uf_ydzczjb where sskf = "+map.get("id") + sql_where + " group by convert(char(7),czny)";
		// oracle
		String sql = "select substr(czny, 0, 7) as czny,sum(yzjje) as zje from uf_ydzczjb where sskf = "+map.get("id") + sql_where + " group by substr(czny, 0, 7)";
		
		new BaseBean().writeLog("sql = " + sql);
		rs.execute(sql);
		while(rs.next()){
			String czny = Util.null2String(rs.getString("czny"));
			float zje = Util.getFloatValue(rs.getString("zje"));
			String month = getMonthName(calendar, sdf, czny);
			map.put(month, zje);
		}
	}
	new BaseBean().writeLog("库房：sskf_list = " + JSONObject.toJSONString(sskfList));
	
	JSONObject result = new JSONObject();

	result.put("total",sskfList.size());//返回列表总条数
	int end = offset+limit;
	if(end - sskfList.size() > 0){
		end = sskfList.size();
	}
	result.put("rows",sskfList.subList(offset, end));//返回列表数据
	
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