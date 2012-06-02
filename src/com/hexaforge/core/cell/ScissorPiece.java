package com.hexaforge.core.cell;

import com.hexaforge.core.interfaces.CellInterface;

public class ScissorPiece extends Piece {

    public ScissorPiece(int xPos, int yPos, int owner) {
	super(xPos, yPos, owner);
	this.code = CellImplementationEnum.SCISSOR;
    }

    protected boolean doAttack(CellInterface cell) {
	boolean success = false;
	switch (cell.getCode()) {
	case PAPER:
	case LIZZARD:
	    success = true;
	    break;
	case SCISSOR:
	    success = super.doAttack(cell);
	    break;
	}
	return success;
    }

}
