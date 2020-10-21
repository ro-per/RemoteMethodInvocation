package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.util.List;

public interface ChatServerInterface extends Remote {

    public void broadcast(String userName, String message) throws RemoteException;

    public void checkId(RemoteRef ref) throws RemoteException;

    public void registerClient(String userName) throws RemoteException;

    public void sendPrivateMessage(List<String> group, String message) throws RemoteException;

}
