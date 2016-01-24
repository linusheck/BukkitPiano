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

    protected int octave = 0;
    protected boolean velocitySensitivity;

    private BukkitPianoSender sender;

    protected InetAddress serverAddress;
    protected int port;

    public BukkitPianoMain() {
        sender = new BukkitPianoSender(this);

        id = new Random().nextLong();

        frame = new JFrame("BukkitPiano");
        frame.setSize(500, 500);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
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
        ipTextField.setBounds(50, 100, 300, 32);
        ipTextField.setFont(new Font("Arial", 0, 28));
        ipTextField.setToolTipText("Server IP");
        panel.add(ipTextField);

        final JTextField portField = new JTextField("25565");
        portField.setBounds(360, 100, 90, 32);
        portField.setFont(new Font("Arial", 0, 28));
        portField.setToolTipText("Port");
        panel.add(portField);

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
                    port = Integer.parseInt(portField.getText());
                    statusLabel.setText("Found host " + serverAddress.toString());
                } catch (UnknownHostException e1) {
                    //
                } catch (RuntimeException e1) {
                    statusLabel.setText("This does not seem to be a valid port.");
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

        final Integer[] octaves = {0, 1, 2, 3, 4, 5};
        String[] strings = new String[octaves.length];
        for (int i = 0; i < octaves.length; i++) {
            strings[i] = "+" + octaves[i] + " Octaves";
        }
        final JComboBox<String> comboBox = new JComboBox<String>(strings);
        comboBox.setBounds(50, 350, 200, 30);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                octave = octaves[comboBox.getSelectedIndex()];
            }
        });
        panel.add(comboBox);

        final JCheckBox velocityButton = new JCheckBox("Velocity Sensitivity");
        velocityButton.setBounds(260, 350, 200, 30);
        velocityButton.setSelected(true);
        velocityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                velocitySensitivity = velocityButton.isSelected();
            }
        });
        panel.add(velocityButton);

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
