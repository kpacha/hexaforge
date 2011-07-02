package com.hexaforge.util;

import java.util.Iterator;
import java.util.Vector;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.hexaforge.core.Game;
import com.hexaforge.core.Player;

public class Channel {
	
	private Game game;

	public Channel(Game game) {
		this.game = game;
	}

	public String getChannelKey(Player player) {
		String key = player.getId() + game.getId();
	    return Long.toString(key.hashCode());
	}

	public String getChannelKey(String playerId) {
		String key = playerId + game.getId();
	    return Long.toString(key.hashCode());
	}
	
	public void sendUpdateToClients() {
		Vector<Player> players = game.getPlayersVector();
		Iterator<Player> player = players.iterator();
		while(player.hasNext()){
			sendUpdateToUser(player.next());
		}
	}
	
	private void sendUpdateToUser(Player player) {
	    if (player != null) {
	      ChannelService channelService = ChannelServiceFactory.getChannelService();
	      String channelKey = getChannelKey(player);
	      channelService.sendMessage(new ChannelMessage(channelKey, getMessageString(player)));
	    }
	}

	private String getMessageString(Player player) {
		return game.toString(player.getId());
	}

}
