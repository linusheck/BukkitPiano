package me.glatteis.bukkitpiano.server;

import me.glatteis.bukkitpiano.NoteHandler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by Linus on 11.01.2016.
 */
public class MusicalStuff {

    public void playNote(Player player, byte[] midiData) {

        byte note = midiData[1];
        int bukkitNote = note - 6;

        Sound sound;

        if (bukkitNote > 24) {
            bukkitNote -= 24;
            sound = Sound.NOTE_PIANO;
        } else {
            sound = Sound.NOTE_BASS;
        }

        if (bukkitNote > 24 || bukkitNote < 0) return;

        float velocity = midiData[2] / 127F;
        float pitch = NoteHandler.getPitch(bukkitNote);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(player.getLocation(), sound, velocity, pitch);
        }
    }
}
