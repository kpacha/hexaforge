package com.hexaforge.core.cell;

import com.hexaforge.core.interfaces.CellInterface;

public class RockPiece extends Piece {

	public RockPiece(int xPos, int yPos, int owner) {
		super(xPos, yPos, owner);
		this.code = CellImplementationEnum.ROCK;
	}

	protected boolean doAttack(CellInterface cell) {
		boolean success = false;
		switch (cell.getCode()) {
		case SPOK:
		case SCISSOR:
			success = true;
			break;
		case ROCK:
			success = super.doAttack(cell);
			break;
		}
		return success;
	}

}
