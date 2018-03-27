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
@WebServlet("/DocumentEdit")
public class DocumentEdit extends HttpServlet {

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
      int ReaderID = 0;
      if (session == null) {
       // Not created yet.
       String redirect = new String("Login");
       response.setStatus(response.SC_MOVED_TEMPORARILY);
       response.setHeader("Location", redirect);
      } else {
       userPrivilege = (String) session.getAttribute("userPrivilege");
       ReaderID = (int) session.getAttribute("ReaderID");
       if (userPrivilege.equals("Reader") || userPrivilege.equals("Admin")) {} else {
        String redirect = new String("Dashboard");
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", redirect);
       }
      }

      String docType = "<!doctype html>\n";
      String title = "Document Edit";
      /*String displayBody = "<form action=\"DocumentProcess\">\n" +
    		  			"<table width=\"100%\" border=\"1\" align=\"center\">\n" +
	    		        "<th>Select</th>" +
              	  "<th>ID</th>\n" +
	    		        "<th>Title</th>\n" +
              	  "<th>Publisher</th>\n";
      String id = "";
      String doc_title = "";*/

      Enumeration paramNames = request.getParameterNames();

      try {
    	  dbConnection dbconn = new dbConnection();
		      while(paramNames.hasMoreElements()) {
		    	  String paramName = (String)paramNames.nextElement();
		    	  String[] paramValues = request.getParameterValues(paramName);
		    	  String paramValue = paramValues[0];
            out.println("paramName = " + paramName + "<br>paramValue = " + paramValue + "<br>");
		    	  String paramValue2 = "";
		    	  if (paramValues.length > 1) {
		    		  paramValue2 = paramValues[1];
              out.println("paramValue2: " + paramValue2);
		          }
		    	  if (paramName.equals("viewdocuments_length")) {
            }
            if (paramValue.equals("Reserve")) {
              out.println("Reserve doc #" + paramName);
            }
            if (paramValue.equals("Return")) {
              if (paramName.split("_")[0].equals("res_")) {
                out.println("Return doc #" + paramName);
              } else if (paramName.split("_")[0].equals("bor_")) {
                out.println("Borrow doc #" + paramName);
              }
            }
            if (paramValue.equals("Search Copies")) {
              out.println("Search copies of doc #" + paramName);
            }
            if (paramValue.equals("Cancel Reservation")) {
              out.println("Cancel doc #" + paramName);
            }
          }

		    	  /*if (paramName.equals("action")) {
		    		  if (paramValue.equals("delete")) {
		    			  title = "Confirm Documents for Deletion";
		    			  displayBody += "</table>\n<br>\n" +
		    							 "<input type=\"submit\" name=\"DocumentAction\" value=\"Delete\">\n</form>\n<br>\n" +
		    							 "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n<br>\n";
                       break;
		    		  } else if (paramValue.equals("reserve")) {
		    			  title = "Confirm Documents for Reservation";
		    			  displayBody += "</table>\n<br>\n" +
		    							 "<input type=\"submit\" name=\"DocumentAction\" value=\"Reserve\">\n</form>\n<br>\n" +
		    							 "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n<br>\n";
                break;
              } else if (paramValue.equals("checkout")) {
		    			  title = "Confirm Documents for Checkout";
		    			  displayBody += "</table>\n<br>\n" +
		    							 "<input type=\"submit\" name=\"DocumentAction\" value=\"Checkout\">\n</form>\n<br>\n" +
		    							 "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n<br>\n";
                break;
              } else if (paramValue.equals("cancelreservation")) {
		    			  title = "Confirm Document Reservation Cancellations";
		    			  displayBody += "</table>\n<br>\n" +
		    							 "<input type=\"submit\" name=\"DocumentAction\" value=\"CancelReservation\">\n</form>\n<br>\n" +
		    							 "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n<br>\n";
                break;
              } else if (paramValue.equals("return")) {
		    			  title = "Confirm Document Returns";
		    			  displayBody += "</table>\n<br>\n" +
		    							 "<input type=\"submit\" name=\"DocumentAction\" value=\"Return\">\n</form>\n<br>\n" +
		    							 "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n<br>\n";
                break;
              }
		    	  }
		    	  try {
		    		  double d = Double.parseDouble(paramName);
			    	  id = paramName;
			    	  ResultSet rs = dbconn.executeSQL("SELECT * FROM category where category_id = " + id);
			    	  if (rs.next())
			    		  doc_title = rs.getString("category_name");
		    	  } catch(NumberFormatException nfe) {
		    		  out.println(nfe);
		    	  }
		    	  if (paramValue.equals("Edit") || paramValue2.equals("Edit")) {
		    		  title = "Editing Document " + doc_title;
		    		  displayBody = "<form action=\"DocumentProcess\">\n" +
		    				  "Document ID: <input type=\"text\" name=\"Document_id\" value=\"" + id + "\" disabled>\n<br>\n" +
                  "Document Name: <input type=\"text\" name=\"edit_" + id + "\" value=\"" + doc_title + "\">\n<br>\n" +

		    				  "<input type=\"submit\" name=\"DocumentAction\" value=\"Edit\">\n<br>\n</form>\n" +
		    				  "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n<br>\n";
		    		  break;
		    	  } else {
		    		  displayBody += "<tr>\n	<td><input type=\"checkbox\" name=\"" + id + "\" /></td>\n" +
		    				  		"<td>" + id + "</td>\n" +
		    				  		"<td>" + doc_title + "</td>\n" +
                      //"<td>" + doc_publisher + "</td>\n" +
                      "</tr>\n";
	    		  }
		      }*/
      } catch(Exception e) {
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

        out.println("</head>\n\n" +
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
        displayBody);

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
