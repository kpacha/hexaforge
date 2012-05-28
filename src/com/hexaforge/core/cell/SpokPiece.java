package com.hexaforge.core.cell;

import com.hexaforge.core.interfaces.CellInterface;

public class SpokPiece extends Piece {

	public SpokPiece(int xPos, int yPos, int owner) {
		super(xPos, yPos, owner);
		this.code = CellImplementationEnum.SPOK;
	}

	protected boolean doAttack(CellInterface cell) {
		boolean success = false;
		switch (cell.getCode()) {
		case PAPER:
		case LIZZARD:
			success = true;
			break;
		case SPOK:
			success = super.doAttack(cell);
			break;
		}
		return success;
	}

}
