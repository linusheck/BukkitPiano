package me.glatteis.bukkitpiano.server;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    private PacketReceiver packetReceiver;

    protected List<PianoPlayer> pianoPlayers;
    protected List<PianoPlayer> confirmationPlayers;
    private String[] message = new String[] {
            ChatColor.GREEN + "" + ChatColor.UNDERLINE + "BukkitPiano Help",
            "",
            "BukkitPiano is a plugin for playing the piano in Minecraft.",
            ChatColor.BLUE + "How can I play the piano in Minecraft?",
            ChatColor.BLUE + "Step 1: " + ChatColor.RESET + "Download the BukkitPiano JAR from https://www.spigotmc.org/resources/16931/",
            ChatColor.BLUE + "Step 2: " + ChatColor.RESET + "Plug in any MIDI controller.",
            ChatColor.BLUE + "Step 3: " + ChatColor.RESET + "Double click the JAR. If you have a mac, it might warn you about " +
                    "JARs being dangerous. BukkitPiano doesn't want do any harm. Just agree to open it.",
            ChatColor.BLUE + "Step 4: " + ChatColor.RESET + "Type in the IP of this server in the first line, and your " +
                    "player name in the second line.",
            ChatColor.BLUE + "Step 5: " + ChatColor.RESET + "Click 'connect'. Then type in the command in chat."
    };

    public void onDisable() {
        packetReceiver.disable();
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        pianoPlayers = new ArrayList<PianoPlayer>();
        confirmationPlayers = new ArrayList<PianoPlayer>();
        try {
            packetReceiver = new PacketReceiver(this);
            new BukkitRunnable() {
                public void run() {
                    packetReceiver.recievePacket();
                }
            }.runTaskTimerAsynchronously(this, 0, 1);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("bukkitpiano")) {
            if (args.length == 0) {
                sender.sendMessage(message);
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.BLUE + "[BukkitPiano] " + ChatColor.RESET + "Only players can execute this command.");
                return true;
            }
            if (args[0].equals("confirm")) {
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