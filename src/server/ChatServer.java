package server;

import client.ChatClientInterface;
import javafx.collections.ObservableList;

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
    private ObservableList<String> users;

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
    public void addUser(String username, ChatClientInterface client) throws RemoteException {
        userNames.put(username, client);
        client.addToUsers(username);
        client.receiveMessage(username + " has entered the chat");
    }

    @Override
    public void removeUser(String username, ChatClientInterface client) throws RemoteException {
        userNames.remove(username);
        client.removeFromUsers(username);
    }

    Set<String> getUserNames() {
        return this.userNames.keySet();
    }

    @Override
    public boolean isValidUserName(String userName) {

        return !userNames.containsKey(userName);
    }

    private String formatMessage(String userName, String msg) {
        return "[" + userName + "]: " + msg;
    }

    public static void main(String[] args) throws RemoteException {
        int portNumber = 1099;
        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind(ChatServerInterface.SERVICE_NAME, new ChatServer());
    }


}
