package com.techstar.utils.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

/**
 * 数据库增删改查操作
 * 
 * @author zhengyanlin
 *
 */
public class DBHelper {
	private static Logger log = Logger.getLogger(DBHelper.class);
	private static String[] dbInfoList = {};
	private static String sql = "";

	public static List<Object> daoOperate(String dbName, String sqls) {
		// 处理sql语句确定使用数据库操作类型
		List<Object> list = new ArrayList<Object>();
		sql = sqls;
		sqls = sqls.toLowerCase();
		dbInfoList = DBCont.getDBInfo(dbName);
		initDB(dbName);
		if (sqls.contains("insert")) {
			list.add(insertData());
		} else if (sqls.contains("update")) {
			list.add(updateData());
		} else if (sqls.contains("delete")) {
			list.add(deleteData());
		} else if (sqls.contains("select")) {
			list.add(selectData());
		} else {
			log.error("sql语句有误");
		}
		return list;
	}

	/**
	 * 初始化数据库连接信息
	 * 
	 * @param dbName
	 */
	private static void initDB(String dbName) {
		String dbInfo = DBCont.getDBInfo(dbName)[0];
		if (dbInfo.contains("jdbc:mysql")) {
			MysqlHelper.initConnectionInfo(dbName);
		} else if (dbInfo.contains("jdbc:postgres")) {
			PostgreSQLHelper.initConnectionInfo(dbName);
		} else if (dbInfo.contains("jdbc:oracle")) {
			OracleHelper.initConnectionInfo(dbName);
		} else {
			log.warn("该数据库类型暂未做处理");
		}
	}
	
	/**
	 * 插入数据
	 * @return
	 */
	private static int insertData() {
		int cont = 0;
		String dbInfo = dbInfoList[0].toLowerCase();
		if (dbInfo.contains("jdbc:mysql")) {
			cont = MysqlHelper.insertInfo(sql);
		} else if (dbInfo.contains("jdbc:postgres")) {
			cont = PostgreSQLHelper.insertInfo(sql);
		} else if (dbInfo.contains("jdbc:oracle")) {
			cont = OracleHelper.insertInfo(sql);
		} else {
			log.warn("该数据库类型暂未做处理");
		}
		return cont;
	}
	
	/**
	 * 更新数据
	 * @return
	 */
	private static int updateData() {
		int cont = 0;
		String dbInfo = dbInfoList[0].toLowerCase();
		if (dbInfo.contains("jdbc:mysql")) {
			cont = MysqlHelper.modifyInfo(sql);
		} else if (dbInfo.contains("jdbc:postgresql")) {
			cont = PostgreSQLHelper.modifyInfo(sql);
		} else if (dbInfo.contains("jdbc:oracle")) {
			cont = OracleHelper.modifyInfo(sql);
		} else {
			log.warn("该数据库类型暂未做处理");
		}
		return cont;
	}
	
	/**
	 * 删除数据
	 * @return
	 */
	private static int deleteData() {
		int cont = 0;
		String dbInfo = dbInfoList[0].toLowerCase();
		if (dbInfo.contains("jdbc:mysql")) {
			cont = MysqlHelper.deleteInfo(sql);
		} else if (dbInfo.contains("jdbc:postgresql")) {
			cont = PostgreSQLHelper.deleteInfo(sql);
		} else if (dbInfo.contains("jdbc:oracle")) {
			cont = OracleHelper.deleteInfo(sql);
		} else {
			log.warn("该数据库类型暂未做处理");
		}
		return cont;
	}
	
	/**
	 * 查询数据
	 * @return
	 */
	private static ResultSet selectData() {
		ResultSet rs = null;
		String dbInfo = dbInfoList[0].toLowerCase();
		if (dbInfo.contains("jdbc:mysql")) {
			rs = MysqlHelper.getAllInfo(sql);
		} else if (dbInfo.contains("jdbc:postgresql")) {
			rs = PostgreSQLHelper.getAllInfo(sql);
		} else if (dbInfo.contains("jdbc:oracle")) {
			rs = OracleHelper.getAllInfo(sql);
		} else {
			log.warn("该数据库类型暂未做处理");
		}
		return rs;
	}

	public static void closeConnection() {
		String dbInfo = dbInfoList[0].toLowerCase();
		if (dbInfo.contains("jdbc:mysql")) {
			MysqlHelper.closeConnection();
		} else if (dbInfo.contains("jdbc:postgresql")) {
			PostgreSQLHelper.closeConnection();
		} else if (dbInfo.contains("jdbc:oracle")) {
			OracleHelper.closeConnection();
		} else {
			log.warn("该数据库类型暂未做处理");
		}
	}
}
