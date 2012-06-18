package jdbc_pool.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/*
This is part of the sample code for the article,
"App-Managed JDBC DataSources with commons-dbcp"
by Ethan McCallum.

http://today.java.net/2005/11/17/app-managed-datasources-with-commons-dbcp.html
*/
/**
 * Very simple object registry. Classes look here, instead of having to
 * find/instantiate several other classes on their own.
 */
public class ObjectRegistry {
  private static ObjectRegistry _instance = new ObjectRegistry();
  private static Logger _log = Logger.getLogger(ObjectRegistry.class);

  public static ObjectRegistry getInstance() {
    return (_instance);
  } // getInstance()

  private Map<String, Object> _singletons;

  private ObjectRegistry() {
    _singletons = new HashMap<String, Object>();
  } // ctor

  public void put(final String key, final Object obj) {
    _log.debug("Storing object: \"" + key + "\" => " + obj);
    _singletons.put(key, obj);
  } // put()

  public Object get(final String key) {
    return (_singletons.get(key));
  } // get()

  public boolean isRegistered(final String key) {
    return (_singletons.containsKey(key));
  } // isRegistered()
} // public class ObjectRegistry
