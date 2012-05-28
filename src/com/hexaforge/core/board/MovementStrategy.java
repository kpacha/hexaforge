package com.hexaforge.core.board;

import com.hexaforge.core.interfaces.BoardInterface;
import com.hexaforge.core.interfaces.CellInterface;

public class MovementStrategy {
	private BoardInterface board;

	public MovementStrategy(AbstractBoard board) {
		this.board = board;
	}

	public boolean doMove(int player, CellInterface cell, int toX, int toY) {
		boolean result = false;
		// valida si el usuario es el propietario de la celda de origen
		if (this.check(player, cell, toX, toY)) {
			// valida que la celda de destino está vacía
			boolean targetIsEmpty = this.board.isEmpty(toX, toY);
			if (!targetIsEmpty) {
				CellInterface targetCell = this.board.getCell(this.board
						.getCellIndex(toX, toY));
				if (targetCell.getOwner() != player) {
					targetIsEmpty = attack(cell, targetCell);
					result = true;
				}
			}
			if (targetIsEmpty) {
				// ordena movimiento a la celda de origen
				result = cell.move(toX, toY);
			}
		}
		return result;
	}

	protected boolean attack(CellInterface sourceCell, CellInterface targetCell) {
		boolean success = false;
		success = sourceCell.attack(targetCell);
		if (success) {
			System.out.println("The attacker has win!");
			this.board.removeCell(targetCell);
		} else {
			System.out.println("The attacker has lost!");
			this.board.removeCell(sourceCell);
		}
		return success;
	}

	protected boolean check(int playerId, CellInterface cell, int toX, int toY) {
		// valida que la celda de destino está dentro del tablero
		boolean isOnBoard = this.board.cellIsInBoard(toX, toY);
		if (!isOnBoard) {
			System.out.println("La celda de destino [" + toX + ", " + toY
					+ "] no está en el tablero");
		}

		// valida que el jugador sea el propietario de la pieza a mover
		boolean isOwner = (cell.getOwner() == playerId);
		if (!isOwner) {
			System.out.println("El usuario " + playerId
					+ " no es el propietario de la celda de origen");
		}

		// valida que la celda de destino está a una distancia alcanzable
		boolean isReachable = (this.board.getDistance(cell, toX, toY) <= cell
				.getRange());
		if (!isReachable) {
			System.out.println("La celda de destino [" + toX + ", " + toY
					+ "] está a [" + this.board.getDistance(cell, toX, toY)
					+ "] celdas de distancia");
		}

		return (isOnBoard && isOwner && isReachable);
	}
}
