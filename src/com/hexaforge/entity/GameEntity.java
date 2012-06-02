package com.hexaforge.entity;

import java.util.Date;
import java.util.zip.CRC32;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@Entity
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String id;

    @Basic
    private Text game;

    private long nextCheck;

    private int status;

    public GameEntity() {
	long created = (new Date()).getTime();
	CRC32 crc32 = new CRC32();
	crc32.reset();
	crc32.update((int) created);
	setId(Long.toString(created) + Long.toString(crc32.getValue()));
    }

    public GameEntity(String pid, String game) {
	setId(id);
	setGame(game);
    }

    /**
     * @return the key
     */
    public Key getKey() {
	return key;
    }

    /**
     * @return the game
     */
    public String getGame() {
	return game.getValue();
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
	this.key = KeyFactory.createKey(GameEntity.class.getSimpleName(),
		this.id);
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(Key key) {
	this.key = key;
    }

    /**
     * @param game
     *            the game to set
     */
    public void setGame(String game) {
	this.game = new Text(game);
    }

    /**
     * @return the nextCheck
     */
    public long getNextCheck() {
	return nextCheck;
    }

    /**
     * @param nextCheck
     *            the nextCheck to set
     */
    public void setNextCheck(long nextCheck) {
	this.nextCheck = nextCheck;
    }

    /**
     * @return the status
     */
    public int getStatus() {
	return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
	this.status = status;
    }

}
