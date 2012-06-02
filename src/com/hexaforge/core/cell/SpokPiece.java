package com.hexaforge.core.cell;

import com.hexaforge.core.interfaces.CellInterface;

public class SpokPiece extends Piece {

    public SpokPiece(int xPos, int yPos, int owner) {
	super(xPos, yPos, owner);
	this.code = CellImplementationEnum.SPOCK;
    }

    protected boolean doAttack(CellInterface cell) {
	boolean success = false;
	switch (cell.getCode()) {
	case ROCK:
	case SCISSOR:
	    success = true;
	    break;
	case SPOCK:
	    success = super.doAttack(cell);
	    break;
	}
	return success;
    }

}
