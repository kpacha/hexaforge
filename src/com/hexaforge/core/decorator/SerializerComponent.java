package com.hexaforge.core.decorator;

import com.hexaforge.core.interfaces.GameInterface;

public interface SerializerComponent {

	public String serializeGame(GameInterface game);
	public GameInterface deserializeGame(String serializedGame);
	
}
