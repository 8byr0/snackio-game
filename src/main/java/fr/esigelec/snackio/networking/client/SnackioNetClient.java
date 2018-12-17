package fr.esigelec.snackio.networking.client;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.awt.EventQueue;
import java.net.InetAddress;
import java.util.List;

import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import fr.esigelec.snackio.Snackio;
import fr.esigelec.snackio.core.IGameEngine;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.models.IRMIExecutablePlayer;
import fr.esigelec.snackio.core.models.INetPlayer;
import fr.esigelec.snackio.networking.NetworkConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Network interface that exchanges with server.
 *
 */
public class SnackioNetClient {
    private static final Logger logger = LogManager.getLogger(Snackio.class);

    private Client client;
    private INetPlayer serverPlayer;

    private IGameEngine gameEngine;

    /**
     * Default class constructor
     * @param engine the game engine to use
     */
    public SnackioNetClient(IGameEngine engine) {
        gameEngine = engine;

        client = new Client();
        client.start();

        // Register the classes that will be sent over the network.
        NetworkConfig.register(client);

        // Get the Player created on the other end of the connection.
        // This allows the client to call methods on the server.
        serverPlayer = ObjectSpace.getRemoteObject(client, NetworkConfig.RMI_PLAYER_ID, INetPlayer.class);

        client.addListener(new Listener() {
            public void disconnected(Connection connection) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        // Closing the frame calls the close listener which will stop the client's update thread.
                        System.out.println("Client disconnected");
                    }
                });
            }
        });

        // Register game engine so that it can be called from server
        new ObjectSpace(client).register(NetworkConfig.RMI_GAME_ENGINE_ID, gameEngine);

    }

    /**
     * Connect a SnackioNetServer
     * @param serverAddress the address of the server. see {@link SnackioNetClient#getAvailableServers()} to find available servers
     */
    public void connectServer(InetAddress serverAddress) {
        IRMIExecutablePlayer localPlayer = gameEngine.getPlayer();

        new Thread("Connect") {
            public void run() {
                try {

                    client.connect(NetworkConfig.timeout, serverAddress, NetworkConfig.tcpPort, NetworkConfig.udpPort);

                    // Server communication after connection can go here, or in Listener#connected().
                    localPlayer.setID(client.getID());

                    serverPlayer.registerPlayer(localPlayer);

                    localPlayer.addMoveListener(() -> {
                        try {
                            serverPlayer.updatePlayerMotion(localPlayer.getID(), localPlayer.getPosition(), localPlayer.getDirection());
                        } catch (NoCharacterSetException e) {
                            logger.error("Attempting to perform operations on a Player whose Character is not set");
                            e.printStackTrace();
                        }
                    });
                    localPlayer.addRoomChangeListener(() -> {
                        try {
                            serverPlayer.updatePlayerRoom(localPlayer.getID(), localPlayer.getRoom());
                        } catch (NoCharacterSetException e) {
                            logger.error("Attempting to perform operations on a Player whose Character is not set");
                            e.printStackTrace();
                        }

                    });

                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                } catch (NoCharacterSetException e) {
                    logger.error("Attempting to perform operations on a Player whose Character is not set");
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * Use lan discovery to find all available servers on lan
     * TODO instead of a list of addresses return a list of objects containing address, name and remaining players
     * @return a list of all addresses
     */
    public List<InetAddress> getAvailableServers() {
        List<InetAddress> availableServers;
        availableServers = client.discoverHosts(NetworkConfig.udpPort, NetworkConfig.timeout);
        return availableServers;
    }


}