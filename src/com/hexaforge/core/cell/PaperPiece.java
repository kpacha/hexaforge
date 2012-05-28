package com.hexaforge.core.cell;

import com.hexaforge.core.interfaces.CellInterface;

public class PaperPiece extends Piece {

	public PaperPiece(int xPos, int yPos, int owner) {
		super(xPos, yPos, owner);
		this.code = CellImplementationEnum.PAPER;
	}

	protected boolean doAttack(CellInterface cell) {
		boolean success = false;
		switch (cell.getCode()) {
		case ROCK:
		case LIZZARD:
			success = true;
			break;
		case PAPER:
			success = super.doAttack(cell);
			break;
		}
		return success;
	}

}
