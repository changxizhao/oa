<%@page import="weaver.general.BaseBean"%>
<%@page import="com.change.workflow.action.inventory.util.FlowUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%
String retUrl = request.getHeader("Referer");
String uri = request.getRequestURI();
String url = request.getRequestURL().toString();
String addr = request.getRemoteAddr();
String bh = request.getParameter("bh");
new BaseBean().writeLog("参数addr = " + addr);
new BaseBean().writeLog("参数bh = " + bh);
new BaseBean().writeLog("参数url = " + url);
new BaseBean().writeLog("参数uri = " + uri);
new BaseBean().writeLog("参数retUrl = " + retUrl);
/* String[] key = new String[]{"cjr","bm"};
String[] val = new String[]{"1","2"};
FlowUtil flow = new FlowUtil(); 
String reqid = flow.createFlow("242", "1", "测试工具类", "1", key, val, null, null, "1");

new BaseBean().writeLog("req id = " + reqid); */

%>
