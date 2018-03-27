package com.citylib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class dbConnection {

	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://localhost/cityLib";
	public Connection conn = null;
	public Statement stmt = null;
	public PreparedStatement prepstmt = null;

	public dbConnection() throws ClassNotFoundException, SQLException
	{
		connectToDatabase("", "");
	}

	public boolean connectToDatabase(String username, String password) throws SQLException, ClassNotFoundException
	{

	      //STEP 2: Register JDBC driver
	      Class.forName(JDBC_DRIVER);

	      //STEP 3: Open a connection
	      System.out.println("Connecting to database...");
	      conn = DriverManager.getConnection(DB_URL,username,password);

	      return true;
	}

	public ResultSet executeSQL(String sql)
	{
		ResultSet result = null;
	    try
	    {
			stmt = conn.createStatement();
		    result = stmt.executeQuery(sql);
		}
	    catch (SQLException e)
	    {
			e.printStackTrace();
		}

	    return result;
	}

	public void executeUpdate(String sql)
	{
	    try
	    {
			stmt = conn.createStatement();
		    stmt.executeUpdate(sql);
		}
	    catch (SQLException e)
	    {
			e.printStackTrace();
		}
	}

	public PreparedStatement prepareStatement(String sql)
	{
		try
		{
			prepstmt = conn.prepareStatement(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return prepstmt;
	}
}
