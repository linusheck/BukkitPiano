package me.glatteis.bukkitpiano.server;

import me.glatteis.bukkitpiano.NoteHandler;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;

/**
 * Created by Linus on 11.01.2016.
 */
public class MusicalStuff {

    public void playNote(Player player, byte[] midiData) {

        byte note = midiData[1];

        int octave = ((note + 6) / 12) - 3;
        int currentNote = note % 12;

        NoteHandler.BukkitPianoNote bukkitPianoNote = NoteHandler.getNote(currentNote);

        if (bukkitPianoNote == null) return;

        Instrument instrument;

        if (octave < 0) {
            instrument = Instrument.BASS_GUITAR;
            octave++;
        } else {
            instrument = Instrument.PIANO;
        }

        if (octave < 0 || octave > 1) {
            if (octave == -1) {
                switch (bukkitPianoNote.tone) {
                    case C:
                        instrument = Instrument.BASS_DRUM;
                        break;
                    case D:
                        instrument = Instrument.SNARE_DRUM;
                        break;
                    default:
                        instrument = Instrument.STICKS;
                }
            } else {
                instrument = Instrument.STICKS;
            }
            octave = 0;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playNote(player.getLocation(), instrument, new Note(octave, bukkitPianoNote.tone, bukkitPianoNote.sharp));
        }
    }
}
