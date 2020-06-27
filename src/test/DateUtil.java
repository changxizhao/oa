package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static void main(String[] args) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM"); 
		Date parse = sdf.parse("2020-12");
		calendar.setTime(parse);
		int month = calendar.get(Calendar.YEAR);
		System.out.println(month);
	}

}
