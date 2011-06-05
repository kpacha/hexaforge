package com.hexaforge.core;

public class Hexagon {

	private int x;
	private int y;
	private char contenido;
	private int propietario;

	public static final String fichas = "rtpls";
	public static final int TIPOS_FICHAS = 5;

	public Hexagon(String h) {
		// System.out.print("Hexagon: maquetando: ['"+h+"']\n");
		String[] params = h.split(",");
		x = Integer.parseInt(params[0]);
		y = Integer.parseInt(params[1]);
		for (int i = 0; i < fichas.length(); i++) {
			if (params[2].charAt(0) == fichas.charAt(i)) {
				contenido = params[2].charAt(0);
			}
		}
		propietario = Integer.parseInt(params[3]);
		// System.out.print("Hexagon: maquetando: ['"+x+"','"+y+"','"+contenido+"','"+propietario+"']\n");
	}

	public Hexagon(int xPos, int yPos, char c, int p) {
		x = xPos;
		y = yPos;
		contenido = c;
		propietario = p;
		// System.out.print("Hexagon: maquetando: ['"+x+"','"+y+"','"+contenido+"','"+propietario+"']\n");
	}

	public String toString() {
		// System.out.print("Hexagon: maquetando: [\"" + x + "\",\"" + y +
		// "\",\"" + contenido + "\",\"" + propietario + "\"]\n");
		return "[\"" + x + "\",\"" + y + "\",\"" + contenido + "\",\""
				+ propietario + "\"]";
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
