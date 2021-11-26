package org.dew.saml.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lastpass.saml.AttributeSet;
import com.lastpass.saml.SAMLClient;
import com.lastpass.saml.SAMLUtils;

public
class WebSSOPost extends HttpServlet
{
  private static final long serialVersionUID = 9145953474733766353L;
  
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
      String relayState      = request.getParameter("RelayState");
      String sB64SAMLReponse = request.getParameter("SAMLResponse");
      
      // CWE-79 Improper Neutralization of Input During Web Page Generation (Cross-site Scripting)
      // WASC-8 Cross Site Scripting
      sB64SAMLReponse = SAMLUtils.escapeHtml(sB64SAMLReponse);
      relayState      = SAMLUtils.escapeHtml(relayState);
      
      if(sB64SAMLReponse == null || sB64SAMLReponse.length() == 0) {
        sendMessage(request, response, "NO SAMLResponse");
      }
      else {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        AttributeSet attributeSet = SAMLClient.getInstance().validateResponse(sB64SAMLReponse);
        String loginReqId = attributeSet != null ? attributeSet.getRequestId() : null;
        String username   = attributeSet != null ? attributeSet.getNameId() : null;
        
        out.println("<html>");
        out.println("<body>");
        out.println("<b>RelayState:</b> " + relayState + "<br>");
        out.println("<b>SAML Response:</b>");
        out.println("<br>");
        out.println(sB64SAMLReponse);
        
        out.println("<br>");
        out.println("<hr>");
        out.println("<b>SAML Response:</b>");
        out.println("<br>");
        out.println(SAMLClient.getInstance().base64Decode(sB64SAMLReponse).replace("<", "&lt;").replace(">", "&gt;"));
        
        if(username != null && username.length() > 0) {
          out.println("<br>");
          out.println("<hr>");
          out.println("<b>Result:</b>");
          out.println("<br>");
          out.println("<b>User:</b> " + username + " , <b>Request Id:</b>" + loginReqId);
        }
        
        out.println("<br>");
        out.println("<hr>");
        out.println("<b>Header:</b>");
        out.println("<br>");
        Enumeration<String> enumHeaderNames = request.getHeaderNames();
        while(enumHeaderNames.hasMoreElements()) {
          String sName  = enumHeaderNames.nextElement();
          out.println("<b>" + sName + "</b>: " + request.getHeader(sName) + "<br>");
        }
        
        if(loginReqId != null && loginReqId.length() > 0) {
          out.println("<br>");
          out.println("<hr>");
          out.println("<br>");
          out.println("<a href=\"" + SAMLClient.getInstance().getRedirectLogoutURL(loginReqId) + "\" target=\"_blank\">Logout</a>");
        }
        out.println("</body>");
        out.println("</html>");
      }
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
    out.println("<html><body>" + sMessage.replace("<", "&lt;").replace(">", "&gt;") + "</body></html>");
  }
  
  protected
  void sendMessage(HttpServletRequest request, HttpServletResponse response, Exception ex)
      throws ServletException, IOException
  {
    String sMessage = "Exception";
    if(ex != null) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ex.printStackTrace(new PrintStream(baos));
      sMessage = new String(baos.toByteArray());
    }
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<html><body>" + sMessage.replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>") + "</body></html>");
  }
}