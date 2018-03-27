package com.citylib;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.*;

// Extend HttpServlet class
@WebServlet("/ReaderAdd")
public class ReaderAdd extends HttpServlet {

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
  String title = "Add New Reader";

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

  out.println("</head>\n" +
   "<body>");

  try {
	  Scanner inputFile = new Scanner(this.getClass().getClassLoader().getResourceAsStream("web-shared/navbar_admin.htm"));
	  while (inputFile.hasNextLine()) {
		  out.println(inputFile.nextLine());
	  }
  } catch (Exception e) {
   out.println("Error: " + e);
  }

  out.println("<h1 align=\"center\">" + title + "</h1>\n" +
   "<form action=\"ReaderProcess\" method=\"Post\">\n" +
   "Reader Type: <select name=\"RType\">\n" +
   "    <option value=\"Primary Student\">Primary Student</option>\n" +
   "    <option value=\"Secondary Student\">Secondary Student</option>\n" +
   "    <option value=\"Educator\">Educator</option>\n" +
   "    <option value=\"Adult\">Adult</option>\n" +
   "    <option value=\"Senior Citizen\">Senior Citizen</option>\n" +
   "  </select>\n<br>" +
   "Reader Full Name: <input type=\"text\" name=\"RName\" maxlength=\"35\" required>\n<br>" +
   "Reader Address: <input type=\"text\" name=\"Address\" required>\n<br>" +
   "Reader Phone (only digits): <input type=\"tel\" name=\"phone_number\" maxlength=\"10\" pattern=\"\\d{10}\" required>\n<br>" +
   "<input type=\"submit\" name=\"Add Reader\" value=\"Add Reader\">\n<br>" +
   "\n</form>\n" +
   "<input type=\"submit\" value=\"Cancel\" onClick=\"history.go(-1);return true;\">\n");

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
