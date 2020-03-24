package org.dew.saml.web;

import java.io.*;

import javax.servlet.http.*;

import com.lastpass.saml.SAMLClient;

import javax.servlet.*;

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
      response.setHeader("Location", SAMLClient.getInstance().getRedirectURL(request.getRequestURL().toString()));
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
