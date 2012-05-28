package com.hexaforge.core.decorator;

import com.hexaforge.core.interfaces.GameInterface;

abstract public class AbstractSerializer implements SerializerComponent {

	abstract public String serializeGame(GameInterface game);

	abstract public GameInterface deserializeGame(String serializedGame);

}
