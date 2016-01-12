package me.glatteis.bukkitpiano.desktop;

import me.glatteis.bukkitpiano.LoginPacket;
import me.glatteis.bukkitpiano.NotePacket;
import me.glatteis.bukkitpiano.PackMethods;
import me.glatteis.bukkitpiano.QuitPacket;

import javax.sound.midi.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Linus on 10.01.2016.
 */
public class BukkitPianoSender implements Receiver {

    private BukkitPianoMain main;
    private DatagramSocket clientSocket;
    private boolean isConnected = true;

    private long savedTimeStamp;

    public BukkitPianoSender(BukkitPianoMain main) {
        this.main = main;
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(0);
        }
        savedTimeStamp = 0;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (timeStamp == savedTimeStamp) return; //No double notes!
        byte[] byteMessage = message.getMessage();
        if (byteMessage[0] != -112 || !isConnected | byteMessage[2] == 0) return; //We only want ON messages that are of velocities greater than zero.

        byteMessage[1] -= (main.octave * 12) + 12;
        if (!main.velocitySensitivity) byteMessage[2] = 127;

        NotePacket notePacket = new NotePacket();

        notePacket.id = main.id;
        notePacket.midiData = byteMessage;

        try {
            byte[] packet = PackMethods.pack(notePacket);
            DatagramPacket datagramPacket = new DatagramPacket(packet, packet.length, main.serverAddress, 25565);
            clientSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendConnect(String username) {
        LoginPacket loginPacket = new LoginPacket();
        loginPacket.mcName = username;
        loginPacket.programID = main.id;
        int retry = 3;
        while (true) {
            try {
                byte[] packet= PackMethods.pack(loginPacket);
                DatagramPacket datagramPacket = new DatagramPacket(packet, packet.length, main.serverAddress, 25565);
                clientSocket.send(datagramPacket);
                clientSocket.setSoTimeout(1500);
                clientSocket.receive(datagramPacket);
                main.statusLabel.setText("Requested login.");
                isConnected = true;
                return;
            } catch (IOException e) {
                if (retry == 0) return;
                retry--;
                main.statusLabel.setText("Connection unsuccessful.");
                main.connectButton.setBackground(new Color(200, 54, 42));
            }
        }



    }

    @Override
    public void close() {
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            try {
                MidiSystem.getMidiDevice(info).close();
            } catch (MidiUnavailableException e) {
                //Well, we don't have to close that.
            }
        }


        if (isConnected) {
            QuitPacket quitPacket = new QuitPacket();
            quitPacket.id = main.id;

            try {
                byte[] packet = PackMethods.pack(quitPacket);
                DatagramPacket datagramPacket = new DatagramPacket(packet, packet.length, main.serverAddress, 25565);
                clientSocket.send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        clientSocket.close();
    }
}
