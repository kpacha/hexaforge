package com.hexaforge.core;

public class GamePreferences {
	public final static int MAX_PLAYERS = 6;
	public final static int MIN_PLAYERS = 2;
	public final static int MAX_CELLS = 20;
	public final static int MIN_CELLS = 10;
	public final static int INITIAL_DELTA_TURN = 5; // turnos iniciales
	public final static int DELTA_TURN = 1; // turnos a incrementar
	public final static int ETA_TURN = 6 * 60 * 60 * 1000;
	// segundos hasta la siguiente actualización de turnos. 6h en  milisegundos
	
	private int initialDeltaTurn;
	private int deltaTrun;
	private int etaTurn;
	
	public GamePreferences(int initialDeltaTurn, int deltaTrun, int etaTurn) {
		super();
		this.initialDeltaTurn = initialDeltaTurn;
		this.deltaTrun = deltaTrun;
		this.etaTurn = etaTurn;
	}

	public GamePreferences(){
		initialDeltaTurn = INITIAL_DELTA_TURN;
		deltaTrun = DELTA_TURN;
		etaTurn = ETA_TURN;
	}
	
	public GamePreferences(String s){
		s = (String) s.subSequence(1, s.length() - 1);
		String[] preferences = s.split(",");
		initialDeltaTurn = extractValue(preferences[0]);
		deltaTrun = extractValue(preferences[1]);
		etaTurn = extractValue(preferences[2]);
	}
	
	private int extractValue(String v){
		v = v.split(":")[1];
		return Integer.valueOf((String) v.subSequence(1, v.length() - 1));
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
	public int getDeltaTrun() {
		return deltaTrun;
	}

	/**
	 * @return the etaTurn
	 */
	public int getEtaTurn() {
		return etaTurn;
	}

	/**
	 * @param initialDeltaTurn the initialDeltaTurn to set
	 */
	public void setInitialDeltaTurn(int initialDeltaTurn) {
		this.initialDeltaTurn = initialDeltaTurn;
	}

	/**
	 * @param deltaTrun the deltaTrun to set
	 */
	public void setDeltaTrun(int deltaTrun) {
		this.deltaTrun = deltaTrun;
	}

	/**
	 * @param etaTurn the etaTurn to set
	 */
	public void setEtaTurn(int etaTurn) {
		this.etaTurn = etaTurn;
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return String
	 */
	@Override
	public String toString() {
		return "[\"initialDeltaTurn\":\"" + initialDeltaTurn
				+ "\", \"deltaTrun\":\"" + deltaTrun + "\", \"etaTurn\":\""
				+ etaTurn + "\"]";
	}

}