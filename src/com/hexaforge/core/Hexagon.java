package com.hexaforge.core;

public class Hexagon {

	private int x;
	private int y;
	protected char contenido;

	public Hexagon(String h) {
		Hexagon hexagon = fromString(h);
		this.setX(hexagon.getX());
		this.setY(hexagon.getY());
		this.setContenido(hexagon.getContenido());
		// System.out.print("Hexagon: maquetando: ['"+x+"','"+y+"','"+contenido+"']\n");
	}

	public Hexagon(int xPos, int yPos, char c) {
		x = xPos;
		y = yPos;
		contenido = c;
		// System.out.print("Hexagon: maquetando: ['"+x+"','"+y+"','"+contenido+"']\n");
	}
	
	public Hexagon(Hexagon hexagon) {
		this.setX(hexagon.getX());
		this.setY(hexagon.getY());
		this.setContenido(hexagon.getContenido());
	}
	
	protected static final String extractValue(String v){
		v = v.split(":")[1];
		return (String) v.subSequence(1, v.length() - 1);
	}

	@Override
	public String toString() {
		return "{\"x\":\"" + x + "\", \"y\":\"" + y + "\", \"contenido\":\""
				+ contenido + "\"}";
	}
	
	public Hexagon fromString(String h) {
		// System.out.print("Hexagon: maquetando: '"+h+"'\n");
		// h = {"x":"0", "y":"0", "piece":"p", "player":"0"}
		h = (String) h.subSequence(1, h.length() - 1);
		String[] params = h.split(", ");
		char contenido = extractValue(params[2]).charAt(0);
		for (int i = 0; i < Piece.fichas.length(); i++) {
			if (contenido == Piece.fichas.charAt(i)) {
				return new Piece(h);
			}
		}
		int x = Integer.parseInt(extractValue(params[0]));
		int y = Integer.parseInt(extractValue(params[1]));
		return new Obstacle(x, y, contenido);
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	/**
	 * @return the contenido
	 */
	public final char getContenido() {
		return contenido;
	}

	/**
	 * @param x the x to set
	 */
	public final void setX(int x) {
		this.x = x;
	}

	/**
	 * @param y the y to set
	 */
	public final void setY(int y) {
		this.y = y;
	}

	/**
	 * @param contenido the contenido to set
	 */
	public final void setContenido(char contenido) {
		this.contenido = contenido;
	}
}
