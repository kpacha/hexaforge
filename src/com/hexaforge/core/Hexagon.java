package com.hexaforge.core;

public class Hexagon {

	private int x;
	private int y;
	private char contenido;
	private int propietario;

	public static final String fichas = "rtpls";
	public static final int TIPOS_FICHAS = 5;

	public Hexagon(String h) {
		// System.out.print("Hexagon: maquetando: '"+h+"'\n");
		// h = {"x":"0","y":"0","piece":"p","player":"0"}
		h = (String) h.subSequence(1, h.length() - 1);
		String[] params = h.split(",");
		x = Integer.parseInt(extractValue(params[0]));
		y = Integer.parseInt(extractValue(params[1]));
		char pieza = extractValue(params[2]).charAt(0);
		for (int i = 0; i < fichas.length(); i++) {
			if (pieza == fichas.charAt(i)) {
				contenido = pieza;
			}
		}
		propietario = Integer.parseInt(extractValue(params[3]));
		// System.out.print("Hexagon: maquetando: ['"+x+"','"+y+"','"+contenido+"','"+propietario+"']\n");
	}

	public Hexagon(int xPos, int yPos, char c, int p) {
		x = xPos;
		y = yPos;
		contenido = c;
		propietario = p;
		// System.out.print("Hexagon: maquetando: ['"+x+"','"+y+"','"+contenido+"','"+propietario+"']\n");
	}
	
	private String extractValue(String v){
		v = v.split(":")[1];
		return (String) v.subSequence(1, v.length() - 1);
	}

	@Override
	public String toString() {
		return "{\"x\":\"" + x + "\",\"y\":\"" + y + "\",\"contenido\":\""
				+ contenido + "\",\"propietario\":\"" + propietario + "\"}";
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
