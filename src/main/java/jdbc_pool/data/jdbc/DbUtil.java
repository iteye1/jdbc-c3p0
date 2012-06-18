package jdbc_pool.data.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

/*
This is part of the sample code for the article,
"App-Managed JDBC DataSources with commons-dbcp"
by Ethan McCallum.

http://today.java.net/2005/11/17/app-managed-datasources-with-commons-dbcp.html
*/
/**
 * For closing various JDBC objects.
 */
class DbUtil {
  private static Logger _log = Logger.getLogger(DbUtil.class);

  private DbUtil() {
  }

  public static void close(Connection obj) {
    if (null != obj) {
      try {
        obj.close();
      } catch (Throwable t) {
        _log.warn("Problem closing provided object " + obj, t);
      }
    }
  } // close( Connection )

  public static void close(Statement obj) {
    if (null != obj) {
      try {
        obj.close();
      } catch (Throwable t) {
        _log.warn("Problem closing provided object " + obj, t);
      }
    }
  } // close( Statement )

  public static void close(ResultSet obj) {
    if (null != obj) {
      try {
        obj.close();
      } catch (Throwable t) {
        _log.warn("Problem closing provided object " + obj, t);
      }
    }
  } // close( ResultSet )
} // class DbUtil
// EOF
