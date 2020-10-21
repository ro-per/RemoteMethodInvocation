package server;

import client.ChatClientInterface;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {

    private Map<String, ChatClientInterface> userNames = new HashMap<>();

    public ChatServer() throws RemoteException {
        super();
    }


    @Override
    public void broadcast(String name, String msg) {
        String formattedMsg = formatMessage(name, msg);

        Iterator<Map.Entry<String, ChatClientInterface>> iterator = userNames.entrySet().iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().getValue().receiveMessage(formattedMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addUser(String username, ChatClientInterface client) {
        userNames.put(username, client);
    }

    @Override
    public void removeUser(String username) {
        userNames.remove(username);
    }

    Set<String> getUserNames() {
        return this.userNames.keySet();
    }

    @Override
    public boolean isValidUserName(String userName) {

        if (userNames.keySet().contains(userName)) {
            return false;
        } else {
            return true;
        }
    }

    private String formatMessage(String userName, String msg) {
        return "[ " + userName + " ]: " + msg;
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
        int portNumber = 1099;
        Registry registry = LocateRegistry.createRegistry(portNumber);
        Naming.rebind(ChatServerInterface.SERVICE_NAME, new ChatServer());
    }


}
