import com.sun.istack.internal.Nullable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import messages.Message;
import messages.MessageType;
import user.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient extends UnicastRemoteObject implements ChatClientInterface {

    private static final long serialVersionUID = 77L;
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());

    private User user;
    private String serverName;
    private int serverPortNumber;
    private final ObservableList<String> messages;
    private final ObservableList<String> users;
    private ChatServiceInterface service;

    public ChatClient(String serverName, int serverPortNumber) throws RemoteException, MalformedURLException, NotBoundException {
        super();
        this.serverName = serverName;
        this.serverPortNumber = serverPortNumber;
        messages = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();

    }

    public void start() throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(serverName, serverPortNumber);
        service = (ChatServiceInterface) registry.lookup(ChatServiceInterface.SERVICE_NAME);
    }

    public boolean isConnected(String name) throws RemoteException {
        user = new User(name);
        if (service.connectUser(user, this)) {
            info("Trying to connect " + name);
            return true;
        }
        return false;
    }

    public void sendBroadcast(String message) {
        new Thread(() -> this.sendBroadcastMsg(message)).start();
    }

    private void sendBroadcastMsg(String text) {
        Message message = new Message(user, MessageType.BROADCAST, text);

        try {
            info("Broadcasting...");
            service.sendBroadcastMsg(message);
        } catch (RemoteException e) {
            error("Could not connect with the server.");
            e.printStackTrace();
        }
    }

    public void sendPrivateMSG(String text, String receiver) {
        Message message = new Message(user, MessageType.PRIVATE, text, receiver); // PRIVATE has 1 receiver
        try {
            info("Sending private message ...");
            service.sendPrivateMsg(message);
        } catch (IOException e) {
            error("Could not connect with the server.");
        }
    }

    public void leave() {
        try {
            service.disconnectUser(user, this);
            info("Leaving...");
        } catch (RemoteException e) {
            error("Could not connect with the server.");
            e.printStackTrace();
        }
    }


    @Override
    public void receiveMessage(Message message) throws RemoteException {
        Platform.runLater(() -> messages.add(message.getContent()));
    }

    @Override
    public void addToUsers(User user) throws RemoteException {
        Platform.runLater(() -> users.add(user.getName()));
    }

    @Override
    public void removeFromUsers(User user) throws RemoteException {
        Platform.runLater(() -> users.remove(user.getName()));
    }

    private static void info(String msg, @Nullable Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private static void error(String msg, @Nullable Object... params) {
        logger.log(Level.WARNING, msg, params);
    }

    public ObservableList<String> getMessages() {
        return messages;
    }

    public ObservableList<String> getUsers() {
        return users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
