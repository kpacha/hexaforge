package com.hexaforge.core;

public class Player {
	private String id;
	private String name;
	private int color;
	private int turns;

	public Player() {
	}

	/**
	 * @param id
	 * @param name
	 * @param color
	 * @param turns
	 */
	public Player(String id, String name, int color, int turns) {
		this(name, color, turns);
		this.id = id;
	}

	/**
	 * @param name
	 * @param color
	 * @param turns
	 */
	public Player(String name, int color, int turns) {
		this.name = name;
		this.color = color;
		this.turns = turns;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @return the turns
	 */
	public int getTurns() {
		return turns;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @param turns
	 *            the turns to set
	 */
	public void setTurns(int turns) {
		this.turns = turns;
	}

	public boolean canMove() {
		return this.getTurns() > 0;
	}

	public void addTurns(int t) {
		this.turns += t;
	}

	public void wasteTurn() {
		this.turns--;
	}
}
