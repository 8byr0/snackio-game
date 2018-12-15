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
import fr.esigelec.snackio.core.IGameEngine;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.networking.Position;
import fr.esigelec.snackio.networking.experi.IPlayer;
import fr.esigelec.snackio.networking.NetworkConfig;
import javax.swing.*;

/**
 * SnackioNetServer class handles all incoming connections from connected clients and is in charge
 * of synchronizing positions from one to all.
 *
 * The way it interacts is based on RMI (Remote Method Invocation).
 *
 * # How it works:
 * ## Initialisation
 * - Client (Associated to Player) connects
 * - A NetPlayer object is created
 * - The Client's Player Character is sent to all existing clients
 * - The Client receives all existing Character to render them on his own map
 *
 * # Game
 * - During the Game, the client sends his Player's Character Position each time it's updated.
 */
public class SnackioNetServer {
    private Server server;
    // TODO move this to a proper HashMap
    private ArrayList<NetPlayer> players = new ArrayList<>();

    /**
     * Default Class constructor
     * @throws IOException When the server cannot bind given port
     */
    private SnackioNetServer() throws IOException {

        server = new Server() {
            protected Connection newConnection () {
                // Each connection represents a player and has fields
                // to store state and methods to perform actions.
                Log.debug("New connection");
                NetPlayer newlyCreatedPlayer = new NetPlayer();
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
                }
            }
        });
        server.bind(NetworkConfig.port);
        server.start();

        // Open a window to provide an easy way to stop the server.
        JFrame frame = new JFrame("Snackio DEBUG server");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosed (WindowEvent evt) {
                server.stop();
            }
        });
        frame.getContentPane().add(new JLabel("Close to stop the snackio server."));
        frame.setSize(320, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    class NetPlayer extends Connection implements IPlayer {
        IGameEngine gameEngine;
        String name;
        Player localPlayer;

        NetPlayer() {
            // Each connection has an ObjectSpace containing the NetPlayer.
            // This allows the other end of the connection to call methods on the NetPlayer.
            new ObjectSpace(this).register(NetworkConfig.RMI_PLAYER_ID, this);
            // Get the ChatFrame on the other end of the connection.
            // This allows the server to call methods on the client.
            gameEngine = ObjectSpace.getRemoteObject(this, NetworkConfig.RMI_GAME_ENGINE_ID, IGameEngine.class);
        }


        /**
         * Method called when a new player registers on the server
         * @param receivedPlayer the player that just registered
         */
        public void registerPlayer (Player receivedPlayer) {
            this.localPlayer = receivedPlayer;
            // Add a "connected" message to everyone's chat frame, except the new player.
            Thread t = new Thread(()->{
                for (NetPlayer netPlayer : players) {
                    if (netPlayer != this) {
                        try {
                            netPlayer.gameEngine.addPlayer(receivedPlayer);
                            gameEngine.addPlayer(netPlayer.localPlayer);
                        } catch (NoCharacterSetException | UnhandledControllerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.start();
        }

        public void setPosition(Position pos) throws NoCharacterSetException, UnhandledCharacterTypeException {
            Player randomPlayer = new Player("YOLO", CharacterFactory.CharacterType.NUDE_MAN);
            randomPlayer.getCharacter().setPosition(1000,1000);
            Thread t = new Thread(()->{
                for (NetPlayer player : players){
                    try {
                        player.gameEngine.addPlayer(randomPlayer);
                    } catch (NoCharacterSetException | UnhandledControllerException e) {
                        e.printStackTrace();
                    }
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
                        try {
                            player.gameEngine.updatePlayerPosition(id, position, direction);
                        } catch (NoCharacterSetException e) {
                            e.printStackTrace();
                        }
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
                        try {
                            this.gameEngine.addPlayer(player.gameEngine.getPlayer());
                        } catch (NoCharacterSetException | UnhandledControllerException e) {
                            e.printStackTrace();
                        }
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