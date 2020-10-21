package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
    private Set<String> userNames = new HashSet<>();
    private int portNumber;

    public ChatServer(int portNumber) throws RemoteException {
        this.portNumber = portNumber;
    }

    private void start() {
        try {
            Registry registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind("ChatService", this);

        } catch (Exception e) {
            System.err.println("Could not listen on port " + portNumber);
            e.printStackTrace();
        }
        System.out.println("Server listening on port " + portNumber);
    }

    public static void main(String[] args) {
        int portNumber = 1000;
        ChatServer server = null;
        try {
            server = new ChatServer(portNumber);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        server.start();
    }

    @Override
    public void broadcast(String userName, String message) throws RemoteException {

    }

    @Override
    public void checkId(RemoteRef ref) throws RemoteException {

    }

    @Override
    public void registerClient(String userName) throws RemoteException {

    }

    @Override
    public void sendPrivateMessage(List<String> group, String message) throws RemoteException {

    }
}
