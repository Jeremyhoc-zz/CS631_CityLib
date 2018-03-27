package com.citylib;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

// Extend HttpServlet class
@WebServlet("/Logout")
public class Logout extends HttpServlet {

 public void doGet(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {

  //Check User Session
  HttpSession session = request.getSession(false);
  if (session != null)
	  session.invalidate();
  String redirect = new String("Dashboard");
  response.setStatus(response.SC_MOVED_TEMPORARILY);
  response.setHeader("Location", redirect);
 }

 public void doPost(HttpServletRequest request,
  HttpServletResponse response)
 throws ServletException, IOException {
  doGet(request, response);
 }
}
