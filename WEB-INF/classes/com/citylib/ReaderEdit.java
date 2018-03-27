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
@WebServlet("/ReaderEdit")
public class ReaderEdit extends HttpServlet {

 // Method to handle GET method request.
 public void doGet(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
	 
  // Set response content type
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();

  //Check User Session/Privilege
  HttpSession session = request.getSession(false);
  String session_name, session_user_name, userPrivilege;
  session_name = session_user_name = userPrivilege = "";
  if (session == null) {
   // Not created yet.
   String redirect = new String("Login");
   response.setStatus(response.SC_MOVED_TEMPORARILY);
   response.setHeader("Location", redirect);
  } else {
   session_name = (String) session.getAttribute("name");
   session_user_name = (String) session.getAttribute("user_name");
   userPrivilege = (String) session.getAttribute("userPrivilege");
   if (userPrivilege.equals("Administrator")) {
   } else {
	   String redirect = new String("Dashboard");
	   response.setStatus(response.SC_MOVED_TEMPORARILY);
	   response.setHeader("Location", redirect);
   }
  }
  
  String docType = "<!doctype html>\n";
  String title = "Edit Reader";
  String displayBody = "<form action=\"ReaderProcess\">\n" +
   "<h1 align=\"center\">Confirm Accounts for Activation/Deactivation</h1>\n" +
   "   <table width=\"100%\" border=\"1\" align=\"center\">\n" +
   "      <th>Select</th>\n" +
   "      <th>Name</th>\n" +
   "      <th>ReaderID</th>\n" +
   "      <th>Status</th>\n" +
   "      <th>Email</th>\n" +
   "      <th>Phone</th>\n" +
   "      <th>Area</th>\n" +
   "      <th>Active</th>\n";

  String first_name, last_name, user_name, user_password, user_status, email, phone_number, address, town, state, active, pwValidate;
  first_name = last_name = user_name = user_password = user_status = email = phone_number = address = town = state = active = pwValidate = "";
  int zip = 0;

  Enumeration paramNames = request.getParameterNames();

  try {
   dbConnection dbconn = new dbConnection();
   while (paramNames.hasMoreElements()) {
    String paramName = (String) paramNames.nextElement();
    String[] paramValues = request.getParameterValues(paramName);
    String paramValue = paramValues[0];
    out.println("Name: " + paramName + "\nValue: " + paramValue);
    if (paramName.equals("viewreaders_length")) {
     continue;
    }
    if (paramName.equals("action")) {
     if (paramValue.equals("activate")) {
		    			  displayBody += "</table>\n<br>\n" +
		    							 "<input type=\"submit\" name=\"ReaderAction\" value=\"Activate\">\n</form>\n<br>\n" +
		    							 "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n<br>\n";
       break;
     } else if (paramValue.equals("deactivate")) {
		    			  displayBody += "</table>\n<br>\n" +
		    							 "<input type=\"submit\" name=\"ReaderAction\" value=\"Deactivate\">\n</form>\n<br>\n" +
		    							 "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n<br>\n";
       break;
      }
    }
    if (paramValue.equals("Edit")) {
     user_name = paramName;
     String SQLSearch = "SELECT * FROM `user` WHERE `user_name` = '" + user_name + "'";
     ResultSet rs = dbconn.executeSQL(SQLSearch);
     if (rs.next()) {
      first_name = rs.getString("first_name");
      last_name = rs.getString("last_name");
      user_name = rs.getString("user_name");
      user_status = rs.getString("user_status");
      email = rs.getString("email");
      phone_number = rs.getString("phone_number");
      address = rs.getString("address");
      town = rs.getString("town");
      state = rs.getString("state");
      zip = rs.getInt("zip");
      active = rs.getString("active");
      title = "Editing Reader " + first_name + " " + last_name;
     }
     SQLSearch = "SELECT aes_decrypt(`user_password`,'greenway') AS `pass` FROM `user` WHERE `user_name` = '" + user_name + "'";
     rs = dbconn.executeSQL(SQLSearch);
     if (rs.next()) {
      user_password = rs.getString("pass");
     }
     /*pwValidate = "<script language=\"javascript\" type=\"text/javascript\">\n" +
      "function passwordCheck(input) {\n" +
      "	if (input.value != document.getElementById(\"user_password\").value) {\n" +
      "		input.setCustomValidity(\"Password Must be Matching.\");\n" +
      "	} else {\n" +
      "		// input is valid -- reset the error message\n" +
      "		input.setCustomValidity(\"\");\n" +
      "	}\n" +
      "}\n" +
      "</script>\n";*/

     displayBody = "<h1 align=\"center\">" + title + "</h1>\n" +
      "<form action=\"ReaderProcess\" method=\"Post\">\n" +
      "First Name: <input type=\"text\" name=\"first_name\" value=\"" + first_name + "\" maxlength=\"30\" required>\n<br>" +
      "Last Name: <input type=\"text\" name=\"last_name\" value=\"" + last_name + "\" maxlength=\"30\" required>\n<br>" +
      "Email: <input type=\"email\" name=\"email\" value=\"" + email + "\" required>\n<br>" +
      "Phone (only digits): <input type=\"tel\" name=\"phone_number\" value=\"" + phone_number + "\" maxlength=\"10\" pattern=\"\\d{10}\" required>\n<br>" +
      "Address: <input type=\"text\" name=\"address\" value=\"" + address + "\" required>\n<br>" +
      "Town: <input type=\"text\" name=\"town\" value=\"" + town + "\" required>\n<br>" +
      "State (use 2-letter abbreviation): <input type=\"text\" name=\"state\" value=\"" + state + "\" required>\n<br>" +
      "ZIP: <input type=\"text\" name=\"zip\" value=\"" + zip + "\" pattern=\"\\d{5}\" required>\n<br><br>" +
      "Username: <input type=\"text\" name=\"user_name\" value=\"" + user_name + "\" readonly required>\n<br>" +
      "Password: <input type=\"password\" name=\"user_password\" value=\"" + user_password + "\" id=\"password\" disabled required>\n<br>" +
      //"Password Again: <input type=\"password\" name=\"passwordDoubleCheck\" value=\"" + user_password + "\" oninput=\"passwordCheck(this)\" disabled required>\n<br>" +
      "User Status: \n" +
      "	<select name=\"user_status\">\n" +
      "		<option value=\"Client\"" + (user_status.equals("Client") ? "selected=\"selected\"" : "") + ">Client</option>\n" +
      "		<option value=\"Administrator\"" + (user_status.equals("Administrator") ? "selected=\"selected\"" : "") + ">Administrator</option>\n" +
      "	</select>\n<br>" +
      "Active: <input type=\"checkbox\" name=\"active\" " + (active.equals("Yes") ? "checked" : "") + ">\n<br>" +
      "<input type=\"submit\" name=\"ReaderAction\" value=\"Save\">\n<br>" +
      "\n</form>\n" +
      "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n";
     break;
    } else {
     user_name = paramName;
     String SQLSearch = "SELECT * FROM `user` WHERE `user_name` = '" + user_name + "'";
     ResultSet rs = dbconn.executeSQL(SQLSearch);
     if (rs.next()) {
      first_name = rs.getString("first_name");
      last_name = rs.getString("last_name");
      user_name = rs.getString("user_name");
      user_status = rs.getString("user_status");
      email = rs.getString("email");
      phone_number = rs.getString("phone_number");
      address = rs.getString("address");
      town = rs.getString("town");
      state = rs.getString("state");
      zip = rs.getInt("zip");
      active = rs.getString("active");
      title = "Editing Reader " + first_name + " " + last_name;
     }
      displayBody += "<tr>\n	<td><input type=\"checkbox\" name=\"" + user_name + "\" /></td>\n" + 
                     "		<td>" + first_name + " " + last_name + "</td>\n" +
                     "		<td>" + user_name + "</td>\n" +
                     "		<td>" + user_status + "</td>\n" +
                     "		<td>" + email + "</td>\n" +
                     "		<td>" + phone_number + "</td>\n" +
                     "		<td>" + address + "\n<br>\n" + town + state + ", " + zip + "</td>\n" +
                     "		<td>" + active + "</td>\n" +
                     "</tr>\n";
        }
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

  out.println(pwValidate + "</head>\n" + "<body>\n");

  try {
	  Scanner inputFile = null;
		  inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/navbar_administrator.htm"));
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