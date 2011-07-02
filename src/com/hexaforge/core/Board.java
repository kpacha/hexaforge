package com.hexaforge.core;

import java.util.Vector;

public class Board {

	private int maxX;
	private int maxY;
	private Vector<Hexagon> hexagon;
	private int totHexagon;

	public Board(String b) {
		// {"maxX":"20", "maxY":"20", "celdas":[{"x":"0", "y":"0", "piece":"p", "player":"0"}, {"x":"5", "y":"3", "piece":"s", "player":"1"}]}
		b = (String) b.subSequence(1, b.length() - 1);
		// "maxX":"20", "maxY":"20", "celdas":[{"x":"0", "y":"0", "piece":"p", "player":"0"}, {"x":"5", "y":"3", "piece":"s", "player":"1"}]
		String[] p = b.split(", \"celdas\":\\[");
		// p[0]="maxX":"20","maxY":"20"
		maxX = Integer.valueOf(p[0].split(", ")[0].split(":")[1]);
		maxY = Integer.valueOf(p[0].split(", ")[1].split(":")[1]);
		// p[1]={"x":"0","y":"0","piece":"p","player":"0"}, {"x":"5","y":"3","piece":"s","player":"1"}]
		p[1] = (String) p[1].subSequence(1, p[1].length() - 2);
		// p[1]="x":"0","y":"0","piece":"p","player":"0"}, {"x":"5","y":"3","piece":"s","player":"1"
		String[] cells = p[1].split("\\}, \\{");
		// cells[0]="x":"0","y":"0","piece":"p","player":"0"
		// cells[1]="x":"5","y":"3","piece":"s","player":"1"
		totHexagon = cells.length;
		hexagon = new Vector<Hexagon>();
		for (int i = 0; i < totHexagon; i++) {
			hexagon.add(new Hexagon("{" + cells[i] + "}"));
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
		return "{\"maxX\":\"" + maxX + "\", \"maxY\":\"" + maxY + "\", " + serializeCells() + "}";
	}

	private String serializeCells() {
		String result = "\"celdas\":[";
		for (int i = 0; i < totHexagon; i++) {
			Hexagon c = hexagon.get(i);
			result += c.toString();
			if (i < totHexagon - 1) {
				result += ", ";
			}
		}
		result += "]";
		// System.out.print("Board: serializeCells() : " +result+ "\n"); 
		return result;
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
		for (int i = 0; i < totHexagon; i++) {
			if (hexagon.get(i).getX() == x && hexagon.get(i).getY() == y) {
				return i;
			}
		}
		return -1;
	}

	public boolean move(Player p, String m) {
		// TODO deserializar m
		// TODO validar que p posee la celda de origen
		// TODO validar que la celda de destino está vacía
		// TODO validar que la celda de destino está a 1 casilla
		// TODO ordenar movimiento a la celda de origen
		return false;
	}
}
