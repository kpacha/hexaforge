package com.hexaforge.core;

import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Board {

	private int maxX;
	private int maxY;
	private Vector<Hexagon> hexagon;
	private int totHexagon;

	public Board(String b) {
		// {"maxX":"20", "maxY":"20", "celdas":[{"x":"0", "y":"0", "piece":"p", "player":"0"}, {"x":"5", "y":"3", "piece":"s", "player":"1"}]}
		System.out.print("Borad: " + b +"\n");
		JSONObject board = (JSONObject) JSONValue.parse(b);
		this.maxX = Integer.valueOf((String)board.get("maxX"));
		this.maxY = Integer.valueOf((String)board.get("maxY"));
		JSONArray array = (JSONArray)board.get("celdas");
		this.totHexagon = array.size();
		hexagon = new Vector<Hexagon>();
		for (int i = 0; i < totHexagon; i++) {
			hexagon.add(new Hexagon(array.get(i).toString()));
		}
	}

	public Board(int max) {
		maxX = max;
		maxY = max;
		hexagon = new Vector<Hexagon>();
		totHexagon = 0;
	}

	public Board(int x, int y) {
		maxX = x;
		maxY = y;
		hexagon = new Vector<Hexagon>();
		totHexagon = 0;
	}

	public String serializeBoard() {
		JSONObject obj=new JSONObject();
		obj.put("maxX", String.valueOf(this.maxX));
		obj.put("maxY", String.valueOf(this.maxY));
		obj.put("celdas", this.serializeCells());
		return obj.toJSONString();
	}

	private JSONArray serializeCells() {
		JSONArray list = new JSONArray();
		for (int i = 0; i < totHexagon; i++) {
			Hexagon c = hexagon.get(i);
			list.add(c.toJSON());
		}
		return list;
	}

	private Vector<Hexagon> getCells() {
		return hexagon;
	}

	public boolean addCell(Hexagon c) {
		// System.out.print("Intentando añadir la celda: ['"+c.getX()+"','"+c.getY()+"']. Ya hay "+totHexagon+" celda(s)\n");
		if (totHexagon < maxX * maxY && isEmpty(c.getX(), c.getY())) {
			hexagon.add(c);
			totHexagon++;
			// System.out.print("Añadir la celda: ['"+c.getX()+"','"+c.getY()+"']. Ya hay "+totHexagon+" celda(s)\n");
			return true;
		}
		// System.out.print("No se añadió la celda: ['"+c.getX()+"','"+c.getY()+"']!\n");
		return false;
	}

	public boolean isEmpty(int x, int y) {
		return (getCellIndex(x, y) == -1);
	}

	public int getCellIndex(int x, int y) {
		System.out.print("Board.getCellIndex : buscando la celda [" + x + "," + y + "]\n");
		for (int i = 0; i < totHexagon; i++) {
			System.out.print("Board.getCellIndex : [" + i +"]=[" + hexagon.get(i).getX()
					+ "," + hexagon.get(i).getY() + "]\n");
			if (hexagon.get(i).getX() == x && hexagon.get(i).getY() == y) {
				return i;
			}
		}
		return -1;
	}

	protected Hexagon getCell(int index) {
		return this.getCells().get(index);
	}
	
	protected boolean cellIsInBoard(int x, int y) {
		return (x>-1 && x<this.maxX && y>-1 && y<this.maxY);
	}
	
	/**
	 * Procesa el movimiento.
	 * 
	 * Valida que el jugador posee una pieza en la celda de origen, que la celda de destino está vacía
	 * y a una distáncia válida para realizar el movimiento. Si todo es correcto, ordena a la piza que
	 * se mueva al destino.
	 * 
	 * @param int player
	 * @param JSONObject fromCell
	 * @param JSONObject toCell
	 * @return boolean
	 */
	public boolean move(int player, JSONObject fromCell, JSONObject toCell) {
		// el formato de los JSONObject recibidos es {"x": 12, "y": 6}
		
		int fromX = ((Long) fromCell.get("x")).intValue();
		System.out.print("Board.move fromX=" + fromX + "\n");
		int fromY = ((Long) fromCell.get("y")).intValue();
		System.out.print("Board.move fromY=" + fromY + "\n");
		
		int toX = ((Long) toCell.get("x")).intValue();
		System.out.print("Board.move toX=" + toX + "\n");
		int toY = ((Long) toCell.get("y")).intValue();
		System.out.print("Board.move toY=" + toY + "\n");
		
		// valida si el usuario es el propietario de la pieza situada en la celda de origan
		Hexagon cell = this.getCell(this.getCellIndex(fromX, fromY));
		if (!this.isValidMovement(player, cell, toX, toY)) {
			return false;
		}
		// ordenar movimiento a la celda de origen
		return cell.move(toX, toY);
	}
	
	protected boolean isValidMovement(int player, Hexagon cell, int toX, int toY){
		// comprueba que existe una pieza en la celda de origen
		if (!cell.isPiece()) {
			System.out.print("La celda de origen no contiene ninguna pieza\n");
			return false;
		}
		
		// valida que el jugador sea el propietario de la pieza
		if (cell.getPropietario() != player) {
			System.out.print("El usuario " + player + " no es el propietario de la celda de origen\n");
			return false;
		}
		
		// valida que la celda de destino está vacía
		if (this.getCellIndex(toX, toY) != -1) {
			System.out.print("La celda de destino [" + toX + ", " + toY + "] no está vacía\n");
			return false;
		}
		
		// valida que la celda de destino está dentro del tablero
		if (!this.cellIsInBoard(toX, toY)) {
			System.out.print("La celda de destino [" + toX + ", " + toY + "] no está en el tablero\n");
			return false;
		}

		// valida que la celda de destino está a 1 casilla
		if (cell.getDistanceTo(toX, toY) > 1) {
			System.out.print("La celda de destino [" + toX + ", " + toY + "] no está a ["
					+ cell.getDistanceTo(toX, toY) + "] celdas de distancia\n");
			return false;
		}
		
		return true;
	}
}
