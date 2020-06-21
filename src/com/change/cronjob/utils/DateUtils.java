package com.change.cronjob.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static Integer getDiffMonth(String begin,String end) throws ParseException {
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(DateUtils.getDateParseStr(begin, "yyyy-MM-dd"));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(DateUtils.getDateParseStr(end, "yyyy-MM-dd"));
        return getMonth(beginDate,endDate);
	}
	
	public static int getMonth(Calendar begin, Calendar end){
        int year1= begin.get(Calendar.YEAR);
        int year2 = end.get(Calendar.YEAR);
        int month1= begin.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int ret = year2 * 12 + month2 - (year1 * 12 + month1);
        return ret;
    }
	

	public static String getNowStr(String p){
		SimpleDateFormat format = new SimpleDateFormat(p);
		return format.format(new Date());
	}
	
	public static Date getDateParseStr(String date,String p) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat(p);
		return format.parse(date);
	}
	
	public static void main(String[] args) throws ParseException {
//		Integer diffMonth = DateUtils.getDiffMonth("2019-10-21", DateUtils.getNowStr("yyyy-MM-dd"));
//		System.out.println(diffMonth);
		
		float t = 33333.00F;
		float l = 0.02F;
		float totalPrice = (t*l) / 100;
		System.out.println(totalPrice);
		BigDecimal   b   =   new   BigDecimal(totalPrice); 
		float   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue(); 
		System.out.println(f1);
	}
	
	public static Integer getMonthByDate(String dateStr){
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(DateUtils.getDateParseStr(dateStr, "yyyy-MM-dd"));
			return cal.get(Calendar.MONTH) + 1;
		} catch (ParseException e) {
			return 1;
		}
	}
	
}
