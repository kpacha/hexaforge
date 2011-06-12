package com.hexaforge.core;

import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.util.zip.CRC32;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Game {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String id;
	@Persistent
	private int state;
	@Persistent
	private int turn;
	@Persistent
	private String board;
	@Persistent
	private Text tiradas;
	@Persistent
	private String players;
	@Persistent
	private int totPlayers;
	@Persistent
	private long created;
	@Persistent
	private long updated;
	@Persistent
	private long nextCheck;
	@Persistent
	private String preferences;

	@NotPersistent
	public final static int MAX_PLAYERS = 6;
	@NotPersistent
	public final static int MIN_PLAYERS = 2;
	@NotPersistent
	public final static int MAX_CELLS = 20;
	@NotPersistent
	public final static int MIN_CELLS = 10;
	@NotPersistent
	public final static int STATE_NEW_GAME = 0;
	// creando el juego. se deberán establecer los parámetos de la partida
	@NotPersistent
	public final static int STATE_JOIN_GAME = 1;
	// esperando a que se unan los jugadores mínimos necesarios
	@NotPersistent
	public final static int STATE_PREPARE_GAME = 2; // preparando la partida
	@NotPersistent
	public final static int STATE_PLAYING = 3; // partida en marcha
	@NotPersistent
	public final static int STATE_GAME_OVER = 4; // partida finalizada
	@NotPersistent
	private GamePreferences prefs; // configuración de la partida

	@NotPersistent
	private Random generator = new Random(); // generador de aleatorios

	public Game() {
		players = null;
		state = STATE_NEW_GAME;
		tiradas = null;
		board = null;
		turn = 0;
		totPlayers = 0;
		created = (new Date()).getTime();
		CRC32 crc32 = new CRC32();
		crc32.reset();
		crc32.update((int) created);
		id = Long.toString(created) + Long.toString(crc32.getValue());
		key = KeyFactory.createKey(Game.class.getSimpleName(), id);
		setPreferences(new GamePreferences());
	}

	public Game(Key k, long c, String i) {
		this();
		key = k;
		created = c;
		id = i;
	}

	public Game(GamePreferences p) {
		this();
		setPreferences(p);		
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public void setId(String i) {
		id = i;
	}

	public void setState(int e) {
		state = e;
	}

	public void setTurn(int t) {
		turn = t;
	}

	public void setPreferences(String p) {
		preferences = p;
		prefs = new GamePreferences(p);
	}

	public void setPreferences(GamePreferences p) {
		preferences = p.toString();
		prefs = p;
	}

	public void setBoard(String t) {
		board = t;
	}

	public void setTiradas(String t) {
		tiradas = new Text(t);
	}

	public void setPlayers(String j) {
		players = j;
		totPlayers = j.split("\\},\\{").length;
		// System.out.print("Seteando jugadores desde String: "+j+"\n");
	}

	public void setPlayer(Vector<Player> p) {
		totPlayers = p.size();
		players = players2Txt(p);
		// System.out.print("Seteando jugadores desde Vector con "+p.size()+" elementos\n");
	}

	public void setCreated(long c) {
		created = c;
	}

	public void setUpdated(long u) {
		updated = u;
	}

	public void setNextCheck(long c) {
		nextCheck = c;
	}

	public Key getKey() {
		return key;
	}

	public String getId() {
		return id;
	}

	public int getState() {
		return state;
	}

	public int getTurn() {
		return turn;
	}

	public String getPreferences() {
		return preferences;
	}

	public GamePreferences getGamePreferences() {
		prefs = new GamePreferences(preferences);
		return prefs;
	}

	public String getTiradas() {
		return tiradas.getValue();
	}

	public String getBoard() {
		return board;
	}

	public String getPlayers() {
		return players;
	}

	public long getCreated() {
		return created;
	}

	public long getUpdated() {
		return updated;
	}

	public long getNextCheck() {
		return nextCheck;
	}

	public boolean addPlayer(String id, String name) {
		return addPlayer(id, name, totPlayers);
	}

	/**
	 * This adds a player to the game
	 * 
	 * @param id
	 *            id of player (0 - AI)
	 * @param name
	 *            Name of player
	 * @param color
	 *            Color of player
	 * @return boolean Returns true if the player is added, returns false if the
	 *         player can't be added.
	 */
	public boolean addPlayer(String id, String name, int color) {
		Vector<Player> pV = new Vector<Player>();
		if (totPlayers != 0) {
			pV = txt2Players(players);
			if (pV.size() != totPlayers) {
				 System.out.print("Los totales no coinciden: " +totPlayers+
				 ", " +pV.size()+ "\n"); // testing
				return false;
			}
		}
		if (state <= STATE_PREPARE_GAME && totPlayers < MAX_PLAYERS) {
			for (int c = 0; c < totPlayers; c++) {
				if ((name.equals(((Player) pV.elementAt(c)).getName()))
						|| (color == ((Player) pV.elementAt(c)).getColor()))
					return false;
			}
			//System.out.print("Player added. id: " +id+ ", name: " +name+
			// ", color: " +color+ "\n"); // testing
			Player p = new Player(id, name, color);
			pV.add(p);
			if (pV.size() >= MIN_PLAYERS)
				state = STATE_PREPARE_GAME;
			else
				state = STATE_JOIN_GAME;
			setPlayers(players2Txt(pV));
			// System.out.print("-----------------------------------------------\n");
			// System.out.print("Player added. totPlayers: " +totPlayers+ "\n");
			// System.out.print("Player added. players: " +players+ "\n"); 
			// System.out.print("-----------------------------------------------\n");
			return true;
		} else
			return false;
	}

	/**
	 * This deletes a player in the game
	 * 
	 * @param name
	 *            Name of the player
	 * @return boolean Returns true if the player is deleted, returns false if
	 *         the player cannot be deleted
	 */
	public boolean delPlayer(String name) {
		Vector<Player> pV = new Vector<Player>();
		if (totPlayers != 0) {
			pV = txt2Players(players);
		} else {
			return false;
		}
		if (state == STATE_JOIN_GAME || state == STATE_PREPARE_GAME) {
			int n = -1;
			for (int c = 0; c < totPlayers; c++) {
				if (name.equals(((Player) pV.elementAt(c)).getName()))
					n = c;
			}
			if (n == -1) {
				// System.out.print("Error: No player found\n"); // testing
				return false;
			} else {
				pV.removeElementAt(n);
				pV.trimToSize();
				totPlayers = pV.size();
				// System.out.print("Player removed\n"); // testing
				if (totPlayers < MIN_PLAYERS)
					state = STATE_JOIN_GAME;
				return true;
			}
		} else
			return false;
	}

	private String players2Txt(Vector<Player> player) {
		String result = "[";
		for (int i = 0; i < player.size(); i++) {
			Player p = (Player) player.get(i);
			result += p.toString();
			if (i < player.size() - 1) {
				result += ",";
			}
		}
		result += "]";
		// System.out.print("getJugadoresTxt() : " +result+ "\n"); // testing
		return result;
	}

	private Vector<Player> txt2Players(String j) {
		Vector<Player> v = new Vector<Player>();
		j = (String) j.subSequence(1, j.length() - 1);
		String[] p = j.split("\\},\\{");
		for (int i = 0; i < p.length; i++) {
			if (i == 0)
				p[i] = (String) p[i].subSequence(1, p[i].length());
			if (i == p.length - 1)
				p[i] = (String) p[i].subSequence(0, p[i].length() - 1);
			v.add(new Player(p[i]));
		}
		return v;
	}

	/**
	 * Starts the game
	 */
	public boolean startGame() {
		// System.out.print("-----------------------------------------------\n");
		// System.out.print("Iniciando juego con " +totPlayers+ " jugadores\n");
		// System.out.print("estado inicial : " +state+ "\n");
		if (state == STATE_PREPARE_GAME && totPlayers != 0) {
			// creamos tablero
			Board b = new Board(MAX_CELLS);
			for (int i = 0; i < totPlayers; i++) {
				// asignamos posiciones iniciales
				b.addCell(new Hexagon(rnd(MAX_CELLS), rnd(MAX_CELLS),
						Hexagon.fichas.charAt(rnd(Hexagon.TIPOS_FICHAS)), i));
			}
			board = b.serializeBoard();
			// asignamos turnos iniciales
			//System.out.print("\n-----------------------------------------------\n");
			//System.out.print("startGame(): initialDeltaTurn=" + this.getGamePreferences().getInitialDeltaTurn());
			//System.out.print("\npreferences=" + preferences);
			//System.out.print("\n-----------------------------------------------\n");
			if (!addTurns(this.getGamePreferences().getInitialDeltaTurn())) {
				// System.out.print("error iniciando los turnos de : " +id+
				// "\n"); // testing
				return false;
			}
			// programamos incremento turnos
			nextCheck = (new Date()).getTime() + prefs.getEtaTurn();
			state = STATE_PLAYING;
		}
		// System.out.print("estado final : " +state+ "\n"); 
		// System.out.print("-----------------------------------------------\n");
		return state == STATE_PLAYING;
	}

	public boolean move(String m) {
		return false;
	}

	public boolean isJoinable() {
		return (state < STATE_PLAYING);
	}

	public boolean isFinished() {
		return (state == STATE_GAME_OVER);
	}

	private String pickRand(String[] array) {
		return array[rnd(array.length)];
	}

	public boolean addTurns(int t) {
		Vector<Player> pV = txt2Players(players);
		// System.out.print("addTurns(t) pre:-> t=" +t+
		// ". Total: "+pV.size()+"/"+totPlayers+"\n");
		for (int i = 0; i < totPlayers; i++) {
			pV.get(i).addTurns(t);
			//System.out.print("addTurns(" + t + "). " + pV.get(i).getName()
			//		+ ". Total turnos: " + pV.get(i).getTurns() + "\n");
		}
		setPlayers(players2Txt(pV));
		// System.out.print("addTurns(t) post:-> t=" +t+
		// ". Total: "+pV.size()+"/"+totPlayers+"\n"); // testing
		return (totPlayers == pV.size());
	}

	private int rnd(int max) {
		return generator.nextInt(max);
	}
}
