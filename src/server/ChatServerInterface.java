package server;

import client.ChatClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServerInterface extends Remote {

    String SERVICE_NAME = "ChatServerService";

    public void broadcast(String name, String msg) throws RemoteException;

    public void addUser(String name, ChatClientInterface client) throws RemoteException;

    public void removeUser(String name, ChatClientInterface client) throws RemoteException;

    public boolean isValidUserName(String name) throws RemoteException;

}
