import exceptions.DuplicateUsernameException;
import exceptions.UserNotFoundException;
import main.ChatClientInterface;
import user.User;

import java.util.*;
import java.util.logging.Logger;


public class UserManager {

    private static final Logger logger = Logger.getLogger(UserManager.class.getName());
    private final Map<User, ChatClientInterface> userChatClientInterfaceMap;

    public UserManager() {
        userChatClientInterfaceMap = new HashMap<>();
    }

    public void connectUser(User user, ChatClientInterface chatClientInterface) throws DuplicateUsernameException {
        boolean duplicate = false;
        for (User u : userChatClientInterfaceMap.keySet()) {
            if (u.equals(user)) {
                duplicate = true;
            }
        }
        if (duplicate || user.compareWithString("Server")) {


            throw new DuplicateUsernameException(user.getName());
        } else {

            userChatClientInterfaceMap.put(user, chatClientInterface);

        }
    }

    public void disconnectUser(User user) throws UserNotFoundException {
        if (userChatClientInterfaceMap.containsKey(user)) {
            userChatClientInterfaceMap.remove(user);
        } else throw new UserNotFoundException(user.getName());
    }

    public ChatClientInterface getClientInterface(User user) {

        return userChatClientInterfaceMap.get(user);

    }

    public Set<String> getUsernames() {
        Set<String> userNames = new HashSet<>();
        for (User u : userChatClientInterfaceMap.keySet()) {
            userNames.add(u.getName());
        }
        return userNames;
    }

    public ChatClientInterface getClientInterfaceByString(String username) {
        User u = null;
        for (User user : userChatClientInterfaceMap.keySet()) {
            if (user.compareWithString(username)) {
                u = user;
            }
        }

        return userChatClientInterfaceMap.get(u);
    }

    public Collection<ChatClientInterface> getChatClients() {
        return userChatClientInterfaceMap.values();
    }

    public Set<ChatClientInterface> getChatClientsByUsers(Set<User> userSet) {
        Set<ChatClientInterface> clients = new HashSet<>();
        for (User user : userChatClientInterfaceMap.keySet()) {
            if (userSet.contains(user)) {
                clients.add(userChatClientInterfaceMap.get(user));
            }
        }
        return clients;
    }
}
