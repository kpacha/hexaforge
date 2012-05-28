<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.hexaforge.core.GamePreferences" %>

<html>
  <head>
    <title>HexaForge</title>
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
	  <h2>HexaForge</h2>
	  <ul>
	  	<li><a href="/preferences" target="_blank">New game</a></li>
	  	<li><a href="/game" target="_blank">Game actions</a></li>
	  	<li><a href="/games" target="_blank">Game list</a></li>
	  	<li>
	  		<h3>Game board</h3>
		  	<form action="/board" method="get">
		    <div class="leftColumn"><label>pid:</label></div>
		    <div class="rightColumn">
		      <input name="pid" type="text" id="pid" value="" />
		      <input name="" type="submit" class="botton" value="GO" />
		    </div>
		  	</form>
	  	</li>
	  </ul>
    <%
	}
	%>
  </body>
</html>