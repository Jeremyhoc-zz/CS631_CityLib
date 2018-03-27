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
@WebServlet("/ReaderProcess")
public class ReaderProcess extends HttpServlet {

 // Method to handle GET method request.
 public void doGet(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {

  // Set response content type
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();

  Enumeration paramNames = request.getParameterNames();

  try {
   dbConnection dbconn = new dbConnection();

   String paramName, paramValue = "";
   String[] readerInfo = new String[12];
   int i = 0;

   while (paramNames.hasMoreElements()) {
    paramName = (String) paramNames.nextElement();

    String[] paramValues = request.getParameterValues(paramName);
    paramValue = paramValues[0];

    if (paramValue.equals("Add Reader")) {
      String SQLaddReader = "INSERT INTO Reader (RType, RName, RAddress, Phone_Number, MemStart, Fine, NumResDocs, NumBorDocs) VALUES (?, ?, ?, ?, curdate(), 0.00, 0, 0);";
      PreparedStatement addReader = dbconn.prepareStatement(SQLaddReader);
      addReader.setString(1, readerInfo[0]);
      addReader.setString(2, readerInfo[1]);
      addReader.setString(3, readerInfo[2]);
      addReader.setString(4, readerInfo[3]);
      addReader.executeUpdate();

      String redirect = new String("ReaderSearch");
      response.setStatus(response.SC_MOVED_TEMPORARILY);
      response.setHeader("Location", redirect);
      break;
     /*} else if (paramValue.equals("Save")) {
      for (String c: readerInfo)
       out.println(c);
      String SQLedit = "UPDATE `greenway`.`user` SET `user_name` = ?, `user_status` = ?, `email` = ?, `phone_number` = ?, `address` = ?, `town` = ?, `state` = ?, `zip` = ?, `first_name` = ?, `last_name` = ?, `active` = ? WHERE `user`.`user_name` = ?;";
      out.println(SQLedit);
      PreparedStatement editreader = dbconn.prepareStatement(SQLedit);
      out.println("prepare statement");
      editreader.setString(1, readerInfo[8]);
      editreader.setString(2, readerInfo[9]);
      editreader.setString(3, readerInfo[2]);
      editreader.setString(4, readerInfo[3]);
      editreader.setString(5, readerInfo[4]);
      editreader.setString(6, readerInfo[5]);
      editreader.setString(7, readerInfo[6]);
      editreader.setInt(8, Integer.valueOf(readerInfo[7]));
      editreader.setString(9, readerInfo[0]);
      editreader.setString(10, readerInfo[1]);
      editreader.setString(11, (readerInfo[10].equals("on") ? "Yes" : "No"));
      editreader.setString(12, readerInfo[8]);
      editreader.executeUpdate();

      String redirect = new String("ReaderSearch");
      response.setStatus(response.SC_MOVED_TEMPORARILY);
      response.setHeader("Location", redirect);
      break;*/
     } else {
       out.println("paramName = " + paramName + "<br>paramValue = " + paramValue + "<br>");
       readerInfo[i++] = paramValues[0];
     }
    }
   //}
  } catch (Exception e) {
   out.println("Error: " + e);
   out.println(e);
  }
 }
 // Method to handle POST method request.
 public void doPost(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
  doGet(request, response);
 }
}
