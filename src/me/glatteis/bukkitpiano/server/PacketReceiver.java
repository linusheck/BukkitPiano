package me.glatteis.bukkitpiano.server;

import me.glatteis.bukkitpiano.LoginPacket;
import me.glatteis.bukkitpiano.NotePacket;
import me.glatteis.bukkitpiano.PackMethods;
import me.glatteis.bukkitpiano.QuitPacket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Linus on 10.01.2016.
 */
public class PacketReceiver {

    private DatagramPacket packet;
    private DatagramSocket socket;

    private MusicalStuff musicalStuff;

    private ServerMain main;

    public PacketReceiver(ServerMain main) throws SocketException {
        this.main = main;
        socket = new DatagramSocket(25566);
        socket.setSoTimeout(1);
        byte[] buf = new byte[1080];
        packet = new DatagramPacket(buf, buf.length);
        musicalStuff = new MusicalStuff();
    }

    public void recievePacket() {
        try {
            while (socket.getBroadcast()) {
                socket.receive(packet);
                Object o = PackMethods.unpack(packet.getData());
                if (o instanceof LoginPacket) {
                    LoginPacket loginPacket = ((LoginPacket) o);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().equals(loginPacket.mcName)) {
                            p.sendMessage("A BukkitPiano client just requested a login. Type " +
                                    ChatColor.GOLD + "[/bukkitpiano confirm]" + ChatColor.RESET + " if this is you.");
                            PianoPlayer pianoPlayer = new PianoPlayer(p, loginPacket.programID);
                            for (PianoPlayer pianoPlayer2 : main.confirmationPlayers) {
                                if (pianoPlayer2.player.equals(pianoPlayer.player)) {
                                    main.confirmationPlayers.remove(pianoPlayer2);
                                    break;
                                }
                            }
                            for (PianoPlayer pianoPlayer2 : main.pianoPlayers) {
                                if (pianoPlayer2.player.equals(pianoPlayer.player)) {
                                    main.pianoPlayers.remove(pianoPlayer2);
                                    break;
                                }
                            }
                            main.confirmationPlayers.add(pianoPlayer);
                            break;
                        }
                    }
                    socket.send(packet);
                }
                if (main.pianoPlayers.isEmpty()) break;
                if (o instanceof NotePacket) {
                    NotePacket notePacket = (NotePacket) o;
                    byte[] midiBytes = notePacket.midiData;
                    for (PianoPlayer pianoPlayer : main.pianoPlayers) {
                        if (pianoPlayer.id == notePacket.id) {
                            musicalStuff.playNote(pianoPlayer.player, midiBytes);
                            break;
                        }
                    }
                } else if (o instanceof QuitPacket) {
                    QuitPacket quitPacket = (QuitPacket) o;
                    for (PianoPlayer pianoPlayer : main.pianoPlayers) {
                        if (pianoPlayer.id == quitPacket.id) {
                            main.pianoPlayers.remove(pianoPlayer);
                            pianoPlayer.player.sendMessage("Closed connection.");
                            break;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //
        }
    }

    public void disable() {
        socket.close();

    }

}
