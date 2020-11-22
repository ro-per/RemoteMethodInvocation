package main;

import com.sun.istack.internal.Nullable;
import com.sun.org.glassfish.gmbal.ManagedObject;
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
    /*  -------------------------------- LOGGER -------------------------------- */
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());

    /*  -------------------------------- CONNECTION STUFF -------------------------------- */
    private ChatServiceInterface service;
    private final String server;
    private final int serverPortNumber;

    /*  -------------------------------- LISTS -------------------------------- */
    private final ObservableList<String> messagesPublic;
    private final ObservableList<String> messagesPrivate;
    private final ObservableList<String> users;

    /*  -------------------------------- USER INFO -------------------------------- */
    private User user;

    /*  -------------------------------- CONSTRUCTORS -------------------------------- */
    public ChatClient(String serverName, int serverPortNumber) throws RemoteException, MalformedURLException, NotBoundException {
        super();
        this.server = serverName;
        this.serverPortNumber = serverPortNumber;

        //ClientThread Part
        this.messagesPublic = FXCollections.observableArrayList();
        this.messagesPrivate = FXCollections.observableArrayList();
        this.users = FXCollections.observableArrayList();

        logger.log(Level.FINE, "Constructor 'ChatClient(String serverName, int serverPortNumber)' used");
    }

    /*  -------------------------------- START -------------------------------- */
    public void start() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(server, serverPortNumber);
        service = (ChatServiceInterface) registry.lookup(ChatServiceInterface.SERVICE_NAME);
        logger.log(Level.INFO, "Client started");
    }

    /*  -------------------------------- CONNECT/DISCONNECT -------------------------------- */
    public boolean connectUser(String name) throws IOException {
        user = new User(name);
        if (service.connectUser(user, this)) {
            info("Trying to connect " + name);
            removeUser(this.user);
            return true;
        }
        return false;
    }

    public void disconnectUser() {
        try {
            service.disconnectUser(user, this);
            info("Leaving...");
        } catch (RemoteException e) {
            error("Could not connect with the server.");
            e.printStackTrace();
        }
    }

    /*  -------------------------------- SENDING MESSAGES -------------------------------- */
    public void sendBroadcastMsg(String message) {
        new Thread(() -> this.sendBroadcastUtil(message)).start();
    }

    private void sendBroadcastUtil(String text) {
        Message message = new Message(user, MessageType.BROADCAST, text);
        try {
            info("Broadcasting...");
            service.sendBroadcastMsg(message);
        } catch (RemoteException e) {
            error("Could not connect with the server.");
            e.printStackTrace();
        }
    }

    public void sendPrivateMsg(String text, String receiver) {
        Message message = new Message(user, MessageType.PRIVATE, text, receiver); // PRIVATE has 1 receiver
        try {
            info("Sending private message ...");
            service.sendPrivateMsg(message);
        } catch (IOException e) {
            error("Could not connect with the server.");
        }
    }

    public void sendRequestPrivateMSG(String text, String receiver) {
        Message message = new Message(user, MessageType.REQUEST_PRIVATE, text, receiver); // PRIVATE has 1 receiver
        try {
            info("Sending private message ...");
            service.sendPrivateMsg(message);
        } catch (IOException e) {
            error("Could not connect with the server.");
        }
    }

    /*  -------------------------------- GETTING MESSAGES -------------------------------- */
    @Override
    public void receivePublicMessage(Message message) throws RemoteException {
        Platform.runLater(() -> {
            MessageType messageType = message.getType();
            String content = message.getContent();
            switch (messageType) {
                case ERROR_LOGIN:
                    try {
                        ChatApplication.showLogin(content);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    messagesPublic.add(message.getContent());
                    break;
            }
        });
    }

    @Override
    public void receivePrivateMessage(Message message) throws RemoteException {
        Platform.runLater(() -> {
            try {
                MessageType messageType = message.getType();
                String sender = message.getSender().toString();
                switch (messageType) {
                    case PRIVATE:
                        messagesPrivate.add(message.getContent());
                        break;
                    case REQUEST_PRIVATE:
                        boolean b = ChatApplication.askOpenNewChat(sender);
                        if (b) {
                            ChatApplication.resetPrivateChat();
                            ChatApplication.launchPrivateChat(sender);
                        } else {
                            Message msg = new Message(user, MessageType.PRIVATE, "I do not want to talk now", sender);
                            service.sendPrivateMsg(msg);
                        }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void receiveUserList(Message message) throws RemoteException {
        Platform.runLater(() -> users.addAll(message.getActiveUsers()));
    }

    /*  -------------------------------- METHODS -------------------------------- */
    @Override
    public void addUser(User user) throws RemoteException {
        Platform.runLater(() -> users.add(user.getName()));
    }

    @Override
    public void removeUser(User user) throws RemoteException {
        Platform.runLater(() -> users.remove(user.getName()));
    }

    public void resetPrivateChat() {
        messagesPrivate.clear();
    }

    public void addPrivateMessage(String text) {
        messagesPrivate.add(text);
    }

    /*  -------------------------------- GETTERS -------------------------------- */
    public ObservableList<String> getPublicMessages() {
        return messagesPublic;
    }

    public ObservableList<String> getPrivateMessages() {
        return messagesPrivate;
    }

    public ObservableList<String> getUsers() {
        return users;
    }

    public User getUser() {
        return user;
    }

    public String getServer() {
        return server;
    }

    /*  -------------------------------- SETTERS -------------------------------- */
    public void setUser(User user) {
        this.user = user;
    }

    /*  -------------------------------- LOGGER -------------------------------- */
    private void info(String msg, @Nullable Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private void error(String msg, @Nullable Object... params) {
        logger.log(Level.WARNING, msg, params);
    }
}
