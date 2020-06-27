<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="weaver.conn.RecordSet"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea"%>
<%@ taglib uri="/WEB-INF/tld/browser.tld" prefix="brow"%>
<%@ include file="/systeminfo/init_wev8.jsp"%>

<HTML>
<HEAD>
	<LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
	<link href="/js/ecology8/jNice/jNice/jNice_wev8.css" type=text/css rel=stylesheet>
	<script language=javascript src="/js/ecology8/jNice/jNice/jquery.jNice_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/weaver_wev8.js"></script>
	<link rel="stylesheet" href="/css/ecology8/request/requestTopMenu_wev8.css" type="text/css" />

<!-- 引入相关JS -->
		<style type="text/css">
 			.float-left {
 				float: left !important;
 			} 

 			.float-right {
				float: right !important;
 			}
 		</style>
</HEAD>
<body scroll="no">
	<%
		// sql server
		//String sql = "select distinct convert(char(4),czny) as czny from uf_ydzczjb order by czny desc";
		// oracle
		String sql = "select distinct substr(czny, 0, 4) as czny from uf_ydzczjb order by czny desc";
		RecordSet rs = new RecordSet();
		rs.execute(sql);
		List<String> years = new ArrayList<String>();
		while(rs.next()){
			String ny = Util.null2String(rs.getString("czny"));
			years.add(ny);
		}
		String czny = request.getParameter("czny");
		String sybm = request.getParameter("sybm");
		
		String year;
		if(czny != null && !"".equals(czny) && !"null".equals(czny)){
			year = czny;
		}else {
			year = getNowYear();
		}
		
		String iframeUrl = "zczjhz_sybm_iframe.jsp?czny=" + czny + "&sybm=" + sybm;
	%>

		<form action="zczjhz_sybm.jsp" method="post" id="weaver" name="weaver">
		<table id="topTitle" cellpadding="0" cellspacing="0">
			<tr>
				<td></td>
				<td class="rightSearchSpan" style="text-align: right; width: 500px !important">
					<input id="checkedYear" value="<%=year %>" type="hidden"/>
					<span><%=year %></span>
					<input type="button" value="导出选定年数据" class="e8_btn_top" onclick="exportCheckedYear()" /> 
					<%-- <input type="button" value="<%=SystemEnv.getHtmlLabelName(32136,user.getLanguage())%>" class="e8_btn_top" onclick="DeleteData()" /> 
					<input type="text" class="searchInput" name="flowTitle" value="<%=name %>" />
					&nbsp;&nbsp;&nbsp;  --%>
					<span id="advancedSearch" class="advancedSearch"><%=SystemEnv.getHtmlLabelName(21995,user.getLanguage())%></span>
					<span title="<%=SystemEnv.getHtmlLabelName(81804,user.getLanguage())%>" class="cornerMenu"></span>
				</td>
			</tr>
		</table>
		<div class="advancedSearchDiv" id="advancedSearchDiv">
			<wea:layout type="fourCol">
				<wea:group context="查询条件">
									
					<wea:item>使用单位</wea:item>
					<wea:item>
						<brow:browser name="sybm" viewType="0" hasBrowser="true"
							hasAdd="false"
							browserUrl='/systeminfo/BrowserMain.jsp?url=/interface/MultiCommonBrowser.jsp?type=browser.yjzz2'
							isMustInput="1" isSingle="false" hasInput="true"
							onpropertychange="setName('11799')" 
							completeUrl="/data.jsp?type=162" width="300px" browserValue=''
							browserSpanValue='' />
					</wea:item>
					
					<%-- <wea:item>操作年月</wea:item>
					<wea:item>            
					<span id="senddate">
						<input id="czny" type="date" name="czny" value="">
					</span>
					</wea:item> --%>
					
					<wea:item>操作年</wea:item>
					<wea:item>            
					<span id="opyear">
						<select name="czny">
							<option value=""></option>
							<%for(String y : years){ %>
							<option value="<%= y%>"><%= y%></option>
							<%} %>
							
						</select>
					</span>
					</wea:item>
				</wea:group>
				
				<wea:group context="">
					<wea:item type="toolbar">
						<!-- 提交、重置、取消相关的操作按钮 -->
						<input class="e8_btn_submit" type="submit" name="submit" value="<%=SystemEnv.getHtmlLabelName(527,user.getLanguage())%>" />
						<input class="e8_btn_cancel" type="button" name="reset" onclick="onReset()" value="<%=SystemEnv.getHtmlLabelName(2022,user.getLanguage())%>" />
						<input class="e8_btn_cancel" type="button" id="cancel" value="<%=SystemEnv.getHtmlLabelName(201,user.getLanguage())%>" />
					</wea:item>
				</wea:group>
			</wea:layout>
		</div>
	</form>
	
	<iframe src="<%=iframeUrl %>" frameborder="0" height="100%" width="100%;"></iframe>
	<script type="text/javascript">
		jQuery(document).ready(function () {//初始化表单查询按钮    
			$("#topTitle").topMenuTitle({searchFn:onBtnSearchClick});    
			$(".topMenuTitle td:eq(0)").html($("#tabDiv").html());    
			$("#tabDiv").remove();

		});
		
		function onBtnSearchClick(){//点击快捷搜索的放大镜调用的方法。    
			enableAllmenu();    
			var name=$("input[name='flowTitle']",parent.document).val();    
			jQuery("input[name='name']").val(name);    
			location.href="bmzjhz.jsp?temp="+Math.random()+"&name="+name;
		}
		
		function onReset() {    
			jQuery('input[name="flowTitle"]', parent.document).val('');    
			
			jQuery('#cznyspan').val('');    
			jQuery('#czny').val('');
			
			jQuery('#sybmspan').text('');
			jQuery('#sybm').val('');
		}
		
		function dosubmit(){    
			document.weaver.submit();
		}
		
		function exportCheckedYear(){
			var year = $("#checkedYear").val();
			if(year == null || year == "" || year == undefined){
				alert("请选择导出的年份");
				return false;
			}
			
			window.open("/change/report/zctz/zczjhz_sybm_export.jsp?czny=" + year)
		}
		
		function DeleteData(){    
			alert("删除数据");
		}
		
	</script>
</body>
<%!
public int getYear(String ny){
	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
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
public String getNowYear(){
	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
	Date parse = new Date();
	calendar.setTime(parse);
	int year = calendar.get(Calendar.YEAR);
	return year+"";
}
%>
</html>