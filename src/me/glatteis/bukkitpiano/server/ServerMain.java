package me.glatteis.bukkitpiano.server;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linus on 10.01.2016.
 */
public class ServerMain extends JavaPlugin implements Listener {

    private PacketReciever packetReciever;

    protected List<PianoPlayer> pianoPlayers;
    protected List<PianoPlayer> confirmationPlayers;

    public void onDisable() {
        packetReciever.disable();
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        pianoPlayers = new ArrayList<PianoPlayer>();
        confirmationPlayers = new ArrayList<PianoPlayer>();
        try {
            packetReciever = new PacketReciever(this);
            new BukkitRunnable() {
                public void run() {
                    packetReciever.recievePacket();
                }
            }.runTaskTimerAsynchronously(this, 0, 1);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("bukkitpiano")) {
            if (args[0].equals("confirm") && sender instanceof Player) {
                for (PianoPlayer pianoPlayer : confirmationPlayers) {
                    if (pianoPlayer.player.equals(sender)) {
                        sender.sendMessage("You are now ready to play!");
                        pianoPlayers.add(pianoPlayer);
                        confirmationPlayers.remove(pianoPlayer);
                        break;
                    }
                }
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (PianoPlayer p : pianoPlayers) {
            if (p.player.equals(event.getPlayer())) {
                pianoPlayers.remove(p);
                return;
            }
        }
    }

}