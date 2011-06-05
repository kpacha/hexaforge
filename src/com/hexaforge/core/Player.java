package com.hexaforge.core;

public class Player {
	private String id;
	private String name;
	private int color;
	private int turns;

	public Player(String s) {
		fromString(s);
	}

	public Player(String id, String n, int c) {
		name = n;
		color = c;
		this.id = id;
		turns = 0;
	}

	public int getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public int getTurns() {
		return turns;
	}

	public void setColor(int c) {
		color = c;
	}

	public void setName(String n) {
		name = n;
	}

	public void setId(String i) {
		id = i;
	}

	public void setTurns(int t) {
		turns = t;
	}

	public void addTurns(int t) {
		turns += t;
	}

	public String[] toArray() {
		return new String[] { id, name, String.valueOf(color),
				String.valueOf(turns) };
	}

	public String toString() {
		// System.out.print("maquetando: [\"" + id + "\",\"" + name + "\",\"" +
		// color + "\",\"" + turns + "\"]\n");
		return "[\"" + id + "\",\"" + name + "\",\"" + color + "\",\"" + turns
				+ "\"]";
	}

	public void fromArray(String[] s) {
		id = (String) s[0].subSequence(1, s[0].length() - 1);
		name = (String) s[1].subSequence(1, s[1].length() - 1);
		color = Integer
				.parseInt((String) s[2].subSequence(1, s[2].length() - 1));
		turns = Integer
				.parseInt((String) s[3].subSequence(1, s[3].length() - 1));
		// System.out.print("Player: parseando array: ['"+id+"','"+name+"','"+color+"','"+turns+"']\n");
	}

	public void fromString(String s) {
		// System.out.print("Player: parseando string: ["+s+"]\n");
		fromArray(s.split(","));
	}
}
