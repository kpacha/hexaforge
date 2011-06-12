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

	public String toString() {
		// System.out.print("maquetando: {\"id\":\"" + id + "\", \"name\":\"" + name + "\", \"color\":\""
		//		+ color + "\", \"turns\":\"" + turns + "\"}\n");
		return "{\"id\":\"" + id + "\", \"name\":\"" + name + "\", \"color\":\"" + color 
				+ "\", \"turns\":\"" + turns + "\"}";
	}

	public void fromString(String s) {
		// System.out.print("Player: maquetando: "+s+"\n");
		// "id":"18580476422013912411", "name":"test@example.com", "color":"0", "turns":"0"
		String[] properties = s.split(",");
		id = extractValue(properties[0]);
		name = extractValue(properties[1]);
		color = Integer.parseInt(extractValue(properties[2]));
		turns = Integer.parseInt(extractValue(properties[3]));
	}
	
	private String extractValue(String v){
		v = v.split(":")[1];
		return (String) v.subSequence(1, v.length() - 1);
	}
}
