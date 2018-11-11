package com.aishangxuejie.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.aishangxuejie.connection.MysqlJdbcConnectionsPool;
import com.aishangxuejie.connection.OracleJdbcConnectionsPool;
/**
 * 
 * @author Aishangxuejie
 * @date 二〇一八年十一月九日
 *
 */
public class JdbcConnection {

	/**
	 * @Field: MYSQLPOOL 数据库连接池
	 */
	private static MysqlJdbcConnectionsPool MYSQLPOOL = new MysqlJdbcConnectionsPool();
	
	/**
	 * @Field: ORACLEPOOL 数据库连接池
	 */
	private static OracleJdbcConnectionsPool ORACLEPOOL = new OracleJdbcConnectionsPool();

	/**
	 * @author Aishangxuejie
	 * @Method: getMySQLConnection
	 * @Description: 从数据库连接池中获取数据库连接对象
	 * @return Connection数据库连接对象
	 * @throws SQLException
	 */
	public static Connection getMySQLConnection() throws SQLException {
		return MYSQLPOOL.getConnection();
	}

	/**
	 * @author Aishangxuejie
	 * @Method: getOracleConnection
	 * @Description: 从数据库连接池中获取数据库连接对象
	 * @return Connection数据库连接对象
	 * @throws SQLException
	 */
	public static Connection getOracleConnection() throws SQLException {
		return ORACLEPOOL.getConnection();
	}

	/**
	 * @author Aishangxuejie
	 * @Method: release
	 * @Description: 释放资源， 释放的资源包括Connection数据库连接对象，负责执行SQL命令的Statement对象，存储查询结果的ResultSet对象
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public static void release(Connection conn, Statement st, ResultSet rs) {
		if (rs != null) {
			try {
				// 关闭存储查询结果的ResultSet对象
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (st != null) {
			try {
				// 关闭负责执行SQL命令的Statement对象
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				// 关闭Connection数据库连接对象
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
