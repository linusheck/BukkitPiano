package me.glatteis.bukkitpiano;

/**
 * Created by Linus on 11.01.2016.
 */
public class NoteHandler {

    public static float getPitch(int note) {
        switch (note) {
            case 0:
                return 0.5F; //F#
            case 1:
                return 0.53F;//G
            case 2:
                return 0.56F;//G#
            case 3:
                return 0.6F;//A
            case 4:
                return 0.63F;//A#
            case 5:
                return 0.67F;//B
            case 6:
                return 0.7F;//C
            case 7:
                return 0.76F;//C#
            case 8:
                return 0.8F;//D
            case 9:
                return 0.84F;//D#
            case 10:
                return 0.9F;//E
            case 11:
                return 0.94F;//F
            case 12:
                return 1.0F;//F#
            case 13:
                return 1.06F;//G
            case 14:
                return 1.12F;//G#
            case 15:
                return 1.18F;//A
            case 16:
                return 1.26F;//A#
            case 17:
                return 1.34F;//B
            case 18:
                return 1.42F;//C
            case 19:
                return 1.5F;//C#
            case 20:
                return 1.6F;//D
            case 21:
                return 1.68F;//D#
            case 22:
                return 1.78F;//E
            case 23:
                return 1.88F;//F
            case 24:
                return 2.0F;//F#
        }
        return -1;
    }

}
