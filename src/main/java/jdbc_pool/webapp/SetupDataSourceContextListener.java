package jdbc_pool.webapp;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import jdbc_pool.core.AppTokens;
import jdbc_pool.util.ObjectRegistry;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/*
This is part of the sample code for the article,
"App-Managed JDBC DataSources with commons-dbcp"
by Ethan McCallum.

http://today.java.net/2005/11/17/app-managed-datasources-with-commons-dbcp.html
*/
/**
 * Setup the pool, associate that with a DataSource, and store that DataSource
 * where other code can find it.
 * 
 * Obvious deficiencies:
 * 
 * 1/ ideally, a ServletContextListener should call some other, generic class to
 * do the real work. That way, the pool/DataSource would be available outside of
 * the container.
 * 
 * 2/ The database config should be passed in via context params, config file,
 * or something else. As this is demo code, there's no need...
 */
public class SetupDataSourceContextListener implements ServletContextListener {
  // - - - - - - - - - - - - - - - - - - - -
  private static Logger _log = Logger
      .getLogger(SetupDataSourceContextListener.class);

  // - - - - - - - - - - - - - - - - - - - -
  public void contextInitialized(ServletContextEvent sce) {
    try {
      _log.info("called for init");
      final Properties dbConfig = (Properties) ObjectRegistry.getInstance()
          .get(AppTokens.OBJECT_REGISTRY_JDBC_CONFIG);
      final Properties appConfig = (Properties) ObjectRegistry.getInstance()
          .get(AppTokens.OBJECT_REGISTRY_APP_CONFIG);
      final DataSource ds = setupDataSource(dbConfig);
      /*
      // if your container doesn't provide a writable JNDI tree, you can go the other route:
      // store the DataSource in the object registry.  This isn't as transparent as
      // using JNDI, though, because existing classes would have be updated to use the
      // object registry.  (Otherwise, you could just change the JNDI lookup name
      // and those classes would run the same as they did when using a container-provided
      // DataSource.)
      
      _log.info( "(storing DataSource under key \"" + AppTokens.OBJECT_REGISTRY_JDBC_DATASOURCE + "\")" ) ;
      ObjectRegistry.getInstance().put( AppTokens.OBJECT_REGISTRY_JDBC_DATASOURCE , ds ) ;
      */
      final String jndiLookupName = appConfig
          .getProperty(AppTokens.APP_CONFIG_DATASOURCE_JNDI_NAME);
      _log.info("Storing DataSource in JNDI, under \"" + jndiLookupName + "\")");
      bindObject(jndiLookupName, ds);
      _log.info("done with init");
      return;
    } catch (Throwable t) {
      final String message = "Error setting up data source: " + t.getMessage();
      _log.error(message, t);
      // this exception will cause Tomcat to disable the context
      throw (new RuntimeException(message, t));
    }
  } // contextInitialized()

  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext ctx = sce.getServletContext();
    ctx.log("SetupDataSourceContextListener: called for shutdown");
    try {
      final ComboPooledDataSource  cpds = (ComboPooledDataSource) ObjectRegistry
          .getInstance().get(AppTokens.OBJECT_REGISTRY_JDBC_POOL);
      if (null != cpds) {
        DataSources.destroy( cpds );
      }
    } catch (Throwable ignored) {
      _log.error("Failed to clear connection pool: " + ignored.getMessage(),
          ignored);
    }
    ctx.log("SetupDataSourceContextListener: completed shutdown");
    return;
  } // contextDestroyed()

  private DataSource setupDataSource(final Properties config) throws Exception {
    ComboPooledDataSource cpds = new ComboPooledDataSource(); 
    cpds.setDriverClass( config.getProperty(AppTokens.DB_CONFIG_JDBC_CLASSNAME).trim()); 
    //loads the jdbc driver 
    cpds.setJdbcUrl(config
        .getProperty(AppTokens.DB_CONFIG_JDBC_URL).trim() ); 
    cpds.setUser(config.getProperty(
        AppTokens.DB_CONFIG_JDBC_LOGIN).trim()); 
    cpds.setPassword(config.getProperty(
        AppTokens.DB_CONFIG_JDBC_PASSWORD).trim()); 
    return cpds;
  } // setupDataSource()

  private void bindObject(final String fullPath, final Object toBind)
      throws Exception {
    _log.info("attempting to bind object " + toBind + " to context path \""
        + fullPath + "\"");
    Context currentContext = new InitialContext();
    final String name = currentContext.composeName(fullPath,
        currentContext.getNameInNamespace());
    final String[] components = name.split("/");
    // the last item in the array refers to the object itself.
    // we don't want to create a (sub)Context for that; we
    // want to bind the object to it
    final int stop = components.length - 1;
    for (int ix = 0; ix < stop; ++ix) {
      final String nextPath = components[ix];
      _log.debug("Looking up subcontext named \"" + nextPath + "\" in context "
          + currentContext);
      try {
        currentContext = (Context) currentContext.lookup(nextPath);
        _log.debug("found");
      } catch (final NameNotFoundException ignored) {
        _log.debug("not found; creating subcontext");
        currentContext = currentContext.createSubcontext(nextPath);
        _log.debug("done");
      }
    }
    // by this point, we've built up the entire context path leading up
    // to the desired bind point... so we can bind the object itself
    _log.info("binding to " + currentContext);
    currentContext.bind(components[stop], toBind);
    return;
  } // createContextDepth()
} // public class SetupDataSourceContextListener
