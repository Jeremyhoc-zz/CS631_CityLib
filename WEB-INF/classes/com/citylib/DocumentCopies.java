package com.citylib;
import com.citylib.dbConnection;

// Import required java libraries
import java.io.*;
import java.util.Scanner;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.ResultSet;
import java.sql.SQLException;

// Extend HttpServlet class
@WebServlet("/DocumentCopies")
public class DocumentCopies extends HttpServlet {

 private String message;

 public void doGet(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {

  // Set response content type
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();

  //Check User Session/Privilege
  HttpSession session = request.getSession(false);
  String userPrivilege = "";
  if (session == null) {
   // Not created yet.
   String redirect = new String("Login");
   response.setStatus(response.SC_MOVED_TEMPORARILY);
   response.setHeader("Location", redirect);
  } else {
   userPrivilege = (String) session.getAttribute("userPrivilege");
   if (userPrivilege.equals("Admin")) {
   } else {
    String redirect = new String("Dashboard");
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", redirect);
   }
  }

  String title = "Document Copies";
  String docType = "<!doctype html>\n";
  String documents = "";
  int DocID = 0;

      Enumeration paramNames = request.getParameterNames();

      try {
        dbConnection dbconn = new dbConnection();

        String paramName, paramValue, redirect = "";
        while(paramNames.hasMoreElements()) {
          paramName = (String)paramNames.nextElement();
          String[] paramValues = request.getParameterValues(paramName);
          paramValue = paramValues[0];
          //out.println("paramName = " + paramName + "<br>paramValue = " + paramValue + "<br>");
            if (paramName.equals("viewdocuments_length")) {
              continue;
            }
          DocID = Integer.valueOf(paramName);
          String docCopyQuery = "SELECT Document.DocID as DocID, Title, PubName, LName, Copy.CopyNo as CopyNo, Position, RName, (SELECT DATEDIFF(RTimeStamp + Interval 14 Day, now())) as ReservedDue, (SELECT DATEDIFF(BTimeStamp + Interval 14 Day, now())) as BorrowedDue " +
                        "FROM Document " +
                        "NATURAL JOIN Publisher " +
                        "NATURAL JOIN Branch " +
                        "NATURAL JOIN Copy " +
                        "LEFT JOIN Borrows on Copy.CopyNo = Borrows.CopyNo AND Copy.DocID = Borrows.DocID " +
                        "LEFT JOIN Reserves on Copy.CopyNo = Reserves.CopyNo AND Copy.DocID = Reserves.DocID " +
                        "LEFT JOIN Reader ON (IF (Reserves.DocID IS NOT NULL, Reserves.ReaderID = Reader.ReaderID, Borrows.ReaderID = Reader.ReaderID)) " +
                        "WHERE Document.DocID = " + DocID +
                        " GROUP BY Copy.CopyNo;";
     //out.println(docCopyQuery);
     ResultSet rs = dbconn.executeSQL(docCopyQuery);
     while (rs.next()) {
      int CopyNo = rs.getInt("CopyNo");
      String Title = rs.getString("Title");
      String PubName = rs.getString("PubName");
      String LName = rs.getString("LName");
      String Position = rs.getString("Position");
      String RName = rs.getString("RName");
      int ReservedDue = rs.getInt("ReservedDue");
      int BorrowedDue = rs.getInt("BorrowedDue");
      String Location = "" + LName + " at Position " + Position;
      String Status = "" + RName + " has " + (!(ReservedDue == 0) ? "reserved" : "borrowed") + " this, due " + (!(ReservedDue == 0) ? (ReservedDue < 0 ? ReservedDue + " days ago." : "in " + ReservedDue + " days.") : (BorrowedDue < 0 ? BorrowedDue + " days ago." : "in " + BorrowedDue + " days."));
      documents += "	<tr>\n" +
       "		<td data-order=\"" + CopyNo + "\">" + CopyNo + "</td>\n" +
       "		<td data-search=\"" + Title + "\">" + Title + "</td>\n" +
       "    <td data-search=\"" + PubName + "\">" + PubName + "</td>\n" +
       "    <td data-search=\"" + Location + "\">" + Location + "</td>\n" +
       "		<td data-search=\"" + Status + "\">" + Status + "</td>\n";
      }
  }
    } catch (Exception e) {
       out.println(e);
    }

  out.println(docType +
   "<html>\n" +
   "<head>\n" +
   "<title>" + title + "</title>");

  try {
   Scanner inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/header.htm"));
   while (inputFile.hasNextLine()) {
    out.println(inputFile.nextLine());
   }
  } catch (Exception e) {
   out.println("Error: " + e);
  }

  out.println("\n<link rel=\"stylesheet\" type=\"text/css\" href=\"css/jquery.dataTables.min.css\">\n" +
   "<script type=\"text/javascript\" language=\"javascript\" src=\"js/jquery-1.12.3.js\">\n</script>\n" +
   "<script type=\"text/javascript\" language=\"javascript\" src=\"js/jquery.dataTables.min.js\">\n</script>\n" +
   "<script type=\"text/javascript\" class=\"init\">\n" +
   "$(document).ready(function() {\n" +
   "	$(\'#viewdocuments\').DataTable();\n" +
   /*"               dom: \'Bfrtip\',\n" +
   "               buttons: [\n" +
   "                       \'copyHtml5\',\n" +
   "                       \'excelHtml5\',\n" +
   "                       \'csvHtml5\',\n" +
   "                       \'pdfHtml5\'\n" +
   "               ]\n" +
   "       } );\n" +*/
   "} );\n" +
   "</script>\n" +
   "</head>\n\n" +
   "<body>\n");

  try {
   Scanner inputFile = null;
    inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/navbar_admin.htm"));
   while (inputFile.hasNextLine()) {
    out.println(inputFile.nextLine());
   }
  } catch (Exception e) {
   out.println("Error: " + e);
  }

  out.println("<h1 align=\"center\">" + title + "</h1>\n" +
   "<div style=\"margin: 0 auto; width:75%;\">\n" +
   "<form action=\"DocumentProcess\" method=\"POST\">\n" +
   "<table id=\"viewdocuments\" class=\"display\" cellspacing=\"0\" width=\"80%\">\n" +
   "<thead>\n<tr>\n" +
   "<th>CopyNo</th>\n" +
   "<th>Title</th>\n" +
   "<th>PubName</th>\n" +
   "<th>Location</th>\n" +
   "<th>Status</th>\n" +
   "</tr>\n</thead>\n" +
   "<tfoot>\n<tr>\n" +
   "<th>CopyNo</th>\n" +
   "<th>Title</th>\n" +
   "<th>PubName</th>\n" +
   "<th>Location</th>\n" +
   "<th>Status</th>\n" +
   "</tr>\n" +
   "</tfoot>\n" +
   "<tbody>\n" + documents + "</tbody>\n" +
   "</table>\n" +
    "<input type=\"submit\" name=\"" + DocID + "\" value=\"Add Copy\" />\n" +
    "</form></div>\n");

   try {
    Scanner inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/footer.htm"));
    while (inputFile.hasNextLine()) {
     out.println(inputFile.nextLine());
    }
   } catch (Exception e) {
    out.println("Error: " + e);
   }

   out.println("</body>\n</html>");
  }
  public void doPost(HttpServletRequest request,
   HttpServletResponse response)
  throws ServletException, IOException {
   doGet(request, response);
  }
 }
