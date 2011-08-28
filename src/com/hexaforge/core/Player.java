package com.hexaforge.core;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
	
	public void wasteTurn() {
		turns --;
	}

	public String toString() {
		return this.toJSON().toJSONString();
	}

	public JSONObject toJSON() {
		JSONObject obj=new JSONObject();
		obj.put("id", this.getId());
		obj.put("name", this.getName());
		obj.put("color", String.valueOf(this.getColor()));
		obj.put("turns", String.valueOf(this.getTurns()));
		return obj;
	}

	public void fromString(String s) {
		// "id":"18580476422013912411", "name":"test@example.com", "color":"0", "turns":"0"
		System.out.print("Player: " + s + "\n");
		JSONObject player = (JSONObject) JSONValue.parse(s);
		this.id = (String)player.get("id");
		this.name = (String)player.get("name");
		this.color = Integer.valueOf((String)player.get("color"));
		this.turns = Integer.valueOf((String)player.get("turns"));
		System.out.print("Player: parsed as " + this + "\n");
	}
	
	private String extractValue(String v){
		v = v.split(":")[1];
		return (String) v.subSequence(1, v.length() - 1);
	}
	
	public boolean canMove() {
		return this.getTurns() > 0;
	}
}
