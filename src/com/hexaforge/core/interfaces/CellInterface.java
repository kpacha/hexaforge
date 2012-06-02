package com.hexaforge.core.interfaces;

import com.hexaforge.command.exception.MovementException;
import com.hexaforge.core.cell.CellImplementationEnum;

public interface CellInterface {

    public boolean move(int x, int y);

    public int getX();

    public int getY();

    public int getOwner();

    public int getRange();

    public void setX(int x);

    public void setY(int y);

    public void setOwner(int owner);

    public CellImplementationEnum getCode();

    public boolean attack(CellInterface cell) throws MovementException;
}
