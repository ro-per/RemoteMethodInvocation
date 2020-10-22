package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.ChatServerInterface;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient extends UnicastRemoteObject implements ChatClientInterface {

    private String userName;
    private String serverName;
    private int serverPortNumber;
    private ObservableList<String> messages;
    private ObservableList<String> users;
    private ChatServerInterface server;

    public ChatClient(String serverName, int serverPortNumber) throws RemoteException, MalformedURLException, NotBoundException {
        super();
        this.serverName = serverName;
        this.serverPortNumber = serverPortNumber;
        messages = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();

        Registry registry = LocateRegistry.getRegistry(serverName, serverPortNumber);
        server = (ChatServerInterface) registry.lookup(ChatServerInterface.SERVICE_NAME);
    }

    public void broadcast(String message) {
        new Thread(() -> this.send(message)).start();
    }

    private void send(String message) {
        try {
            server.broadcast(userName, message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUserName(String name) throws RemoteException {

        if (server.isValidUserName(name)) {
            server.addUser(name, this);
            userName = name;
            return true;
        }
        return false;
    }

    public void leave() {
        try {
            server.removeUser(userName, this);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        Platform.runLater(() -> messages.add(message));
    }

    @Override
    public void addToUsers(String userName) throws RemoteException {
        Platform.runLater(() -> users.add(userName));
    }

    @Override
    public void removeFromUsers(String userName) throws RemoteException {
        Platform.runLater(() -> users.remove(userName));
    }

    public ObservableList<String> getMessages() {
        return messages;
    }

    public ObservableList<String> getUsers() {
        return users;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPortNumber() {
        return serverPortNumber;
    }

    public void setServerPortNumber(int serverPortNumber) {
        this.serverPortNumber = serverPortNumber;
    }
}
