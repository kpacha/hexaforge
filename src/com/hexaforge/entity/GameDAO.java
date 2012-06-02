package com.hexaforge.entity;

import javax.cache.CacheException;
import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.hexaforge.core.decorator.JsonDecorator;
import com.hexaforge.core.interfaces.GameInterface;
import com.hexaforge.util.MemcacheConnector;
import com.hexaforge.util.MemcacheConnectorException;
import com.hexaforge.util.MemcacheKeyNotFoundException;

public class GameDAO {

    private boolean isLoadedFromMemcache;
    private EntityManager entityManager;
    private JsonDecorator game2Json;

    public GameDAO(EntityManager entityManager) {
	this.entityManager = entityManager;
	this.game2Json = JsonDecorator.getInstance();
    }

    public boolean persist(GameInterface game, GameEntity gameEntity)
	    throws MemcacheConnectorException, CacheException {
	if (isLoadedFromMemcache)
	    gameEntity = loadFromDatabase(gameEntity.getId());
	gameEntity.setGame(game2Json.serializeGame(game));
	gameEntity.setNextCheck(game.getNextCheck());
	gameEntity.setStatus(game.getStatus());
	entityManager.persist(gameEntity);
	entityManager.close();
	MemcacheConnector memcache = new MemcacheConnector();
	memcache.put(gameEntity.getId(), game2Json.serializeGame(game));
	return true;
    }

    public GameEntity load(String pid) {
	GameEntity gameEntity;
	MemcacheConnector memcache = null;
	try {
	    memcache = new MemcacheConnector();
	    gameEntity = loadFromMemcahe(memcache, pid);
	    isLoadedFromMemcache = true;
	} catch (Exception e) {
	    gameEntity = loadFromDatabase(pid);
	    isLoadedFromMemcache = false;
	    if (memcache != null) {
		memcache.put(pid, gameEntity.getGame());
	    }
	}
	return gameEntity;
    }

    private GameEntity loadFromMemcahe(MemcacheConnector memcache, String pid)
	    throws MemcacheKeyNotFoundException {
	String serializedGame = memcache.getString(pid);
	GameEntity gameEntity = new GameEntity(pid, serializedGame);
	GameInterface game = game2Json.deserializeGame(serializedGame);
	gameEntity.setNextCheck(game.getNextCheck());
	gameEntity.setStatus(game.getStatus());
	return gameEntity;
    }

    private GameEntity loadFromDatabase(String pid) {
	return entityManager.find(GameEntity.class,
		KeyFactory.createKey(GameEntity.class.getSimpleName(), pid));
    }
}
