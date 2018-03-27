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
@WebServlet("/BranchLookup")
public class BranchLookup extends HttpServlet {

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
  String title, displayBody;
  title = displayBody = "";

  Enumeration paramNames = request.getParameterNames();

  while (paramNames.hasMoreElements()) {
   String paramName = (String) paramNames.nextElement();
   String[] paramValues = request.getParameterValues(paramName);
   String paramValue = paramValues[0];
   //out.println("Name: " + paramName + "\nValue: " + paramValue);
   if (paramName.equals("viewbranches_length")) {
    continue;
   }
   if (paramValue.equals("Top Docs/Readers")) {
     //SQL GOES HERE
     try {
      dbConnection dbconn = new dbConnection();
         //SQL GOES HERE
       String BranchTop10BorrowedSearch = "Select LName as Branch_Name, Title, Count(*) AS Borr_Freq from Borrows NATURAL JOIN Branch JOIN Document ON Borrows.DocID=Document.DocID" +
                             " Where LibID = " + paramName + " Group by Borrows.DocID Order by Borr_Freq DESC Limit 10;";
       ResultSet rs = dbconn.executeSQL(BranchTop10BorrowedSearch);
       int i = 1;
       while (rs.next()) {
        if (i == 1) {
          String branch_Name = rs.getString("Branch_Name");
          title = "Statistics for Branch '" + branch_Name + "'";
          displayBody = "<h1 align=\"center\">Statistics for Branch '" + branch_Name + "'</h1>\n<hr><h4 align=\"center\">Top 10 Documents Borrowed</h4><center>";
        }
        displayBody += "\n<b>" + i++ + ")</b> " + rs.getString("Title") + " has been borrowed " + rs.getInt("Borr_Freq") + " times.<br>";
       }
        displayBody += "</center>\n<hr>\n";

       String Top10Readers = "Select RName,Count(*) AS NoDocsBorrowed " +
                              "from Borrows JOIN Reader ON Borrows.ReaderID=Reader.ReaderID " +
                              "where Borrows.LibID = " + paramName +
                              " Group by Borrows.ReaderID " +
                              "Order by NoDocsBorrowed DESC " +
                              "Limit 10;";

       rs = dbconn.executeSQL(Top10Readers);
       i = 1;
       while (rs.next()) {
        if (i == 1) {
          displayBody += "<h4 align=\"center\">Top 10 Most Frequent Borrowers and # of books they've borrowed.</h4>\n<center>";
        }
        displayBody += "\n<b>" + i++ + ")</b> " + rs.getString("RName") + " has borrowed " + rs.getInt("NoDocsBorrowed") + " books.<br>";
      }

    displayBody += "</center><br>\n";
     break;
     } catch (Exception e) {
      out.println(e);
     }
   }
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

  try {
	  Scanner inputFile = null;
		  inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/navbar_admin.htm"));
	  while (inputFile.hasNextLine()) {
		  out.println(inputFile.nextLine());
	  }
  } catch (Exception e) {
   out.println("Error: " + e);
  }

  out.println(displayBody);

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
