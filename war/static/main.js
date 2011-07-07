
// JavaScript Document
	
	//variables globales
	var active_div=null;
	var active_div_range=new Array();
	var positions=new Array();
	var moviendo=false;
	var pieza_moviendo="";
	var rango=1;//La distancia que se puede mover.
	
	//rutas im�genes para piezas
	var clase_pieza=new Array();
	clase_pieza["r"]="rock";
	clase_pieza["t"]="scissors";
	clase_pieza["p"]="paper";
	clase_pieza["l"]="lizzard";
	clase_pieza["s"]="spock";
	
	//Info jugadores
	var color_jugador=new Array();
	color_jugador["0"]="red";
	color_jugador["1"]="blue";
	color_jugador["2"]="green";
	color_jugador["3"]="pink";
	color_jugador["4"]="brown";
	id_jugador="baterpruf";
	
	//simulaci�n posici�n inicial tablero, la letra es la pieza y el n�mero es el jugador
	var positions;
	//positions["12-1"]="r5";
	//positions["3-3"]="s3";
	
    //Comprobaci�n paridad
    function isEven(value){
      	return value%2 == 0;
    }
    function getX(celda){
    	return parseInt(celda.substring(3,(celda.indexOf("-"))));
    }
    function getY(celda){
    	return parseInt(celda.substring((celda.indexOf("-")+1)));
    }
	function get_destinos(celda){
	//funcion que devuelve las casillas vecinas libres
		//celda es el id central, tipo hex6-2
		var lista=new Array();
        range = rango;
        leftOffset = getX(celda);
        topOffset = getY(celda);
        //up significa que la celda est� m�s alta que su equivalente siguiente y down el contrario
        up=isEven(leftOffset);
        down=!isEven(leftOffset);
        for (i=1;i<(range+1);i++) {
            //encima
			lista.push("hex" + (leftOffset) + "-" + (topOffset-i));
            //variables para calculo de vecinos
			addoneAbove = (isEven(i) && !isEven(leftOffset));
			addoneBelow = (isEven(i) && isEven(leftOffset));
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
                    lista.push("hex" + (leftOffset+i) + "-" + (topOffset-up-j-addoneAbove));
                    //arriba izquierda
                    lista.push("hex" + (leftOffset-i) + "-" + (topOffset-up-j-addoneAbove)) ;
                }else if (addoneBelow != 0) {
                    //abajo derecha
                    lista.push("hex" + (leftOffset+i) + "-" + (topOffset+down+j+addoneBelow)) ;
                    //abajo izquierda
                    lista.push("hex" + (leftOffset-i) + "-" + (topOffset+down+j+addoneBelow)) ;
                }
            }
            //abajo
            lista.push("hex" + (leftOffset) + "-" + (topOffset+i));
        }
		for(i in lista){
			tx=getX(lista[i]);
			ty=getY(lista[i]);
			if( tx>=0 && ty>=0 && tx<columnas && ty<filas){
				$("#"+lista[i])[0].esperar();
				active_div_range.push(lista[i]);
			}
		}
    }
	function tracear(algo){
		$("#marcador").html($("#marcador").html()+algo);
	}
	//creaci�n de una casilla y su comportamiento
	function createHexSpace(leftOffset,topOffset)
    {
        //Contenedor global, m�s grande, muestra el fondo hexagono
		var divTag = document.createElement("div");
		divTag.x=x;
		divTag.y=y;
        divTag.id = "hex" + x + "-" + y;
		divTag.title= x + "-" + y;
        divTag.setAttribute("align","center");
        divTag.style.left = leftOffset + "px";
        divTag.style.top = topOffset + "px";
        divTag.className ="hexspace";
        this.contenido="";
		
		divTag.insertar = function(piezacolor){
			//subdiv.css("background-color",color_jugador[piezacolor.charAt(1)]);
			$(this).children().css("background-color",color_jugador[piezacolor.charAt(1)]);
			$(this).children().addClass(clase_pieza[piezacolor.charAt(0)]);
			
		}
		//Subcontenedor con la pieza, muestra el color y el gif de la pieza
		$("body").append('<div id="sub' + x + "-" + y+'"></div>');
		subdiv=$("#sub"+ x + "-" + y);
		subdiv.addClass ('pieza');
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
				//TODO: en modo an�lisis no hace falta mirar que sea propia.
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
				moviendo=false;
				valido=$(this).hasClass("hex_esperar");
				apagarTodo();
				
				if(valido){//Es v�lido si est� iluminado de esperar
					if(this.contiene() /*&& no es mi color*/){
						//Esto ser�a un ataque, moverse a una celda ocupada que no es de mi color
						intentarAtaque(this.id,active_div.id);
					}else{//Esto ser�a un movimiento a casilla vac�a
						apagarTodo();
						id_origen=active_div.id;
						
						positions[id_origen.substring(3,(id_origen.length))]="";
						positions[this.x.toString()+"-"+this.y.toString()]=pieza_moviendo;					
						active_div=null;
						$(this).children().css("background-color",color_jugador[pieza_moviendo.charAt(1)]);
						$(this).children().addClass(clase_pieza[pieza_moviendo.charAt(0)]);
						enviarMovimiento(id_origen, this.id);
						$(this).children().hide();
						$(this).children().fadeIn();
						$("#"+id_origen)[0].vaciar();
					}
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
	function intentarAtaque(id_origen,id_destino){
		tracear("atacando: "+id_origen+" hacia "+ id_destino+"<br>");
		$.get("hexagame", { origen: id_origen, destino: id_destino },function(data){
				tracear("Respuesta: " + data);
				//actualizar tablero, eso seguro
				actualizarTablero();
				if(true/*Si no se ha podido hacer el movimiento*/){
					//Mostrar aviso del error. o si no devolver el error en el return.
				}
		});
		return null;
	};
	function enviarMovimiento(id_origen, id_destino){
		tracear("enviar al servidor "+id_origen+" hacia "+ id_destino+"<br>");
		$.get("hexagame", { origen: id_origen, destino: id_destino },function(data){
			for (x in data){
				tracear("<br>Respuesta: " + x);
			}
		});
		return null;
	}
	function enviar(){
		
	}
	function apagarTodo(){
		while(active_div_range.length>0){
			eliminado=active_div_range.pop();
			$("#"+eliminado)[0].limpiar();
		}
		if(active_div){
			temp=active_div
			$(active_div).children().fadeOut(100,function(){
				   temp.limpiar();
				   $(temp).children().show();
			})
		}
	}
	function actualizarTablero(){
		//$.getJSON("hexagame.json",function (data){montarTablero(data)});
		$.getJSON("http://hexaforge.appspot.com/hexagame?pid=13079204655491091244048",function (data){montarTablero(data)});
		
	}
	function montarTablero(data){
		marcador=new marcador(data.turno, data.jugadores);
		for(x in data.jugadores){
			//tracear('Nombre:'+data.jugadores[x].name+'<br>');
		};
		for (v in data.tablero.celdas){
			nombre=data.tablero.celdas[v].x+"-"+data.tablero.celdas[v].y
			valor=data.tablero.celdas[v].contenido+data.tablero.celdas[v].propietario;
			positions[nombre]=valor;
			$("#hex"+nombre)[0].insertar(valor);
		}
	}
	
	function getPlayer(){
		 return id_jugador;
	 }
	function pintarMarcador(){
		//$("#info").html('Nombre: <span style="background-color:green">'+getPlayer()+'</span> - Piezas: 4 - Turnos: 5 - <a href="#">Analizar</a> ------ Colores: <span style="background-color:blue">kandahar</span> - <span style="background-color:red">koco</span> - <span style="background-color:brown">kpacha</span> - <span style="background-color:pink">jj</span>');
	}
	function marcador(turno,datos){
		$("#info").html("");
		for(x in datos){
			tracear('Nombre:'+datos[x].name+'<br>');
			$("#info").html($("#info").html()+'<span style="background-color:'+color_jugador[datos[x].color]+'">'+datos[x].name+"</span> ");
		};
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
	function iniciarTablero(){
		filas=7;
		columnas=15
        crearTablero(filas,columnas);
		$("#floorplan").css("width",columnas*73+50);
		$("#floorplan").css("height",filas*82+60);
		$("body").hide()
		$("body").fadeIn()
	}
	
	 $(document).ready( 
		function() { 
			iniciarTablero();//pero en vac�o
			pintarMarcador();//iniciar marcador
			actualizarTablero();//recuperar json partida y rellenar las celdas
		}
    );
	
	
    
    
	

   