package me.glatteis.bukkitpiano.desktop;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created by Linus on 10.01.2016.
 */
public class BukkitPianoMain {

    protected long id;

    private JFrame frame;
    private JPanel panel;
    protected JLabel statusLabel;
    protected JButton connectButton;

    private BukkitPianoSender sender;

    protected InetAddress serverAddress;

    public BukkitPianoMain() {
        sender = new BukkitPianoSender(this);

        id = new Random().nextLong();

        frame = new JFrame("BukkitPiano");
        frame.setSize(500, 500);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                sender.close();
                System.exit(0);
            }
        });
        frame.setResizable(false);

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        JLabel title = new JLabel("BukkitPiano Client");
        title.setFont(new Font("Arial", 0, 50));
        title.setBounds(20, 20, 500, 50);
        panel.add(title);

        final JTextField ipTextField = new JTextField();
        ipTextField.setBounds(50, 100, 400, 32);
        ipTextField.setFont(new Font("Arial", 0, 28));
        ipTextField.setToolTipText("Server IP");
        panel.add(ipTextField);

        final JTextField playerTextField = new JTextField();
        playerTextField.setBounds(50, 150, 400, 32);
        playerTextField.setFont(new Font("Arial", 0, 28));
        playerTextField.setToolTipText("Player Name");
        panel.add(playerTextField);

        connectButton = new JButton("Connect");
        connectButton.setBounds(50, 250, 400, 50);
        connectButton.setBackground(Color.LIGHT_GRAY);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    statusLabel.setText("Looking for host...");
                    serverAddress = InetAddress.getByName(ipTextField.getText());
                    statusLabel.setText("Found host " + serverAddress.toString());
                } catch (UnknownHostException e1) {
                    //
                }
                connectButton.setBackground(Color.GREEN);
                sender.sendConnect(playerTextField.getText());
            }
        });
        panel.add(connectButton);

        statusLabel = new JLabel();
        statusLabel.setBounds(50, 400, 400, 32);
        statusLabel.setFont(new Font("Arial", 0, 20));
        panel.add(statusLabel);

        updateDevices();

        frame.setVisible(true);

        try {
            serverAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void updateDevices() {
        try {
            MidiSystem.getTransmitter().setReceiver(sender);
        } catch (MidiUnavailableException e) {
            //This device doesn't seem to be interesting to us.
        }
    }

}
