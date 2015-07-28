package com.floreantpos;

import org.apache.commons.lang.StringUtils;

public enum Database {
	DERBY_SINGLE(Messages.getString("Database.DERBY_SINGLE"), "jdbc:derby:database/derby-single/posdb", "jdbc:derby:database/derby-single/posdb;create=true", "", "org.apache.derby.jdbc.EmbeddedDriver", "org.hibernate.dialect.DerbyDialect"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	DERBY_SERVER(Messages.getString("Database.DERBY_SERVER"), "jdbc:derby://<host>:<port>/<db>", "jdbc:derby://<host>:<port>/<db>;create=true", "51527", "org.apache.derby.jdbc.ClientDriver", "org.hibernate.dialect.DerbyDialect"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	MYSQL(Messages.getString("Database.MYSQL"), "jdbc:mysql://<host>:<port>/<db>?characterEncoding=UTF-8", "jdbc:mysql://<host>:<port>/<db>?characterEncoding=UTF-8", "3306", "com.mysql.jdbc.Driver", "org.hibernate.dialect.MySQLDialect"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	POSTGRES(Messages.getString("Database.POSTGRES"), "jdbc:postgresql://<host>:<port>/<db>", "jdbc:postgresql://<host>:<port>/<db>", "5432", "org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	
	;

	private String providerName;
	private String jdbcUrlFormat;
	private String jdbcUrlFormatToCreateDb;
	private String defaultPort;
	private String driverClass;
	private String hibernateDialect;

	private Database(String providerName, String jdbcURL, String jdbcURL2CreateDb, String defaultPort, String driverClass, String hibernateDialect) {
		this.providerName = providerName;
		this.jdbcUrlFormat = jdbcURL;
		this.jdbcUrlFormatToCreateDb = jdbcURL2CreateDb;
		this.defaultPort = defaultPort;
		this.driverClass = driverClass;
		this.hibernateDialect = hibernateDialect;
	}

	public String getConnectString(String host, String port, String databaseName) {
		String connectionURL = jdbcUrlFormat.replace("<host>", host); //$NON-NLS-1$

		if (StringUtils.isEmpty(port)) {
			port = defaultPort;
		}

		connectionURL = connectionURL.replace("<port>", port); //$NON-NLS-1$
		connectionURL = connectionURL.replace("<db>", databaseName); //$NON-NLS-1$

		return connectionURL;
	}

	public String getCreateDbConnectString(String host, String port, String databaseName) {
		String connectionURL = jdbcUrlFormatToCreateDb.replace("<host>", host); //$NON-NLS-1$

		if (StringUtils.isEmpty(port)) {
			port = defaultPort;
		}

		connectionURL = connectionURL.replace("<port>", port); //$NON-NLS-1$
		connectionURL = connectionURL.replace("<db>", databaseName); //$NON-NLS-1$

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
			if (database.providerName.equals(providerName)) {
				return database;
			}
		}

		return null;
	}
	
//	public String getConnectString() {
//		String connectionURL = jdbcUrlFormat.replace("<host>", getHost()); //$NON-NLS-1$
//
//		connectionURL = connectionURL.replace("<port>", getPort()); //$NON-NLS-1$
//		connectionURL = connectionURL.replace("<db>", getDatabaseName()); //$NON-NLS-1$
//
//		return connectionURL;
//	}
//	
//	public String getConnectString(String host, String port, String databaseName) {
//		String connectionURL = jdbcUrlFormat.replace("<host>", host); //$NON-NLS-1$
//
//		connectionURL = connectionURL.replace("<port>", port); //$NON-NLS-1$
//		connectionURL = connectionURL.replace("<db>", databaseName); //$NON-NLS-1$
//
//		return connectionURL;
//	}
//
//	public String getCreateDbConnectString() {
//		String connectionURL = jdbcUrlFormatToCreateDb.replace("<host>", getHost()); //$NON-NLS-1$
//
//		connectionURL = connectionURL.replace("<port>", getPort()); //$NON-NLS-1$
//		connectionURL = connectionURL.replace("<db>", getDatabaseName()); //$NON-NLS-1$
//
//		return connectionURL;
//	}
//
//	public String getEngineName() {
//		return engineName;
//	}
//
//	public String getJdbcUrlFormat() {
//		return jdbcUrlFormat;
//	}
//
//	public String getDefaultPort() {
//		return defaultPort;
//	}
//	
//	public void setHost(String host) {
//		AppConfig.put(POSUtil.escapePropertyKey(getEngineName()) + ".host", host);
//	}
//
//	public String getHost() {
//		return AppConfig.getString(POSUtil.escapePropertyKey(getEngineName()) + ".host", "localhost"); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//	
//	public void setPort(String port) {
//		AppConfig.put(POSUtil.escapePropertyKey(getEngineName()) + ".port", port);
//	}
//
//	public String getPort() {
//		return AppConfig.getString(POSUtil.escapePropertyKey(getEngineName()) + ".port", getDefaultPort()); //$NON-NLS-1$
//	}
//	
//	public void setDatabaseName(String databaseName) {
//		AppConfig.put(POSUtil.escapePropertyKey(getEngineName()) + ".dbname", databaseName);
//	}
//
//	public String getDatabaseName() {
//		return AppConfig.getString(POSUtil.escapePropertyKey(getEngineName()) + ".dbname", "posdb"); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//	
//	public void setUserName(String userName) {
//		AppConfig.put(POSUtil.escapePropertyKey(getEngineName()) + ".user", userName);
//	}
//
//	public String getUserName() {
//		return AppConfig.getString(POSUtil.escapePropertyKey(getEngineName()) + ".user", "app"); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//	
//	public void setUserPass(String userPass) {
//		AppConfig.put(POSUtil.escapePropertyKey(getEngineName()) + ".pass", userPass);
//	}
//
//	public String getUserPass() {
//		return AppConfig.getString(POSUtil.escapePropertyKey(getEngineName()) + ".pass", "sa"); //$NON-NLS-1$ //$NON-NLS-2$
//	}
}
