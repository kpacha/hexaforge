<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.hexaforge.core.GamePreferences" %>
<%@ page import="com.hexaforge.core.board.BoardImplementationEnum" %>

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
    <% GamePreferences p = new GamePreferences(); %>
	<div id="bodymiddlePan">
		<h2>Configuración de la nueva partida</h2>
		<form action="/hexagame" method="post">
		<ul>
		<li>
			<div class="leftColumn"><label>initialDeltaTurn:</label></div>
			<div class="rightColumn"><input type="text" name="idt" value="<%= p.getInitialDeltaTurn() %>"></input></div>
		</li>
		<li>
			<div class="leftColumn"><label>deltaTurn:</label></div>
			<div class="rightColumn"><input type="text" name="dt" value="<%= p.getDeltaTurn() %>"></input></div>
		</li>
		<li>
			<div class="leftColumn"><label>etaTurn:</label></div>
			<div class="rightColumn"><input type="text" name="et" value="<%= p.getEtaTurn() %>"></input></div>
		</li>
		<li>
			<div class="leftColumn"><label>Board Type:</label></div>
			<div class="rightColumn">
				<select name="bt" >
					<option value="<%= BoardImplementationEnum.HEXAGONAL.name() %>">Hexagonal</option>
					<option value="<%= BoardImplementationEnum.SQUARE.name() %>">Square</option>
				</select>
			</div>
		</li>
		<li>
			<div class="leftColumn"><label>Board X Size:</label></div>
			<div class="rightColumn"><input type="text" name="sx" value="<%= p.getBoardSizeX() %>"></input></div>
		</li>
		<li>
			<div class="leftColumn"><label>Board Y Size:</label></div>
			<div class="rightColumn"><input type="text" name="sy" value="<%= p.getBoardSizeY() %>"></input></div>
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
  </body>
</html>