package com.hexaforge.core.board;

import com.hexaforge.command.exception.MovementException;
import com.hexaforge.core.interfaces.BoardInterface;
import com.hexaforge.core.interfaces.CellInterface;

public class MovementStrategy {
    private BoardInterface board;

    public MovementStrategy(AbstractBoard board) {
	this.board = board;
    }

    public boolean doMove(int player, CellInterface cell, int toX, int toY) {
	boolean result = false;
	try {
	    // valida si el usuario es el propietario de la celda de origen
	    if (this.check(player, cell, toX, toY)) {
		// valida que la celda de destino está vacía
		boolean targetIsEmpty = this.board.isEmpty(toX, toY);
		if (!targetIsEmpty) {
		    CellInterface targetCell = this.board.getCell(this.board
			    .getCellIndex(toX, toY));
		    targetIsEmpty = attack(cell, targetCell);
		    result = true;
		}
		if (targetIsEmpty) {
		    // ordena movimiento a la celda de origen
		    result = cell.move(toX, toY);
		}
	    }
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}
	return result;
    }

    protected boolean attack(CellInterface sourceCell, CellInterface targetCell)
	    throws MovementException {
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

	// valida que el jugador sea el propietario de la pieza a mover
	boolean isOwner = (cell.getOwner() == playerId);

	// valida que la celda de destino está a una distancia alcanzable
	boolean isReachable = (this.board.getDistance(cell, toX, toY) <= cell
		.getRange());

	return (isOnBoard && isOwner && isReachable);
    }
}
