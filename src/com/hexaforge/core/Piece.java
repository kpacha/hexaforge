package com.hexaforge.core;

public class Piece extends Hexagon {

	private int propietario;
	
	public static final String fichas = "rtpls";
	public static final int TIPOS_FICHAS = 5;

	public Piece(int xPos, int yPos, char c, int p) {
		super(xPos, yPos, c);
		propietario = p;
	}

	public Piece(String p) {
		super(p);
		Piece piece = fromString(p);
		this.setPropietario(piece.getPropietario());
	}

	public Piece(Hexagon hexagon) {
		super(hexagon);
		propietario = -1;
	}

	public Piece(Piece piece) {
		this((Hexagon)piece);
		propietario = piece.getPropietario();
	}

	public Piece(int xPos, int yPos, char c) {
		this(xPos, yPos, c, -1);
	}

	/**
	 * @return the propietario
	 */
	public final int getPropietario() {
		return propietario;
	}

	/**
	 * @param propietario the propietario to set
	 */
	public final void setPropietario(int propietario) {
		this.propietario = propietario;
	}

	@Override
	public String toString() {
		return "{\"x\":\"" + getX() + "\", \"y\":\"" + getY() + "\", \"contenido\":\""
				+ getContenido() + "\", \"propietario\":\"" + getPropietario() + "\"}";
	}
	
	public Piece fromString(String h) {
		// System.out.print("Hexagon: maquetando: '"+h+"'\n");
		// h = {"x":"0", "y":"0", "piece":"p", "player":"0"}
		h = (String) h.subSequence(1, h.length() - 1);
		String[] params = h.split(", ");
		int x = Integer.parseInt(extractValue(params[0]));
		int y = Integer.parseInt(extractValue(params[1]));
		char pieza = extractValue(params[2]).charAt(0);
		char contenido = ' ';
		for (int i = 0; i < fichas.length(); i++) {
			if (pieza == fichas.charAt(i)) {
				contenido = pieza;
			}
		}
		int propietario = Integer.parseInt(extractValue(params[3]));
		return new Piece(x, y, contenido, propietario);
	}
	
	public boolean move() {
		return false;
	}

}
