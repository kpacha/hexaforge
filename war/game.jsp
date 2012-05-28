<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.hexaforge.core.GamePreferences" %>

<html>
  <head>
    <title>Hexaforge</title>
  </head>

  <body>
    <%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user == null) {
	%>
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Login</a>
	<%
	} else {
	%>
      <p>Hi, <%= user.getNickname() %>. <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Logout</a></p>
	  <h2>Otras acciones</h2>
	  <form action="/hexagame" method="post">
	    <div class="leftColumn"><label>pid:</label></div>
	    <div class="rightColumn">
	      <input name="pid" type="text" id="pid" value="" />
	      <select name="aid"/>
	          <option name="join" value="join">join</option>
	          <option name="quit" value="quit">quit</option>
	          <option name="start" value="start">start</option>
	      </select>
	      <input name="" type="submit" class="botton" value="GO" />
	    </div>
	  </form>
    
    
    <%
	}
	%>
  </body>
</html>