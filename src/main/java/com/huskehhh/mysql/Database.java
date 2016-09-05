package com.huskehhh.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abstract Database class, serves as a base for any connection method (MySQL,
 * SQLite, etc.)
 * 
 * @author -_Husky_-
 * @author tips48
 */
public abstract class Database {

	protected Connection connection;

	/**
	 * Creates a new Database
	 *
	 */
	protected Database() {
		this.connection = null;
	}

	/**
	 * Opens a connection with the database
	 * 
	 * @return Opened connection
	 * @throws SQLException
	 *             if the connection can not be opened
	 * @throws ClassNotFoundException
	 *             if the driver cannot be found
	 */
	public abstract Connection openConnection() throws SQLException,
			ClassNotFoundException;

	/**
	 * Checks if a connection is open with the database
	 * 
	 * @return true if the connection is open
	 * @throws SQLException
	 *             if the connection cannot be checked
	 */
	public boolean checkConnection() throws SQLException {
		return connection != null && !connection.isClosed();
	}

	/**
	 * Gets the connection with the database
	 * 
	 * @return Connection with the database, null if none
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Closes the connection with the database
	 * 
	 * @return true if successful
	 * @throws SQLException
	 *             if the connection cannot be closed
	 */
	public boolean closeConnection() throws SQLException {
		if (connection == null) {
			return false;
		}
		connection.close();
		return true;
	}


	/**
	 * Executes a SQL Query<br>
	 * 
	 * If the connection is closed, it will be opened
	 * 
	 * @param query
	 *            Query to be run
	 * @return the results of the query
	 * @throws SQLException
	 *             If the query cannot be executed
	 * @throws ClassNotFoundException
	 *             If the driver cannot be found; see {@link #openConnection()}
	 */
	public ResultSet querySQL(String query) throws SQLException,
			ClassNotFoundException {
		if (!checkConnection()) {
			openConnection();
		}

		Statement statement = connection.createStatement();

		ResultSet result = statement.executeQuery(query);

		return result;
	}

	/**
	 * Executes an Update SQL Query<br>
	 * See {@link java.sql.Statement#executeUpdate(String)}<br>
	 * If the connection is closed, it will be opened
	 * 
	 * @param query
	 *            Query to be run
	 * @return Result Code, see {@link java.sql.Statement#executeUpdate(String)}
	 * @throws SQLException
	 *             If the query cannot be executed
	 * @throws ClassNotFoundException
	 *             If the driver cannot be found; see {@link #openConnection()}
	 */
	public int updateSQL(String query) throws SQLException,
			ClassNotFoundException {
		if (!checkConnection()) {
			openConnection();
		}

		Statement statement = connection.createStatement();

		int result = statement.executeUpdate(query);

		return result;
	}
	
	
	/* 
	 * Namikon's convenient-addons. Yes, i'm lazy
	 * 
	 */
	public int getSimpleINT(String pQuery)
	{
		return getSimpleINT(pQuery, 0);
	}
	
	public int getSimpleINT(String pQuery, int pDefaultValue)
	{
		int tRet = pDefaultValue;
		try
		{
			ResultSet tRes = querySQL(pQuery);
			if (tRes != null)
			{
				tRes.next();
				tRet = tRes.getInt(0);
			}
		}
		catch (Exception e)
		{
			tRet = -99;
		}
		
		return tRet;
	}

	public long getSimpleLONG(String pQuery)
	{
		return getSimpleLONG(pQuery, 0);
	}
	
	public long getSimpleLONG(String pQuery, long pDefaultValue)
	{
		long tRet = pDefaultValue;
		try
		{
			ResultSet tRes = querySQL(pQuery);
			if (tRes != null)
			{
				tRes.next();
				tRet = tRes.getLong(0);
			}			
		}
		catch (Exception e)
		{
			tRet = -99;
		}
		
		return tRet;
	}
	
	public String getSimpleSTRING(String pQuery)
	{
		return getSimpleSTRING(pQuery, "");
	}
	
	public String getSimpleSTRING(String pQuery, String pDefaultValue)
	{
		String tRet = pDefaultValue;
		try
		{
			ResultSet tRes = querySQL(pQuery);
			if (tRes != null)
			{
				tRes.next();
				tRet = tRes.getString(0);
			}			
		}
		catch (Exception e)
		{
			tRet = "ERROR";
		}
		
		return tRet;
	}
	
	
}