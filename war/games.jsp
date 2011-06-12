<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.hexaforge.core.Game" %>
<%@ page import="com.hexaforge.util.PMF" %>

<html>
<body>

<%
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
if (user != null) {
%>
<p>Hello, <%= user.getNickname() %>! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
} else {
%>
<p>Hello!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
to include your name with greetings you post.</p>
<%
return;
}
%>

<%
PersistenceManager pm = PMF.get().getPersistenceManager();
String query = "select from " + Game.class.getName() + " order by created desc range 0,10";
List<Game> games = (List<Game>) pm.newQuery(query).execute();
if (games.isEmpty()) {
%>
<p>No hay partidas.</p>
<%
} else {
%>
<ul>
<%
for (Game g : games) {
if(g.getId() != null){
%>
<li>
	<p>Partida <b><%= g.getId() %></b>. Estado: [<%= g.getState() %>].</p>
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
		<li>Turnos a incrementar: <%= g.getGamePreferences().getDeltaTrun() %></li>
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
</body>
</html>
