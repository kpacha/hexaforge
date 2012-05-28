package com.hexaforge.core.interfaces;

import java.util.List;

import com.hexaforge.core.GamePreferences;
import com.hexaforge.core.Player;

public interface GameInterface {
	
	public int getStatus();
	
	public GamePreferences getPreferences();
	
	public List<Player> getPlayers();
	
	public BoardInterface getBoard();
	
	public int getTurn();
	
	public boolean addPlayer(Player p);

	public boolean addPlayer(String nickName);

	public boolean removePlayer(Player p);

	public boolean startGame();

	public boolean addTurns(int t);

	public boolean isJoinable();

	public boolean isFinished();
	
	public long getNextCheck();

	public boolean move(String userId, int turn, int fromX, int fromY, int toX, int toY);

	public void setNextCheck(long nextCheck);
}
