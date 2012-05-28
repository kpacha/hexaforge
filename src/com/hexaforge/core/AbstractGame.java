package com.hexaforge.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hexaforge.core.board.BoardBuilder;
import com.hexaforge.core.interfaces.BoardInterface;
import com.hexaforge.core.interfaces.GameInterface;

abstract public class AbstractGame implements GameInterface {

	protected BoardInterface board;
	protected List<Player> players = new ArrayList<Player>();
	protected int status;
	protected GamePreferences preferences;
	protected int turn;
	protected long nextCheck;
	protected long created;
	protected long updated;

	// creando el juego. se deberán establecer los parámetos de la partida
	public final static int STATE_NEW_GAME = 0;
	// esperando a que se unan los jugadores mínimos necesarios
	public final static int STATE_JOIN_GAME = 1;
	// preparando la partida
	public final static int STATE_PREPARE_GAME = 2;
	// partida en marcha
	public final static int STATE_PLAYING = 3;
	// partida finalizada
	public final static int STATE_GAME_OVER = 4;

	public AbstractGame() {
		this.status = AbstractGame.STATE_NEW_GAME;
		this.created = new Date().getTime();
	}

	public AbstractGame(GamePreferences gp) {
		this();
		this.preferences = gp;
	}

	public GamePreferences getPreferences() {
		return preferences;
	}

	public int getStatus() {
		return this.status;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public BoardInterface getBoard() {
		return board;
	}

	public int getTurn() {
		return turn;
	}

	public boolean addPlayer(Player p) {
		boolean result = false;
		if (this.status <= STATE_PREPARE_GAME
				&& this.players.size() < GamePreferences.MAX_PLAYERS) {
			this.players.add(p);
			if (this.players.size() >= GamePreferences.MIN_PLAYERS)
				this.status = STATE_PREPARE_GAME;
			else
				this.status = STATE_JOIN_GAME;
			this.updated = new Date().getTime();
			result = true;
		}
		return result;
	}

	public boolean addPlayer(String nickName) {
		Player p = new Player(nickName, this.players.size(), 0);
		return this.addPlayer(p);
	}

	public boolean removePlayer(Player p) {
		boolean result = false;
		if (this.status == STATE_JOIN_GAME || this.status == STATE_PREPARE_GAME) {
			this.players.remove(p);
			if (this.players.size() < GamePreferences.MIN_PLAYERS)
				this.status = STATE_JOIN_GAME;
			this.updated = new Date().getTime();
			result = true;
		}
		return result;
	}

	public boolean startGame() {
		boolean result = false;
		if (this.status == STATE_PREPARE_GAME && this.players.size() != 0) {
			this.board = BoardBuilder.makeBoard(this.preferences);
			populateBoard();
			addTurns(this.preferences.getInitialDeltaTurn());
			this.nextCheck = (new Date()).getTime()
					+ this.preferences.getEtaTurn();
			this.status = STATE_PLAYING;
			this.updated = new Date().getTime();
			result = true;
		}
		return result;
	}

	abstract protected void populateBoard();

	public boolean addTurns(int t) {
		for (int j = 0; j < this.players.size(); j++) {
			Player p = this.players.get(j);
			p.addTurns(t);
		}
		return true;
	}

	public boolean isJoinable() {
		return (this.status < STATE_PLAYING);
	}

	public boolean isFinished() {
		return (this.status == STATE_GAME_OVER);
	}

	public long getNextCheck() {
		return nextCheck;
	}

	/**
	 * Gestiona el turno del jugador y actualiza los párametros relativos a la
	 * partida. Delega el movimiento en el método doMove.
	 */
	public boolean move(String userId, int turn, int fromX, int fromY, int toX,
			int toY) {
		boolean success = false;
		if (turn == this.turn)
			success = doMove(userId, fromX, fromY, toX, toY);
		if (success) {
			this.turn++;
			checkGameStatus();
			this.updated = new Date().getTime();
		}
		return success;
	}

	abstract protected boolean doMove(String userId, int fromX, int fromY,
			int toX, int toY);

	protected Player getPlayer(int userId) {
		return this.players.get(userId);
	}

	protected int getPlayerId(String userId) {
		int id = -1;
		for (int i = 0; i < this.players.size(); i++) {
			Player p = this.players.get(i);
			if (p.getName().equalsIgnoreCase(userId)) {
				id = i;
				break;
			}
		}
		return id;
	}

	/**
	 * Revisamos si hay más de un jugador con piezas en el tablero
	 */
	protected void checkGameStatus() {
		int activePlayers = 0;
		for (Player player : this.players) {
			if (this.board.getCellsByPlayer(player.getColor()).size() == 0) {
				player.setTurns(0);
			} else {
				activePlayers++;
			}
		}
		if (activePlayers < 2)
			this.status = STATE_GAME_OVER;
	}

	/**
	 * @param nextCheck the nextCheck to set
	 */
	public void setNextCheck(long nextCheck) {
		this.nextCheck = nextCheck;
	}
}
