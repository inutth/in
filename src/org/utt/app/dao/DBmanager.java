package org.utt.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.utt.app.util.Prop;

public class DBmanager {
	 static String mysql_driver = Prop.getProperty("mysql.db.driver");
	 static String mysql_url = Prop.getProperty("mysql.db.url");
	 static String mysql_url_hdc = Prop.getProperty("mysql.db.url.hdc");
	 
	 static String mssql_driver = Prop.getProperty("mssql.db.driver");
	 static String mssql_url = Prop.getProperty("mssql.db.url");

	public DBmanager(){
		
	}
	
	public static Connection getConnMySql(){
		Connection conn=null;
		try {
			Class.forName(mysql_driver);
			conn = DriverManager.getConnection(mysql_url);
		} catch (ClassNotFoundException   e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		return conn;
	}
	public static Connection getConnMySqlHDC(){
		Connection conn=null;
		try {
			Class.forName(mysql_driver);
			conn = DriverManager.getConnection(mysql_url_hdc);
		} catch (ClassNotFoundException   e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		return conn;
	}
	public static Connection getConnMSSql(){
		Connection conn=null;
		try {
			Class.forName(mssql_driver);
			conn = DriverManager.getConnection(mssql_url);
		} catch (ClassNotFoundException   e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

}
