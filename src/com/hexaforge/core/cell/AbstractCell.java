package com.hexaforge.core.cell;

import com.hexaforge.controller.MovementException;
import com.hexaforge.core.interfaces.CellInterface;

public abstract class AbstractCell implements CellInterface {

    private int x;
    private int y;
    protected int owner;
    protected int range = 0;
    protected CellImplementationEnum code;

    public boolean move(int x, int y) {
	return doMove(x, y);
    }

    abstract protected boolean doMove(int x, int y);

    public abstract boolean attack(CellInterface cell) throws MovementException;

    public final int getX() {
	return x;
    }

    public final int getY() {
	return y;
    }

    /**
     * @return the owner
     */
    public int getOwner() {
	return -1;
    }

    /**
     * @return the range
     */
    public final int getRange() {
	return range;
    }

    /**
     * @param x
     *            the x to set
     */
    public final void setX(int x) {
	this.x = x;
    }

    /**
     * @param y
     *            the y to set
     */
    public final void setY(int y) {
	this.y = y;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(int owner) {
    }

    /**
     * @return the code
     */
    public CellImplementationEnum getCode() {
	return code;
    }

}
