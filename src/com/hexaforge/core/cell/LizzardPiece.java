package com.hexaforge.core.cell;

import com.hexaforge.core.interfaces.CellInterface;

public class LizzardPiece extends Piece {

    public LizzardPiece(int xPos, int yPos, int owner) {
	super(xPos, yPos, owner);
	this.code = CellImplementationEnum.LIZZARD;
    }

    protected boolean doAttack(CellInterface cell) {
	boolean success = false;
	switch (cell.getCode()) {
	case PAPER:
	case SPOCK:
	    success = true;
	    break;
	case LIZZARD:
	    success = super.doAttack(cell);
	    break;
	}
	return success;
    }

}
