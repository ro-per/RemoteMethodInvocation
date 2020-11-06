import messages.Message;
import user.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServiceInterface extends Remote {

    String SERVICE_NAME = "ChatServiceService";

    public boolean connectUser(User user, ChatClientInterface client) throws RemoteException;

    public void sendBroadcastMsg(Message message) throws RemoteException;

    public void sendPrivateMsg(Message message) throws RemoteException;

    public void disconnectUser(User user, ChatClientInterface client) throws RemoteException;

}
