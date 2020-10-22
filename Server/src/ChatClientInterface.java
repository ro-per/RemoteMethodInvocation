import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientInterface extends Remote {

    public void receiveMessage(String message) throws RemoteException;

    public void addToUsers(String userName) throws RemoteException;

    public void removeFromUsers(String userName) throws RemoteException;
}
