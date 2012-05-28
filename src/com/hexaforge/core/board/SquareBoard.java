package com.hexaforge.core.board;

import com.hexaforge.core.interfaces.BoardInterface;
import com.hexaforge.core.interfaces.CellInterface;

public class SquareBoard extends AbstractBoard implements BoardInterface {

	public SquareBoard() {
		type = BoardImplementationEnum.SQUARE;
	}

	public SquareBoard(int side) {
		super(side);
		type = BoardImplementationEnum.SQUARE;
	}

	public SquareBoard(int maxX, int maxY) {
		super(maxX, maxY);
		type = BoardImplementationEnum.SQUARE;
	}

	/**
	 * Calcula la distancia entre la celda y las coordenadas recibidas
	 * 
	 * @param CellInterface
	 *            source
	 * @param int toX
	 * @param int toY
	 * @return int
	 */
	public int getDistance(CellInterface source, int toX, int toY) {
		int dx = Math.abs(source.getX() - toX);
		int dy = Math.abs(source.getY() - toY);
		return dx + dy;
	}

}
