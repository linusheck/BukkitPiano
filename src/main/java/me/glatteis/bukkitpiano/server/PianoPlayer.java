package me.glatteis.bukkitpiano.server;

import org.bukkit.entity.Player;

/**
 * Created by Linus on 11.01.2016.
 */
public class PianoPlayer {

    public final Player player;
    public final long id;

    public PianoPlayer(Player player, long id) {
        this.player = player;
        this.id = id;
    }
}
