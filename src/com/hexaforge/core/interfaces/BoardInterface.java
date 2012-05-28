package com.hexaforge.core.interfaces;

import java.util.List;

import com.hexaforge.core.Player;
import com.hexaforge.core.board.BoardImplementationEnum;

public interface BoardInterface {

	public boolean addCell(CellInterface cell);

	public int getCellIndex(int x, int y);

	public boolean isEmpty(int x, int y);

	public int getDistance(CellInterface source, CellInterface target);

	public int getDistance(CellInterface cell, int targetX, int targetY);

	public boolean move(int playerId, int fromX, int fromY, int toX, int toY);
	
	public BoardImplementationEnum getBoardType();

	public void setCells(List<CellInterface> cells);
	
	public List<CellInterface> getCells();

	public CellInterface getCell(int cellIndex);

	public boolean cellIsInBoard(int toX, int toY);

	public void removeCell(CellInterface cell);

	public int getMaxX();
	
	public int getMaxY();

	public void setMaxX(int maxX);
	
	public void setMaxY(int maxY);

	public List<CellInterface> getCellsByPlayer(int player);
}
