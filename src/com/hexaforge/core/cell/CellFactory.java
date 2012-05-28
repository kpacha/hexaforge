package com.hexaforge.core.cell;

import com.hexaforge.core.interfaces.CellInterface;

public class CellFactory {

	public static CellInterface makeCell(char code, int x, int y, int owner) {
		CellInterface cell;
		if (code == CellImplementationEnum.ROCK.getCode()) {
			cell = new RockPiece(x, y, owner);
		} else if (code == CellImplementationEnum.SCISSOR.getCode()) {
			cell = new ScissorPiece(x, y, owner);
		} else if (code == CellImplementationEnum.PAPER.getCode()) {
			cell = new PaperPiece(x, y, owner);
		} else if (code == CellImplementationEnum.LIZZARD.getCode()) {
			cell = new LizzardPiece(x, y, owner);
		} else if (code == CellImplementationEnum.SPOK.getCode()) {
			cell = new SpokPiece(x, y, owner);
		} else {
			cell = new Obstacle(x, y);
		}
		return cell;
	}
}
