package com.aishangxuejie.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.aishangxuejie.connection.JdbcConnection;

/**
 * 
 * jdbc测试
 * 
 * @author Aishangxuejie
 * @date 二〇一八年十一月九日
 *
 */
public class JdbcTest {

	public static void main(String[] args) throws SQLException {

		// 获得数据库连接对象
		Connection conn = JdbcConnection.getMySQLConnection();
		String mysqlstr = "SELECT SYSDATE() date FROM dual";
		Statement mysqlstmt = conn.createStatement();
		ResultSet mysqlset = mysqlstmt.executeQuery(mysqlstr);
		while (mysqlset.next()) {
			String date1 = mysqlset.getString("date");
			System.out.println("MySQL数据库时间：" + date1);
		}
		JdbcConnection.release(conn, mysqlstmt, mysqlset);

		Connection conn1 = JdbcConnection.getOracleConnection();
		String orclestr = "SELECT SYSDATE FROM dual";
		Statement oraclestmt = conn1.createStatement();
		ResultSet oracleset = oraclestmt.executeQuery(orclestr);
		while (oracleset.next()) {
			String date2 = oracleset.getString("SYSDATE");
			System.out.println("Oracle数据库时间：" + date2);
		}
		JdbcConnection.release(conn1, oraclestmt, oracleset);
	}
}
