package com.hexaforge.controller;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;
import com.hexaforge.core.Game;
import com.hexaforge.core.GamePreferences;
import com.hexaforge.util.PMF;
import com.hexaforge.util.Reflector;

public class GameController {
	private Game game;
	private PersistenceManager pm;
	private Reflector reflector;

	/**
	 * 
	 * @param game
	 */
	public GameController(Game game, PersistenceManager pm) {
		this.game = game;
		this.reflector = new Reflector(this);
		this.pm = pm;
	}
	
	/**
	 * Método encargado de 'redirigir' las acciones
	 * 
	 * Utilizando la clase Reflector accede a los métodos del propio controlador
	 * 
	 * @param methodName
	 * @param args
	 * @return
	 */
	public boolean execute(String methodName, Object...args){
		//System.out.print("\nHexagameController: llamando a la accion " + methodName
		//		+ " con [" + args.length + "] parámetros." );
		return this.reflector.executeMethod(methodName, args).equals(true);
	}
	
	private boolean finalizeAction(boolean success){
		//System.out.print("\nHexagameController: resultado accion " + success);
		if(success){
			this.pm.makePersistent(game);
			System.out.print(" así que se han guardado los cambios!");
			return true;
		} 
		return false;
	}
	
	private boolean create(User user){
		return finalizeAction(game.addPlayer(user.getUserId(), user.getNickname()));
	}
	
	private boolean join(User user){
		return finalizeAction(game.addPlayer(user.getUserId(), user.getNickname()));
	}
	
	private boolean quit(User user){
		return finalizeAction(game.delPlayer(user.getNickname()));
	}
	
	private boolean start(){
		return finalizeAction(game.startGame());
	}
	
	private boolean move(User user, String movementString){
		return finalizeAction(game.move(user.getUserId(), movementString));
	}

}
