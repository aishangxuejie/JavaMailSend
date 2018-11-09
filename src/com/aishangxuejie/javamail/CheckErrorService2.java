package com.aishangxuejie.javamail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;
import javax.security.auth.login.Configuration;

import com.aishangxuejie.connection.JdbcConnection;
import com.aishangxuejie.util.SysDate;

/**
 * @author Aishangxuejie
 * @date 2018年6月14日 上午8:57:15
 * @param name
 * @param pathName
 */
public class CheckErrorService2 {
	/** ylfsh系统校验sql */
	private static String CHECK_DATA = "select count(1) c from test ";
	
	/** Timer1周期 1000*60*10 =10分钟 */
	public static int PERIOD1 = 600000;
	
	/** Timer2周期 1000*60*30 =30分钟 */
	static int PERIOD2 = 1800000;
	
	/** 规则条数 */
	static int SIZE = 10;
	
	/**  */
	static int NUM = 0;
	
	/** 问题数据*/
	static int COUNT = 1;
	
	/** 上次未审核条数:10 */
	static int COUNT_NUM = 10;

	/** 执行次数:1 */
	static int NUMBER = 1;

	/** 发件人显示名称 */
	static String PERSONAL = "";
	
	/** 主题 */
	static String SUBJECT = "";

	/** 启动时记录第二天日期 */
	static LocalDate REFRESH_DATE;
	
	/** 每个程序同一天限制发送邮件次数 :5 */
	static int LIMIT_DAY = 5;
	
	static Boolean STATUS = true;

	static Boolean OVER = true;
	
	/**
	 * @author Aishangxuejie
	 * @date 2018年6月15日17:20:21
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println("--------------启动邮件提醒服务---------------");
		System.out.println("服务启动时间：" + LocalDateTime.now());
		refreshDate();
		testOracleConn();
	}
	/**
	 * 邮件发送规则
	 * @author Aishangxuejie
	 */
	public static void checkError() throws InterruptedException, ParseException {
		String btn = "----------------- 邮件服务.第" + NUMBER + "次循环-----------------";
		System.out.println(btn);
		testOracleConn();
		
		try {
			if (STATUS) {
				System.out.println("任务启动时间：" + SysDate.DateToString(new Date(), SysDate.y_M_dHm));
				Connection oracleConn = JdbcConnection.getOracleConnection();
				Statement oraclestmt = oracleConn.createStatement();
				ResultSet oracleset = oraclestmt.executeQuery(CHECK_DATA);
				Integer count = 0;
				while (oracleset.next()) {
					String date2 = oracleset.getString("SYSDATE");
					System.out.println("Oracle数据库时间：" + date2);
					count = Integer.valueOf(oracleset.getString("c"));
				}
				if (count != null && count > COUNT_NUM) {
					if (count > SIZE) {
						sendYlfshMail();
					}
				} else {
					SIZE = NUM;// 恢复参数
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// param2当前时间 - param1刷新时间
		Period period = Period.between(LocalDate.now(), REFRESH_DATE);
		if (period.getDays() == 1) {
			// 加个限制，控制一天内发送邮件数量
			if (COUNT > LIMIT_DAY) {
				String over = "邮件服务器于 " + new Date() + " 发送提醒邮件过多，" + "今天自动强制停止邮件服务！请及时处理问题！";
				System.out.println(over);
				if (STATUS) {
					SendMailForErrorService.sendMain(over, "邮件提醒程序", "今天已自动停止邮件提醒服务，请优先处理");
					STATUS = false;
				}
			}
		} else if (period.getDays() == 0) {
			refreshDate();
		}
		NUMBER++;
	}
	

	/**
	 * 发送关邮件
	 * 
	 * @param sessionGsbx
	 * @param mailSendYlfshCount
	 * @param ylfshCount
	 */
	private static void sendYlfshMail() {
		PERSONAL = "服务器";
		SUBJECT = "请检查服务器是否正常！";
		COUNT++;
		try {
			List<Object[]> listobj = new ArrayList<Object[]>();
			
			StringBuffer yerrMess = new StringBuffer();
			yerrMess.append("<br>");
			yerrMess.append("<h2>未审核数据列表：</h2>");
			yerrMess.append("<table border=\"1\"><tr><td>行政区编码</td><td>行政区</td><td>医院</td><td>count</td></tr>");
			for (Object[] obj : listobj) {
				yerrMess.append("<tr><td>").append(obj[0].toString()).append("</td>");
				yerrMess.append("<td>").append(obj[1].toString()).append("</td>");
				yerrMess.append("<td>").append(obj[2].toString()).append("</td>");
				yerrMess.append("<td>").append(obj[3].toString()).append("</td></tr>");
			}
			yerrMess.append("</table>");
			yerrMess.append("<br>");
			String ylfshMess = "【程序异常数据：" + COUNT + "条<br><br>!!=_= —> 第" + COUNT + "次发送邮件！<br>";
			System.out.println(ylfshMess);
			SIZE = COUNT + NUM / 2;
			Boolean bl = SendMailForErrorService.sendMain(ylfshMess + yerrMess.toString(), PERSONAL, SUBJECT);
			if (bl) {
				System.out.println("!!=_= —> 发送邮件次数" + NUMBER + "次。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 重置refreshdate
	 * @author Aishangxuejie
	 */
	public static void refreshDate() {
		// 昨天 今天 明天
		STATUS = true;
		REFRESH_DATE = LocalDate.now().plusDays(1);// 第二天
		System.out.println("重置已发送邮件次数0,重置 REFRESH_DATE：" + REFRESH_DATE);
	}

	public static boolean testconn() {
		try {
			Connection conn = JdbcConnection.getOracleConnection();
			String oraclestr = "SELECT SYSDATE date FROM dual";
			Statement oraclestmt = conn.createStatement();
			ResultSet oracleset = oraclestmt.executeQuery(oraclestr);
			while (oracleset.next()) {
				String date1 = oracleset.getString("date");
				System.out.println("OracleL数据库时间：" + date1);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public static boolean testMysqlConn() {
		try {
			Connection conn = JdbcConnection.getMySQLConnection();
			String mysqlstr = "SELECT SYSDATE() date FROM dual";
			Statement mysqlstmt = conn.createStatement();
			ResultSet mysqlset = mysqlstmt.executeQuery(mysqlstr);
			while (mysqlset.next()) {
				String date1 = mysqlset.getString("date");
				System.out.println("MySQL数据库时间：" + date1);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public static boolean testOracleConn() {
		 try {
			Connection conn1 = JdbcConnection.getOracleConnection();
			String orclestr = "SELECT SYSDATE FROM dual";
			Statement oraclestmt = conn1.createStatement();
			ResultSet oracleset = oraclestmt.executeQuery(orclestr);
			while (oracleset.next()) {
				String date2 = oracleset.getString("SYSDATE");
				System.out.println("Oracle数据库时间：" + date2);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
}