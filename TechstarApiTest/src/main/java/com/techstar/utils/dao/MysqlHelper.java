package com.techstar.utils.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlHelper {
	private static Connection conn;
	private static PreparedStatement ps;
	private static ResultSet rs;
	private static String mysqlDriver;
	private static String mysqlUrl;
	private static String mysqlUserName;
	private static String mysqlPassword;
	
	/**
	 * 初始化数据库连接信息
	 * @param dbName
	 */
	public static void initConnectionInfo(String dbName) {
		String[] dbInfoList = DBCont.getDBInfo(dbName);
		mysqlDriver = dbInfoList[3];
		mysqlUrl = dbInfoList[0];
		mysqlUserName = dbInfoList[1];
		mysqlPassword = dbInfoList[2];
	}
	
	/**
	 * 连接数据库
	 * @return
	 */
	public static Connection getConnection() {
		try {
			Class.forName(mysqlDriver);
		} catch(ClassNotFoundException e) {
			System.out.println(mysqlDriver+"加载失败");
		}
		try {
			conn = DriverManager.getConnection(mysqlUrl, mysqlUserName, mysqlPassword);
			System.out.println(mysqlUrl+"连接成功");
		} catch(SQLException e) {
			System.out.println(mysqlUrl+"连接失败");
			System.out.println(e.fillInStackTrace());
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
			conn = MysqlHelper.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch(Exception e) {
			e.printStackTrace();
		}
		MysqlHelper.closeConnection();
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
			conn = MysqlHelper.getConnection();
			ps = conn.prepareStatement(sql);
			insertCount = ps.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		MysqlHelper.closeConnection();
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
			conn = MysqlHelper.getConnection();
			ps = conn.prepareStatement(sql);
			deleteCount = ps.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		MysqlHelper.closeConnection();
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
			conn = MysqlHelper.getConnection();
			ps = conn.prepareStatement(sql);
			modifyCount = ps.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		MysqlHelper.closeConnection();
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
