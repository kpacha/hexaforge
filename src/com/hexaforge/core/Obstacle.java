package com.hexaforge.core;

public class Obstacle extends Hexagon {
	
	public static final char contenido = 'o';

	public Obstacle(Hexagon hexagon) {
		super(hexagon);
	}

	public Obstacle(int xPos, int yPos, char c) {
		super(xPos, yPos, c);
	}

	public Obstacle(String h) {
		super(h);
	}

	public Obstacle(int xPos, int yPos) {
		super(xPos, yPos, contenido);
	}

}
