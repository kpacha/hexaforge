package com.hexaforge.core.cell;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.hexaforge.util.RandomGenerator;

public enum CellImplementationEnum {
	ROCK('r'), SCISSOR('t'), PAPER('p'), LIZZARD('l'), SPOK('s'), OBSTACLE('o');

	private char code;

	private static final List<CellImplementationEnum> VALUES = Collections
			.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = RandomGenerator.getInstance();

	private CellImplementationEnum(char code) {
		this.code = code;
	}

	public char getCode() {
		return code;
	}
	
	public boolean isObstacle(){
		return (this == OBSTACLE);
	}

	public static CellImplementationEnum randomPiece(boolean excludeObstacle) {
		CellImplementationEnum result = VALUES.get(RANDOM.nextInt(SIZE));
		if (excludeObstacle) {
			while (result == OBSTACLE) {
				result = VALUES.get(RANDOM.nextInt(SIZE));
			}
		}
		return result;
	}
}
