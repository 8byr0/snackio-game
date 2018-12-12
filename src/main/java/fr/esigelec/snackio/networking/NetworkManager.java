package fr.esigelec.snackio.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class NetworkManager {
    private Server server;

    public NetworkManager() {

        server = new Server();

        Kryo kryo = server.getKryo();
        kryo.register(Request.class);
        kryo.register(Response.class);
        kryo.register(Position.class);


        try {
            server.bind(54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Request) {
                    Request request = (Request) object;
                    System.out.println(request.text);

                    Response response = new Response();
                    response.text = "Thanks - " + request.text;
                    connection.sendTCP(response);
                } else if (object instanceof Position) {
                    Position receivedPos = (Position) object;
                    System.out.println(receivedPos.toString());
                    Response response = new Response();
                    response.text = "Thanks - " + receivedPos.y;
                    connection.sendTCP(response);

                    // Update position
//                    RemotePlayer someObject = ObjectSpace.getRemoteObject(connection, 42, RemotePlayer.class);
//                    System.out.println(someObject);
//                    someObject.updatePosition(receivedPos);
                }
            }
        });
        for (int itr = 0; itr < 10; ++itr) {
            Thread t = new Thread(this::clientTCPTest);
            t.start();
        }
    }

    public void testBroadcast() {
//        Position test = new Position("213", "423");
//        server.sendToAllTCP(test);

    }

    public void clientTCPTest() {

        // Create client
        Client client = new Client();

        // Register data classes
        Kryo kryoX = client.getKryo();
        kryoX.register(Request.class);
        kryoX.register(Response.class);
        kryoX.register(Position.class);

        // Add listener
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Response) {
                    Response response = (Response) object;
                    System.out.println(response.text);
                } else if (object instanceof Position) {
                    Position response = (Position) object;
                    System.out.println(response.toString());
                }
            }
        });

        client.start();
        // Send test position
//        Position testPos = new Position("123", "3456");
        new Thread("Connect") {
            public void run() {
                try {
                    InetAddress serverAddress = client.discoverHost(54777, 5000);
                    client.connect(5000, serverAddress, 54555, 54777);

//                    client.sendTCP(testPos);

                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }.start();




    }

}
