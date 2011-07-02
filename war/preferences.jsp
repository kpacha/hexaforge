<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.hexaforge.core.GamePreferences" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Hexaforge</title>
	<link href="static/main.css" rel="stylesheet" type="text/css" />
  </head>

  <body>

<div id="header_container">
  <div id="header">
    <div id="logo">
      <div id="login">
<%
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
if (user == null) {
%>
<p>Hola!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Logueate</a>
para continuar.</p>
<%
} else {
%>
<p>Hola, <%= user.getNickname() %>!
(<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">cerrar sesión</a>.)</p>
<%
GamePreferences p = new GamePreferences();
%>
 	  </div>
    </div>
    <%@include file='header.htm'%>
  </div>
</div>

<%@include file='news.htm'%>

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
<%
}
%>

<%@include file='footer.htm'%>
</body>
</html>

