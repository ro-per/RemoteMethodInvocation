package main;

import com.sun.istack.internal.Nullable;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {

    private static final Logger logger = Logger.getLogger(ChatServer.class.getName());

    public static void main(String[] args) {
        int portNumber = 1000;
        try {
            portNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException ne) {
            System.out.println("Port could not be parsed, cause: " + ne.getCause());
            System.out.println("Standard port 1000 is used.");
        }
        try {
            Registry registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind(ChatServiceInterface.SERVICE_NAME, new ChatService());
            info("Server is running on port: " + portNumber);
        } catch (RemoteException e) {
            error("Server could not run on port: " + portNumber);
            e.printStackTrace();
        }
    }

    private static void info(String msg, @Nullable Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private static void error(String msg, @Nullable Object... params) {
        logger.log(Level.WARNING, msg, params);
    }
}
