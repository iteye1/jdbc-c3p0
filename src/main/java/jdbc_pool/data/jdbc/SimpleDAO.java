package jdbc_pool.data.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import jdbc_pool.core.SimpleDTO;

/*
This is part of the sample code for the article,
"App-Managed JDBC DataSources with commons-dbcp"
by Ethan McCallum.

http://today.java.net/2005/11/17/app-managed-datasources-with-commons-dbcp.html
*/
/**
 * Data access for the sample app.
 * 
 * This is more of a database-everything-object, since it sets up the sample
 * database as well as handles data fetches/puts.
 */
public class SimpleDAO {
  // - - - - - - - - - - - - - - - - - - - -
  private final SQLStatements _sql;
  private final DataSource _ds;

  // - - - - - - - - - - - - - - - - - - - -
  /**
   * Handles all database access through the DataSource provided in the ctor
   */
  public SimpleDAO(DataSource ds) {
    _ds = ds;
    _sql = new SQLStatements();
    _sql.add("delete_table", "DROP TABLE info");
    _sql.add("create_table", "CREATE TABLE info( names VARCHAR , numbers INT )");
    _sql.add("fetch_data", "SELECT * FROM info");
    _sql.add("insert_data", "INSERT INTO info VALUES( ? , ? )");
    return;
  } // ctor

  /**
   * Populate the table with data.
   */
  public void setupTable() throws Exception {
    Connection conn = null;
    Statement sql = null;
    try {
      conn = _ds.getConnection();
      sql = conn.createStatement();
      sql.execute(_sql.get("create_table"));
      return;
    } catch (Throwable t) {
      throw (new Exception(t));
    } finally {
      DbUtil.close(sql);
      DbUtil.close(conn);
    }
  } // setupTable()

  // - - - - - - - - - - - - - - - - - - - -
  /**
   * Populate the table with data.
   */
  public void putData() throws Exception {
    Connection conn = null;
    PreparedStatement sql = null;
    try {
      conn = _ds.getConnection();
      sql = conn.prepareStatement(_sql.get("insert_data"));
      for (int ix = 1; ix < 10; ++ix) {
        final int number = (int) (100 * Math.random());
        final String name = "text " + number;
        sql.setString(1, name);
        sql.setInt(2, number);
        sql.execute();
      }
      return;
    } catch (Throwable t) {
      throw (new Exception(t));
    } finally {
      DbUtil.close(sql);
      DbUtil.close(conn);
    }
  } // putData()

  // - - - - - - - - - - - - - - - - - - - -
  public Collection<SimpleDTO> getData() throws Exception {
    Connection conn = null;
    Statement sql = null;
    try {
      conn = _ds.getConnection();
      sql = conn.createStatement();
      ResultSet rs = sql.executeQuery(_sql.get("fetch_data"));
      ArrayList<SimpleDTO> result = new ArrayList<SimpleDTO>();
      while (rs.next()) {
        result.add(new SimpleDTO(rs.getString("names"), rs.getInt("numbers")));
      }
      return (result);
    } catch (Throwable t) {
      throw (new Exception(t));
    } finally {
      DbUtil.close(sql);
      DbUtil.close(conn);
    }
  } // getData()

  // - - - - - - - - - - - - - - - - - - - -
  public void destroyTable() throws Exception {
    Connection conn = null;
    Statement sql = null;
    try {
      conn = _ds.getConnection();
      sql = conn.createStatement();
      sql.execute(_sql.get("delete_table"));
      return;
    } catch (Throwable t) {
      throw (new Exception(t));
    } finally {
      DbUtil.close(sql);
      DbUtil.close(conn);
    }
  } // contextDestroyed()
} // public class SimpleDAO
// - - - - - - - - - - - - - - - - - - - -
// EOF
