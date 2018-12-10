package fr.esigelec.snackio.networking;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.minlog.Log;

// This class is the server for a simple chat client/server example that uses RMI.
// It is recommended to review the non-RMI chat example first.
// While this example uses only RMI, RMI can be mixed with non-RMI KryoNet usage.
// RMI has more overhead (usually 4 bytes) then just sending an object.
public class SnackioServer {
    Server server;
    ArrayList<RemotePlayer> remotePlayers = new ArrayList();

    public SnackioServer () throws IOException {
        server = new Server() {
            protected Connection newConnection () {
                // Each connection represents a remotePlayer and has fields
                // to store state and methods to perform actions.
                RemotePlayer remotePlayer = new RemotePlayer();
                remotePlayers.add(remotePlayer);
                return remotePlayer;
            }
        };

        // Register the classes that will be sent over the network.
        Network.register(server);

        server.addListener(new Listener() {
            public void disconnected (Connection connection) {
                RemotePlayer remotePlayer = (RemotePlayer)connection;
                remotePlayers.remove(remotePlayer);
                if (remotePlayer.name != null) {
                    // Announce to everyone that someone (with a registered name) has left.
                    String message = remotePlayer.name + " disconnected.";
                    for (RemotePlayer p : remotePlayers)
                        p.frame.addMessage(message);
                    updateNames();
                }
            }
        });
        server.bind(Network.port);
        server.start();

        // Open a window to provide an easy way to stop the server.
        JFrame frame = new JFrame("Chat RMI Server");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosed (WindowEvent evt) {
                server.stop();
            }
        });
        frame.getContentPane().add(new JLabel("Close to stop the chat server."));
        frame.setSize(320, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void updateNames () {
        // Collect the names of each player.
        ArrayList namesList = new ArrayList(remotePlayers.size());
        for (RemotePlayer remotePlayer : remotePlayers)
            if (remotePlayer.name != null) namesList.add(remotePlayer.name);
        // Set the names on everyone's chat frame.
        String[] names = (String[])namesList.toArray(new String[namesList.size()]);
        for (RemotePlayer remotePlayer : remotePlayers)
            remotePlayer.frame.setNames(names);
    }

    class RemotePlayer extends Connection implements IPlayer {
        IChatFrame frame;
        String name;

        public RemotePlayer() {
            // Each connection has an ObjectSpace containing the RemotePlayer.
            // This allows the other end of the connection to call methods on the RemotePlayer.
            new ObjectSpace(this).register(Network.PLAYER, this);
            // Get the ChatFrame on the other end of the connection.
            // This allows the server to call methods on the client.
            frame = ObjectSpace.getRemoteObject(this, Network.CHAT_FRAME, IChatFrame.class);
        }

        public void registerName (String name) {
            // Do nothing if the player already registered a name.
            if (this.name != null) return;
            // Do nothing if the name is invalid.
            if (name == null) return;
            name = name.trim();
            if (name.length() == 0) return;
            // Store the player's name.
            this.name = name;
            // Add a "connected" message to everyone's chat frame, except the new player.
            String message = name + " connected.";
            for (RemotePlayer remotePlayer : remotePlayers)
                if (remotePlayer != this) remotePlayer.frame.addMessage(message);
            // Set the names on everyone's chat frame.
            updateNames();
        }

        public void sendMessage (String message) {
            // Do nothing if a player tries to chat before registering a name.
            if (this.name == null) return;
            // Do nothing if the chat message is invalid.
            if (message == null) return;
            message = message.trim();
            if (message.length() == 0) return;
            // Prepend the player's name and add to everyone's chat frame.
            message = this.name + ": " + message;
            for (RemotePlayer remotePlayer : remotePlayers)
                remotePlayer.frame.addMessage(message);
        }
    }

}