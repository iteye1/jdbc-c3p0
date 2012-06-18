package jdbc_pool.webapp;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * Controller: store data in the database.
 */
@SuppressWarnings("serial")
public class PutDataServlet extends HttpServlet {
  private String _DISPATCH_SUCCESS = null;
  private String _DISPATCH_FAILURE = null;
  private static final String DISPATCH_DATA_NAME = "DATA";
  private static final Logger _log = Logger.getLogger(PutDataServlet.class);

  public void init(ServletConfig cfg) throws ServletException {
    super.init(cfg);
    _DISPATCH_SUCCESS = cfg.getInitParameter("dispatch.success");
    _DISPATCH_FAILURE = cfg.getInitParameter("dispatch.failure");
    return;
  } // init()

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    final HashMap<String, Object> map = new HashMap<String, Object>();
    request.setAttribute(DISPATCH_DATA_NAME, map);
    String forward = null;
    try {
      final SimpleDAO dao = (SimpleDAO) ObjectRegistry.getInstance().get(
          AppTokens.OBJECT_REGISTRY_DAO);
      dao.putData();
      forward = _DISPATCH_SUCCESS;
    } catch (Throwable t) {
      _log.error("Problem storing data", t);
      map.put("Message", t.getMessage());
      map.put("Throwable", t);
      forward = _DISPATCH_FAILURE;
    }
    RequestDispatcher rd = request.getRequestDispatcher(forward);
    rd.forward(request, response);
    return;
  } // doGet()
} // public class PutDataServlet
