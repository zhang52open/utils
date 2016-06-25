package com.froad.test.order;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.BooleanUtils;
import org.apache.http.impl.cookie.DateUtils;

import com.alibaba.fastjson.JSON;
import com.froad.enums.ResultCode;
import com.froad.exceptions.FroadBusinessException;
import com.froad.logic.impl.order.OrderLogger;
import com.froad.thirdparty.util.DateUtil;
import com.froad.util.Arith;
import com.froad.util.EmptyChecker;
import com.froad.util.payment.TimeHelper;
import com.froad.util.payment.TimeHelper.TimeType;
import com.froad.util.secret.Tools;

public class MyTest {
	
	public static void dataPrecisionChecker(double number,int n,String errorMsg) throws FroadBusinessException {
		//1.请求数据校验
		String s = number + "";
		System.out.println(s);
		System.out.println(s.length());
		System.out.println(s.indexOf("."));
		if(s.contains(".")){
			int position = s.length() - (s.indexOf(".") + 1);
			System.out.println("小数位数："+position);
			if(position > n){
				throw new FroadBusinessException(ResultCode.failed.getCode(), errorMsg);
			}
		}
	}
	
	public static int calLastedTime(Date startDate,Date endDate) {
		long a = endDate.getTime();
		long b = startDate.getTime();
		int c = (int) ((a - b) / 1000);
		System.out.println(c);
		return c;
	}
	
	public void test2(){
		List<String> list = new ArrayList<String>(){};
		list.add("1");
		list.add("2");
		for(String str : list){
			if(str.equals("1")){
				list.remove(str);
			}
		}
		System.out.println(JSON.toJSON(list));
	}
	
	public void test3(){
		String str = "1234500000000000000000000000001212";
		System.out.println(Tools.rightFill(str, 32, ""));
		System.out.println(Tools.rightFill(str, 32, "").length());
	}
	
