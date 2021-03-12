package me.glatteis.bukkitpiano.server;

import me.glatteis.bukkitpiano.NoteHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by Linus on 11.01.2016.
 * Thanks Linus, awesome plugin! Updated for 1.16 by avixk on March 12th 2021
 */
public class MusicalStuff {

    public void playNote(Player player, byte[] midiData) {


        byte note = midiData[1];//note 6 is lowest 78 is highest
        int bukkitNote = note - 6; // bukkit note 0 is lowest bukkit note 72 highest

        Sound sound;

        if (bukkitNote > 48) {
            bukkitNote -= 48;
            sound = Sound.BLOCK_NOTE_BLOCK_BELL;//we have more noteblock sounds now!!!!
        } else if(bukkitNote > 24){
            bukkitNote -= 24;
            sound = Sound.BLOCK_NOTE_BLOCK_HARP;
        } else {
            sound = Sound.BLOCK_NOTE_BLOCK_BASS;
        }
        float velocity = midiData[2] / 127F;

        //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
        //       "§eNote: " + note + " §6Bukkit Note: " + bukkitNote + " §eVelocity: " + velocity + " §6Sound: " + sound.name()));

        if (bukkitNote > 24 || bukkitNote < 0) return;
        float pitch = NoteHandler.getPitch(bukkitNote);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(player.getLocation(), sound, velocity, pitch);
        }
        double y = (note-6) / 72D;

        //Sorry, I can't be bothered to make this look pretty. ~avixk
        player.getWorld().spawnParticle(Particle.NOTE,
                player.getEyeLocation().add(
                        rotateVectorAroundY(player.getEyeLocation().getDirection().multiply(new Vector(1,0,1)).normalize(),90)
                                .multiply((y-0.5D)*3)).add(0,velocity,0) ,0,1,0,0, y);
    }
    public static Vector rotateVectorAroundY(Vector vector, double degrees) {
        double rad = Math.toRadians(degrees);

        double currentX = vector.getX();
        double currentZ = vector.getZ();

        double cosine = Math.cos(rad);
        double sine = Math.sin(rad);

        return new Vector((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
    }
}
