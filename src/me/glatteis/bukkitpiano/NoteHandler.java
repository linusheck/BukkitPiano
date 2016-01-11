package me.glatteis.bukkitpiano;

import org.bukkit.Note;

/**
 * Created by Linus on 11.01.2016.
 */
public class NoteHandler {

    public static BukkitPianoNote getNote(int note) {
        switch (note) {
            case 0:
                return new BukkitPianoNote(Note.Tone.C, false);
            case 1:
                return new BukkitPianoNote(Note.Tone.C, true);
            case 2:
                return new BukkitPianoNote(Note.Tone.D, false);
            case 3:
                return new BukkitPianoNote(Note.Tone.D, true);
            case 4:
                return new BukkitPianoNote(Note.Tone.E, false);
            case 5:
                return new BukkitPianoNote(Note.Tone.F, false);
            case 6:
                return new BukkitPianoNote(Note.Tone.F, true);
            case 7:
                return new BukkitPianoNote(Note.Tone.G, false);
            case 8:
                return new BukkitPianoNote(Note.Tone.G, true);
            case 9:
                return new BukkitPianoNote(Note.Tone.A, false);
            case 10:
                return new BukkitPianoNote(Note.Tone.A, true);
            case 11:
                return new BukkitPianoNote(Note.Tone.B, false);
            default:
                return null;
        }
    }

    public static class BukkitPianoNote {
        public final boolean sharp;
        public final Note.Tone tone;
        public BukkitPianoNote(Note.Tone tone, boolean sharp) {
            this.tone = tone;
            this.sharp = sharp;
        }
    }


}
