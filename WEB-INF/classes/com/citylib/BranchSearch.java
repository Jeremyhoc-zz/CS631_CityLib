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
@WebServlet("/BranchSearch")
public class BranchSearch extends HttpServlet {

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
  String title = "Branches";

  String branches = "";
  try {
   dbConnection dbconn = new dbConnection();
   ResultSet rs = dbconn.executeSQL("Select LName AS Branch_Name, LibID AS Branch_ID, LLocation AS Branch_Location from Branch");
   while (rs.next()) {
    String branch_Name = rs.getString("Branch_Name");
    String branch_ID = rs.getString("Branch_ID");
    String branch_Location = rs.getString("Branch_Location");
    branches += "	<tr>\n" +
     "		<td data-search=\"" + branch_ID + "\">" + branch_ID + "</td>\n" +
     "		<td >" + branch_Name + "</td>\n" +
     "		<td>" + branch_Location + "</td>\n" +
     "		<td><input type=\"submit\" name=\"" + branch_ID + "\" value=\"Top Docs/Readers\" formnovalidate></td>\n" +
     "	</tr>\n";
   }
  } catch (Exception e) {
   e.printStackTrace();
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
   "	$(\'#viewbranches\').DataTable();\n" +
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
   "<form action=\"BranchLookup\" method=\"POST\">\n" +
   "<table id=\"viewbranches\" class=\"display\" cellspacing=\"0\" width=\"80%\">\n" +
   "  <thead>\n" +
   "    <tr>\n" +
   "      <th>Branch ID</th>\n" +
   "      <th>Branch Name</th>\n" +
   "      <th>Branch Location</th>\n" +
   "      <th>Statistics</th>\n" +
   "    </tr>\n" +
   "  </thead>\n" +
   "  <tfoot>\n" +
   "    <tr>\n" +
   "      <th>Branch ID</th>\n" +
   "      <th>Branch Name</th>\n" +
   "      <th>Branch Location</th>\n" +
   "      <th>Statistics</th>\n" +
   "    </tr>\n" +
   "  </tfoot>\n" +
   "  <tbody>\n" + branches + "  </tbody>\n" +
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