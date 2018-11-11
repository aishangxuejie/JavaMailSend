package com.aishangxuejie.javamail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aishangxuejie.connection.JdbcConnection;

/**
 * 
 * @author Aishangxuejie
 * @date 2018年11月12日00:22:54
 * 
 */
public class CheckErrorService2 {
	/** ylfsh系统校验sql */
	private static String CHECK_DATA = "select count(1) c from lka4 ";
	
	/** Timer1周期 1000*60*10 =10分钟 */
	public static int PERIOD1 = 60000;
	
	/** Timer2周期 1000*60*30 =30分钟 */
	static int PERIOD2 = 18000;
	
	/** 规则条数 */
	static int SIZE = 10;
	
	/** 判断条件 */
	static int NUM = 0;
	
	/** 问题数据*/
	static int COUNT = 1;
	
	/** 上次未审核条数:10 */
	static int COUNT_NUM = 10;

	/** 执行次数:1 */
	static int NUMBER = 1;

	/** 发件人显示名称 */
	static String PERSONAL = "Aishangxuejie";
	
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
//		refreshDate();
//		testOracleConn();
//		try {
//			checkError();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	/**
	 * @author Aishangxuejie
	 * @Description 检测数据库异常判断是否发送邮件
	 */
	public static void checkError() throws InterruptedException, ParseException {
		String btn = "----------------- 邮件服务.第" + NUMBER + "次循环-----------------";
		System.out.println(btn);
		testOracleConn();
		testMysqlConn();
		try {
			if (STATUS) {
				System.out.println("任务启动时间：" + LocalDate.now());
				Connection oracleConn = JdbcConnection.getOracleConnection();
				Statement oraclestmt = oracleConn.createStatement();
				ResultSet oracleset = oraclestmt.executeQuery(CHECK_DATA);
				Integer count = 0;
				while (oracleset.next()) {
					count = Integer.valueOf(oracleset.getString("c"));
				}
				JdbcConnection.release(oracleConn, oraclestmt, oracleset); 
				if (count != null && count > COUNT_NUM) {
					if (count > SIZE) {
						sendMail();
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
				String over = "爱上学姐邮件服务器于 " + new Date() + " 发送提醒邮件过多，" + "今天自动强制停止邮件服务！请及时处理问题！";
				System.out.println(over);
				if (STATUS) {
					SendMailForErrorService.sendMain(over, "邮件提醒程序", "今天已自动停止邮件提醒服务，请优先处理");
					STATUS = false;
				}
			}
		} else if (period.getDays() >= 0) {
			refreshDate();
		}
		NUMBER++;
	}
	

	/**
	 * @author Aishangxuejie
	 * @Description sendMail 发送邮件
	 */
	private static void sendMail() {
		PERSONAL = "爱上学姐Java_Mail测试";
		SUBJECT = "aishagnxuejie检测异常，请及时注意数据情况！";
		COUNT++;
		try {
			List<Object[]> listobj = new ArrayList<Object[]>();
			Object[] object = {"1","源码 GitHub:https://github.com/aishangxuejie/JavaMailSend.git","2018年11月11日","异常",};
			listobj.add(object);
			//这里可以查询错误日志表数据
			StringBuffer errMess = new StringBuffer();
			errMess.append("<br>");
			errMess.append("<h2>异常数据列表：</h2>");
			errMess.append("<table border=\"1\"><tr><td>日志ID</td><td>错误原因</td><td>插入时间</td><td>状态</td></tr>");
			for (Object[] obj : listobj) {
				errMess.append("<tr><td>").append(obj[0].toString()).append("</td>");
				errMess.append("<td>").append(obj[1].toString()).append("</td>");
				errMess.append("<td>").append(obj[2].toString()).append("</td>");
				errMess.append("<td>").append(obj[3].toString()).append("</td></tr>");
			}
			errMess.append("</table>");
			errMess.append("<br>");
			String sMess = "【aishangxuejie_java_mail 发现程序异常数据：" + COUNT + "条<br><br>!!=_= —> 第" + NUMBER + "次发送邮件！<br>";
			System.out.println(sMess);
			SIZE = COUNT + NUM / 2;
			Boolean bl = SendMailForErrorService.sendMain(sMess + errMess.toString(), PERSONAL, SUBJECT);
			if (bl) {
				System.out.println("!=_= —>报告首长, 今天发送邮件提醒：" + NUMBER + "次。源码GitHub:https://github.com/aishangxuejie/JavaMailSend.git");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @author Aishangxuejie
	 * @Description 重置refreshdate
	 */
	public static void refreshDate() {
		// 昨天 今天 明天
		STATUS = true;
		REFRESH_DATE = LocalDate.now().plusDays(1);// 第二天
		System.out.println("重置已发送邮件次数0,重置 REFRESH_DATE：" + REFRESH_DATE);
	}
	/**
	 * @author Aishangxuejie
	 * @Description 测试MySQL连接
	 */
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
			JdbcConnection.release(conn, mysqlstmt, mysqlset);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * @author Aishangxuejie
	 * @Description 测试Oracle连接
	 */
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
			JdbcConnection.release(conn1, oraclestmt, oracleset);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
}