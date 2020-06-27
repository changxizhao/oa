<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/systeminfo/init_wev8.jsp"%>
<%@ page import="java.util.*"%>
<HTML>
<HEAD>
	<script src="/js/tabs/jquery.tabs.extend_wev8.js"></script>
	<link type="text/css" href="/js/tabs/css/e8tabs1_wev8.css" rel="stylesheet" />
	<link rel="stylesheet" href="/css/ecology8/request/searchInput_wev8.css" type="text/css" />
	<script type="text/javascript" src="/js/ecology8/request/searchInput_wev8.js"></script>
	<link rel="stylesheet" href="/css/ecology8/request/seachBody_wev8.css" type="text/css" />
	<link rel="stylesheet" href="/css/ecology8/request/hoverBtn_wev8.css" type="text/css" />
	<script type="text/javascript" src="/js/ecology8/request/hoverBtn_wev8.js"></script>
	<script type="text/javascript" src="/js/ecology8/request/titleCommon_wev8.js"></script>
	<!-- 引入相关js脚本 -->
	<%    
		String navName = "资产折旧汇总表";//Tab组件显示的名称    
		int _fromURL = Util.getIntValue(request.getParameter("_fromURL"),0);    
		String url = "";    
		if(_fromURL==0){
			url = "zczjhz_sybm.jsp";   
			navName = "部门折旧汇总表";
		}else if(_fromURL==1){        
			url = "zczjhz_sskf.jsp";    
			navName = "库房折旧汇总表";
		}
	%>
	<script type="text/javascript">
		$(function(){    
			$('.e8_box').Tabs({ //初始化Tab组件        
				
				getLine:1, //getLine:是否需要下标指示器，默认为1，该参数不用改动
				mouldID:"<%= MouldIDConst.getID("workflow")%>", //组件显示的图标        
				iframe:"tabcontentframe",        
				staticOnLoad:true    
			});
		}); 
		
		jQuery(document).ready(function(){ 
			setTabObjName("<%=navName%>"); 
		});//设置名称
	</script>
</head>
<BODY scroll="no">
	<!-- 以下html代码为固定形式，相关样式名称不能去掉 -->
	<div class="e8_box">
		<div class="e8_boxhead">
			<div class="div_e8_xtree" id="div_e8_xtree"></div>
			<div class="e8_tablogo" id="e8_tablogo"></div>
			<div class="e8_ultab">
				<div class="e8_navtab" id="e8_navtab">
					<span id="objName"></span>
				</div>
				<div>
					<!-- ul里增加tab要显示的二级菜单名称和打开地址 -->
					<%-- <ul class="tab_menu">
						<li <%if(_fromURL==0){%> class="current" <%} %>>
							<a href="bmzjhz.jsp?_fromURL=0" target="tabcontentframe">部门折旧汇总表</a>
						</li>
						<li <%if(_fromURL==1){%> class="current" <%} %>>
							<a href="kfzjhz.jsp?_fromURL=1" target="tabcontentframe">库房折旧汇总表</a>
						</li>
						
					</ul> --%>
					<div id="rightBox" class="e8_rightBox"></div>
				</div>
			</div>
		</div>
		<div class="tab_box">
			<div>
				<!-- 页面打开的Iframe,系统默认有个 update() 方法来更新Iframe的款高度，也可以自定以方法实现 -->
				<iframe src="<%=url %>" onload="update()" id="tabcontentframe"
					name="tabcontentframe" class="flowFrame" frameborder="0"
					height="100%" width="100%;"></iframe>
			</div>
		</div>
	</div>
</body>
</html>