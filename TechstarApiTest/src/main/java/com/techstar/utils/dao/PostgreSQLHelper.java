package com.techstar.utils.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQLHelper {
	private static Connection conn;
	private static Statement ps;
	private static ResultSet rs;
	private static String pgDriver;
	private static String pgUrl;
	private static String pgUserName;
	private static String pgPassword;
	
	/**
	 * 初始化数据库连接信息
	 * @param dbName
	 */
	public static void initConnectionInfo(String dbName) {
		String[] dbInfoList = DBCont.getDBInfo(dbName);
		pgDriver = dbInfoList[3];
		pgUrl = dbInfoList[0];
		pgUserName = dbInfoList[1];
		pgPassword = dbInfoList[2];
	}
	
	/**
	 * 连接数据库
	 * @return
	 */
	public static Connection getConnection() {
		try {
			Class.forName(pgDriver);
		} catch(ClassNotFoundException e) {
			System.out.println(pgDriver+"加载失败");
		}
		try {
			conn = DriverManager.getConnection(pgUrl, pgUserName, pgPassword);
			System.out.println(pgUrl+"连接成功");
		} catch(SQLException e) {
			System.out.println(pgUrl+"连接失败");
		}
		return conn;
	}
	
	/**
	 * 查询信息
	 * @param sql
	 * @return ResultSet 
	 */
	public static ResultSet getAllInfo(String sql){
		try {
			conn = PostgreSQLHelper.getConnection();
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
		PostgreSQLHelper.closeConnection();
		return rs;
	}
	
	/**
	 * 插入信息
	 * @param sql
	 * @return int 影响数据库的行数
	 */
	public static int insertInfo(String sql) {
		int insertCount = 0;
		try {
			conn = PostgreSQLHelper.getConnection();
			ps = conn.createStatement();
			insertCount = ps.executeUpdate(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
		PostgreSQLHelper.closeConnection();
		return insertCount;
	}
	
	/**
	 * 删除信息
	 * @param sql
	 * @return int 影响数据库的行数
	 */
	public static int deleteInfo(String sql) {
		int deleteCount = 0;
		try {
			conn = PostgreSQLHelper.getConnection();
			ps = conn.createStatement();
			deleteCount = ps.executeUpdate(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
		PostgreSQLHelper.closeConnection();
		return deleteCount;
	}
	
	/**
	 * 修改信息
	 * @param sql
	 * @return int 影响数据库的行数
	 */
	public static int modifyInfo(String sql) {
		int modifyCount = 0;
		try {
			conn = PostgreSQLHelper.getConnection();
			ps = conn.createStatement();
			modifyCount = ps.executeUpdate(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
		PostgreSQLHelper.closeConnection();
		return modifyCount;
	}
	
	/**
	 * 关闭数据库连接
	 * @throw SQLException
	 */
	public static void closeConnection() {
		if(ps != null) {
			try {
				ps.close();
			} catch(SQLException e) {
				System.out.println(e.fillInStackTrace());
			}
		}
		if(rs != null) {
			try {
				rs.close();
			} catch(SQLException e) {
				System.out.println(e.fillInStackTrace());
			}
		}
		if(conn != null) {
			try {
				conn.close();
			}catch(SQLException e) {
				System.out.println(e.fillInStackTrace());
			}			
		}
	}
}
