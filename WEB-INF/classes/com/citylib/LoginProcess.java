package com.citylib;
import com.citylib.dbConnection;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

// Extend HttpServlet class
@WebServlet("/LoginProcess")
public class LoginProcess extends HttpServlet {

 public void doGet(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
    response.setContentType("text/html");
  PrintWriter out = response.getWriter();

  try {
  Enumeration paramNames = request.getParameterNames();
   String paramName, paramValue = "";
    while (paramNames.hasMoreElements()) {
    paramName = (String) paramNames.nextElement();
    String[] paramValues = request.getParameterValues(paramName);
    paramValue = paramValues[0];
    out.println("paramName: " + paramName + ", paramValue: " + paramValue);
    }
   dbConnection dbconn = new dbConnection();

   ResultSet rs = null;
   if (!request.getParameter("cardnum").isEmpty()) {
     String SQLsearch = "SELECT ReaderID from Reader where ReaderID = ?;";
     PreparedStatement readerSearch = dbconn.prepareStatement(SQLsearch);
     readerSearch.setInt(1, Integer.valueOf(request.getParameter("cardnum")));
     rs = readerSearch.executeQuery();

     if (rs.next()) {
      HttpSession session = request.getSession(true);

      SQLsearch = "SELECT * from Reader where ReaderID = ?;";
      PreparedStatement statusSearch = dbconn.prepareStatement(SQLsearch);
      statusSearch.setInt(1, Integer.valueOf(request.getParameter("cardnum")));
      rs = statusSearch.executeQuery();
      if (rs.next()) {
        session.setAttribute("userPrivilege", "Reader");
        session.setAttribute("ReaderID", rs.getInt("ReaderID"));
        session.setAttribute("RType", rs.getString("RType"));
        session.setAttribute("RName", rs.getString("RName"));
        session.setAttribute("RAddress", rs.getString("RAddress"));
        session.setAttribute("Phone_Number", rs.getLong("Phone_Number"));
        session.setAttribute("MemStart", rs.getDate("MemStart"));
        session.setAttribute("Fine", 0);//rs.getInt("Fine"));
        session.setAttribute("NumResDocs", rs.getInt("NumResDocs"));
        session.setAttribute("NumBorDocs", rs.getInt("NumBorDocs"));
       out.println("hit");
    }
       String redirect = new String("Dashboard");
       response.setStatus(response.SC_MOVED_TEMPORARILY);
       response.setHeader("Location", redirect);
     } else {
    String redirect = new String("Login");
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", redirect);
   }
   } else if (!request.getParameter("adminID").isEmpty()) {
     out.println("admin");
     String SQLsearch = "SELECT LoginPassword as pass from AdminLogin where AdminID = ?;";
     PreparedStatement passwordSearch = dbconn.prepareStatement(SQLsearch);
     passwordSearch.setString(1, request.getParameter("adminID"));
     rs = passwordSearch.executeQuery();
     if (rs.next() && (request.getParameter("adminpwd")).equals(rs.getString("pass"))) {
       out.println("found admin");
      HttpSession session = request.getSession(true);

      session.setAttribute("AdminID", request.getParameter("adminID"));
      session.setAttribute("userPrivilege", "Admin");

     String redirect = new String("Dashboard");
     response.setStatus(response.SC_MOVED_TEMPORARILY);
     response.setHeader("Location", redirect);
     }
   } else {
    String redirect = new String("Login");
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", redirect);
   }
  } catch (Exception e) {
   out.println(e);
  }
 }

 public void doPost(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
  doGet(request, response);
 }
}
