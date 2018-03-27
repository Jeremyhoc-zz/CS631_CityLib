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

// Extend HttpServlet class
@WebServlet("/ReaderSearch")
public class ReaderSearch extends HttpServlet {

 // Method to handle GET method request.
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

  String docType = "<!doctype html>\n";
  String title = "Readers";

  String readers = "";
  try {
   dbConnection dbconn = new dbConnection();
   dbConnection dbconn2 = new dbConnection();
   ResultSet rs = dbconn.executeSQL("SELECT * FROM Reader");
   while (rs.next()) {
    int ReaderID = rs.getInt("ReaderID");
    String RName = rs.getString("RName");
    String RType = rs.getString("RType");
    String RAddress = rs.getString("RAddress");
    String Phone_Number = rs.getString("Phone_Number");
    String MemStart = rs.getString("MemStart");
    String Fine = "";
    String NumBorRes = rs.getString("NumResDocs") + " reserved, " + rs.getString("NumBorDocs") + " borrowed";

    ResultSet rs2 = dbconn2.executeSQL("SELECT Avg(Fine) FROM Borrow_History WHERE ReaderID = " + ReaderID);
    while (rs2.next()) {
      Fine = String.format("%.2f", rs2.getFloat("Avg(Fine)"));
    }
    readers += "	<tr>\n" +
     "		<td data-search=\"" + ReaderID + "\">" + ReaderID + "</td>\n" +
     "		<td >" + RName + "</td>\n" +
     "		<td>" + RType + "</td>\n" +
     "		<td>" + RAddress + "</td>\n" +
     "		<td>" + Phone_Number + "</td>\n" +
     "		<td>" + MemStart + "</td>\n" +
     "		<td>" + Fine + "</td>\n" +
     "    <td>" + NumBorRes + "</td>\n" +
     "	</tr>\n";
   }
  } catch (Exception e) {
   out.println(e);
  }
  out.println(docType +
   "<html>\n" +
   "<head>\n" +
   "<title>" + title + "</title>\n");

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
   "	$(\'#viewreaders\').DataTable();\n" +
   "} );\n" +
   "</script>\n" +
   "</head>\n\n" +
   "<body>\n");

  try {
   Scanner inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/navbar_admin.htm"));
   while (inputFile.hasNextLine()) {
    out.println(inputFile.nextLine());
   }
  } catch (Exception e) {
   out.println("Error: " + e);
  }

  out.println("<h1 align=\"center\">" + title + "</h1>\n" +
   "<div style=\"margin: 0 auto; width:75%;\">\n" +
   //"<form action=\"ReaderEdit\" method=\"POST\">\n" +
   "<table id=\"viewreaders\" class=\"display\" cellspacing=\"0\" width=\"80%\">\n" +
   "  <thead>\n" +
   "    <tr>\n" +
   "      <th>ReaderID</th>\n" +
   "      <th>Name</th>\n" +
   "      <th>Type</th>\n" +
   "      <th>Address</th>\n" +
   "      <th>Phone</th>\n" +
   "      <th>Member Since</th>\n" +
   "      <th>Averaged Fine</th>\n" +
   "      <th>Borrow/Reserve</th>\n" +
   "    </tr>\n" +
   "  </thead>\n" +
   "  <tfoot>\n" +
   "    <tr>\n" +
   "      <th>ReaderID</th>\n" +
   "      <th>Name</th>\n" +
   "      <th>Type</th>\n" +
   "      <th>Address</th>\n" +
   "      <th>Phone</th>\n" +
   "      <th>Member Since</th>\n" +
   "      <th>Averaged Fine</th>\n" +
   "      <th>Borrow/Reserve</th>\n" +
   "    </tr>\n" +
   "  </tfoot>\n" +
   "  <tbody>\n" + readers + " </tbody>\n" +
   "</table>\n" +
   "</form>\n</div>\n");

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
 // Method to handle POST method request.
 public void doPost(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
  doGet(request, response);
 }
}
