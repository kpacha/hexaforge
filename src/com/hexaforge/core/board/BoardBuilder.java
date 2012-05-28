package com.hexaforge.core.board;

import com.hexaforge.core.GamePreferences;
import com.hexaforge.core.interfaces.BoardInterface;

public class BoardBuilder {

	public static BoardInterface makeBoard(GamePreferences preferences) {
		return makeBoard(preferences.getBoardType(),
				preferences.getBoardSizeX(), preferences.getBoardSizeY());
	}

	public static BoardInterface makeBoard(BoardImplementationEnum boardType,
			int maxX, int maxY) {
		BoardInterface board;
		if (boardType == BoardImplementationEnum.SQUARE) {
			board = new SquareBoard(maxX, maxY);
		} else {
			board = new HexagonalBoard(maxX, maxY);
		}
		return board;
	}
}
