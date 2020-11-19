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


            Message message = new Message(user, MessageType.PRIVATE, "Here are users", user.toString());
            sendUserList(message);

            // NOTIFY OTHER USERS
            addUser(user, "connected");
            return true;

        } catch (DuplicateUsernameException | IOException e) {
            Message msg = new Message(serverUser, MessageType.ERROR_LOGIN, "Username is already been used.");
            client.receivePublicMessage(msg);
            info(user.getName() + " failed to connect to server.");
            return false;
        }
    }

    @Override
    public void disconnectUser(User user, ChatClientInterface client) throws RemoteException {
        try {
            info(user.getName() + " is leaving the chat.");
            manager.disconnectUser(user);

            // NOTIFY OTHER USERS
            removeUser(user, "disconnected");

            client.removeUser(user);

        } catch (IOException | UserNotFoundException e) {
            error(e.getMessage());
        }

    }

    @Override
    public void sendBroadcastMsg(Message message) throws RemoteException {
        Collection<ChatClientInterface> clients = manager.getChatClients();
        //message.setActiveUsers(manager.getUsernames()); //SEND UPDATE ABOUT USERS

        if (!clients.isEmpty()) {
            for (ChatClientInterface client : manager.getChatClients()) { // each client has own server thread
                client.receivePublicMessage(message);
            }
            info(message.getSender() + " is broadcasting: " + message);
        }
    }

    @Override
    public void sendPrivateMsg(Message message) throws RemoteException {
        ChatClientInterface client = manager.getClientInterfaceByString(message.getReceiverString());
        client.receivePrivateMessage(message);
    }

    public void sendUserList(Message message) throws IOException {
        ChatClientInterface client = manager.getClientInterfaceByString(message.getReceiverString());
        Set<String> users = manager.getUsernames();
        message.setActiveUsers(users);
        client.receiveUserList(message);
    }

    private void addUser(User user, String info) throws IOException {
        Collection<ChatClientInterface> clients = manager.getChatClients();

        if (!clients.isEmpty()) {
            for (ChatClientInterface client : manager.getChatClients()) { // each client has own server thread
                client.addUser(user);
            }
            info(user.toString() + " is being added to all lists" + "info:" + info);
        }
    }


    private void removeUser(User user, String info) throws IOException {
        Collection<ChatClientInterface> clients = manager.getChatClients();

        if (!clients.isEmpty()) {
            for (ChatClientInterface client : manager.getChatClients()) { // each client has own server thread
                client.removeUser(user);
            }
            info(user.toString() + " is being removed from all lists" + "info:" + info);
        }
    }


    private static void info(String msg, @Nullable Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private static void error(String msg, @Nullable Object... params) {
        logger.log(Level.WARNING, msg, params);
    }
}
