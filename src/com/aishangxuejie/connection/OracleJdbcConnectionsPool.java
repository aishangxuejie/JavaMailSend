package com.aishangxuejie.connection;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * 
 * @author Administrator
 * @date 二〇一八年十一月十日
 *
 */
public class OracleJdbcConnectionsPool implements DataSource {
	private static LinkedList<Connection> linkedlist = new LinkedList<Connection>();
	private static String JDBC_DRIVER;
	private static String DB_URL;
	private static String USER;
	private static String PASS;
	private static int JDBC_CONNECTION_InitSize;// 最小连接数量jdbcConnectionInitSize
	private static int JDBC_MAX = 1; // 当前最大连接数量=JDBC_MAX*JDBC_InitSize
	private static String ORACLEDB_PROPERTIES = "\\resource\\oracledb.properties";
	static {
		Properties properties = new Properties();
    	String realpath = System.getProperty("user.dir");
    	String path = realpath + ORACLEDB_PROPERTIES;
    	System.out.println("配置文件路径 " + path);
		try {
			BufferedInputStream in = new BufferedInputStream (new FileInputStream(path));
			properties.load(in);
			in.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}     ///加载属性列表
		Iterator<String> it=properties.stringPropertyNames().iterator();
		while(it.hasNext()){
		    String value=it.next();
		    System.out.println(value+"="+properties.getProperty(value));
		}
		JDBC_CONNECTION_InitSize = Integer.valueOf(properties.getProperty("jdbcConnectionInitSize"));
		JDBC_DRIVER = properties.getProperty("driver");
		DB_URL = properties.getProperty("url");
		USER = properties.getProperty("user");
		PASS = properties.getProperty("pass");
//		ResourceBundle resource = ResourceBundle.getBundle("com/aishangxuejie/config/oracledb");
//		JDBC_DRIVER = resource.getString("driver");
//		DB_URL = resource.getString("url");
//		USER = resource.getString("user");
//		PASS = resource.getString("pass");
//		JDBC_CONNECTION_InitSize = Integer.parseInt(resource.getString("jdbcConnectionInitSize"));
		try {
			Class.forName(JDBC_DRIVER);

			// 创建最小连接数个数据库连接对象以备使用
			for (int i = 0; i < JDBC_CONNECTION_InitSize; i++) {
				Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				// 将创建好的数据库连接对象添加到Linkedlist集合中
				linkedlist.add(conn);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * 实现数据库连接的获取和新创建
	 */
	@Override
	public Connection getConnection() throws SQLException {
		// 如果集合中没有数据库连接对象了，且创建的数据库连接对象没有达到最大连接数量，可以再创建一组数据库连接对象以备使用
		if (linkedlist.size() == 0 && JDBC_MAX <= 5) {
			try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < JDBC_CONNECTION_InitSize; i++) {
				Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("获取到了链接" + conn);
				// 将创建好的数据库连接对象添加到Linkedlist集合中
				linkedlist.add(conn);
			}
			JDBC_MAX++;
		}
		if (linkedlist.size() > 0) {
			// 从linkedlist集合中取出一个数据库链接对象Connection使用
			Connection conn1 = linkedlist.removeFirst();
			System.out.println("Oralce--linkedlist数据库连接池大小是" + linkedlist.size());
			/*
			 * 返回一个Connection对象，并且设置Connection对象方法调用的限制，
			 * 当调用connection类对象的close()方法时会将Connection对象重新收集放入linkedlist集合中。
			 */
			return (Connection) Proxy.newProxyInstance(OracleJdbcConnectionsPool.class.getClassLoader(),
					new Class[] { Connection.class }, new InvocationHandler() {

						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							if (!method.getName().equalsIgnoreCase("close")) {
								return method.invoke(conn1, args);
							} else {
								linkedlist.add(conn1);
								System.out.println(conn1 + "对象被释放，重新放回Oralce--linkedlist集合中！");
								System.out.println("此时Oralce--Linkedlist集合中有" + linkedlist.size() + "个数据库连接对象！");
								return null;
							}
						}
					});
		} else {
			System.out.println("连接数据库失败！");
		}
		return null;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
