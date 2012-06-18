package jdbc_pool.webapp;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import jdbc_pool.core.AppTokens;
import jdbc_pool.data.jdbc.SimpleDAO;
import jdbc_pool.util.ObjectRegistry;

import org.apache.log4j.Logger;

/*
This is part of the sample code for the article,
"App-Managed JDBC DataSources with commons-dbcp"
by Ethan McCallum.

http://today.java.net/2005/11/17/app-managed-datasources-with-commons-dbcp.html
*/
/**
 * Setup the database on app startup.
 */
public class SetupDBContextListener implements ServletContextListener {
  // - - - - - - - - - - - - - - - - - - - -
  private static Logger _log = Logger.getLogger(SetupDBContextListener.class);

  // - - - - - - - - - - - - - - - - - - - -
  /**
   * Setup the database tables, via the BusinessObject/DataSource.
   */
  public void contextInitialized(ServletContextEvent sce) {
    //ServletContext ctx = sce.getServletContext();
    _log.info("SetupDBContextListener: called for init");
    try {
      _log.info("Fetching DataSource");
      /*
      // Storing the DataSource in JNDI is more transparent to older classes.
      // If your container doesn't provide a writable JNDI tree, however, you'll
      // have to go the extra step of storing the DataSource in the object registry
      // and altering your older code to use that instead of JNDI.
      
      DataSource ds = (DataSource) ObjectRegistry.getInstance().get( AppTokens.OBJECT_REGISTRY_JDBC_DATASOURCE ) ;

      if( null == ds ){
      	throw( new Exception( "DataSource not found at expected location (\"" + AppTokens.OBJECT_REGISTRY_JDBC_DATASOURCE + "\"" ) ) ;
      }
      */
      Context jndiCtx = new InitialContext();
      final Properties appConfig = (Properties) ObjectRegistry.getInstance()
          .get(AppTokens.OBJECT_REGISTRY_APP_CONFIG);
      final String jndiLookupName = appConfig
          .getProperty(AppTokens.APP_CONFIG_DATASOURCE_JNDI_NAME);
      final Object fromJNDI = jndiCtx.lookup(jndiLookupName);
      if (null == fromJNDI) {
        _log.error("Unable to locate DataSource object in JNDI under \""
            + jndiLookupName + "\"");
      } else {
        _log.info("Found object registered in JNDI under \"" + jndiLookupName
            + "\": " + fromJNDI);
      }
      DataSource ds = (DataSource) fromJNDI;
      SimpleDAO dao = new SimpleDAO(ds);
      dao.setupTable();
      ObjectRegistry.getInstance().put(AppTokens.OBJECT_REGISTRY_DAO, dao);
    } catch (Throwable t) {
      final String message = "Error setting up database: " + t.getMessage();
      _log.error(message, t);
      // this exception will cause Tomcat to disable the context;
      // results with other containers may vary
      throw (new RuntimeException(message, t));
    }
    _log.info("SetupDBContextListener: completed init");
    return;
  } // contextInitialized()

  // - - - - - - - - - - - - - - - - - - - -
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext ctx = sce.getServletContext();
    ctx.log("SetupDBContextListener: called for destroy");
    try {
      ctx.log("dropping table from data source");
      SimpleDAO dao = (SimpleDAO) ObjectRegistry.getInstance().get(
          AppTokens.OBJECT_REGISTRY_DAO);
      if (null != dao) {
        dao.destroyTable();
      }
    } catch (Throwable rethrow) {
      final String message = "Error cleaning up database: "
          + rethrow.getMessage();
      ctx.log(message, rethrow);
      // this exception will cause Tomcat to disable the context
      throw (new RuntimeException(message, rethrow));
    }
    ctx.log("SetupDBContextListener: completed destroy");
    return;
  } // contextDestroyed()
} // public class SetupDBContextListener
// - - - - - - - - - - - - - - - - - - - -
// EOF
