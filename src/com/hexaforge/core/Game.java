package com.hexaforge.core;

import java.util.Random;

import com.hexaforge.core.cell.CellFactory;
import com.hexaforge.core.cell.CellImplementationEnum;
import com.hexaforge.core.interfaces.CellInterface;
import com.hexaforge.util.RandomGenerator;

public class Game extends AbstractGame {

	public Game(GamePreferences gp) {
		super(gp);
	}

	public Game() {
	}

	protected void populateBoard() {
		int totalPlayers = this.players.size();
		for (int j = 0; j < totalPlayers; j++) {
			initPlayerPieces(j);
		}
	}

	protected void initPlayerPieces(int playerId) {
		int maxPieces = this.preferences.getInitialDeltaTurn();
		for (int i = 0; i < maxPieces; i++) {
			insertNewRandomPiece(playerId);
		}
	}

	protected void insertNewRandomPiece(int playerId) {
		Random generator = RandomGenerator.getInstance();
		int posX, posY;
		char code;
		CellInterface cell;
		do {
			posX = generator.nextInt(this.preferences.getBoardSizeX());
			posY = generator.nextInt(this.preferences.getBoardSizeY());
			code = CellImplementationEnum.randomPiece(true).getCode();
			cell = CellFactory.makeCell(code, posX, posY, playerId);
		} while (!this.board.addCell(cell));
	}

	protected boolean doMove(String userId, int fromX, int fromY, int toX,
			int toY) {
		boolean result = false;
		int playerId = getPlayerId(userId);
		Player player = getPlayer(playerId);
		if (player != null && player.getTurns() > 0)
			result = this.board.move(playerId, fromX, fromY, toX, toY);
		if (result)
			player.wasteTurn();
		return result;
	}

}
