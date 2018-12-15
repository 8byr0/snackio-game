package fr.esigelec.snackio.networking.server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.core.models.IGameEngine;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.pois.Coin;
import fr.esigelec.snackio.networking.Position;
import fr.esigelec.snackio.networking.experi.IPlayer;
import fr.esigelec.snackio.networking.NetworkConfig;


import javax.swing.*;

public class SnackioNetServer {
    Server server;
    ArrayList<NetPlayer> players = new ArrayList();

    public SnackioNetServer () throws IOException {
        Thread serverThread = new Thread(()->{
            System.out.println("Server thread started");

            Coin testCoin = new Coin();
                testCoin.setPosition(750,300);

//            Thread t = new Thread(()->{
                for (NetPlayer player : players){
                    System.out.println("Sending coin to player " + player.name);
                    player.gameEngine.addPointOfInterest(testCoin);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//            });
//            t.start();
        });
        server = new Server() {
            protected Connection newConnection () {
                // Each connection represents a player and has fields
                // to store state and methods to perform actions.
                Log.debug("New connection");
                NetPlayer newlyCreatedPlayer = new NetPlayer();

//                Thread t = new Thread(()-> {
//                    for (NetPlayer existingPlayer : players) {
//                        Player existing = existingPlayer.gameEngine.getPlayer();
//                        System.out.println(existing);
//                        newlyCreatedPlayer.addRemotePlayer(existing);
//                    }
//                });
//                t.start();
                players.add(newlyCreatedPlayer);

                return newlyCreatedPlayer;

            }
        };




        // Register the classes that will be sent over the network.
        NetworkConfig.register(server);

        server.addListener(new Listener() {
            public void disconnected (Connection connection) {
                NetPlayer player = (NetPlayer)connection;
                players.remove(player);
                if (player.name != null) {
                    // Announce to everyone that someone (with a registered name) has left.
                    String message = player.name + " disconnected.";
//                    for (NetPlayer p : players)
//                        p.frame.addMessage(message);
                    updateNames();
                }
            }
        });
        server.bind(NetworkConfig.port);
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

    void updatePlayers () {
//        Thread t = new Thread(()-> {
//            for (NetPlayer existingPlayer : players) {
//                Player existing = existingPlayer.gameEngine.getPlayer();
//
//                newlyCreatedPlayer.addRemotePlayer(existing);
//            }
//        });
//        t.start();
    }

    void updateNames () {
        // Collect the names of each player.
//        ArrayList namesList = new ArrayList(players.size());
//        for (NetPlayer player : players)
//            if (player.name != null) namesList.add(player.name);
//        // Set the names on everyone's chat frame.
//        String[] names = (String[])namesList.toArray(new String[namesList.size()]);
//        Thread t = new Thread(()->{
//        for (NetPlayer player : players)
//            player.frame.setNames(names);});
//        t.start();
    }

    class NetPlayer extends Connection implements IPlayer {
        IGameEngine gameEngine;
        String name;
        Player localPlayer;

        public NetPlayer() {
            // Each connection has an ObjectSpace containing the NetPlayer.
            // This allows the other end of the connection to call methods on the NetPlayer.
            new ObjectSpace(this).register(NetworkConfig.RMI_PLAYER_ID, this);
            // Get the ChatFrame on the other end of the connection.
            // This allows the server to call methods on the client.
            gameEngine = ObjectSpace.getRemoteObject(this, NetworkConfig.RMI_GAME_ENGINE_ID, IGameEngine.class);
        }


        /**
         * Method called when a new player registers on the server
         * @param receivedPlayer
         */
        public void registerPlayer (Player receivedPlayer) {
            this.localPlayer = receivedPlayer;
            // Add a "connected" message to everyone's chat frame, except the new player.
            Thread t = new Thread(()->{
                for (NetPlayer netPlayer : players) {
                    if (netPlayer != this) {
                        netPlayer.gameEngine.addPlayer(receivedPlayer);
                        gameEngine.addPlayer(netPlayer.localPlayer);
                    }
                }
            // Set the names on everyone's chat frame.
                updatePlayers();
            });
            t.start();
        }

        public void setPosition(Position pos) {
            Player randomPlayer = new Player("YOLO", CharacterFactory.CharacterType.NUDE_MAN);
            randomPlayer.getCharacter().setPosition(1000,1000);
            Thread t = new Thread(()->{
                for (NetPlayer player : players){
                    player.gameEngine.addPlayer(randomPlayer);
                }
            });
            t.start();
        }

        @Override
        public void updatePlayerMotion(int id, Position position, Direction direction) {
            System.out.println("Player position updated");
            Thread t = new Thread(()->{
                for (NetPlayer player : players){
                    if(player != this){
                        System.out.println("Sending position to player");
//                        int idx = player.gameEngine.getPlayers().indexOf(this.gameEngine.getPlayer());
//                        player.gameEngine.getPlayers().get(idx).setPosition(position);
                        player.gameEngine.updatePlayerPosition(id, position, direction);
                    }
                }
            });
            t.start();
        }

        @Override
        public void addRemotePlayer(Player existingPlayer) {
            Thread t = new Thread(()->{
                for (NetPlayer player : players){
                    if(player != this){
                        this.gameEngine.addPlayer(player.gameEngine.getPlayer());
                    }
                }
            });
            t.start();
        }

        @Override
        public ArrayList<Player> getPlayers() {
            ArrayList<Player> playersReturned = new ArrayList<>();
            for(NetPlayer netPlayer: players)
            {
                playersReturned.add(netPlayer.gameEngine.getPlayer());
            }
            return playersReturned;
        }
    }

    public static void main (String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
        new SnackioNetServer();
    }
}