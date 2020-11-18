package main;

import com.sun.istack.internal.Nullable;
import exceptions.DuplicateUsernameException;
import exceptions.UserNotFoundException;
import messages.Message;
import messages.MessageType;
import user.User;
import user.UserManager;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatService extends UnicastRemoteObject implements ChatServiceInterface {

    private static final Logger logger = Logger.getLogger(ChatService.class.getName());
    private final UserManager manager;

    private User serverUser = new User("Server");

    public ChatService() throws RemoteException {
        super();
        manager = new UserManager();
    }

    @Override
    public boolean connectUser(User user, ChatClientInterface client) throws RemoteException {
        info(user.getName() + " is connecting to the server.");
        try {
            manager.connectUser(user, client);

/*          Message msg = new Message(serverUser,MessageType.PRIVATE, "Welcome to the chat!");
            thread.printOnOutputStream(msg);*/

            // NOTIFY OTHER USERS
            notifyUsers(user, "connected");
            return true;

        } catch (DuplicateUsernameException | IOException e) {
            Message msg = new Message(serverUser, MessageType.PRIVATE, "Username is already been used.");
            client.receiveMessage(msg);
            info(user.getName() + " failed to connect to server.");
            return false;
        }
    }

    @Override
    public void sendBroadcastMsg(Message message) throws RemoteException {
        Collection<ChatClientInterface> clients = manager.getChatClients();
        message.setActiveUsers(manager.getUsernames()); //SEND UPDATE ABOUT USERS

        if (!clients.isEmpty()) {
            for (ChatClientInterface client : manager.getChatClients()) { // each client has own server thread
                client.receiveMessage(message);
            }
            info(message.getSender() + " is broadcasting: " + message);
        }
    }

    @Override
    public void sendPrivateMsg(Message message) throws RemoteException {
        ChatClientInterface client = manager.getClientInterfaceByString(message.getReceiverString());
        client.receiveMessage(message);
    }

    @Override //TODO
    public void sendUserList(Message message) throws IOException {
        ChatClientInterface client = manager.getClientInterfaceByString(message.getReceiverString());
        Set<String> users = manager.getUsernames();
        message.setActiveUsers(users);
        client.receiveUserList(message);
    }

    @Override
    public void disconnectUser(User user, ChatClientInterface client) throws RemoteException {
        try {
            info(user.getName() + " is leaving the chat.");
            manager.disconnectUser(user);

            // NOTIFY OTHER USERS
            notifyUsers(user, "disconnected");

            client.removeUser(user);

        } catch (IOException | UserNotFoundException e) {
            error(e.getMessage());
        }

    }

    private void notifyUsers(User user, String info) throws IOException {
        // NOTIFY OTHER USERS
        Message msg1 = new Message(MessageType.BROADCAST, user.getName() + " is " + info);
        msg1.setSender(serverUser);
        sendBroadcastMsg(msg1);
    }

    private static void info(String msg, @Nullable Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private static void error(String msg, @Nullable Object... params) {
        logger.log(Level.WARNING, msg, params);
    }
}
