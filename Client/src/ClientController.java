import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.Lighting;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import messages.Message;
import messages.MessageType;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientController {

    static final String RED = "#d80412";
    static final String ERROR_EMPTY_MESSAGE = "Cannot send empty message !";
    static final String ERROR_EMPTY_USER = "Required !";
    static final String ERROR_EMPTY_SERVER = "Required ! !";
    static final String ERROR_EMPTY_PORT = "Required ! ! !";
    static final String ERROR_FORMAT_PORT = "Only numbers allowed !";
    static final String ERROR_DUPLICATE_USERNAME = "Username is already taken !";

    @FXML
    private TextField userName;
    @FXML
    private TextField serverName;
    @FXML
    private TextField portNumber;
    @FXML
    private TextField message;
    @FXML
    private ListView chatPane;
    @FXML
    private ScrollPane userListScrollPane;
    @FXML
    private ScrollPane messageListScrollPane;

    private ChatClient client;

    public void keyPressed(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) sendMessage();
    }


    public void connectButtonAction() {
        String userNameString = this.userName.getText();
        String serverString = serverName.getText();
        String portString = portNumber.getText();

        Lighting errorLighting = new Lighting();

        boolean correct = true;
        if (userNameString.isEmpty()) {
            this.userName.setText(ERROR_EMPTY_USER);
            flash(this.userName, errorLighting);
            correct = false;
        }
        if (serverString.isEmpty()) {
            this.serverName.setText(ERROR_EMPTY_SERVER);
            flash(this.serverName, errorLighting);
            correct = false;

        }
        if (portString.isEmpty()) {
            this.portNumber.setText(ERROR_EMPTY_PORT);
            flash(this.portNumber, errorLighting);
            correct = false;

        } else if (!isInteger(portString)) {
            this.portNumber.setText(ERROR_FORMAT_PORT);
            flash(this.portNumber, errorLighting);
            correct = false;
        }
        if (correct) {

            int port = Integer.parseInt(portString);
            connectToServer(userNameString, serverString, port);
        }

    }

    public void flash(TextField t, Lighting l) {
        t.setEffect(l);
    }

    private void connectToServer(String userName, String serverName, int portNumber) {

        try {
            client = new ChatClient(serverName, portNumber);
            client.start();

            if (!client.isConnected(userName)) {
                this.userName.clear();
                this.serverName.clear();
                this.portNumber.clear();
                client.receiveMessage(new Message(MessageType.PRIVATE,"Error duplicate username"));
            }

        } catch (RemoteException | MalformedURLException | NotBoundException  e) {
            e.printStackTrace();
        }

        chatPane.setItems(client.getMessages());

    }


    private void clearUserList() {
        /** TODO : clear the user list */

    }

    private void clearMessageList() {
        /** TODO : clear the chat list */

    }


    public void userNameClicked() {
        userName.clear();
        userName.setEffect(null);
    }


    public void serverClicked() {
        serverName.clear();
        serverName.setEffect(null);
    }

    public void portClicked() {
        portNumber.clear();
        portNumber.setEffect(null);
    }


    public void sendMessage() {
        String msg = message.getText();
        if (!message.getText().isEmpty()) {
            message.setText(ERROR_EMPTY_MESSAGE);
            message.clear();
        }
        client.sendBroadcast(msg);
    }


    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
