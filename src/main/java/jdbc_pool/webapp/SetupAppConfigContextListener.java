package jdbc_pool.webapp;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jdbc_pool.core.AppTokens;
import jdbc_pool.util.ObjectRegistry;

import org.apache.log4j.Logger;

/*
This is part of the sample code for the article,
"App-Managed JDBC DataSources with commons-dbcp"
by Ethan McCallum.

http://today.java.net/2005/11/17/app-managed-datasources-with-commons-dbcp.html
*/
/**
 * File the various configs in the object registry.
 */
public class SetupAppConfigContextListener implements ServletContextListener {
  private static final Logger _log = Logger
      .getLogger(SetupAppConfigContextListener.class);

  public void contextInitialized(ServletContextEvent event) {
    _log.info("Called for init");
    try {
      // global app config
      final String appConfigFile = event.getServletContext().getInitParameter(
          "jdbc_pool.mapping.app_config_file");
      _log.info("Loading app config from file \"" + appConfigFile + "\"");
      final Properties appConfig = new Properties();
      appConfig.load(Thread.currentThread().getContextClassLoader()
          .getResourceAsStream(appConfigFile));
      ObjectRegistry.getInstance().put(AppTokens.OBJECT_REGISTRY_APP_CONFIG,
          appConfig);
      // JDBC config
      final String dbConfigFile = event.getServletContext().getInitParameter(
          "jdbc_pool.mapping.db_config_file");
      _log.info("Loading DB config from file \"" + dbConfigFile + "\"");
      final Properties dbConfig = new Properties();
      dbConfig.load(Thread.currentThread().getContextClassLoader()
          .getResourceAsStream("jdbc-config.properties"));
      ObjectRegistry.getInstance().put(AppTokens.OBJECT_REGISTRY_JDBC_CONFIG,
          dbConfig);
    } catch (final Throwable rethrow) {
      // this exception will cause Tomcat to disable the context;
      // results with other containers may vary
      final String message = "Error loading app config: " + rethrow;
      throw (new RuntimeException(message, rethrow));
    }
  }

  public void contextDestroyed(ServletContextEvent event) {
    _log.info("Called for shutdown");
  }
}
