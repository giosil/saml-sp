package org.dew.saml.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lastpass.saml.SAMLClient;

public
class WebIndex extends HttpServlet
{
  private static final long serialVersionUID = 6443844856748282229L;
  
  public
  void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    doPost(request, response);
  }
  
  public
  void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    try {
      String requestURL = request.getRequestURL().toString();
      response.setHeader("Location", SAMLClient.getInstance(requestURL).getRedirectURL(requestURL));
      response.sendError(302);
    }
    catch(Exception ex) {
      sendMessage(request, response, ex);
    }
  }
  
  protected
  void sendMessage(HttpServletRequest request, HttpServletResponse response, String sMessage)
      throws ServletException, IOException
  {
    if(sMessage == null) sMessage = "";
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<body>");
    out.println(sMessage);
    out.println("</body>");
    out.println("</html>");
  }
  
  protected
  void sendMessage(HttpServletRequest request, HttpServletResponse response, Exception ex)
      throws ServletException, IOException
  {
    String sMessage = "Exception";
    if(ex != null) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(baos);
      ex.printStackTrace(ps);
      sMessage = new String(baos.toByteArray()).replace("\n", "<br>");
    }
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<body>");
    out.println(sMessage);
    out.println("</body>");
    out.println("</html>");
  }
}
