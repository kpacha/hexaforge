package com.hexaforge.core;

import java.util.Vector;

public class Board {

	private int maxX;
	private int maxY;
	private Vector<Hexagon> hexagon;
	private int totHexagon;

	public Board(String b) {
		// [[20,20],[[0,0,"p",0],[0,1,"s",0],[19,19,"l",2]]]
		b = (String) b.subSequence(1, b.length() - 1);
		// [20,20],[[0,0,"p",0],[0,1,"s",0],[19,19,"l",2]]
		String[] p = b.split("\\],\\[");
		// p[0]=[20,20
		p[0] = (String) p[0].subSequence(1, p[0].length());
		String[] max = p[0].split(",");
		maxX = Integer.valueOf(max[0]);
		maxY = Integer.valueOf(max[1]);
		// p[1]=[0,0,"p",0],[0,1,"s",0],[19,19,"l",2]]
		p[1] = (String) p[1].subSequence(0, p[1].length() - 1);
		// p[1]=[0,0,"p",0],[0,1,"s",0],[19,19,"l",2]
		String[] cells = p[1].split("\\],\\[");
		totHexagon = cells.length;
		hexagon = new Vector<Hexagon>();
		for (int i = 0; i < totHexagon; i++) {
			if (i == 0)
				cells[i] = (String) cells[i].subSequence(1, cells[i].length());
			if (i == totHexagon - 1)
				cells[i] = (String) cells[i].subSequence(0,
						cells[i].length() - 1);
			// cells[0]=0,0,"p",0
			hexagon.add(new Hexagon(cells[i]));
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
		return "[[" + maxX + "," + maxY + "]," + serializeCells() + "]";
	}

	private String serializeCells() {
		String result = "[";
		for (int i = 0; i < totHexagon; i++) {
			Hexagon c = hexagon.get(i);
			result += c.toString();
			if (i < totHexagon - 1) {
				result += ",";
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
}
