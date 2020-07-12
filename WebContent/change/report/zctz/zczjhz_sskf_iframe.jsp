<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title></title>
		<link href="resource/bootstrap.css" rel="stylesheet" />
		<link rel="stylesheet" href="resource/bootstrap-table.css" />
		<script src="resource/jquery-2.1.4.min.js"></script>
		<script src="resource/bootstrap.js"></script>
		<script src="resource/bootstrap-table.js"></script>
		<script src="resource/bootstrap-table-zh-CN.min.js"></script>
		<!--<script src="bootstrap-table-master/tableExport.js"></script>
		<script src="bootstrap-table-master/jquery.base64.js"></script>
		<script src="resource/bootbox.min.js"></script>-->
 		<style type="text/css">
 			.float-left {
 				float: left !important;
 			} 

 			.float-right {
				float: right !important;
 			}
 		</style>
	</head>
	<body>	  
	<%
		String czny = request.getParameter("czny");
		String sskf = request.getParameter("sskf");
	%>  
		<table data-classes="table table-hover table-no-bordered" data-align="center" id="reportTable"></table>
	</body>
	<script type="text/javascript">
		jQuery(function () {
			initTable();
    	});
		
		
		
		function initTable(){
			$('#reportTable').bootstrapTable({
	            url:"getdata_sskf.jsp",
	            method: 'get',
	            cache: false,
	            async:false,
				striped: false,//表格渐变
	            pagination: true,//分页开启
	            pageSize: 5,//数据个数
	            pageNumber:1,
	            pageList: [5, 10, 20, 50 , 100],  
	            sidePagination:'server',//页面数据个数选择
	            clickToSelect: true,
	            showFooter: true,
	            queryParams : function(params){
	            	return {
	                    offset: params.offset,      //从数据库第几条记录开始
	                    limit: params.limit,        //找多少条
	                    czny: "<%=czny %>" ,
	                    sskf: "<%=sskf %>"
	                }
	            },
				columns:
	                [
	                    {field:"sskf",title:"所在库房",align:"center",valign:"middle",sortable:"true" ,width:100,
	                    		footerFormatter: function (value) {
	                    			return '合计';
	                			}
	                	},
	                    {
	                    	field:"month1",title:"一月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0.0; 
					            for (var i in value) {
					            	if(value[i].month1 == null || value[i].month1 == "" || value[i].month1 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month1);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			},
	                    {
	                    	field:"month2",title:"二月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) {
					            	if(value[i].month2 == null || value[i].month2 == "" || value[i].month2 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month2);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			},
	                    {
	                    	field:"month3",title:"三月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) { 
					            	if(value[i].month3 == null || value[i].month3 == "" || value[i].month3 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month3);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			},
	                    {
	                    	field:"month4",title:"四月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) { 
					            	if(value[i].month4 == null || value[i].month4 == "" || value[i].month4 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month4);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			},
	                    {
	                    	field:"month5",title:"五月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) { 
					            	if(value[i].month5 == null || value[i].month5 == "" || value[i].month5 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month5);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			},
	                    {
	                    	field:"month6",title:"六月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) { 
					            	if(value[i].month6 == null || value[i].month6 == "" || value[i].month6 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month6);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			},
	                    {
	                    	field:"month7",title:"七月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) { 
					            	if(value[i].month7 == null || value[i].month7 == "" || value[i].month7 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month7);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			},
	                    {
	                    	field:"month8",title:"八月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) { 
					            	if(value[i].month8 == null || value[i].month8 == "" || value[i].month8 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month8);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			},
	                    {
	                    	field:"month9",title:"九月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) { 
					            	if(value[i].month9 == null || value[i].month9 == "" || value[i].month9 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month9);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			},
	                    {
	                    	field:"month10",title:"十月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) { 
					            	if(value[i].month10 == null || value[i].month10 == "" || value[i].month10 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month10);
					            	}
					            }
					            return count.toFixed(2);
					        }
	        			},
	                    {
	                    	field:"month11",title:"十一月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) { 
					            	if(value[i].month11 == null || value[i].month11 == "" || value[i].month11 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month11);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			},
	                    {
	                    	field:"month12",title:"十二月",align:"center",valign:"middle",sortable:"true",width:200,formatter:setDefaultValue,
	                		footerFormatter: function (value) {
					            var count = 0; 
					            for (var i in value) { 
					            	if(value[i].month12 == null || value[i].month12 == "" || value[i].month12 == undefined){
					            		count += 0;
					            	}else {
						            	count += parseFloat(value[i].month12);
					            	}
					            }
					            return count.toFixed(2); 
					        }
	        			}
	        		],
	        		onLoadSuccess : function(res) { 
	        			$('#reportTable').bootstrapTable('load',res);
	        		},
	        		onLoadError: function(){
	        			alert("fail");
	        		}
	        });
		}
		
	function queryParams(params) {
        var batchId=1;
        var param={"batchId":batchId};
        return param;
    }
	
	function setDefaultValue(value, row, index) {
		if (value) {
			return value.toFixed(2);
		} else {
			return "0.00";
	    }
		
	}
	</script>
 
</html>