<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.persistence.EntityManager" %>
<%@ page import="javax.persistence.Query" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.hexaforge.core.decorator.JsonDecorator" %>
<%@ page import="com.hexaforge.entity.GameEntity" %>
<%@ page import="com.hexaforge.core.interfaces.GameInterface" %>
<%@ page import="com.hexaforge.util.EMF" %>

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
	
	<h2>Listado partidas</h2>
	  
	<%
	EntityManager entityManager = EMF.getEntityManager();
		Query queryGames = entityManager
				.createQuery("SELECT game FROM GameEntity game ORDER BY game.nextCheck DESC, game.status ASC");
	List<GameEntity> games = queryGames.getResultList();
	if (games.isEmpty()) {
	%>
		<p>No hay partidas.</p>
	<%
	} else {
	%>
		<h4>Actualizado: <%= DateFormat.getDateTimeInstance().format((new Date()).getTime()) %> (GMT)</h4>
		<ul>
		<%
		for (GameEntity g : games) {
			if(g.getId() != null){
				GameInterface game = JsonDecorator.getInstance().deserializeGame(g.getGame());
		%>
				<li>
					<h3>Partida <b><%= g.getId() %></b>. Estado: [<%= game.getStatus() %>].</h3>
				<ul>
					<li>Preferencias: <%= game.getPreferences().toString() %></li>
					<li>Jugadores: <%= game.getPlayers() %></li>
					<li>Tablero: <%= game.getBoard() %></li>
					<li>Turnos realizados: <%= game.getTurn() %></li>
					<li>Tablero: <a href="/board?pid=<%= g.getId() %>" target="_blank">Ir al tablero</a></li>
					<% 
					if(game.isJoinable()) {
					%>
						<li>
						<form action="/hexagame" method="post">
						<input type="hidden" name="pid" value="<%= g.getId()%>"/>
						<input type="hidden" name="aid" value="join"/>
						<button value="submit" value="submit" >Entrar</button>
						</form>
						</li>
					<%
					} else if (!game.isFinished()) {
					%>
						<li>Proximo incremento de turnos: <%= DateFormat.getDateTimeInstance().format(game.getNextCheck()) %> (GMT)</li>
						<li>Turnos a incrementar: <%= game.getPreferences().getDeltaTurn() %> cada <%= game.getPreferences().getEtaTurn()/60000 %> minuto(s) [<%= game.getPreferences().getEtaTurn()/1000 %> segundo(s)]</li>
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
		%>
		</ul>
		</div>
	<%
	}
	entityManager.close();
}
%>

</body>
</html>