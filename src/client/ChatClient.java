package client;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import server.ChatServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient extends UnicastRemoteObject implements ChatClientInterface {

    private String userName;
    private String serverName;
    private int serverPortNumber;
    private ObservableList<String> messages;
    private ChatServerInterface server;

    public ChatClient(String serverName, int serverPortNumber) throws RemoteException, MalformedURLException, NotBoundException {
        super();
        this.serverName = serverName;
        this.serverPortNumber = serverPortNumber;
        server = (ChatServerInterface) Naming.lookup("rmi://" + serverName + "/" + ChatServerInterface.SERVICE_NAME);
    }

    public void sendMessage(String message) {
        new Thread(() -> this.send(message)).start();
    }

    private void send(String message) {
        try {
            server.broadcast(userName,message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUserName(String name) throws RemoteException {
        if (server.isValidUserName(name)) {
            server.addUser(name,this);
            userName = name;
            return true;
        }
        return false;
    }

    public void leave(){
        try{
            server.removeUser(userName);

        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messages.add(message);
            }
        });
    }

    @Override
    public ObservableList<String> getMessages() {
        return messages;
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
