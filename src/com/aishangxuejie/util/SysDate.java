package com.aishangxuejie.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * @author Administrator
 * @date 二〇一八年十一月十日
 *
 */
public class SysDate {
	/**
	 * 日期格式-yyyy-MM-dd
	 */
	public static String y_M_d = "yyyy-MM-dd";
	/**
	 * 日期格式-yyyyMMdd
	 */
	public static String yMd = "yyyyMMdd";
	/**
	 * 日期格式-yyyy-MM-dd HH:mm:ss
	 */
	public static String y_M_dHms = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 日期格式-yyyy-MM-dd HH:mm
	 */
	public static String y_M_dHm = "yyyy-MM-dd HH:mm";
	/**
	 * 日期格式-yyyyMMdd HH:mm:ss
	 */
	public static String yMdHms = "yyyyMMdd HH:mm:ss";
	/**
	 * 日期格式-yyyyMM----费款期
	 */
	public static String yM = "yyyyMM";
	/**
	 * 日期格式-yyyy-MM-dd 23:59:59
	 */
	public static String y_M_d235959 = "yyyy-MM-dd 23:59:59";

	/**
	 * String 转date
	 * 
	 * @param date
	 * @param formatType
	 * @return
	 * @throws AppException
	 */
	public static Date StringToDate(String date, String formatType) throws Exception {
		if (date == null || date.equals("")) {
			return null;
		}
		DateFormat format = new SimpleDateFormat(formatType);
		Date $_1 = new Date();
		try {
			$_1 = format.parse(date);
		} catch (ParseException e) {
			throw new Exception("日期解析出错!");
		}
		return $_1;
	}
	/**
	 * 日期格式化字符串
	 * @param date
	 * @param formatType
	 * @return
	 */
	public static String DateToString(Date date,String formatType) {
		if (date == null) {
			return null;
		}
		DateFormat format = new SimpleDateFormat(formatType);
		return format.format(date);
	}
}
