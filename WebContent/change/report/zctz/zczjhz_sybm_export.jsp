<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="weaver.general.BaseBean"%>
<%@page import="com.change.report.util.ExportUtil"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.change.report.entities.AssestExportEntity"%>
<%@page import="weaver.conn.RecordSet"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea"%>
<%@ taglib uri="/WEB-INF/tld/browser.tld" prefix="brow"%>
<%@ include file="/systeminfo/init_wev8.jsp"%>


	<%
		String cznyp = request.getParameter("czny");
		String sql_where = "";
		BaseBean log = new BaseBean();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); 
		if(cznyp != null && !"".equals(cznyp) && !"null".equals(cznyp)){
			sql_where += " and czny like '" + cznyp + "%'";
		}else {
			int year = getNowYear();
			sql_where += " and czny like '"+year+"%'";
		}
		List<AssestExportEntity> list = new ArrayList<AssestExportEntity>();
		RecordSet yjzz_rs = new RecordSet();
		// sql server
		//String yjzz_sql = "select distinct convert(int,convert(varchar(20),sybm)) as sybm,yjzz from uf_ydzczjb zj,uf_yjzz zz where convert(int,convert(varchar(20),sybm)) = zz.id " + sql_where ;
		// oracle
		String yjzz_sql = "select distinct cast(sybm as int) as sybm,yjzz from uf_ydzczjb zj,uf_yjzz zz where cast(sybm as int) = zz.id " + sql_where ;
		log.writeLog("export yjzz_sql = " +yjzz_sql);
		yjzz_rs.execute(yjzz_sql);
		while(yjzz_rs.next()){
			AssestExportEntity entity = new AssestExportEntity();
			int id = Util.getIntValue(yjzz_rs.getString("sybm"));
			String yjzz = Util.null2String(yjzz_rs.getString("yjzz"));
			entity.setId(id);
			entity.setName(yjzz);
			log.writeLog("export value = " + yjzz + "," + yjzz);
			list.add(entity);
		}
		log.writeLog("export list1 = " + JSONObject.toJSONString(list));
		
		RecordSet rs = new RecordSet();
		for(AssestExportEntity entity : list){
			// sql server
			//String sql = "select convert(char(7),czny) as czny,sum(yzjje) as zje from uf_ydzczjb where convert(int,convert(varchar(20),sybm)) = "+entity.getId() + sql_where + " group by convert(char(7),czny)";
			// oracle
			String sql = "select substr(czny, 0, 7) as czny,sum(yzjje) as zje from uf_ydzczjb where cast(sybm as int) = "+entity.getId() + sql_where + " group by substr(czny, 0, 7)";
			
			//new BaseBean().writeLog("sql = " + sql);
			rs.execute(sql);
			while(rs.next()){
				String czny = Util.null2String(rs.getString("czny"));
				float zje = Util.getFloatValue(rs.getString("zje"));
				int month = getMonthIndex(calendar, sdf, czny);
				setEntityValue(month,entity,zje);
				//map.put(month, zje);
			}
		}
		log.writeLog("export list = " + JSONObject.toJSONString(list));
		
		AssestExportEntity total = new AssestExportEntity();
		float v1 = 0F;
		float v2 = 0F;
		float v3 = 0F;
		float v4 = 0F;
		float v5 = 0F;
		float v6 = 0F;
		float v7 = 0F;
		float v8 = 0F;
		float v9 = 0F;
		float v10 = 0F;
		float v11 = 0F;
		float v12 = 0F;
		for(AssestExportEntity entity : list){
			v1 += entity.getV1();
			v2 += entity.getV2();
			v3 += entity.getV3();
			v4 += entity.getV4();
			v5 += entity.getV5();
			v6 += entity.getV6();
			v7 += entity.getV7();
			v8 += entity.getV8();
			v9 += entity.getV9();
			v10 += entity.getV10();
			v11 += entity.getV11();
			v12 += entity.getV12();
		}
		total.setId(0);
		total.setName("合计");
		total.setV1(v1);
		total.setV2(v2);
		total.setV3(v3);
		total.setV4(v4);
		total.setV5(v5);
		total.setV6(v6);
		total.setV7(v7);
		total.setV8(v8);
		total.setV9(v9);
		total.setV10(v10);
		total.setV11(v11);
		total.setV12(v12);
		
		list.add(total);
		log.writeLog("export list = " + JSONObject.toJSONString(list));
		String[] title = new String[]{"ID","使用单位","一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
		String path = ExportUtil.exportSurplusMaterialExcel(request,response,list, title, cznyp);
		
		response.sendRedirect(path);
	%>
	
<%!
public void setEntityValue(int month,AssestExportEntity entity,float zje){
	switch(month){
		case 1:
			entity.setV1(zje);
			break;
		case 2:
			entity.setV2(zje);
			break;
		case 3:
			entity.setV3(zje);
			break;
		case 4:
			entity.setV4(zje);
			break;
		case 5:
			entity.setV5(zje);
			break;
		case 6:
			entity.setV6(zje);
			break;
		case 7:
			entity.setV7(zje);
			break;
		case 8:
			entity.setV8(zje);
			break;
		case 9:
			entity.setV9(zje);
			break;
		case 10:
			entity.setV10(zje);
			break;
		case 11:
			entity.setV11(zje);
			break;
		case 12:
			entity.setV12(zje);
			break;
	
	}
}
%>
<%!
public Integer getMonthIndex(Calendar calendar,SimpleDateFormat sdf,String ny){
	Date parse;
	try{
		parse = sdf.parse(ny);
	}catch(ParseException e) {
		parse = new Date();
	}
	calendar.setTime(parse);
	return calendar.get(Calendar.MONTH) + 1;
}
%>
<%!
public int getNowYear(){
	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); 
	Date parse = new Date();
	calendar.setTime(parse);
	int year = calendar.get(Calendar.YEAR);
	return year;
}
%>