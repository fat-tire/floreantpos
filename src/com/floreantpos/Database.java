package com.floreantpos;

import org.apache.commons.lang.StringUtils;

public enum Database {
	DERBY("Derby", "jdbc:derby://<host>:<port>/<db>", "jdbc:derby://<host>:<port>/<db>;create=true", "1527", "org.apache.derby.jdbc.ClientDriver", "org.hibernate.dialect.DerbyDialect"),
	MYSQL("MySQL", "jdbc:mysql://<host>:<port>/<db>", "jdbc:mysql://<host>:<port>/<db>", "3306", "com.mysql.jdbc.Driver", "org.hibernate.dialect.MySQLDialect");

	private String providerName;
	private String jdbcUrlFormat;
	private String jdbcUrlFormatToCreateDb;
	private String defaultPort;
	private String driverClass;
	private String hibernateDialect;

	private Database(String name, String jdbcURL, String jdbcURL2CreateDb, String defaultPort, String driverClass, String hibernateDialect) {
		this.providerName = name;
		this.jdbcUrlFormat = jdbcURL;
		this.jdbcUrlFormatToCreateDb = jdbcURL2CreateDb;
		this.defaultPort = defaultPort;
		this.driverClass = driverClass;
		this.hibernateDialect = hibernateDialect;
	}

	public String getConnectString(String host, String port, String databaseName) {
		String connectionURL = jdbcUrlFormat.replace("<host>", host);

		if (StringUtils.isEmpty(port)) {
			port = defaultPort;
		}

		connectionURL = connectionURL.replace("<port>", port);
		connectionURL = connectionURL.replace("<db>", databaseName);

		return connectionURL;
	}
	
	public String getCreateDbConnectString(String host, String port, String databaseName) {
		String connectionURL = jdbcUrlFormatToCreateDb.replace("<host>", host);

		if (StringUtils.isEmpty(port)) {
			port = defaultPort;
		}

		connectionURL = connectionURL.replace("<port>", port);
		connectionURL = connectionURL.replace("<db>", databaseName);
		
		return connectionURL;
	}

	public String getProviderName() {
		return providerName;
	}

	public String getJdbcUrlFormat() {
		return jdbcUrlFormat;
	}

	public String getDefaultPort() {
		return defaultPort;
	}

	@Override
	public String toString() {
		return this.providerName;
	}

	public String getHibernateConnectionDriverClass() {
		return driverClass;
	}

	public String getHibernateDialect() {
		return hibernateDialect;
	}
	
	public static Database getByProviderName(String providerName) {
		Database[] databases = values();
		for (Database database : databases) {
			if(database.providerName.equals(providerName)) {
				return database;
			}
		}
		
		return null;
	}
}
