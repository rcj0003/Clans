package me.rcj0003.clans.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
	private String ip;
	private int port;
	private String database;
	private String username;
	private String password;

	public SQLHelper(String ip, int port, String database, String username, String password) {
		this.ip = ip;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public Connection openConnection() throws SQLException {
		return DriverManager.getConnection(
					"jdbc:mysql://" + ip + ":" + port + '/' + database + "?allowMultiQueries=true", username, password);
	}
}