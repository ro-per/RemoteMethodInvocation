import javafx.collections.ObservableList;

import java.rmi.RemoteException;
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
        broadcast(formattedMsg);
    }

    private void broadcast(String msg){
        Iterator<Map.Entry<String, ChatClientInterface>> iterator = userNames.entrySet().iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().getValue().receiveMessage(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean connectUser(String username, ChatClientInterface client) throws RemoteException {
        if (isValidUserName(username)) {
            userNames.put(username, client);
            client.addToUsers(username);
            client.receiveMessage(welcomeMessage(username));
            broadcast(enterMessage(username));
            return true;
        } else return false;
    }

    @Override
    public void removeUser(String username, ChatClientInterface client) throws RemoteException {
        client.removeFromUsers(username);
        userNames.remove(username);
        broadcast(leaveMessage(username));
    }


    private boolean isValidUserName(String userName) {
        return !userNames.containsKey(userName);
    }

    private String formatMessage(String userName, String msg) {
        return "[" + userName + "]: " + msg;
    }

    private String welcomeMessage(String userName){
        return "Welcome " + userName;
    }

    private String enterMessage(String userName){
        return userName + " has entered the chat";
    }

    private String leaveMessage(String userName){
        return userName + " has left the chat";
    }

    Set<String> getUserNames() {
        return this.userNames.keySet();
    }

}
