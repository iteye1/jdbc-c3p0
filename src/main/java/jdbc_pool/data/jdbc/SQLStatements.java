package jdbc_pool.data.jdbc;

import java.util.HashMap;

/*
This is part of the sample code for the article,
"App-Managed JDBC DataSources with commons-dbcp"
by Ethan McCallum.

http://today.java.net/2005/11/17/app-managed-datasources-with-commons-dbcp.html
*/
/**
 * Fetch SQL statements by known key. In a real app, an object such as this
 * would be loaded from a config file.
 */
class SQLStatements {
  private HashMap<String, String> _sql;

  public SQLStatements() {
    _sql = new HashMap<String, String>();
    return;
  } // ctor

  public void add(final String key, final String value) {
    _sql.put(key, value);
    return;
  } // add()

  public String get(final String key) {
    if (!_sql.containsKey(key)) {
      throw (new IllegalArgumentException("Invalid key \"" + key + "\""));
    }
    return ((String) _sql.get(key));
  } // get()
} // class SQLStatements
