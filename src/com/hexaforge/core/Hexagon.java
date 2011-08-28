package com.hexaforge.core;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Hexagon {

	private int x;
	private int y;
	protected char contenido;
	private int propietario;
	
	public static final String fichas = "rtpls";
	public static final int TIPOS_FICHAS = 5;

	public Hexagon(String h) {
		fromString(h);
	}

	public Hexagon(int xPos, int yPos, char c) {
		x = xPos;
		y = yPos;
		contenido = c;
		propietario = -1;
		// System.out.print("Hexagon: maquetando: ['"+x+"','"+y+"','"+contenido+"']\n");
	}
	
	public Hexagon(Hexagon hexagon) {
		this.setX(hexagon.getX());
		this.setY(hexagon.getY());
		this.setContenido(hexagon.getContenido());
		this.setPropietario(hexagon.getPropietario());
	}
	
	protected static final String extractValue(String v){
		v = v.split(":")[1];
		return (String) v.subSequence(1, v.length() - 1);
	}

	@Override
	public String toString() {
		return this.toJSON().toJSONString();
	}
	
	public JSONObject toJSON(){
		JSONObject obj=new JSONObject();
		obj.put("x", String.valueOf(this.getX()));
		obj.put("y", String.valueOf(this.getY()));
		obj.put("contenido", (this.getContenido() + " ").trim());
		obj.put("propietario", String.valueOf(this.getPropietario()));
		return obj;
	}

	public void fromString(String h) {
		System.out.print("Hexagon: " + h + "\n");
		JSONObject hex = (JSONObject) JSONValue.parse(h);
		this.x = Integer.valueOf((String)hex.get("x"));
		this.y = Integer.valueOf((String)hex.get("y"));
		this.setContenido(((String)hex.get("contenido")).charAt(0));
		this.setPropietario(Integer.valueOf((String)hex.get("propietario")));
		System.out.print("Hexagon: parsed as " + this + "\n");
	}
	
	/**
	 * Calcula la distancia entre la celda y las coordenadas recibidas
	 * 
	 * @param int toX
	 * @param int toY
	 * @return int
	 */
	public int getDistanceTo(int toX, int toY) {
		int dx = Math.abs(this.getX() - toX);
		int dy = Math.abs(this.getY() - toY);
		int distance = 0;

		if (dx/2 >= dy) {
		      distance = dx;
		} else {
		      distance = (int) (dy + dx - Math.floor(dx/2.0)); 
		}
		if (this.getX() % 2 == 0) {
		      if (dx % 2 == 1 && this.getY() > toY) {
		            distance--;
		      }
		} else {
		      if (dx % 2 == 1 && this.getY() < toY) {
		            distance--;
		      }
		}
		return distance;
	}
	
	public boolean isPiece() {
		return Piece.fichas.indexOf(this.getContenido()) > -1;
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

	public boolean move(int x, int y) {
		this.setX(x);
		this.setY(y);
		return true;
	}
}
