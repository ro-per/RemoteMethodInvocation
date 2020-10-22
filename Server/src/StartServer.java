import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StartServer {
    public static void main(String[] args) {
        int portNumber = 1000;
        try {
            Registry registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind(ChatServerInterface.SERVICE_NAME, new ChatServer());
            System.out.println("Server is running on port: " + portNumber);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
