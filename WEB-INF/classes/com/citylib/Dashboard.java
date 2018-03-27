package com.citylib;
import com.citylib.*;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

// Extend HttpServlet class
@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {

 // Method to handle GET method request.
 public void doGet(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
  // Set response content type
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();

  //Check User Session/Privilege
  HttpSession session = request.getSession(false);
  String userPrivilege, AdminID, RType, RName, RAddress, title, body;
  int ReaderID, Fine, NumResDocs, NumBorDocs;
  Long Phone_Number = 0L;
  Date MemStart;
  title = body = userPrivilege = AdminID = RType = RName = RAddress = "";
  ReaderID = Fine = NumResDocs = NumBorDocs = 0;
  if (session == null) {
   // Not created yet.
   String redirect = new String("Login");
   response.setStatus(response.SC_MOVED_TEMPORARILY);
   response.setHeader("Location", redirect);
  } else {
   userPrivilege = (String) session.getAttribute("userPrivilege");
   if (userPrivilege.equals("Reader")) {
    RType = (String) session.getAttribute("RType");
    RName = (String) session.getAttribute("RName");
    RAddress = (String) session.getAttribute("RAddress");
    ReaderID = (int) session.getAttribute("ReaderID");
    Fine = (int) session.getAttribute("Fine");
    NumResDocs = (int) session.getAttribute("NumResDocs");
    NumBorDocs = (int) session.getAttribute("NumBorDocs");
    Phone_Number = (Long) session.getAttribute("Phone_Number");
    MemStart = (Date) session.getAttribute("MemStart");

    title = "" + RName + "'s Dashboard";
    body = "\nReader Type: " + RType +
     "\n<br>Reader Address: " + RAddress +
     "\n<br>Reader Phone_Number: " + String.valueOf(Phone_Number).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3") +
     "\n<br>Total Fines: " + Fine +
     "\n<br>Reader Number of Reserved Documents: " + NumResDocs +
     "\n<br>Reader Number of Borrowed Documents: " + NumBorDocs +
     "\n<br>Reader Membership Start: " + MemStart +
     "\n<br>";

   } else if (userPrivilege.equals("Admin")) {
    AdminID = (String) session.getAttribute("AdminID");
    title = "Administrator " + AdminID + "'s Dashboard";
    body = "\n<<h3 align=\"center\">Top 10 most popular Documents of the year</h3>";
    try {
     dbConnection dbconn = new dbConnection();
     ResultSet rs = dbconn.executeSQL("Select Title, Count(*) AS Borr_Freq" +
      " from Borrows NATURAL JOIN Document" +
      " Group by DocID" +
      " Order by Borr_Freq DESC" +
      " Limit 10;");
     int i = 1;
     body += "<center>";
     while (rs.next()) {
      body += "" + i++ + "\n" + rs.getString("Title") + " has been borrowed " + rs.getInt("Borr_Freq") + " times this year.<br>";
     }
     body += "\n<br><b>Note: </b> Must fix sql here to account for 'of the year', not 'of all time'.</center>";
    } catch (Exception e) {
     out.println("Error: " + e);
    }
  }
}

    String docType = "<!doctype html>\n";

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

    out.println("</head>\n\n" +
     "<body>");

    /*  out.println(this.getServletContext().getContextPath());
      out.println(this.getServletContext().getRealPath(File.separator));
      out.println(this.getClass());
      out.println(this.getClass().getClassLoader());
      out.println(this.getClass().getClassLoader().getResourceAsStream("web-shared/navbar_client.htm"));*/

    try {
     Scanner inputFile = null;
     if (userPrivilege.equals("Reader"))
      inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/navbar_reader.htm"));
     else if (userPrivilege.equals("Admin"))
      inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/navbar_admin.htm"));
     while (inputFile.hasNextLine()) {
      out.println(inputFile.nextLine());
     }
    } catch (Exception e) {
     out.println("Error: " + e);
    }

    out.println("<h1 align=\"center\">" + title + "</h1>" + body);


    try {
     Scanner inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("./web-shared/footer.htm"));
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
