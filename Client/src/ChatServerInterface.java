import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServerInterface extends Remote {

    String SERVICE_NAME = "ChatServerService";

    public void broadcast(String name, String msg) throws RemoteException;

    public boolean isConnected(String name, ChatClientInterface client) throws RemoteException;

    public void removeUser(String name, ChatClientInterface client) throws RemoteException;

}
