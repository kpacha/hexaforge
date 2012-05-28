package com.hexaforge.core.decorator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.hexaforge.core.Game;
import com.hexaforge.core.interfaces.BoardInterface;
import com.hexaforge.core.interfaces.CellInterface;
import com.hexaforge.core.interfaces.GameInterface;

public class JsonDecorator extends AbstractSerializer implements
		SerializerComponent {

	private GsonBuilder gsonBuilder;
	private static JsonDecorator instance;

	private JsonDecorator() {
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(BoardInterface.class,
				new BoardInterfaceSerializer());
	}

	public synchronized static JsonDecorator getInstance() {
		if (instance == null)
			instance = new JsonDecorator();
		return instance;
	}

	public GameInterface deserializeGame(String serializedComponent) {
		Gson gson = gsonBuilder.create();
		return gson.fromJson(serializedComponent, Game.class);
	}

	public String serializeGame(GameInterface game) {
		Gson gson = gsonBuilder.create();
		return gson.toJson(game);
	}

	public GsonBuilder getBuilder() {
		return gsonBuilder;
	}

}
