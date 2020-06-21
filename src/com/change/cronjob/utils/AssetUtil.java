package com.change.cronjob.utils;

import java.util.ArrayList;
import java.util.List;

import org.docx4j.model.datastorage.XPathEnhancerParser.main_return;

import weaver.conn.RecordSet;
import weaver.general.Util;

import com.change.cronjob.entities.UfZctz;;

/**  
 * @Author: changxizhao
 * @Date: 2020年6月20日 下午4:20:30
 */
public class AssetUtil {
	
	public static List<UfZctz> getAssetList(String sql){
		List<UfZctz> result = new ArrayList<UfZctz>();
		RecordSet rs = new RecordSet();
		rs.execute(sql);
		while(rs.next()){
			UfZctz ufZctz = new UfZctz();
			Integer id = getIntValue(rs.getString("id"));
			Integer requestId = getIntValue(rs.getString("requestId"));
			String glzz = Util.null2String(rs.getString("glzz"));
			Integer zczt = getIntValue(rs.getString("zczt"));
			String szkf = Util.null2String(rs.getString("szkf"));
			String sybm = Util.null2String(rs.getString("sybm"));
			Integer syr = getIntValue(rs.getString("syr"));
			String syrgh = Util.null2String(rs.getString("syrgh"));
			String zclx = Util.null2String(rs.getString("zclx"));
			String zclxbm = Util.null2String(rs.getString("zclxbm"));
			String zcmc = Util.null2String(rs.getString("zcmc"));
			String zcbm = Util.null2String(rs.getString("zcbm"));
			String gg = Util.null2String(rs.getString("gg"));
			String xh = Util.null2String(rs.getString("xh"));
			String cfdd = Util.null2String(rs.getString("cfdd"));
			Integer ly = getIntValue(rs.getString("ly"));
			String kssyrq = Util.null2String(rs.getString("kssyrq"));
			String zcczrq = Util.null2String(rs.getString("zcczrq"));
			String gys = Util.null2String(rs.getString("gys"));
			String cgdw = Util.null2String(rs.getString("cgdw"));
			Integer zjqs = getIntValue(rs.getString("zjqs"));
			float czl = getFloatValue(rs.getString("czl"));
			float yzjl = getFloatValue(rs.getString("yzjl"));
			float cgjehs = getFloatValue(rs.getString("cgjehs"));
			float zcyzbhs = getFloatValue(rs.getString("zcyzbhs"));
			float yzjje = getFloatValue(rs.getString("yzjje"));
			Integer yzjqsy = Util.getIntValue(rs.getString("yzjqsy"));
			float zcljzj = getFloatValue(rs.getString("zcljzj"));
			float zcjz = getFloatValue(rs.getString("zcjz"));
			String yzcbm = Util.null2String(rs.getString("yzcbm"));
			String lyrq = Util.null2String(rs.getString("lyrq"));
			String bz = Util.null2String(rs.getString("bz"));
			String glzzbm = Util.null2String(rs.getString("glzzbm"));
			Integer zjsfwc = Util.getIntValue(rs.getString("zjsfwc"));
			Integer sydwzcgly = getIntValue(rs.getString("sydwzcgly"));
			String sydwqtry = Util.null2String(rs.getString("sydwqtry"));
			String glzzzggly = Util.null2String(rs.getString("glzzzggly"));
			String glzzjggly = Util.null2String(rs.getString("glzzjggly"));
			String glzzqtry = Util.null2String(rs.getString("glzzqtry"));
			Integer kfgly = getIntValue(rs.getString("kfgly"));
			String kfqtry = Util.null2String(rs.getString("kfqtry"));
			
			ufZctz.setId(id);
			ufZctz.setRequestid(requestId);
			ufZctz.setGlzz(glzz);
			ufZctz.setZczt(zczt);
			ufZctz.setSzkf(szkf);
			ufZctz.setSybm(sybm);
			ufZctz.setSyr(syr);
			ufZctz.setSyrgh(syrgh);
			ufZctz.setZclx(zclx);
			ufZctz.setZclxbm(zclxbm);
			ufZctz.setZcmc(zcmc);
			ufZctz.setZcbm(zcbm);
			ufZctz.setGg(gg);
			ufZctz.setXh(xh);
			ufZctz.setCfdd(cfdd);
			ufZctz.setLy(ly);
			ufZctz.setKssyrq(kssyrq);
			ufZctz.setZcczrq(zcczrq);
			ufZctz.setGys(gys);
			ufZctz.setCgdw(cgdw);
			ufZctz.setZjqs(zjqs);
			ufZctz.setCzl(czl);
			ufZctz.setYzjl(yzjl);
			ufZctz.setCgjehs(cgjehs);
			ufZctz.setZcyzbhs(zcyzbhs);
			ufZctz.setYzjje(yzjje);
			ufZctz.setYzjqsy(yzjqsy);
			ufZctz.setZcljzj(zcljzj);
			ufZctz.setZcjz(zcjz);
			ufZctz.setYzcbm(yzcbm);
			ufZctz.setLyrq(lyrq);
			ufZctz.setBz(bz);
			ufZctz.setGlzzbm(glzzbm);
			ufZctz.setZjsfwc(zjsfwc);
			ufZctz.setSydwzcgly(sydwzcgly);
			ufZctz.setSydwqtry(sydwqtry);
			ufZctz.setGlzzzggly(glzzzggly);
			ufZctz.setGlzzjggly(glzzjggly);
			ufZctz.setGlzzqtry(glzzqtry);
			ufZctz.setKfgly(kfgly);
			ufZctz.setKfqtry(kfqtry);
			
			result.add(ufZctz);
		}	
		return result;
		
	}
	
	public static float getFloatValue(String var0) {
		return getFloatValue(var0, 0);
	}

	public static float getFloatValue(String var0, float var1) {
		try {
			return Float.parseFloat(var0);
		} catch (Exception var3) {
			return var1;
		}
	}
	
	public static int getIntValue(String var0) {
		return getIntValue(var0, 0);
	}

	public static int getIntValue(String var0, int var1) {
		try {
			return Integer.parseInt(var0);
		} catch (Exception var3) {
			return var1;
		}
	}

}
