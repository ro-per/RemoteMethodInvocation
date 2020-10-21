package Client;

import Server.ChatServer;
import Server.ChatServerInterface;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

public class ChatClient extends UnicastRemoteObject implements ChatClientInterface {

    private String userName;
    private String serverName;
    private int portNumber;
    private ChatServerInterface serverInterface;

    public ChatClient(String userName, String serverName, int portNumber) throws RemoteException {
        this.userName = userName;
        this.serverName = serverName;
        this.portNumber = portNumber;
    }

    public void start() throws RemoteException{
        try{
            Naming.rebind("rmi://" + serverName + "/" + "ChatService",this);
            serverInterface = (ChatServerInterface)Naming.lookup("rmi://"+ serverName + "/" + "ChatService");

        } catch (ConnectException e){
            e.printStackTrace();
        } catch (MalformedURLException me){
            me.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public void addUser(Set<String> users) throws RemoteException {

    }
}
