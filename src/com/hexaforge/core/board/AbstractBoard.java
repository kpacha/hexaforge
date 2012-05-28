package com.hexaforge.core.board;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hexaforge.core.Player;
import com.hexaforge.core.interfaces.BoardInterface;
import com.hexaforge.core.interfaces.CellInterface;

public abstract class AbstractBoard implements BoardInterface {

	private int maxX;
	private int maxY;
	private List<CellInterface> cell = new ArrayList<CellInterface>();
	protected BoardImplementationEnum type;

	public AbstractBoard() {
	}

	public AbstractBoard(int max) {
		this(max, max);
	}

	public AbstractBoard(int x, int y) {
		maxX = x;
		maxY = y;
	}

	public boolean isEmpty(int x, int y) {
		return (getCellIndex(x, y) == -1);
	}

	public void setCells(List<CellInterface> cells) {
		this.cell = cells;
	}

	public int getCellIndex(int x, int y) {
		int i = -1;
		for (Iterator<CellInterface> iter = cell.iterator(); iter.hasNext();) {
			CellInterface c = iter.next();
			if (c.getX() == x && c.getY() == y) {
				i = cell.indexOf(c);
				break;
			}
		}
		return i;
	}

	public CellInterface getCell(int cellIndex) {
		CellInterface cell = null;
		try {
			cell = this.cell.get(cellIndex);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return cell;
	}

	public List<CellInterface> getCells() {
		return cell;
	}

	public List<CellInterface> getCellsByPlayer(int player) {
		List<CellInterface> cells = new ArrayList<CellInterface>();
		for (CellInterface cell : this.cell) {
			if (cell.getOwner() == player)
				cells.add(cell);
		}
		return cells;
	}

	public boolean addCell(CellInterface cell) {
		boolean result = false;
		if (isEmpty(cell.getX(), cell.getY())) {
			result = this.cell.add(cell);
		}
		return result;
	}

	public boolean cellIsInBoard(int x, int y) {
		return (isInRange(x, this.maxX) && isInRange(y, this.maxY));
	}

	public void removeCell(CellInterface cell) {
		this.cell.remove(this.getCellIndex(cell.getX(), cell.getY()));
	}

	protected boolean isInRange(int offset, int max) {
		return (offset > -1 && offset < max);
	}

	/**
	 * Calcula la distancia entre las celdas recibidas
	 * 
	 * @param CellInterface
	 *            source
	 * @param CellInterface
	 *            target
	 * @return int
	 */
	public int getDistance(CellInterface source, CellInterface target) {
		return getDistance(source, target.getX(), target.getY());
	}

	abstract public int getDistance(CellInterface source, int targetX,
			int targetY);

	/**
	 * Procesa el movimiento.
	 * 
	 * Valida que el jugador posee una pieza en la celda de origen, que la celda
	 * de destino está vacía y a una distáncia válida para realizar el
	 * movimiento. Si todo es correcto, ordena a la pieza que se mueva al
	 * destino.
	 * 
	 * @param player
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 * @return
	 */
	public boolean move(int player, int fromX, int fromY, int toX, int toY) {
		CellInterface sourceCell = this
				.getCell(this.getCellIndex(fromX, fromY));
		return movementStrategyFactory().doMove(player, sourceCell, toX, toY);
	}

	protected MovementStrategy movementStrategyFactory() {
		return new MovementStrategy(this);
	}

	public BoardImplementationEnum getBoardType() {
		return type;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	/**
	 * @param maxX
	 *            the maxX to set
	 */
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	/**
	 * @param maxY
	 *            the maxY to set
	 */
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
}
