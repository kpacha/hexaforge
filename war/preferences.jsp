<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.hexaforge.core.GamePreferences" %>

<html>
<body>

<%
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
if (user != null) {
%>
<p>Hola, <%= user.getNickname() %>!
(<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">cerrar sesión</a>.)</p>
<%
GamePreferences p = new GamePreferences();
%>

<form action="/hexagame" method="post">
<div>initialDeltaTurn:<input type="text" name="initialDeltaTurn" value="<%= p.getInitialDeltaTurn() %>"></input></div>
<div>deltaTurn:<input type="text" name="deltaTurn" value="<%= p.getDeltaTurn() %>"></input></div>
<div>etaTurn:<input type="text" name="etaTurn" value="<%= p.getEtaTurn() %>"></input></div>
<div><input type="submit" value="Create Game!" />
<input type="hidden" name="aid" value="new"></div>
</form>

<%
} else {
%>
<p>Hola!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Logueate</a>
para continuar.</p>
<%
}
%>
</body>
</html>

