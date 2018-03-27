package com.citylib;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

// Extend HttpServlet class
@WebServlet("/DocumentAdd")
public class DocumentAdd extends HttpServlet {

  // Method to handle GET method request.
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {

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
      String title = "Add New Document";

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

     out.println("<script type=\"text/javascript\">\n\n" +
         "function showDocumentType(elem) {\n" +
         "  if (elem.value == \"Book\") {\n" +
         "    document.getElementById('Book').style.display = 'block';\n" +
         "    document.getElementById('Proceeding').style.display = 'none';\n" +
         "    document.getElementById('Journal Volume').style.display = 'none';\n" +
         "  } else if (elem.value == \"Proceeding\") {\n" +
         "    document.getElementById('Book').style.display = 'none';\n" +
         "    document.getElementById('Proceeding').style.display = 'block';\n" +
         "    document.getElementById('Journal Volume').style.display = 'none';\n" +
         "  } else if (elem.value == \"Journal Volume\") {\n" +
         "    document.getElementById('Book').style.display = 'none';\n" +
         "    document.getElementById('Proceeding').style.display = 'none';\n" +
         "    document.getElementById('Journal Volume').style.display = 'block';\n" +
         "  }\n" +
         "}\n\n" +
         "</script>\n" +
         "</head>\n\n" +
        "<body>");

        try {
      	  Scanner inputFile = inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/navbar_admin.htm"));
      	  while (inputFile.hasNextLine()) {
      		  out.println(inputFile.nextLine());
      	  }
        } catch (Exception e) {
         out.println("Error: " + e);
        }

      	out.println("<h1 align=\"center\">" + title + "</h1>\n" +
        "<form action=\"DocumentProcess\">\n" +
        "Document Title: <input type=\"text\" name=\"Title\" required>\n<br>\n" +
        "Publisher: <select name=\"Publisher\">\n");
        try {
         dbConnection dbconn = new dbConnection();
         ResultSet rs = dbconn.executeSQL("SELECT PubName FROM Publisher;");
         while (rs.next())
          out.println("   <option value=\"" + rs.getString("PubName") + "\">" + rs.getString("PubName") + "</option>");
        } catch (Exception e) {
         out.println("Error: " + e);
        }

        out.println("</select>\n<br>\n" +
          "Published Date: <input type=\"date\" name=\"PDate\" value=\"2011-01-13\"/>\n<br>\n" +
          "Type of Document: <select name=\"docType\" id=\"docType\" onchange=\"showDocumentType(this)\">\n" +
              "<option selected=\"selected\" value=\"Select One\">Select One</option>\n" +
              "<option value=\"Book\">Book</option>\n" +
              "<option value=\"Proceeding\">Proceeding</option>\n" +
              "<option value=\"Journal Volume\">Journal Volume</option>\n<br>\n" +
            "</select>\n"  +
          "<div id=\"Book\" style=\"display:none\">\n" +
              "ISBN: <input type=\"number\" placeholder=\"e.g. 3210249632522\" name=\"ISBN\" min=\"1\" step='1' value=\"\" >\n<br>\n" +
          "<input type=\"submit\" name=\"Add Document\" value=\"Add Document\">\n" +
          "</div>\n" +
          "<div id=\"Proceeding\" style=\"display:none\">\n" +
              "Date: <input type=\"date\" name=\"CDate\" value=\"2011-01-13\"/>\n<br>\n" +
              "Location: <input type=\"text\" placeholder=\"City, St\" name=\"CLocation\" >\n<br>\n" +
              "Editor: <input type=\"text\" placeholder=\"Editor Full Name\" name=\"CEditor\" >\n<br>\n" +
          "<input type=\"submit\" name=\"Add Document\" value=\"Add Document\">\n" +
          "</div>\n" +
          "<div id=\"Journal Volume\" style=\"display:none\">\n" +
              "Journal Volume: <input type=\"number\" placeholder=\"e.g. 6\" name=\"JVolume\" min=\"1\" step='1' value=\"\" >\n<br>\n" +
              "Chief Editor: <select name=\"EditorID\">\n");

              try {
               dbConnection dbconn = new dbConnection();
               ResultSet rs = dbconn.executeSQL("SELECT EName FROM Chief_Editor;");
               while (rs.next())
                out.println("   <option value=\"" + rs.getString("EName") + "\">" + rs.getString("EName") + "</option>");
              } catch (Exception e) {
               out.println("Error: " + e);
              }
        out.println("</select>\n<br>\n" +
          "<input type=\"submit\" name=\"Add Document\" value=\"Add Document\">\n" +
          "</div>\n<br>\n" +
        "<br>\n</form>\n" +
		    "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n<br>");

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
