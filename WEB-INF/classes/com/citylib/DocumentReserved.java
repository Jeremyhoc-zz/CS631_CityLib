package com.citylib;
import com.citylib.dbConnection;

// Import required java libraries
import java.io.*;
import java.util.Scanner;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.ResultSet;
import java.sql.SQLException;

// Extend HttpServlet class
@WebServlet("/DocumentReserved")
public class DocumentReserved extends HttpServlet {

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
  int ReaderID = 0;
  if (session == null) {
   // Not created yet.
   String redirect = new String("Login");
   response.setStatus(response.SC_MOVED_TEMPORARILY);
   response.setHeader("Location", redirect);
  } else {
   userPrivilege = (String) session.getAttribute("userPrivilege");
   ReaderID = (int) session.getAttribute("ReaderID");
   if (userPrivilege.equals("Reader")) {} else {
    String redirect = new String("Dashboard");
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", redirect);
   }
  }

  String title = "Documents Reserved";
  String docType = "<!doctype html>\n";
  String documents = "";
  try {
   dbConnection dbconn = new dbConnection();
   ResultSet rs = dbconn.executeSQL("SELECT Title, ResNumber, RTimeStamp, PubName FROM Document NATURAL JOIN Reserves NATURAL JOIN Publisher WHERE ReaderID = " + ReaderID + ";");
   while (rs.next()) {
    int ResNumber = rs.getInt("ResNumber");
    String Title = rs.getString("Title");
    String PubName = rs.getString("PubName");
    String RTimeStamp = rs.getString("RTimeStamp");
    String Status = "'Waiting for Pickup', or 'Picked up'";
    documents += "	<tr>\n" +
     "		<td data-order=\"" + Title + "\">" + Title + "</td>\n" +
     "		<td data-search=\"" + PubName + "\">" + PubName + "</td>\n" +
     "   <td data-search=\"" + RTimeStamp + "\">" + RTimeStamp + "</td>\n" +
     "  <td data-search=\"" + Status + "\">" + Status + "</td>\n" +
    "   <td>\n" +
    "   <input type=\"submit\" name=\"" + ResNumber  + "\" value=\"Cancel\" formnovalidate>\n</td>" +
     "	</tr>\n";
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
   inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/navbar_reader.htm"));
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
   "<th>Title</th>\n" +
   "<th>Publisher</th>\n" +
   "<th>Time of Reserve</th>\n" +
   "<th>Status</th>\n" +
   "<th>Action</th>\n" +
   "</tr>\n</thead>\n" +
   "<tfoot>\n<tr>\n" +
   "<th>Title</th>\n" +
   "<th>Publisher</th>\n" +
   "<th>Time of Reserve</th>\n" +
   "<th>Status</th>\n" +
   "<th>Action</th>\n" +
   "</tr>\n" +
   "</tfoot>\n" +
   "<tbody>\n" + documents + "</tbody>\n" +
   "</table>\n" +
/*   "<select name=\"action\" required>\n" +
    "<option value=\"\">None</option>\n" +
    "<option value=\"checkout\">Checkout Selections</option>\n" +
    "<option value=\"cancelreservation\">Cancel Reservation</option>\n" +
   "</select>\n" +
   "<input type=\"submit\" value=\"Process Action\" />\n" +*/
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
