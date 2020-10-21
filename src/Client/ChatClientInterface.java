package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ChatClientInterface extends Remote {
        public void getMessage(String message) throws RemoteException;
        public void addUser(Set<String> users) throws RemoteException;
}
