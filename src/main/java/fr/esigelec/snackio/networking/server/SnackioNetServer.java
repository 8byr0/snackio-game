package fr.esigelec.snackio.networking.server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.HashMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.kryonet.rmi.TimeoutException;
import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.core.IGameEngine;
import fr.esigelec.snackio.core.IGameState;
import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.ServerGameEngine;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.state.AbstractGameState;
import fr.esigelec.snackio.game.state.MultiplayerGameState;
import fr.esigelec.snackio.networking.models.IRMIExecutablePlayer;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.networking.Position;
import fr.esigelec.snackio.networking.models.INetPlayer;
import fr.esigelec.snackio.networking.NetworkConfig;

import javax.swing.*;

/**
 * SnackioNetServer class handles all incoming connections from connected clients and is in charge
 * of synchronizing positions from one to all.
 * <p>
 * The way it interacts is based on RMI (Remote Method Invocation).
 * <p>
 * # How it works:
 * ## Initialisation
 * - Client (Associated to Player) connects
 * - A NetPlayer object is created
 * - The Client's Player Character is sent to all existing clients
 * - The Client receives all existing Character to render them on his own map
 * <p>
 * # Game
 * - During the Game, the client sends his Player's Character Position each time it's updated.
 * <p>
 * /!\/!\/!\
 * YOU WILL SEE MANY TIME IN THIS CLASS SOME METHODS EXECUTED WITHIN THREADS.
 * THIS IS NORMAL AND MUST BE DONE THIS WAY.
 * The reason is the if you don't, you'll block the server's thread.
 * (This is the same principle as for GUI applications: no heavy calls on main thread)
 * /!\/!\/!\
 */
public class SnackioNetServer {
    private Server server;
    private HashMap<Integer, NetPlayer> playersHashmap = new HashMap<>();
    private MultiplayerGameState serverState;
    private ServerGameEngine gameEngine = new ServerGameEngine();

    /**
     * Default Class constructor
     *
     * @throws IOException When the server cannot bind given udpPort
     */
    public SnackioNetServer(MapFactory.MapType mapType, String serverName) throws IOException {
        // Instantiate a gamestate that'll be shared w/ all connected clients
        serverState = new MultiplayerGameState(mapType, serverName);

        server = new Server() {
            protected Connection newConnection() {
                // Each connection represents a player and has fields
                // to store serverState and methods to perform actions.
                NetPlayer newlyCreatedPlayer = new NetPlayer(serverState);

                playersHashmap.put(playersHashmap.size(), newlyCreatedPlayer);

                return newlyCreatedPlayer;

            }
        };

        // Register the classes that will be sent over the network.
        NetworkConfig.register(server);

        server.addListener(new Listener() {
            public void disconnected(Connection connection) {
                System.out.println("PLAYER DISCONNECTED");
                NetPlayer player = (NetPlayer) connection;

                new Thread(() -> {
                    playersHashmap.forEach(((integer, netPlayer) -> {
                        if (netPlayer != player) {
                            if (netPlayer.isConnected()) {
                                netPlayer.gameEngine.removePlayer(player.getID());
                            }
                        }
                    }));
                    playersHashmap.remove(player.getID());

                }).start();
            }
        });

    }

    /**
     * Once your server is ready and initialized, call start to make it
     * bind given tcp && udp port
     */
    public void start() {
        try {
            server.bind(NetworkConfig.tcpPort, NetworkConfig.udpPort);

            server.start();


            // Open a window to provide an easy way to stop the server.
            JFrame frame = new JFrame("Snackio DEBUG server");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent evt) {
                    server.stop();
                }
            });
            frame.getContentPane().add(new JLabel("Close to stop the snackio server."));
            frame.setSize(320, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This inner class is instantiated for each connection.
     * The methods contained can be called from client's side.
     * To instantiate on the client, you need to use INetPlayer interface {@link INetPlayer}
     * <p>
     * {@code
     * // Create the Client instance
     * Client client = new Client();
     * <p>
     * // ...
     * <p>
     * INetPlayer serverPlayer = ObjectSpace.getRemoteObject(client, NetworkConfig.RMI_PLAYER_ID, INetPlayer.class);
     * }
     */
    class NetPlayer extends Connection implements INetPlayer {
        IGameEngine gameEngine;
        String name;
        IRMIExecutablePlayer localPlayer;
        AbstractGameState serverState;

        /**
         * Default Class ocnstructor
         * This constructor registers this object to make it available to the Client
         */
        NetPlayer(MultiplayerGameState gameState) {
            serverState = gameState;
            // Each connection has an ObjectSpace containing the NetPlayer.
            // This allows the other end of the connection to call methods on the NetPlayer.
            new ObjectSpace(this).register(NetworkConfig.RMI_PLAYER_ID, this);

            // Get the GameEngine on the other end of the connection.
            // This allows the server to call methods on the client.
            gameEngine = ObjectSpace.getRemoteObject(this, NetworkConfig.RMI_GAME_ENGINE_ID, IGameEngine.class);
        }

        public String getServerName() {
            return serverState.getName();
        }

        @Override
        public AbstractGameState getGameState() {
            return serverState;
        }

        /**
         * Method called when a new player registers on the server
         *
         * @param receivedPlayer the player that just registered
         */
        public void registerPlayer(IRMIExecutablePlayer receivedPlayer) {
            this.localPlayer = receivedPlayer;

            Thread t = new Thread(() -> {
                playersHashmap.forEach(((integer, netPlayer) -> {
                    if (netPlayer != this) {
                        try {
                            netPlayer.gameEngine.addPlayer(receivedPlayer);
                            gameEngine.addPlayer(netPlayer.localPlayer);
                        } catch (NoCharacterSetException | UnhandledControllerException e) {
                            e.printStackTrace();
                        }
                    }
                }));
            });
            t.start();
        }

        /**
         * Method to notify the server that the Player associated to this NetPlayer insatance has been updated
         *
         * @param id        ID of the updated Player
         * @param position  new Position on the Game's map
         * @param direction new Direction on the Game's map
         */
        @Override
        public void updatePlayerMotion(int id, Position position, Direction direction) {
            System.out.println("Player position updated");
            Thread t = new Thread(() -> {
                playersHashmap.forEach(((integer, player) -> {
                    if (player != this && player.isConnected()) {
                        try {
                            player.gameEngine.updatePlayerPosition(id, position, direction);
                        } catch (NoCharacterSetException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                            System.out.println("UNABLE TO UPDATE PLAYER POSITION (timeout)");
                        }
                    }
                }));
            });
            t.start();
        }

        /**
         * Update the room of a player
         *
         * @param id   ID of the updated Player
         * @param room new room name on the Game's map
         */
        @Override
        public void updatePlayerRoom(int id, String room) {
            Thread t = new Thread(() -> {
                playersHashmap.forEach(((integer, player) -> {
                    if (player != this && player.isConnected()) {
                        try {
                            player.gameEngine.updatePlayerRoom(id, room);
                        } catch (NoCharacterSetException e) {
                            e.printStackTrace();
                        }
                    }
                }));
            });
            t.start();
        }
    }

    /**
     * This main is only intended for debug purpose.
     *
     * @param args args
     * @throws IOException exception
     */
    public static void main(String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
        SnackioNetServer srv = new SnackioNetServer(MapFactory.MapType.DESERT_CASTLE, "A DEBUG SERVER");
        srv.start();
    }
}