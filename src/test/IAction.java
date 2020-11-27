package test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.weaver.update.GetPackageTimmer;

import weaver.formmode.customjavacode.AbstractModeExpandJavaCode;
import weaver.general.BaseBean;
import weaver.hrm.User;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.RequestInfo;

public class IAction extends BaseAction {

	private static BaseBean bb = new BaseBean();
	
	private static Logger log = LoggerFactory.getLogger(IAction.class);
	
	public String execute(RequestInfo var1) {
		log.info("当前");
		
		bb.writeLog("当前登录人");
		return "1";
	}
	
	public static void main(String[] args) {
		String url = "http://192.168.3.162/formmode/search/CustomSearchBySimpleIframe.jsp?customid=842&isfromTab=1&check_con=44462&con44462_colname=jhbhn&con44462_htmltype=3&con44462_type=161&con44462_opt=1&con44462_value=14&con44462_name=__random__B745BC5639781F23F42B2E51DCFE7ABE&con44462_opt1=&con44462_value1=&field44462=14&isfromTab=1&tabid=2527&istabinline=1&mainid=0&customTreeDataId=nu";
		Map<String, String> params = getParams(url);
		String val = params.get("con44462_value");
		System.out.println(val);
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
}
