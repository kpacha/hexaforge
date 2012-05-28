package com.hexaforge.core;

import com.hexaforge.core.board.BoardImplementationEnum;

public class GamePreferences {
	public final static int MAX_PLAYERS = 6;
	public final static int MIN_PLAYERS = 2;
	public final static int MAX_CELLS = 20;
	public final static int MIN_CELLS = 10;
	// número de turnos iniciales
	public final static int INITIAL_DELTA_TURN = 5;
	// número de turnos a incrementar en cada actualización de turnos
	public final static int DELTA_TURN = 1;
	// segundos hasta la siguiente actualización de turnos. 6h en milisegundos
	public final static int ETA_TURN = 6 * 60 * 60 * 1000;
	// tiempo mínimo hasta la siguiente actualización de turnos. 30min en
	// milisegundos
	public final static int MIN_ETA_TURN = 30 * 60 * 1000;

	private int initialDeltaTurn;
	private int deltaTurn;
	private int etaTurn;
	private BoardImplementationEnum boardType;
	private int boardSizeX;
	private int boardSizeY;

	public GamePreferences(int initialDeltaTurn, int deltaTrun, int etaTurn,
			String boardType, int sizeX, int sizeY) {
		super();
		setInitialDeltaTurn(initialDeltaTurn);
		setDeltaTurn(deltaTrun);
		setEtaTurn(etaTurn);
		setBoardType(boardType);
		setBoardSizeX(sizeX);
		setBoardSizeY(sizeY);
	}

	public GamePreferences() {
		setBoardType(BoardImplementationEnum.HEXAGONAL);
		setInitialDeltaTurn(INITIAL_DELTA_TURN);
		setDeltaTurn(DELTA_TURN);
		setEtaTurn(ETA_TURN);
		setBoardSizeX(MAX_CELLS);
		setBoardSizeY(MAX_CELLS);
	}

	public GamePreferences(String s) {
		s = (String) s.subSequence(1, s.length() - 1);
		String[] preferences = s.split(",");
		setInitialDeltaTurn(extractValue(preferences[0]));
		setDeltaTurn(extractValue(preferences[1]));
		setEtaTurn(extractValue(preferences[2]));
//		setBoardType(extractValue(preferences[3]));
		setBoardType(preferences[3].split(":")[1].replaceAll("\"", ""));
		setBoardSizeX(extractValue(preferences[4]));
		setBoardSizeY(extractValue(preferences[5]));
	}

	private int extractValue(String v) {
		v = v.split(":")[1];
		return Integer.parseInt((String) v.subSequence(1, v.length() - 1), 10);
	}

	/**
	 * @return the initialDeltaTurn
	 */
	public int getInitialDeltaTurn() {
		return initialDeltaTurn;
	}

	/**
	 * @return the deltaTrun
	 */
	public int getDeltaTurn() {
		return deltaTurn;
	}

	/**
	 * @return the etaTurn
	 */
	public int getEtaTurn() {
		return etaTurn;
	}

	/**
	 * @return the boardType
	 */
	public BoardImplementationEnum getBoardType() {
		return boardType;
	}

	/**
	 * @return the boardSizeX
	 */
	public int getBoardSizeX() {
		return boardSizeX;
	}

	/**
	 * @return the boardSizeY
	 */
	public int getBoardSizeY() {
		return boardSizeY;
	}

	/**
	 * @param boardSizeX
	 *            the boardSizeX to set
	 */
	public void setBoardSizeX(int boardSizeX) {
		this.boardSizeX = boardSizeX;
	}

	/**
	 * @param boardSizeY
	 *            the boardSizeY to set
	 */
	public void setBoardSizeY(int boardSizeY) {
		this.boardSizeY = boardSizeY;
	}

	/**
	 * @param boardType
	 *            the boardType to set
	 */
	public void setBoardType(BoardImplementationEnum boardType) {
		this.boardType = boardType;
	}

	/**
	 * @param boardType
	 *            the boardType to set
	 */
	public void setBoardType(String boardType) {
		this.boardType = BoardImplementationEnum.valueOf(BoardImplementationEnum.class,boardType);
	}

	/**
	 * @param initialDeltaTurn
	 *            the initialDeltaTurn to set
	 */
	public void setInitialDeltaTurn(int initialDeltaTurn) {
		this.initialDeltaTurn = initialDeltaTurn;
	}

	/**
	 * @param deltaTrun
	 *            the deltaTrun to set
	 */
	public void setDeltaTurn(int deltaTurn) {
		this.deltaTurn = deltaTurn;
	}

	/**
	 * @param etaTurn
	 *            the etaTurn to set
	 */
	public void setEtaTurn(int etaTurn) {
		if (etaTurn < MIN_ETA_TURN) {
			this.etaTurn = MIN_ETA_TURN;
		} else {
			this.etaTurn = etaTurn;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return String
	 */
	public String toString() {
		return "{\"initialDeltaTurn\":\"" + initialDeltaTurn
				+ "\", \"deltaTrun\":\"" + deltaTurn + "\", \"etaTurn\":\""
				+ etaTurn + "\", \"boardType\":\"" + boardType
				+ "\", \"boardSizeX\":\"" + boardSizeX
				+ "\", \"boardSizeY\":\"" + boardSizeY + "\"}";
	}

}
