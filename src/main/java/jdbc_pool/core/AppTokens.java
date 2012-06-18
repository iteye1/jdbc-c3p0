package jdbc_pool.core ;

/*
This is part of the sample code for the article,
"App-Managed JDBC DataSources with commons-dbcp"
by Ethan McCallum.

http://today.java.net/2005/11/17/app-managed-datasources-with-commons-dbcp.html
*/


/**
Constants for the Commons-Pool/Commons-JDBC app

In a real app, the keys for the object registry and JNDI lookup
would live in separate classes; but here, there's no need...

*/

public class AppTokens {

	public static final String OBJECT_REGISTRY_JDBC_CONFIG = "JDBC_CONFIG" ;
	public static final String OBJECT_REGISTRY_APP_CONFIG = "APP_CONFIG" ;
	public static final String OBJECT_REGISTRY_JDBC_POOL = "JDBC_POOL" ;
	
	public static final String OBJECT_REGISTRY_JDBC_DATASOURCE = "DataSource" ;
	public static final String OBJECT_REGISTRY_DAO = "DAO" ;

	
	public static final String APP_CONFIG_DATASOURCE_JNDI_NAME = "datasource.jndi.name" ;

	public static final String DB_CONFIG_JDBC_CLASSNAME = "jdbc.classname" ;
	public static final String DB_CONFIG_JDBC_URL = "jdbc.url" ;
	public static final String DB_CONFIG_JDBC_LOGIN = "jdbc.login" ;
	public static final String DB_CONFIG_JDBC_PASSWORD = "jdbc.password" ; 
	
	
	// not used
	private AppTokens() {}

} // public class AppTokens
