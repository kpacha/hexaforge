<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.hexaforge.core.Game" %>
<%@ page import="com.hexaforge.util.PMF" %>

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
if (user != null) {
%>
<p>Hola, <%= user.getNickname() %>!
(<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">cerrar sesión</a>.)</p>
<%
} else {
%>
<p>Hola!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Logueate</a>
para continuar.</p>
<%
return;
}
%>
 	  </div>
    </div>
    <%@include file='header.htm'%>
  </div>
</div>

<%@include file='news.htm'%>

<div id="bodymiddlePan">
  <h2>Listado partidas</h2>
  
<%
PersistenceManager pm = PMF.get().getPersistenceManager();
String query = "select from " + Game.class.getName() + " order by nextCheck asc range 0,10";
List<Game> games = (List<Game>) pm.newQuery(query).execute();
if (games.isEmpty()) {
%>
<p>No hay partidas.</p>
<%
} else {
%>
<h4>Actualizado: <%= DateFormat.getDateTimeInstance().format((new Date()).getTime()) %> (GMT)</h4>
<ul>
<%
for (Game g : games) {
if(g.getId() != null){
%>
<li>
	<h3>Partida <b><%= g.getId() %></b>. Estado: [<%= g.getState() %>].</h3>
	<ul>
		<li>Preferencias: <%= g.getPreferences() %></li>
		<li>Jugadores: <%= g.getPlayers() %></li>
		<li>Tablero: <%= g.getBoard() %></li>
		<li>Turnos realizados: <%= g.getTurn() %></li>
		<% 
		if(g.isJoinable()) {
		%>
		<li>
		<form action="/hexagame" method="post">
		<input type="hidden" name="pid" value="<%= g.getId()%>"/>
		<input type="hidden" name="aid" value="join"/>
		<button value="submit" value="submit" >Entrar</button>
		</form>
		</li>
		<%
		} else if (!g.isFinished()) {
		%>
		<li>Proximo incremento de turnos: <%= DateFormat.getDateTimeInstance().format(g.getNextCheck()) %> (GMT)</li>
		<li>Turnos a incrementar: <%= g.getGamePreferences().getDeltaTurn() %> cada <%= g.getGamePreferences().getEtaTurn()/60000 %> minuto(s) [<%= g.getGamePreferences().getEtaTurn()/1000 %> segundo(s)]</li>
		<%
		} else {
		%>
		<li>Partida finalizada!</li>
		<%
		}
		%>
	</ul>
</li>
<%
}
}
}
pm.close();
%>
</ul>
</div>

<%@include file='footer.htm'%>

</body>
</html>

