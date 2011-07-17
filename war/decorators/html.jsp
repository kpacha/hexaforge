<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
    <html>
        <head>
    		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
            <title>Hexaforge - <decorator:title default="Welcome!" /></title>
			<link href="/static/main.css" rel="stylesheet" type="text/css">
            <decorator:head />
        </head>
        <decorator:usePage id="base" />
        <body>
	        <div id="header_container">
	  			<div id="header">
				    <div id="logo">
				      <div id="login">
				        <p>Hola, <decorator:getProperty property="meta.user" default="Anonnymous" />!
						<a href="<decorator:getProperty property="meta.authLink" />"><decorator:getProperty property="meta.authLine" default="Login" /></a>
				 	  </div>
				    </div>
		        	<ul>
				      <li><a href="/games.jsp" target="_blank">Listado</a></li>
				      <li><a href="/preferences.jsp" target="_blank">Nueva</a></li>
				      <li><a href="/tablero.html" target="_blank">Tablero</a></li>
				    </ul>
				    <h1>El mejor juego de la historia.</h1>
			  	</div>
			</div>
			
			<div id="body1">
			  <div id="news">
			    <span class="new">Tenemos nuevo template!!</span>
			  </div>
			</div>
		
			<decorator:body />
            
			<div id="bottomPan">
			  <div id="bottomMainPan">
			    <div id="bottomBorderPan">
			      <h2>more tips</h2>
			      <h3>more links</h3>
			      <h4>announcements </h4>
			      <ul>
			        <li><a href="http://www.free-css.com/">Lorem Ipsum has been the</a></li>
			        <li><a href="http://www.free-css.com/">industry's standard dum</a></li>
			        <li><a href="http://www.free-css.com/">text ever since the 1500s, </a></li>
			        <li><a href="http://www.free-css.com/">when an unknown printer</a></li>
			        <li><a href="http://www.free-css.com/">galley of type and</a></li>
			        <li><a href="http://www.free-css.com/">scrambled make a typ</a></li>
			        <li><a href="http://www.free-css.com/">especimen </a></li>
			        <li><a href="http://www.free-css.com/">has survived</a></li>
			      </ul>
			      <ul>
			        <li><a href="http://www.free-css.com/">Lorem Ipsum has been the</a></li>
			        <li><a href="http://www.free-css.com/">industry's standard dum</a></li>
			        <li><a href="http://www.free-css.com/">text ever since the 1500s, </a></li>
			        <li><a href="http://www.free-css.com/">when an unknown printer</a></li>
			        <li><a href="http://www.free-css.com/">galley of type and</a></li>
			        <li><a href="http://www.free-css.com/">scrambled make a typ</a></li>
			        <li><a href="http://www.free-css.com/">especimen </a></li>
			        <li><a href="http://www.free-css.com/">has survived</a></li>
			      </ul>
			      <ul>
			        <li><a href="http://www.free-css.com/">Lorem Ipsum has been the</a></li>
			        <li><a href="http://www.free-css.com/">industry's standard dum</a></li>
			        <li><a href="http://www.free-css.com/">text ever since the 1500s, </a></li>
			        <li><a href="http://www.free-css.com/">when an unknown printer</a></li>
			        <li><a href="http://www.free-css.com/">galley of type and</a></li>
			        <li><a href="http://www.free-css.com/">scrambled make a typ</a></li>
			        <li><a href="http://www.free-css.com/">especimen </a></li>
			        <li><a href="http://www.free-css.com/">has survived</a></li>
			      </ul>
			    </div>
			  </div>
			</div>
			<div id="footermainPan">
			  <div id="footerPan">
			    <div id="footerlogoPan"><a href="http://www.free-css.com/"><img src="/static/images/footerlogo.gif" title="Total Management" alt="Total Management" width="290" height="36" border="0" /></a></div>
			    <ul>
			      <li><a href="http://www.free-css.com/">Home</a>| </li>
			      <li><a href="http://www.free-css.com/">About</a> | </li>
			      <li><a href="http://www.free-css.com/">Whats new</a>| </li>
			      <li><a href="http://www.free-css.com/">Forum</a> | </li>
			      <li><a href="http://www.free-css.com/">Solution</a> |</li>
			      <li><a href="http://www.free-css.com/">Contact</a> </li>
			    </ul>
			    <p class="copyright">©total management. all right reserved.</p>
			    <ul class="templateworld">
			      <li>design by:</li>
			      <li><a href="http://www.templateworld.com" target="_blank">Template World</a></li>
			    </ul>
			    <div id="footerPanhtml"><a href="http://validator.w3.org/check?uri=referer" target="_blank">XHTML</a></div>
			    <div id="footerPancss"><a href="http://jigsaw.w3.org/css-validator/check/referer" target="_blank">css</a></div>
			  </div>
			</div>
        </body>
    </html>