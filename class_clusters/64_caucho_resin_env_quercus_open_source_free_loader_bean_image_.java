/*
 * Copyright (c) 1998-2012 Caucho Technology -- all rights reserved
 *
 * This file is part of Resin(R) Open Source
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Resin Open Source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Resin Open Source is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Resin Open Source; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Scott Ferguson
 */

package com.caucho.jmx;

import com.caucho.util.L10N;

import javax.management.*;
import java.util.logging.Logger;

/**
 * Wrapper around the dynamic mbean to handle classloader lifecycle.
 */
class MBeanWrapper implements DynamicMBean {
  private static L10N L = new L10N(MBeanWrapper.class);
  private static Logger log
    = Logger.getLogger(MBeanWrapper.class.getName());
  
  private MBeanContext _context;

  private ObjectName _name;
  
  protected Object _object;
  protected DynamicMBean _mbean;

  private ObjectInstance _instance;
  
  protected MBeanWrapper(MBeanContext context,
                         ObjectName name,
                         Object object,
                         DynamicMBean mbean)
  {
    _context = context;

    _name = name;

    _object = object;
    _mbean = mbean;
  }

  /**
   * Returns the object instance for the mbean.
   */
  public ObjectInstance getObjectInstance()
  {
    if (_instance == null)
      _instance = new ObjectInstance(_name, getMBeanInfo().getClassName());
    
    return _instance;
  }

  /**
   * Returns the context.
   */
  MBeanContext getContext()
  {
    return _context;
  }

  /**
   * Returns the object.
   */
  Object getObject()
  {
    return _object;
  }

  /**
   * Returns the object as a broadcaster.
   */
  private NotificationBroadcaster getBroadcaster()
  {
    return (NotificationBroadcaster) _object;
  }

  /**
   * Returns the object as an emitter.
   */
  private NotificationEmitter getEmitter()
  {
    return (NotificationEmitter) _object;
  }

  /**
   * Returns the name.
   */
  public ObjectName getObjectName()
  {
    return _name;
  }

  /**
   * Returns the class loader.
   */
  public ClassLoader getClassLoader()
  {
    return _context.getClassLoader();
  }

  /**
   * Returns the MBeanInfo meta-data.
   */
  public MBeanInfo getMBeanInfo()
  {
    return _mbean.getMBeanInfo();
  }

  /**
   * Returns the named attribute.
   */
  public Object getAttribute(String name)
    throws ReflectionException, AttributeNotFoundException, MBeanException
  {
    Thread thread = Thread.currentThread();
    ClassLoader oldLoader = thread.getContextClassLoader();

    try {
      thread.setContextClassLoader(_context.getClassLoader());
      
      return _mbean.getAttribute(name);
    } finally {
      thread.setContextClassLoader(oldLoader);
    }
  }
  
  /**
   * Returns an array of attributes.
   */
  public AttributeList getAttributes(String []names)
  {
    Thread thread = Thread.currentThread();
    ClassLoader oldLoader = thread.getContextClassLoader();

    try {
      thread.setContextClassLoader(_context.getClassLoader());
      
      return _mbean.getAttributes(names);
    } finally {
      thread.setContextClassLoader(oldLoader);
    }
  }

  /**
   * Sets the named attribute.
   */
  public void setAttribute(Attribute attr)
    throws ReflectionException,
           AttributeNotFoundException,
           InvalidAttributeValueException,
           MBeanException
  {
    Thread thread = Thread.currentThread();
    ClassLoader oldLoader = thread.getContextClassLoader();

    try {
      thread.setContextClassLoader(_context.getClassLoader());
      
      _mbean.setAttribute(attr);
    } finally {
      thread.setContextClassLoader(oldLoader);
    }
  }
  
  /**
   * Returns an array of attributes.
   */
  public AttributeList setAttributes(AttributeList list)
  {
    Thread thread = Thread.currentThread();
    ClassLoader oldLoader = thread.getContextClassLoader();

    try {
      thread.setContextClassLoader(_context.getClassLoader());
      
      return _mbean.setAttributes(list);
    } finally {
      thread.setContextClassLoader(oldLoader);
    }
  }

  /**
   * Invokes the operation.
   */
  public Object invoke(String operation,
                       Object []params,
                       String[]signature)
    throws ReflectionException, MBeanException
  {
    Thread thread = Thread.currentThread();
    ClassLoader oldLoader = thread.getContextClassLoader();

    try {
      thread.setContextClassLoader(_context.getClassLoader());
      
      return _mbean.invoke(operation, params, signature);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(_mbean + "." + operation + "\n  " + e, e);
    } finally {
      thread.setContextClassLoader(oldLoader);
    }
  }

  /**
   * Returns as listener.
   */
  public NotificationListener getListener()
  {
    Object obj = getObject();
    
    if (obj instanceof NotificationListener)
      return (NotificationListener) obj;
    else
      return null;
  }

  /**
   * Adds a notification listener.
   */
  public void addNotificationListener(NotificationListener listener,
                                      NotificationFilter filter,
                                      Object handback)
  {
    getBroadcaster().addNotificationListener(listener, filter, handback);
  }

  /**
   * Removes a notification listener.
   */
  public void removeNotificationListener(NotificationListener listener)
    throws ListenerNotFoundException
  {
    getBroadcaster().removeNotificationListener(listener);
  }

  /**
   * Removes a notification listener.
   */
  public void removeNotificationListener(NotificationListener listener,
                                         NotificationFilter filter,
                                         Object handback)
    throws ListenerNotFoundException
  {
    Object obj = getObject();

    if (obj instanceof NotificationEmitter)
      getEmitter().removeNotificationListener(listener, filter, handback);
    else
      getBroadcaster().removeNotificationListener(listener);
  }
}

