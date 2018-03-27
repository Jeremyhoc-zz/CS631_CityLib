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
@WebServlet("/DocumentSearch")
public class DocumentSearch extends HttpServlet {

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
  int ReaderID, NumResDocs, NumBorDocs;
  ReaderID = NumResDocs = NumBorDocs = 0;
  if (session == null) {
   // Not created yet.
   String redirect = new String("Login");
   response.setStatus(response.SC_MOVED_TEMPORARILY);
   response.setHeader("Location", redirect);
  } else {
   userPrivilege = (String) session.getAttribute("userPrivilege");
   if (userPrivilege.equals("Reader") || userPrivilege.equals("Admin")) {
    if (userPrivilege.equals("Reader")) {
      ReaderID = (int) session.getAttribute("ReaderID");
      NumResDocs = (int) session.getAttribute("NumResDocs");
      NumBorDocs = (int) session.getAttribute("NumBorDocs");
    }
   } else {
    String redirect = new String("Dashboard");
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", redirect);
   }
  }

  String title = "Documents";
  String docType = "<!doctype html>\n";
  String documents = "";
  try {
   dbConnection dbconn = new dbConnection();
   String docQuery = "SELECT Document.DocID, Title, PubName, ResNumber, Reserves.ReaderID as ResReaderID, RTimeStamp, BorNumber, Borrows.ReaderID as BorReaderID, BTimeStamp, RName, Fine" +
   " FROM Document" +
   "  NATURAL JOIN Publisher" +
   "  LEFT JOIN Reserves on Document.DocID = Reserves.DocID " + (userPrivilege.equals("Reader") ? "AND Reserves.ReaderID = " + ReaderID : "") +
   "  LEFT JOIN Borrows ON Document.DocID = Borrows.DocID" + (userPrivilege.equals("Reader") ? " AND Borrows.ReaderID = " + ReaderID : "") +
   "  LEFT JOIN Reader ON (IF (Reserves.DocID IS NOT NULL, Reserves.ReaderID = Reader.ReaderID, Borrows.ReaderID = Reader.ReaderID))" +
   (userPrivilege.equals("Admin") ? " GROUP BY Document.DocID;" : ";");
   //out.println(docQuery);
   ResultSet rs = dbconn.executeSQL(docQuery);
   while (rs.next()) {
    int DocID = rs.getInt("DocID");
    String Title = rs.getString("Title");
    String PubName = rs.getString("PubName");
    int ResNumber = rs.getInt("ResNumber");
    int ResReaderID = rs.getInt("ResReaderID");
    String RTimeStamp = rs.getString("RTimeStamp");
    int BorNumber = rs.getInt("BorNumber");
    int BorReaderID = rs.getInt("BorReaderID");
    String BTimeStamp = rs.getString("BTimeStamp");
    String RName = rs.getString("RName");
    int Fine = rs.getInt("Fine");
    documents += "	<tr>\n" +
     "		<td data-order=\"" + DocID + "\">" + DocID + "</td>\n" +
     "		<td data-search=\"" + Title + "\">" + Title + "</td>\n" +
     "   <td data-search=\"" + PubName + "\">" + PubName + "</td>\n";
    if (userPrivilege.equals("Reader")) {
      if ((ResReaderID != 0) || (BorReaderID != 0)) {
        //out.println("Res match: " + ResReaderID + " ?= " + ReaderID + "\n<br>");
        //out.println("Bor match: " + BorReaderID + " ?= " + ReaderID + "\n<br>");
        if (ResReaderID == ReaderID) {
         documents += "   <td>\n" +
         "      <input type=\"submit\" name=\"" + ResNumber  + "\" value=\"Cancel\" formnovalidate>\n</td>";
        } else if (BorReaderID == ReaderID) {
         documents += "   <td>\n" +
         "      <input type=\"submit\" name=\"" + BorNumber + "\" value=\"Return\" formnovalidate>\n</td>";
        } else {
          if (!((NumResDocs + NumBorDocs) >= 10)) {
            String NoCopiesQuery = "SELECT COUNT(*) as NoCopiesLeft FROM Copy WHERE DocID = " + DocID + " AND CopyNo NOT IN " +
                                          "(SELECT CopyNo FROM Reserves WHERE DocID = " + DocID + ") AND CopyNo NOT IN " +
                                          "(SELECT CopyNo FROM Borrows WHERE DocID = " + DocID + ");";
            ResultSet rs2 = dbconn.executeSQL(NoCopiesQuery);
            int NoCopiesLeft = 0;
            if (rs2.next()) {
              NoCopiesLeft = rs2.getInt("NoCopiesLeft");
            }
            if (NoCopiesLeft > 0) {
              documents += "    <td>\n" +
               "      <input type=\"submit\" name=\"" + DocID + "\" value=\"Checkout\" formnovalidate>\n" +
               "     <input type=\"submit\" name=\"" + DocID + "\" value=\"Reserve\" formnovalidate>\n</td>";
             } else {
            documents += "<td>\nOut of Stock\n</td>";
             }
           } else {
            documents += "<td>\nYou've at least 10 books. Cancel/return some books first.\n</td>";
           }
        }
      } else {
          if (!((NumResDocs + NumBorDocs) >= 10)) {
            String NoCopiesQuery = "SELECT COUNT(*) as NoCopiesLeft FROM Copy WHERE DocID = " + DocID + " AND CopyNo NOT IN " +
                                          "(SELECT CopyNo FROM Reserves WHERE DocID = " + DocID + ") AND CopyNo NOT IN " +
                                          "(SELECT CopyNo FROM Borrows WHERE DocID = " + DocID + ");";
            ResultSet rs2 = dbconn.executeSQL(NoCopiesQuery);
            int NoCopiesLeft = 0;
            if (rs2.next()) {
              NoCopiesLeft = rs2.getInt("NoCopiesLeft");
            }
            if (NoCopiesLeft > 0) {
              documents += "    <td>\n" +
               "      <input type=\"submit\" name=\"" + DocID + "\" value=\"Checkout\" formnovalidate>\n" +
               "     <input type=\"submit\" name=\"" + DocID + "\" value=\"Reserve\" formnovalidate>\n</td>";
             } else {
            documents += "<td>\nOut of Stock\n</td>";
             }
       } else {
            documents += "<td>\nYou've at least 10 books. Cancel/return some books first.\n</td>";
       }
      }
    } else if (userPrivilege.equals("Admin")) {
     documents += "			<td data-search=\"status\">" +
     "       <input type=\"submit\" name=\"" + DocID + "\" value=\"Search Copies\" formnovalidate>\n</td>\n";
    }
    documents += "	</tr>\n";
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

  out.println("<h1 align=\"center\">" + title + "</h1>\n" +
   "<div style=\"margin: 0 auto; width:75%;\">\n" +
   "<form action=\"DocumentProcess\" method=\"POST\">\n" +
   "<table id=\"viewdocuments\" class=\"display\" cellspacing=\"0\" width=\"80%\">\n" +
   "<thead>\n<tr>\n" +
   //"<th>Select</th>\n" +
   "<th>ID</th>\n" +
   "<th>Title</th>\n" +
   "<th>Publisher</th>\n");
   if (userPrivilege.equals("Reader")) out.println("<th>Actions</th>\n");
  if (userPrivilege.equals("Admin")) out.println("<th>Status</th>\n");
  out.println("</tr>\n</thead>\n" +
   "<tfoot>\n<tr>\n" +
   //"<th>Select</th>\n" +
   "<th>ID</th>\n" +
   "<th>Title</th>\n" +
   "<th>Publisher</th>\n");
   if (userPrivilege.equals("Reader")) out.println("<th>Actions</th>\n");
  if (userPrivilege.equals("Admin")) out.println("<th>Status</th>\n");
  out.println("</tr>\n" +
   "</tfoot>\n" +
   "<tbody>\n" + documents + "</tbody>\n" +
   "</table>\n" +
   /*"<select name=\"action\" required>\n" +
    "<option value=\"\">None</option>\n");
    if (userPrivilege.equals("Client")) {
      out.println("<option value=\"reserve\">Reserve Selections</option>\n" +
                  "<option value=\"checkout\">Checkout Selections</option>\n");
    } else if (userPrivilege.equals("Administrator")) {
      out.println("<option value=\"delete\">Delete Selections</option>\n");
    }
    out.println("</select>\n" +
    "<input type=\"submit\" value=\"Process Action\" />\n" + */
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
