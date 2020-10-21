package client;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatClientInterface extends Remote {

    String SERVICE_NAME = "ChatClientService";
    public void receiveMessage(String message) throws RemoteException;
    public List<String> getMessages() throws RemoteException;
}
