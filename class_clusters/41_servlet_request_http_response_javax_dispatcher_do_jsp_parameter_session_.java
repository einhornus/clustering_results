import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LogFilter implements Filter
{

	public void init(FilterConfig config) throws ServletException
	{
		System.out
		        .println("------------------------------------------------------");
		System.out.println(" init method is called in "
		        + this.getClass().getName());
		System.out
		        .println("------------------------------------------------------");
	}

	public void doFilter(ServletRequest req, ServletResponse res,
	        FilterChain chain) throws IOException, ServletException
	{

		System.out.println(" doFilter method is called in "
		        + this.getClass().getName());

		PrintWriter out = res.getWriter();
		HttpServletRequest request = (HttpServletRequest) req;

		// Get the IP address of client machine.
		String ipAddress = request.getRemoteAddr();

		// Log the IP address and current timestamp.
		System.out.println("IP " + ipAddress + ", Time "
		        + new Date().toString());

		out.print("LogFilter is invoked before<br>");

		chain.doFilter(req, res);

		out.print("LogFilter is invoked after <br>");

	}

	public void destroy()
	{
		// add code to release any resource
		System.out
		        .println("------------------------------------------------------");
		System.out.println(" destroy method is called in "
		        + this.getClass().getName());
		System.out
		        .println("------------------------------------------------------");
	}
}
--------------------

package net.bitacademy.java67.step16;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/step16/fileUpload")
public class FileUploadServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void service(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String name = request.getParameter("name");
    int age = Integer.parseInt(request.getParameter("age"));
    String file = request.getParameter("file");
  
    response.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.printf("%s, %d, %s\n", name, age, file);
  }
}

















--------------------

package by.slesh.itechart.fullcontact.servlet.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.action.Action;
import by.slesh.itechart.fullcontact.action.ActionFactory;
import by.slesh.itechart.fullcontact.util.PathUtil;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(PathUtil.class);

    @Override
    public void init() throws ServletException {
	super.init();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	LOGGER.info("BEGIN");
	
	try {
	    response.setContentType("text/html;charset=UTF-8");
	    request.setCharacterEncoding("utf-8");

	    StringBuffer url = request.getRequestURL();
	    
	    String actionName = url.substring(url.lastIndexOf("/") + 1, url.length());
	    if(url.indexOf("delete") != -1){
		actionName = "delete";
	    }
	    
	    LOGGER.info("request url: {}", url);
	    LOGGER.info("action {}:", actionName);

	    Action action = ActionFactory.getActionByName(actionName);
	    action.init(request, response);
	    action.execute();
	} catch (Exception e) {
	    LOGGER.error("{}", e);
	    throw new ServletException(e);
	}

	LOGGER.info("END\n\n\n\n\n\n\n");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	processRequest(request, response);
    }
}

--------------------

