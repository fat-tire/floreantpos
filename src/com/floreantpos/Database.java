package com.floreantpos;

import org.apache.commons.lang.StringUtils;

public enum Database {
	DEMO_DATABASE(Messages.getString("Database.DERBY_SINGLE"), "jdbc:derby:database/derby-single/posdb", "jdbc:derby:database/derby-single/posdb;create=true", "", "org.apache.derby.jdbc.EmbeddedDriver", "org.hibernate.dialect.DerbyDialect"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	DERBY_SERVER(Messages.getString("Database.DERBY_SERVER"), "jdbc:derby://<host>:<port>/<db>", "jdbc:derby://<host>:<port>/<db>;create=true", "51527", "org.apache.derby.jdbc.ClientDriver", "org.hibernate.dialect.DerbyDialect"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	MYSQL(Messages.getString("Database.MYSQL"), "jdbc:mysql://<host>:<port>/<db>", "jdbc:mysql://<host>:<port>/<db>", "3306", "com.mysql.jdbc.Driver", "org.hibernate.dialect.MySQLDialect"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

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
			if(database.providerName.equals(providerName)) {
				return database;
			}
		}
		
		return null;
	}
}
