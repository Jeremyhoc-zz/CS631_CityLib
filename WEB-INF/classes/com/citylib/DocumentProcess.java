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
@WebServlet("/DocumentProcess")
public class DocumentProcess extends HttpServlet {

 // Method to handle GET method request.
 public void doGet(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
  // Set response content type
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();

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

   if (userPrivilege.equals("Reader") || userPrivilege.equals("Admin")) {
    if (userPrivilege.equals("Reader")) {
      ReaderID = (int) session.getAttribute("ReaderID");
    }
   } else {
    String redirect = new String("Dashboard");
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", redirect);
   }
  }
  Enumeration paramNames = request.getParameterNames();

  try {
   dbConnection dbconn = new dbConnection();

   String paramName, paramValue, redirect = "";
   String[] addDocParamName = new String[32];
   String[] addDocParamValue = new String[32];
   int iDoc = 0;
   while (paramNames.hasMoreElements()) {
    paramName = (String) paramNames.nextElement();
    String[] paramValues = request.getParameterValues(paramName);
    paramValue = paramValues[0];
    addDocParamName[iDoc] = (String)paramName;
    addDocParamValue[iDoc++] = (String)paramValue;
    //out.println("paramName = " + paramName + "<br>paramValue = " + paramValue + "<br>");
    if (paramName.equals("viewdocuments_length")) {
     continue;
    }
    ResultSet rs = null;
    if (paramValue.equals("Reserve")) {
     int CopyNo, LibID = 0;
     int DocID = Integer.valueOf(paramName);
     String getInfo = new String("SELECT LibID, MIN(c.CopyNo) as CopyNo FROM Copy as c WHERE DocID = " + DocID + " AND c.CopyNo NOT IN (SELECT CopyNo FROM Reserves WHERE DocID = " + DocID + ");");
     rs = dbconn.executeSQL(getInfo);
     if (rs.next()) {
      CopyNo = rs.getInt("CopyNo");
      LibID = rs.getInt("LibID");
      String updateReserve = new String("INSERT INTO Reserves (`ReaderID`, `DocID`, `CopyNo`, `LibID`, `RTimeStamp`) " +
       "VALUES (" + ReaderID + ", " + DocID + ", " + CopyNo + ", " + LibID + ", null);");
      out.println(updateReserve);
      dbconn.executeUpdate(updateReserve);
      session.setAttribute("NumResDocs", (int) session.getAttribute("NumResDocs") + 1);
      redirect = "DocumentSearch";
     }
    }
    if (paramValue.equals("Checkout")) {
     int CopyNo, LibID = 0;
     int DocID = Integer.valueOf(paramName);
     String getInfo = new String("SELECT LibID, MIN(c.CopyNo) as CopyNo FROM Copy as c WHERE DocID = " + DocID + " AND c.CopyNo NOT IN (SELECT CopyNo FROM Reserves WHERE DocID = " + DocID + ");");
     rs = dbconn.executeSQL(getInfo);
     if (rs.next()) {
      CopyNo = rs.getInt("CopyNo");
      LibID = rs.getInt("LibID");
      String checkoutBook = new String("INSERT INTO Borrows (`ReaderID`, `DocID`, `CopyNo`, `LibID`, `BTimeStamp`) " +
       "VALUES (" + ReaderID + ", " + DocID + ", " + CopyNo + ", " + LibID + ", null);");
      out.println(checkoutBook);
      dbconn.executeUpdate(checkoutBook);
      session.setAttribute("NumBorDocs", (int) session.getAttribute("NumBorDocs") + 1);
      redirect = "DocumentSearch";
     }
    }
    if (paramValue.equals("Return")) {
     int DocID = Integer.valueOf(paramName);
     String returnBorrowed = new String("DELETE FROM Borrows WHERE BorNumber = " + DocID + ";");
     out.println(returnBorrowed);
     dbconn.executeUpdate(returnBorrowed);
     session.setAttribute("NumBorDocs", (int) session.getAttribute("NumBorDocs") - 1);
     redirect = "DocumentSearch";
    }
    if (paramValue.equals("Cancel")) {
     int DocID = Integer.valueOf(paramName);
     String cancelReservation = new String("DELETE FROM Reserves WHERE ResNumber = " + DocID + ";");
     out.println(cancelReservation);
     dbconn.executeUpdate(cancelReservation);
     session.setAttribute("NumResDocs", (int) session.getAttribute("NumResDocs") - 1);
     redirect = "DocumentSearch";
    }
    if (paramValue.equals("Search Copies")) {
      int DocID = Integer.valueOf(paramName);
      displayCopiesPage(out, dbconn, DocID);
    }
    if (paramValue.equals("Add Document")) {
      out.println("Add Document");
      out.println("Doc type is: " + addDocParamValue[3]);
      int DocID = 0;
      int PublisherID = 0;
      int EditorID = 0;
      String getInfo = "SELECT MAX(DocID) + 1 as DocID FROM Document;";
      rs = dbconn.executeSQL(getInfo);
      if (rs.next()) {
        DocID = rs.getInt("DocID");
      }
      getInfo = "SELECT PublisherID FROM Publisher WHERE PubName = \"" + addDocParamValue[1] + "\";";
      rs = dbconn.executeSQL(getInfo);
      if (rs.next()) {
        PublisherID = rs.getInt("PublisherID");
      }
      getInfo = "SELECT Editor_ID FROM Chief_Editor WHERE EName = \"" + addDocParamValue[9] + "\";";
      rs = dbconn.executeSQL(getInfo);
      if (rs.next()) {
        EditorID = rs.getInt("Editor_ID");
      }
      out.println("hit");
      String SQLaddDocument = "INSERT INTO Document (`DocID`, `Title`, `PDate`, `PublisherID`) VALUES (" + DocID + ", \"" + addDocParamValue[0] + "\", \"" + addDocParamValue[2] + "\", " + PublisherID + ");";
        out.println(SQLaddDocument);
        dbconn.executeUpdate(SQLaddDocument);
      if (addDocParamValue[3].equals("Book")) {
        SQLaddDocument = "INSERT INTO Book (`DocID`, `ISBN`) VALUES (" + DocID + ", " + addDocParamValue[4] + ");";
        out.println(SQLaddDocument);
        dbconn.executeUpdate(SQLaddDocument);
        //redirect = "DocumentSearch";
      } else if (addDocParamValue[3].equals("Proceeding")) {
        SQLaddDocument = "INSERT INTO Proceedings (`DocID`, `CDate`, `CLocation`, `CEditor`) VALUES (" + DocID + ", \"" + addDocParamValue[5] + "\", \"" + addDocParamValue[6] + "\", \"" + addDocParamValue[7] + "\");";
        out.println(SQLaddDocument);
        dbconn.executeUpdate(SQLaddDocument);
      } else if (addDocParamValue[3].equals("Journal Volume")) {
        SQLaddDocument = "INSERT INTO Journal_Volume (`DocID`, `JVolume`, `Editor_ID`) VALUES (" + DocID + ", " + addDocParamValue[8] + ", " + EditorID + ");";
        out.println(SQLaddDocument);
        dbconn.executeUpdate(SQLaddDocument);
      }
     redirect = "DocumentSearch";
    }
    if (paramValue.equals("Add Copy")) {
      int DocID = Integer.valueOf(paramName);
      int CopyNo, LibID = 0;
      String Position = "";
      String getInfo = new String("SELECT LibID, MAX(CopyNo) + 1 as CopyNo, MAX(Position) as Position FROM Copy WHERE DocID = " + DocID + ";");
     rs = dbconn.executeSQL(getInfo);
     if (rs.next()) {
      CopyNo = rs.getInt("CopyNo");
      LibID = rs.getInt("LibID");
      Position = rs.getString("Position");
      Random r = new Random();
      int rand = r.nextInt(((Position.length() - 1) - 0) + 1);
      out.println("rand: " + rand);
      byte oneCharInPos = (byte)Position.charAt(rand);
      out.println("byte: " + oneCharInPos);
      oneCharInPos += (byte)1;
      out.println("byte + 1: " + oneCharInPos);
      String newPosition = Position.substring(0,rand) + (char)oneCharInPos + Position.substring((rand + 1), Position.length());
      out.println("new Position: " + oneCharInPos);
      String addDocumentCopy = new String("INSERT INTO Copy (`DocID`, `CopyNo`, `LibID`, `Position`) " +
       "VALUES (" + DocID + ", " + CopyNo + ", " + LibID + ", \"" + newPosition + "\");");
      out.println(addDocumentCopy);
     dbconn.executeUpdate(addDocumentCopy);

      displayCopiesPage(out, dbconn, DocID);
   }
  }
 }
   if (redirect.equals("")) {

   } else {
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", redirect);
   }
  } catch (Exception e) {
   out.println(e);
  }
 }
 // Method to handle POST method request.
 public void doPost(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
  doGet(request, response);
 }

 public void displayCopiesPage(PrintWriter out, dbConnection dbconn, int DocID) {
    try {
     String title = "Document Copies";
     String docType = "<!doctype html>\n";
     String documents = "";
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
      String ReservedDue = rs.getString("ReservedDue");
      String BorrowedDue = rs.getString("BorrowedDue");
      int ReservedDueInt = 0;
      int BorrwedDueInt = 0;
      try {
        ReservedDueInt = Integer.valueOf(ReservedDue);
      } catch (NumberFormatException e) {
          ReservedDueInt = 0;
      }
      try {
        BorrwedDueInt = Integer.valueOf(BorrowedDue);
      } catch (NumberFormatException e) {
          BorrwedDueInt = 0;
      }
      String Location = "" + LName + " at Position " + Position;
      String Status = ((ReservedDue != null || BorrowedDue != null) ?
        ("" + RName + " has " + (!(ReservedDueInt == 0) ? "reserved" : "borrowed") + " this, due " + (!(ReservedDueInt == 0) ? (ReservedDueInt < 0 ? ReservedDueInt + " days ago." : "in " + ReservedDueInt + " days.") : (BorrwedDueInt < 0 ? BorrwedDueInt + " days ago." : "in " + BorrwedDueInt + " days."))) : "In stock.");
      documents += "  <tr>\n" +
       "    <td data-order=\"" + CopyNo + "\">" + CopyNo + "</td>\n" +
       "    <td data-search=\"" + Title + "\">" + Title + "</td>\n" +
       "    <td data-search=\"" + PubName + "\">" + PubName + "</td>\n" +
       "    <td data-search=\"" + Location + "\">" + Location + "</td>\n" +
       "    <td data-search=\"" + Status + "\">" + Status + "</td>\n";
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
      "  $(\'#viewdocuments\').DataTable();\n" +
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
 } catch (Exception e) {
  out.println("Error: " + e);
 }
}
}
