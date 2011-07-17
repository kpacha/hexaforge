<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.hexaforge.core.GamePreferences" %>

<html>
  <head>
    <title>Hexaforge</title>
    <%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user != null) {
	%>
    <meta name="user" content="<%= user.getNickname() %>">
    <meta name="authLink" content="<%= userService.createLogoutURL(request.getRequestURI()) %>">
    <meta name="authLine" content="Logout">
	<%
	} else {
	%>
    <meta name="user" content="Annonymous">
    <meta name="authLink" content="<%= userService.createLoginURL(request.getRequestURI()) %>">
    <meta name="authLine" content="Login">
	<%
	}
	%>
  </head>

  <body>
    <% GamePreferences p = new GamePreferences(); %>
	<div id="bodymiddlePan">
		<h2>Listado partidas</h2>
		<form action="/hexagame" method="post">
		<ul>
		<li>
			<div class="leftColumn"><label>initialDeltaTurn:</label></div>
			<div class="rightColumn"><input type="text" name="initialDeltaTurn" value="<%= p.getInitialDeltaTurn() %>"></input></div>
		</li>
		<li>
			<div class="leftColumn"><label>deltaTurn:</label></div>
			<div class="rightColumn"><input type="text" name="deltaTurn" value="<%= p.getDeltaTurn() %>"></input></div>
		</li>
		<li>
			<div class="leftColumn"><label>etaTurn:</label></div>
			<div class="rightColumn"><input type="text" name="etaTurn" value="<%= p.getEtaTurn() %>"></input></div>
		</li>
		<li>
			<input type="submit" value="Create Game!" />
			<input type="hidden" name="aid" value="new">
		</li>
		</form>
	</div>
  </body>
</html>

