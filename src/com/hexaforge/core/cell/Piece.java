package com.hexaforge.core.cell;

import com.hexaforge.core.interfaces.CellInterface;
import com.hexaforge.util.RandomGenerator;

abstract public class Piece extends AbstractCell implements CellInterface {

	public Piece(int xPos, int yPos, int owner) {
		setX(xPos);
		setY(yPos);
		setOwner(owner);
		this.range = 1;
	}

	/**
	 * @return the owner
	 */
	public int getOwner() {
		return this.owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(int owner) {
		this.owner = owner;
	}

	protected boolean doMove(int x, int y) {
		this.setX(x);
		this.setY(y);
		return true;
	}

	public boolean attack(CellInterface target) {
		boolean success = false;
		if (!target.getCode().isObstacle())
			success = doAttack(target);
		return success;
	}

	protected boolean doAttack(CellInterface cell) {
		return RandomGenerator.getInstance().nextBoolean();
	}

}
