package com.hexaforge.core.cell;

import com.hexaforge.core.interfaces.CellInterface;

public class Obstacle extends AbstractCell implements CellInterface {

	public Obstacle(int xPos, int yPos, int p) {
		this(xPos, yPos);
	}

	public Obstacle(int xPos, int yPos) {
		setX(xPos);
		setY(yPos);
		setOwner(-1);
		this.code = CellImplementationEnum.OBSTACLE;
	}

	protected boolean doMove(int x, int y) {
		return false;
	}

	public boolean attack(CellInterface cell) {
		return false;
	}

}
