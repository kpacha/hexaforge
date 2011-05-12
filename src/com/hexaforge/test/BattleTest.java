package com.hexaforge.test;

import javax.jdo.PersistenceManager;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.hexaforge.core.Game;
import com.hexaforge.util.PMF;

public class BattleTest {

	private Game battle;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		battle = new Game();
	}

	@After
	public void tearDown() throws Exception {
	}

	public void testGuardado() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(battle);
		pm.close();

		pm = PMF.get().getPersistenceManager();
		Game b = pm.getObjectById(Game.class, battle.getKey());
		pm.close();

		assertTrue(b.equals(battle));
	}

	public void revisaParseoJugadores() {
		String jugadorId = "00000";
		String jugadorName = "name";
		battle.addPlayer(jugadorId, jugadorName);
		String players = battle.getPlayers();
	}
}