	public static void getTimeByCal(String startTime){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);
		System.out.println(DateUtil.formatDate2Str(cal.getTime(), DateUtil.C_TIME_PATTON_DEFAULT));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 1);
		System.out.println(DateUtil.formatDate2Str(cal.getTime(), DateUtil.C_TIME_PATTON_DEFAULT));
		
		int seconds = (int) ((cal.getTimeInMillis() - StingToDate(startTime,null).getTime()) / 1000);
		
		System.out.println(seconds);
	}
	
	public static double doubleToStr(double d) {
	    DecimalFormat format=new DecimalFormat("###0.00");
	    String str = "";
	    str = format.format(d);
	    double dReturn = Double.parseDouble(str);
	    return dReturn;
	  }
	
	public void test1(){
		BigDecimal a = new BigDecimal(3.33).multiply(new BigDecimal(1000)).setScale(0, BigDecimal.ROUND_UP);
		System.out.println(a.doubleValue());
		BigDecimal b = new BigDecimal(3.33).multiply(new BigDecimal(1000)).setScale(1, BigDecimal.ROUND_UP);
		System.out.println(b.doubleValue());
		double c = Arith.roundUp(Arith.mul(3.33, 1000), 0);
		System.out.println(c);
		
		long d = Arith.mul3(3000000, 1000);
		System.out.println(d);
		
	}
	
	public void testBig(){
		double aa = new BigDecimal("10000999.80").setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		System.out.println(aa);
		System.out.println(doubleToStr(Double.valueOf("10000999.80")));
		
		System.out.println(Arith.getDoubleDecimalString(Arith.div(10, 100,2)));
	}
	
	public static void timeTest(){
		MyTest test = new MyTest();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		Long time = 1435580186476L;
		Long time1 = 1431187200000L;
		Long time2 = 1432051200000L;
		System.out.println("LONG数据     "+time+"   转为时间为：    " + df.format(new Date(time)));
		System.out.println(BooleanUtils.toBoolean("1"));
		
		String str0="2015-06-17 16:22:00";
		String str1="2015-06-17 16:22:35";
		String str2="2015-06-17 16:23:01";
		String str3="2015-06-17 16:22:59";
		String str4="2015-06-17 16:23:59";
		String str5="2015-06-17 16:23:00";
		Date strDate0 = null;
		Date strDate1 = null;
		Date strDate2 = null;
		Date strDate3 = null;
		Date strDate4 = null;
		Date strDate5 = null;
		try {
			strDate0 = df.parse(str0);
			strDate1 = df.parse(str1);
			strDate2 = df.parse(str2);
			strDate3 = df.parse(str3);
			strDate4 = df.parse(str4);
			strDate5 = df.parse(str5);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("    ----------       ");
		System.out.println(str0 + "    转为Long型：       " + strDate0.getTime());
		System.out.println(str0 + "    转为时间片：       " + strDate0.getTime()/1000/60);
		System.out.println(str0 + "    转为分钟：       " + (strDate0.getTime()%(1000*60*60))/(1000*60));
		System.out.println("    ----------       ");
		System.out.println(str1 + "    转为Long型：       " + strDate1.getTime());
		System.out.println(str1 + "    转为时间片：       " + strDate1.getTime()/1000/60);
		System.out.println(str1 + "    转为分钟：       " + (strDate1.getTime()%(1000*60*60))/(1000*60));
		System.out.println("    ----------       ");
		System.out.println(str2 + "    转为Long型：       " + strDate2.getTime());
		System.out.println(str2 + "    转为分钟：       " + strDate2.getTime()/1000/60);
		System.out.println(str2 + "    转为分钟：       " + (strDate2.getTime()%(1000*60*60))/(1000*60));
		System.out.println("    ----------       ");
		System.out.println(str3 + "    转为Long型：       " + strDate3.getTime());
		System.out.println(str3 + "    转为时间片：       " + strDate3.getTime()/1000/60);
		System.out.println(str3 + "    转为分钟：       " + (strDate3.getTime()%(1000*60*60))/(1000*60));
		System.out.println("    ----------       ");
		System.out.println(str4 + "    转为Long型：       " + strDate4.getTime());
		System.out.println(str4 + "    转为时间片：       " + strDate4.getTime()/1000/60);
		System.out.println(str4 + "    转为分钟：       " + (strDate4.getTime()%(1000*60*60))/(1000*60));
		System.out.println("    ----------       ");
		System.out.println(str5 + "    转为Long型：       " + strDate5.getTime());
		System.out.println(str5 + "    转为时间片：       " + strDate5.getTime()/1000/60);
		System.out.println(str5 + "    转为分钟：       " + (strDate5.getTime()%(1000*60*60))/(1000*60));
		System.out.println("    ----------       ");
	}
	
	public static Date StingToDate(String date,String format){
		String defaultFormat = "yyyy-MM-dd HH:mm:sss";
		if(EmptyChecker.isEmpty(format)){
			format = defaultFormat;
		}
		DateFormat df = new SimpleDateFormat(format);
		Date strDate = null;
		try {
			strDate = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return strDate;
	}

	public static void main(String[] args) {
		MyTest test = new MyTest();
		/*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		Long time = 1435580186476L;
		Long time1 = 1431187200000L;
		Long time2 = 1432051200000L;
		System.out.println("LONG数据     "+time+"   转为时间为：    " + df.format(new Date(time)));
		System.out.println(BooleanUtils.toBoolean("1"));
		
		
		String str="2015-06-17 16:22:00";
		Date strDate = null;
		try {
			strDate = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(str + "    转为Long型：       " + strDate.getTime());
		
		System.out.println(Arith.round(Arith.div(190000, 1000), 1));
		System.out.println(190000/1000);
		
		
		// TODO Auto-generated method stub
		Long date = 1430230634400L;
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:sss");
		Date d = new Date(date);
		System.out.println(sd.format(d));
		
		
		Long endTime = Long.valueOf("1433570114013");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
		String endDateYYMMdd;
		endDateYYMMdd = String.valueOf(df1.format(new Date(endTime)));
		System.out.println("endDateYYMMdd:"+endDateYYMMdd);
		
		String count = "-1";
		System.out.println(Integer.valueOf(count));
		
		
		double one = 1.919;
		System.out.println(new BigDecimal(one).intValue());
		System.out.println(new BigDecimal(one).divide(new BigDecimal(1), 2, BigDecimal.ROUND_DOWN).doubleValue());
		
		
		Date dueDate = TimeHelper.parseDate("20161217105447", TimeType.yyyyMMddHHmmss);
		System.out.println("1:"+JSON.toJSONString(dueDate));
		dueDate = TimeHelper.offsetDate(dueDate, Calendar.YEAR, -1);
		System.out.println("2:"+JSON.toJSONString(dueDate));
		dueDate = TimeHelper.offsetDate(dueDate, Calendar.MONTH, 1);
		System.out.println("3:"+JSON.toJSONString(dueDate));
		if(new Date().before(dueDate)){
			System.out.println("1111");
		}
		
		try {
			dataPrecisionChecker(45.123456789,2,"只能保留2为小数");
		} catch (FroadBusinessException e) {
			e.printStackTrace();
			System.out.println(e.getMsg());
		}
		
		double userPointMoney = Arith.roundDown(Arith.div(96.86, 1), 2);
		System.out.println(userPointMoney);*/
		
//		test.timeTest();
//		calLastedTime(StingToDate("2016-03-23 23:58:20",null),StingToDate("2016-03-24 00:00:00",null));
		
//		getTimeByCal("2016-03-23 23:58:20");
		
//		test.testBig();
//		test.test1();
//		test.test2();
		test.test3();
		
	}

}
