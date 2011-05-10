// JavaScript Document
	
	//variables globales
	var active_div=null;
	var active_div_range=new Array();
	var positions=new Array();
	var moviendo=false;
	var pieza_moviendo="";
	
	//rutas imágenes para piezas
	var clase_pieza=new Array();
	clase_pieza["r"]="rock";
	clase_pieza["t"]="scissors";
	clase_pieza["p"]="paper";
	clase_pieza["l"]="lizzard";
	clase_pieza["s"]="spock";
	
	//Info jugadores
	var color_jugador=new Array();
	color_jugador["1"]="red";
	color_jugador["2"]="blue";
	color_jugador["3"]="green";
	color_jugador["4"]="pink";
	color_jugador["5"]="brown";
	
	//simulación posición inicial tablero, la letra es la pieza y el número es el jugador
	positions["2-2"]="p1";
	positions["7-1"]="t1";
	positions["10-5"]="l2";
	positions["1-1"]="s2";
	positions["2-1"]="l2";
	positions["5-4"]="p3";
	positions["2-3"]="s3";
	positions["3-2"]="t4";
	positions["8-3"]="s3";
	positions["9-2"]="s4";
	positions["1-4"]="p5";
	positions["12-4"]="l5";
	positions["12-1"]="r5";
	positions["3-1"]="r2";
	
    //Comprobación paridad
    function isEven(value){
      	return value%2 == 0;
    }
	//funcion que devuelve las casillas vecinas libres
	function get_destinos(celda){
		//celda es el id central, tipo hex6-2
		var lista=new Array();
        range = parseInt($("#range").html());
        leftOffset = parseInt(celda.substring(3,(celda.indexOf("-"))));
        topOffset = parseInt(celda.substring((celda.indexOf("-")+1)));
        if (isEven(leftOffset)) {
            up   = 1;
            down = 0;
        }else{
            up   = 0;
            down = 1;
        }
        for (i=1;i<(range+1);i++) {
            //encima
			lista.push("hex" + (leftOffset) + "-" + (topOffset-i));
            if (isEven(i)) {
                if (isEven(leftOffset)) {
                    addoneAbove = 0;
                    addoneBelow = 1;
                }else{
                    addoneAbove = 1;
                    addoneBelow = 0;
                }
            }else{
                addoneAbove = 0;
                addoneBelow = 0;
            }
            rangeOffset = Math.round(i/2);
            for (j=0;j<(range+rangeOffset-i);j++) {
                //arriba izquierda
                lista.push("hex" + (leftOffset-i) + "-" + (topOffset-up-j));
                //abajo izquierda
                lista.push("hex" + (leftOffset-i) + "-" + (topOffset+down+j));
                //encima derecha
                lista.push("hex" + (leftOffset+i) + "-" + (topOffset-up-j));
                //abajo derecha
                lista.push("hex" + (leftOffset+i) + "-" + (topOffset+down+j));
				//Esto de abajo solo vale si activamos rango para saltar varias casillas.
                if (addoneAbove != 0) {
                    //encima derecha
                    lista.push("hex" + (leftOffset+i) + "-" + (topOffset-up-j-addoneAbove));tracear(3);
                    //arriba izquierda
                    lista.push("hex" + (leftOffset-i) + "-" + (topOffset-up-j-addoneAbove)) ;
                }else if (addoneBelow != 0) {
                    //abajo derecha
                    lista.push("hex" + (leftOffset+i) + "-" + (topOffset+down+j+addoneBelow)) ;tracear(4);
                    //abajo izquierda
                    lista.push("hex" + (leftOffset-i) + "-" + (topOffset+down+j+addoneBelow)) ;
					
                }
            }
            //below
            lista.push("hex" + (leftOffset) + "-" + (topOffset+i));
			
			
        }
		for(i in lista){
			tx=parseInt(lista[i].substring(3,lista[i].indexOf("-")));
			ty=parseInt(lista[i].substring((lista[i].indexOf("-")+1)));
			tracear(tx+ty);
			if( tx>=0 && ty>=0 && tx<columnas && ty<filas){
				if(!$("#"+lista[i])[0].contiene()){
					$("#"+lista[i])[0].esperar();
					active_div_range.push(lista[i]);
				}
			}
			//tracear(lista[i]);
		}
		for(j in active_div_range){
			//tracear(active_div_range[j]);
		}
    }
	
	function checkandchange(value){
			if (value.length) {
                value.esperar();
				
            }
	}
	
	function enviar_movimiento(id_origen, id_final){
		$("#marcador").html($("#marcador").html()+"enviar al servidor "+id_origen+" hacia "+ id_final+"<br>Respuesta: OK<br>");
		return true;	
	}
	function tracear(algo){
		$("#marcador").html(algo+$("#marcador").html());
	}

	//creación de una casilla y su comportamiento
	function createHexSpace(leftOffset,topOffset)
    {
        //Contenedor global, más grande, muestra el fondo hexagono
		var divTag = document.createElement("div");
		divTag.x=x;
		divTag.y=y;
        divTag.id = "hex" + x + "-" + y;
		divTag.title= x + "-" + y;
        divTag.setAttribute("align","center");
        divTag.style.left = leftOffset + "px";
        divTag.style.top = topOffset + "px";
        divTag.className ="hexspace";
		
		//Subcontenedor con la pieza, muestra el color y el gif de la pieza
		$("body").append('<div id="sub' + x + "-" + y+'"></div>');
		subdiv=$("#sub"+ x + "-" + y);
		subdiv.addClass ('pieza');
		if(positions[x+"-"+y]){
			subdiv.css("background-color",color_jugador[positions[x+"-"+y].charAt(1)]);
			subdiv.addClass(clase_pieza[positions[x+"-"+y].charAt(0)]);
		}
		$(divTag).append(subdiv);
		
		$(divTag).bind("mouseover", function() {
            if(!moviendo){
				this.encender();
				var is_in_range=false;
				for(i in active_div_range){
					if(this.attr("id")==active_div_range[i]){
						is_in_range=true;
					}
				}
				if(this!=active_div & !is_in_range){
					this.encender();
				}
			}
        });
        $(divTag).bind("mouseout", function() {
			this.apagar();
        });
		$(divTag).click(function() {
			//limpiar todos los fondos
			
			if(!moviendo){
				apagarTodo();
				//si no es movimiento correcto abortamos movimiento y salimos
				//TODO: que compruebe que la pieza es propia
				//TODO: en modo análisis no hace falta mirar que sea propia.
				if(!this.contiene()){
					active_div=null;
					moviendo=false;
					return;
				}else{
					active_div=this;
					this.fijar();
					destinos=get_destinos($(this)[0].id);
					for(i in destinos){
						destinos[i].esperar();
					}
					moviendo=true;
					pieza_moviendo=positions[this.x.toString()+"-"+this.y.toString()];
					return;
				}
				
			}
			//Soltar una pieza
			if(moviendo){
				if(this.contiene() /*&& no es mi color*/){
					//Esto sería un ataque, moverse a una celda ocupada que no es de mi color
					tipo_defensor=this.tipo();
					tipo_atacante=$("#"+active_div.id)[0].tipo();
					tracear(clase_pieza[tipo_atacante]+" contra "+clase_pieza[tipo_defensor]+"<br>");
				}
				moviendo=false;
				valido=$(this).hasClass("hex_esperar")&&!this.contiene();
				apagarTodo();
				//Si es válido hay que ponerla en la nueva posición
				if(valido){
					apagarTodo();
					id_origen=active_div.id;
					
					positions[id_origen.substring(3,(id_origen.length))]="";
					positions[this.x.toString()+"-"+this.y.toString()]=pieza_moviendo;					
					active_div=null;
					$(this).children().css("background-color",color_jugador[pieza_moviendo.charAt(1)]);
					$(this).children().addClass(clase_pieza[pieza_moviendo.charAt(0)]);
					//enviar_movimiento(id_origen, this.id);
					$(this).children().hide();
					$(this).children().fadeIn();$("#"+id_origen)[0].vaciar();
					
				}
				return;
			}
			
        });
		divTag.encender = function(){
			$(this).removeClass("hex_fijo hex_encendido");
			$(this).addClass("hex_encendido");
		}
		divTag.apagar = function(){
			$(this).removeClass("hex_encendido");
		}
		divTag.fijar = function(){
			$(this).removeClass("hex_fijo hex_esperar hex_encendido");
			$(this).addClass("hex_fijo");
		}
		divTag.esperar = function(){
			$(this).removeClass("hex_fijo hex_esperar hex_encendido");
			$(this).addClass("hex_esperar");
		}
		divTag.limpiar = function(){
			$(this).removeClass("hex_fijo hex_esperar hex_encendido");
		}
		divTag.vaciar = function(){
			$(this).children().removeClass("rock paper scissors lizzard spock");
			$(this).children().css("background-color","");
		}
		divTag.contiene = function(){
			return !(typeof positions[this.x.toString()+"-"+this.y.toString()]=="undefined" || positions[this.x.toString()+"-"+this.y.toString()]=="");
		}
		divTag.tipo = function(){
			return positions[this.x.toString()+"-"+this.y.toString()].charAt(0);
		}
        document.body.appendChild(divTag);
    }
	function apagarTodo(){
		while(active_div_range.length>0){
			eliminado=active_div_range.pop();
			$("#"+eliminado)[0].limpiar();
			
		}
		if(active_div){
			temp=active_div
			$(active_div).children().fadeOut(400,function(){
				   temp.limpiar();
				   $(temp).children().show();
			})
			//$(active_div).limpiar();
		}
	}
	function crearTablero(alto,ancho) {
		
        for (y=0; y<alto; y++) {
            baseTopOffset = (y * 82)+30;
            for (x=0; x<ancho; x++) {
                leftOffset = x * 73+30;
                if (!isEven(x)) {
                    topOffset = baseTopOffset + 41;
                }else{
                    topOffset = baseTopOffset;
                }
                createHexSpace(leftOffset,topOffset);
            }
        }
    }
	
	 $(document).ready( 
		function() { 
			filas=7;
			columnas=15
            crearTablero(filas,columnas);
			$("#floorplan").css("width",columnas*73+50);
			$("#floorplan").css("height",filas*82+60);
			$("body").hide()
			$("body").fadeIn()
			//$("body").fadeOut(500,function(){$("body").fadeIn()});
			$("#info").html('Nombre: <span style="background-color:green">Baterpruf</span> - Piezas: 4 - Turnos: 5 - <a href="#">Analizar</a> ------ Colores: <span style="background-color:blue">Kandahar</span> - <span style="background-color:red">Koco</span> - <span style="background-color:brown">Kpacha</span> - <span style="background-color:pink">Protoss</span>');
			tracear("Traces:<br>");
			
        }
    );
