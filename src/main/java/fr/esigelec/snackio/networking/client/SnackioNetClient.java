package fr.esigelec.snackio.networking.client;
import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.awt.EventQueue;
import java.net.InetAddress;

import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import fr.esigelec.snackio.core.models.IGameEngine;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.networking.experi.IPlayer;
import fr.esigelec.snackio.networking.NetworkConfig;

public class SnackioNetClient {

    Client client;
    IPlayer serverPlayer;

    IGameEngine gameEngine;

    public SnackioNetClient (IGameEngine engine, Player localPlayer) {

        gameEngine = engine;
        client = new Client();
        client.start();
        InetAddress address = client.discoverHost(54777, 5000);
        System.out.println(address);
        // Register the classes that will be sent over the network.
        NetworkConfig.register(client);

        // Get the Player created on the other end of the connection.
        // This allows the client to call methods on the server.
        serverPlayer = ObjectSpace.getRemoteObject(client, NetworkConfig.RMI_PLAYER_ID, IPlayer.class);

        client.addListener(new Listener() {
            public void disconnected (Connection connection) {
                EventQueue.invokeLater(new Runnable() {
                    public void run () {
                        // Closing the frame calls the close listener which will stop the client's update thread.

                    }
                });
            }
        });


        final String host = "localhost";

        final String name = "Hugues";

        // The chat frame contains all the Swing stuff.
        // Register the chat frame so the server can call methods on it.
        new ObjectSpace(client).register(NetworkConfig.RMI_GAME_ENGINE_ID, gameEngine);



        // We'll do the connect on a new thread so the ChatFrame can show a progress bar.
        // Connecting to localhost is usually so fast you won't see the progress bar.
        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(5000, host, NetworkConfig.port);
                    // Server communication after connection can go here, or in Listener#connected().

                    localPlayer.setID(client.getID());


                    gameEngine.addPlayerAddedListener((x)->{
                        new Thread(()->{
//                            serverPlayer.updatePlayerMotion(localPlayer.getID(), localPlayer.getPosition(), localPlayer.getDirection());
                        }).start();
                    });
                    serverPlayer.registerPlayer(localPlayer);

                    localPlayer.addMoveListener(()->{
                        serverPlayer.updatePlayerMotion(localPlayer.getID(), localPlayer.getPosition(), localPlayer.getDirection());
                    });

                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }.start();
    }



}