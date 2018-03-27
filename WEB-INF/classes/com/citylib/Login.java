package com.citylib;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

// Extend HttpServlet class
@WebServlet("/Login")
public class Login extends HttpServlet {

 public void doGet(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();

  //Check User Session
  HttpSession session = request.getSession(false);
  String name, user_name, userPrivilege;
  name = user_name = userPrivilege = "";
  if (session == null) {
   out.println("<html>\n" +
    "<meta name=\"viewport\" content=\"width=device-width,intial-scale=1\">\n" +
    "<body>\n" +
    "<form action=\"LoginProcess\" method=\"POST\">\n" +
    "Reader Login\n" +
    "Card Number: <input type=\"text\" name=\"cardnum\" placeholder=\"Enter Card Number\" value=\"\">\n" +
    "<br>For test cases, login as any of the number from '1' to '15'.<br><br>OR<br><br>\n" +
    "Administrative Login\n" +
    "Username: <input type=\"text\" name=\"adminID\" placeholder=\"Enter administrator ID here\" value=\"\">\n" +
    "<br>\n" +
    "Password: <input type=\"password\" name=\"adminpwd\" placeholder=\"Enter password here\" value=\"\">\n" +
    "<br>\n" +
    "<input type=\"submit\" value=\"Login\">\n" +
    "</form>\n" +
    "The following administrator logins have been created:<br>\n" +
    "username: AP, HP, or JH\n<br>\n" +
    "password: aapala, himpatel, or jerhoc ... respectively\n" +
    "</body>\n" +
    "</html>");
  } else {
   name = (String)session.getAttribute("name");
   user_name = (String)session.getAttribute("user_name");
   userPrivilege = (String)session.getAttribute("userPrivilege");

   String redirect = new String("Dashboard");
   response.setStatus(response.SC_MOVED_TEMPORARILY);
   response.setHeader("Location", redirect);
  }

 }

 public void doPost(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
  doGet(request, response);
 }
}
