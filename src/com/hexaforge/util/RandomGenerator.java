package com.hexaforge.util;

import java.util.Random;

public class RandomGenerator {
	private static Random randomGeneratorInstace;

	private RandomGenerator() {
	}

	public static synchronized Random getInstance() {
		if (randomGeneratorInstace == null)
			randomGeneratorInstace = new Random();
		return randomGeneratorInstace;
	}
}
