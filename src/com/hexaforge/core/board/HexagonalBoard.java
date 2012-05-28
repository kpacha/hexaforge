package com.hexaforge.core.board;

import com.hexaforge.core.interfaces.BoardInterface;
import com.hexaforge.core.interfaces.CellInterface;

public class HexagonalBoard extends AbstractBoard implements BoardInterface {

	public HexagonalBoard() {
		type = BoardImplementationEnum.HEXAGONAL;
	}

	public HexagonalBoard(int max) {
		super(max);
		type = BoardImplementationEnum.HEXAGONAL;
	}

	public HexagonalBoard(int maxX, int maxY) {
		super(maxX, maxY);
		type = BoardImplementationEnum.HEXAGONAL;
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
		int distance = 0;

		if (dx / 2 >= dy) {
			distance = dx;
		} else {
			distance = (int) (dy + dx - Math.floor(dx / 2.0));
		}
		if (source.getX() % 2 == 0) {
			if (dx % 2 == 1 && source.getY() > toY) {
				distance--;
			}
		} else {
			if (dx % 2 == 1 && source.getY() < toY) {
				distance--;
			}
		}
		return distance;
	}
}