--------------------

/*
 * Copyright (c) 1998-2012 Caucho Technology -- all rights reserved
 *
 * This file is part of Resin(R) Open Source
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Resin Open Source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Resin Open Source is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Resin Open Source; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Scott Ferguson
 */

package com.caucho.env.jdbc;

import com.caucho.loader.EnvironmentLocal;
import com.caucho.util.L10N;
import com.caucho.config.ConfigException;
import com.caucho.vfs.*;

import java.net.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.util.logging.*;

/**
 * Manages databases in a local environment, e.g. for PHP dynamic
 * database lookup.
 */
abstract public class DatabaseFactory {
  private static final Logger log
    = Logger.getLogger(DatabaseFactory.class.getName());
  
  private static final Class<?> _databaseFactoryClass;
  
  private String _name;
  
  private Class<?> _driverClass;
  private String _url;
  private String _user;
  private String _password;
  
  public String _databaseName;
  
  public static DatabaseFactory createBuilder()
  {
    try {
      return (DatabaseFactory) _databaseFactoryClass.newInstance();
    } catch (Exception e) {
      throw new IllegalStateException();
    }
  }
  
  public void setName(String name)
  {
    _name = name;
  }
  
  public String getName()
  {
    return _name;
  }
  
  public void setDriverClass(Class<?> driverClass)
  {
    _driverClass = driverClass;
  }
  
  public Class<?> getDriverClass()
  {
    return _driverClass;
  }
  
  public void setUrl(String url)
  {
    _url = url;
  }
  
  public String getUrl()
  {
    return _url;
  }
  
  public void setUser(String user)
  {
    _user = user;
  }
  
  public String getUser()
  {
    return _user;
  }
  
  public void setPassword(String password)
  {
    _password = password;
  }
  
  public String getPassword()
  {
    return _password;
  }
  
  public void setDatabaseName(String databaseName)
  {
    _databaseName = databaseName;
  }
  
  public String getDatabaseName()
  {
    return _databaseName;
  }
  
  abstract public DataSource create();
  
  static {
    Class<?> factoryClass = null;
    
    try {
      String className = DatabaseFactory.class.getName() + "Impl";
      
      factoryClass = Class.forName(className);
    } catch (Exception e) {
      log.log(Level.FINER, e.toString(), e);
    }
    
    _databaseFactoryClass = factoryClass;
  }
}


--------------------

/*
 * Copyright (c) 1998-2012 Caucho Technology -- all rights reserved
 *
 * This file is part of Resin(R) Open Source
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Resin Open Source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Resin Open Source is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Resin Open Source; if not, write to the
 *   Free SoftwareFoundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Scott Ferguson
 */

package com.caucho.log;

import com.caucho.util.L10N;
import com.caucho.vfs.WriteStream;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Resin's rotating path-based log.
 */
public class StreamHandler extends Handler {
  private static final L10N L = new L10N(StreamHandler.class);
  
  private WriteStream _os;

  private Formatter _formatter;

  private String _timestamp;
  private boolean _isNullDelimited;

  public StreamHandler()
  {
  }

  public StreamHandler(WriteStream os)
  {
    _os = os;
  }

  /**
   * Sets the timestamp.
   */
  public void setTimestamp(String timestamp)
  {
    _timestamp = timestamp;
  }

  /**
   * Sets the formatter.
   */
  public void setFormatter(Formatter formatter)
  {
    _formatter = formatter;
  }

  public void setNullDelimited(boolean isNullDelimited)
  {
    _isNullDelimited = isNullDelimited;
  }

  /**
   * Publishes the record.
   */
  public void publish(LogRecord record)
  {
    if (! isLoggable(record))
      return;

    try {
      if (record == null) {
        synchronized (_os) {
          _os.println("no record");
          
          if (_isNullDelimited)
            _os.write(0);
            
          _os.flush();
        }
        return;
      }

      if (_formatter != null) {
        String value = _formatter.format(record);

        synchronized (_os) {
          _os.println(value);
          if (_isNullDelimited)
            _os.write(0);
          
          _os.flush();
        }
        
        return;
      }
      
      String message = record.getMessage();
      Throwable thrown = record.getThrown();
      
      if (thrown == null
          && message != null
          && message.indexOf("java.lang.NullPointerException") >= 0) {
        thrown = new IllegalStateException();
        thrown.fillInStackTrace();
      }

      synchronized (_os) {
        if (_timestamp != null) {
          _os.print(_timestamp);
        }
          
        if (thrown != null) {
          if (message != null
              && ! message.equals(thrown.toString()) 
              && ! message.equals(thrown.getMessage()))
            _os.println(message);
        
          thrown.printStackTrace(_os.getPrintWriter());
        }
        else {
          _os.println(record.getMessage());
        }
        
        if (_isNullDelimited)
          _os.write(0);
        
        _os.flush();
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  /**
   * Flushes the buffer.
   */
  public void flush()
  {
  }

  /**
   * Closes the handler.
   */
  public void close()
  {
  }

  /**
   * Returns the hash code.
   */
  public int hashCode()
  {
    if (_os == null || _os.getPath() == null)
      return super.hashCode();
    else
    return _os.getPath().hashCode();
  }

  /**
   * Test for equality.
   */
  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    else if (getClass() != o.getClass())
      return false;

    StreamHandler handler = (StreamHandler) o;

    if (_os == null || handler._os == null)
      return false;
    else
      return _os.getPath().equals(handler._os.getPath());
  }

  public String toString()
  {
    if (_os == null)
      return "StreamHandler@" + System.identityHashCode(this) + "[]";
    else
      return ("StreamHandler@" + System.identityHashCode(this)
              + "[" + _os.getPath() + "]");
  }
}

--------------------

